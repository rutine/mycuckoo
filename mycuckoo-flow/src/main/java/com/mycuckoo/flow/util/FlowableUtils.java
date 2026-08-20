package com.mycuckoo.flow.util;

import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/8/19 19:30
 */
public abstract class FlowableUtils {
    private static final Logger logger = LoggerFactory.getLogger(FlowableUtils.class);

    private static StartEvent getSubProcessStartEvent(SubProcess subProcess) {
        for (FlowElement flowElement : subProcess.getFlowElements()) {
            if (flowElement instanceof StartEvent) {
                return (StartEvent) flowElement;
            }
        }
        return null;
    }

    /**
     * 根据节点，获取入口连线
     *
     * @param source
     * @return
     */
    public static List<SequenceFlow> getIncomingFlows(FlowElement source) {
        List<SequenceFlow> list = null;
        if (source instanceof FlowNode) {
            list = ((FlowNode) source).getIncomingFlows();
        } else if (source instanceof Gateway) {
            list = ((Gateway) source).getIncomingFlows();
        } else if (source instanceof SubProcess) {
            list = ((SubProcess) source).getIncomingFlows();
        } else if (source instanceof StartEvent) {
            list = ((StartEvent) source).getIncomingFlows();
        } else if (source instanceof EndEvent) {
            list = ((EndEvent) source).getIncomingFlows();
        }

        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 根据节点，获取出口连线
     *
     * @param source
     * @return
     */
    public static List<SequenceFlow> getOutgoingFlows(FlowElement source) {
        List<SequenceFlow> list = null;
        if (source instanceof FlowNode) {
            list = ((FlowNode) source).getOutgoingFlows();
        } else if (source instanceof Gateway) {
            list = ((Gateway) source).getOutgoingFlows();
        } else if (source instanceof SubProcess) {
            list = ((SubProcess) source).getOutgoingFlows();
        } else if (source instanceof StartEvent) {
            list = ((StartEvent) source).getOutgoingFlows();
        } else if (source instanceof EndEvent) {
            list = ((EndEvent) source).getOutgoingFlows();
        }

        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 获取全部节点列表，包含子流程节点
     *
     * @param flowElements
     * @param allElements
     * @return
     */
    public static Collection<FlowElement> getAllElements(Collection<FlowElement> flowElements, Collection<FlowElement> allElements) {
        allElements = allElements == null ? new ArrayList<>() : allElements;
        for (FlowElement element : flowElements) {
            allElements.add(element);
            if (element instanceof SubProcess) {
                // 继续深入子流程，进一步获取子流程
                allElements = FlowableUtils.getAllElements(((SubProcess) element).getFlowElements(), allElements);
            }
        }
        return allElements;
    }

    /**
     * 往前查找最近的上游任务节点列表
     *
     * @param source 起始节点
     * @param walks 已经过的连线ID
     * @param collector 需撤回的用户节点
     * @return
     */
    public static List<UserTask> findParentUserTasks(FlowElement source, Set<String> walks, List<UserTask> collector) {
        walks = walks == null ? new HashSet<>() : walks;
        collector = collector == null ? new ArrayList<>() : collector;

        // 是开始节点且存在子流程中，则顺着子流程继续查找
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            collector = findParentUserTasks(source.getSubProcess(), walks, collector);
        }

        // 获取入口连线
        List<SequenceFlow> sequenceFlows = getIncomingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            // 上游是用户节点，则收集该节点
            FlowElement prev = element.getSourceFlowElement();
            if (prev instanceof UserTask) {
                collector.add((UserTask) prev);
                continue;
            }

            // 上游是子流程，则收集子流程最近的下游用户节点
            if (prev instanceof SubProcess) {
                StartEvent startEvent = getSubProcessStartEvent((SubProcess) prev);
                List<UserTask> children = startEvent == null ? Collections.emptyList() : findChildProcessUserTasks(startEvent, null, null);
                // 如果找到节点，不继续往下找，否则继续
                if (children.size() > 0) {
                    collector.addAll(children);
                    continue;
                }
            }

            // 递归
            collector = findParentUserTasks(prev, walks, collector);
        }

        return collector;
    }

    /**
     * 往后查找正在运行的最近下游用户节点
     *
     * @param source 起始节点(退回节点)
     * @param runTaskKeyList 正在运行的任务 Key，用于校验任务节点是否是正在运行的节点
     * @param walks 已经过的连线ID
     * @param collector 需撤回的用户节点
     * @return
     */
    public static List<UserTask> findChildUserTasks(FlowElement source, List<String> runTaskKeyList, Set<String> walks, List<UserTask> collector) {
        walks = walks == null ? new HashSet<>() : walks;
        collector = collector == null ? new ArrayList<>() : collector;

        // 是结束节点且存在子流程中，则顺着子流程继续查找
        if (source instanceof EndEvent && source.getSubProcess() != null) {
            collector = findChildUserTasks(source.getSubProcess(), runTaskKeyList, walks, collector);
        }

        // 获取出口连线
        List<SequenceFlow> sequenceFlows = getOutgoingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            // 下游是用户节点，且正在运行的任务中存在该节点 Key，则收集该节点
            FlowElement next = element.getTargetFlowElement();
            if (next instanceof UserTask && runTaskKeyList.contains(next.getId())) {
                collector.add((UserTask) next);
                continue;
            }

            // 下游是子流程，则收集子流程最近的下游用户节点
            if (next instanceof SubProcess) {
                StartEvent startEvent = getSubProcessStartEvent((SubProcess) next);
                List<UserTask> children = startEvent == null ? Collections.emptyList() : findChildUserTasks(startEvent, runTaskKeyList, walks, null);
                // 如果找到节点，不继续往下找，否则继续
                if (children.size() > 0) {
                    collector.addAll(children);
                    continue;
                }
            }

            // 递归
            collector = findChildUserTasks(next, runTaskKeyList, walks, collector);
        }

        return collector;
    }

    /**
     * 查找子流程最近的用户节点
     *
     * @param source 起始节点
     * @param walks 已经过的连线ID
     * @param collector 需撤回的用户节点
     * @return
     */
    public static List<UserTask> findChildProcessUserTasks(FlowElement source, Set<String> walks, List<UserTask> collector) {
        walks = walks == null ? new HashSet<>() : walks;
        collector = collector == null ? new ArrayList<>() : collector;

        // 获取出口连线
        List<SequenceFlow> sequenceFlows = getOutgoingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            // 下游是用户节点，则收集该节点
            FlowElement next = element.getTargetFlowElement();
            if (next instanceof UserTask) {
                collector.add((UserTask) next);
                continue;
            }

            // 下游是子流程，则收集子流程最近的下游用户节点
            if (next instanceof SubProcess) {
                StartEvent startEvent = getSubProcessStartEvent((SubProcess) element.getTargetFlowElement());
                List<UserTask> children = startEvent == null ? Collections.emptyList() : findChildProcessUserTasks(startEvent, walks, null);
                // 如果找到节点，不继续往下找，否则继续
                if (children.size() > 0) {
                    collector.addAll(children);
                    continue;
                }
            }

            // 继续迭代
            collector = findChildProcessUserTasks(next, walks, collector);
        }

        return collector;
    }

    /**
     * 从后向前寻路，获取所有脏线路上的点
     *
     * @param source 起始节点
     * @param walkRoads 已经经过的点集合
     * @param walks 已经过的连线ID
     * @param targets 目标脏线路终点
     * @param dirtyRoads 确定为脏数据的点，因为不需要重复，因此使用 set 存储
     * @return
     */
    public static Set<String> findDirtyRoads(FlowElement source, List<String> walkRoads, Set<String> walks, List<String> targets, Set<String> dirtyRoads) {
        walkRoads = walkRoads == null ? new ArrayList<>() : walkRoads;
        walks = walks == null ? new HashSet<>() : walks;
        dirtyRoads = dirtyRoads == null ? new HashSet<>() : dirtyRoads;

        // 是开始节点且存在子流程中，则顺着子流程继续查找
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            dirtyRoads = findDirtyRoads(source.getSubProcess(), walkRoads, walks, targets, dirtyRoads);
        }

        // 获取入口连线
        List<SequenceFlow> sequenceFlows = getIncomingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            Set<String> branchWalks = new HashSet<>(walks);
            branchWalks.add(element.getId());

            FlowElement prev = element.getSourceFlowElement();

            // 经过的路线
            List<String> branchRoads = new ArrayList<>(walkRoads);
            branchRoads.add(prev.getId());

            // 如果此点为目标点，确定经过的路线为脏线路，添加点到脏线路中，然后找下个连线
            if (targets.contains(prev.getId())) {
                dirtyRoads.addAll(branchRoads);
                continue;
            }

            // 如果该节点是子流程，则顺着子流程继续查找
            if (prev instanceof SubProcess) {
                StartEvent startEvent = getSubProcessStartEvent((SubProcess) prev);
                if (startEvent != null) {
                    dirtyRoads = findChildProcessDirtyRoads(startEvent, null, dirtyRoads);
                    Boolean isInChildProcess = isTargetInChildProcess(startEvent, null, targets, null);
                    if (isInChildProcess) {
                        // 已在子流程上找到，该路线结束
                        continue;
                    }
                }
            }

            // 递归
            dirtyRoads = findDirtyRoads(prev, branchRoads, branchWalks, targets, dirtyRoads);
        }

        return dirtyRoads;
    }

    /**
     * 获取子流程脏路线
     * 说明，假如回退的点就是子流程，那么也肯定会回退到子流程最初的用户任务节点，因此子流程中的节点全是脏路线
     *
     * @param source 起始节点
     * @param walks 已经过的连线ID
     * @param dirtyRoads 所有脏数据点
     * @return
     */
    public static Set<String> findChildProcessDirtyRoads(FlowElement source, Set<String> walks, Set<String> dirtyRoads) {
        walks = walks == null ? new HashSet<>() : walks;
        dirtyRoads = dirtyRoads == null ? new HashSet<>() : dirtyRoads;

        // 获取出口连线
        List<SequenceFlow> sequenceFlows = getOutgoingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            FlowElement next = element.getTargetFlowElement();
            // 添加脏路线
            dirtyRoads.add(next.getId());

            // 节点为子流程节点，则从子流程的开始节点出发
            if (next instanceof SubProcess) {
                StartEvent startEvent = getSubProcessStartEvent((SubProcess) next);
                if (startEvent != null) {
                    dirtyRoads = findChildProcessDirtyRoads(startEvent, walks, dirtyRoads);
                }
            }

            // 递归
            dirtyRoads = findChildProcessDirtyRoads(next, walks, dirtyRoads);
        }

        return dirtyRoads;
    }

    /**
     * 判断脏路线结束节点是否在子流程上
     *
     * @param source          起始节点
     * @param walks 已经过的连线ID
     * @param targets         判断脏路线节点是否存在子流程上，只要存在一个，说明脏路线只到子流程为止
     * @param inChildProcess  是否存在子流程上，true 是，false 否
     * @return
     */
    public static Boolean isTargetInChildProcess(FlowElement source, Set<String> walks, List<String> targets, Boolean inChildProcess) {
        walks = walks == null ? new HashSet<>() : walks;
        inChildProcess = Boolean.TRUE.equals(inChildProcess);

        if (inChildProcess) {
            return inChildProcess;
        }

        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getOutgoingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            FlowElement next = element.getTargetFlowElement();
            // 如果发现目标点在子流程上存在，说明只到子流程为止
            if (targets.contains(next.getId())) {
                inChildProcess = true;
                break;
            }

            // 节点为子流程节点，则从子流程的开始节点出发
            if (next instanceof SubProcess) {
                StartEvent startEvent = getSubProcessStartEvent((SubProcess) next);
                if (startEvent != null) {
                    inChildProcess = isTargetInChildProcess(startEvent, walks, targets, inChildProcess);
                }
            }

            // 递归
            inChildProcess = isTargetInChildProcess(next, walks, targets, inChildProcess);
        }

        return inChildProcess;
    }

    /**
     * 从后向前扫描，判断目标节点相对于当前节点是否是串行
     * 不存在直接回退到子流程中的情况，但存在从子流程出去到父流程情况
     *
     * @param source          起始节点
     * @param isSequential    是否串行
     * @param walks 已经过的连线ID
     * @param targetKsy       目标节点
     * @return
     */
    public static Boolean isSequentialToTarget(FlowElement source, String targetKsy, Set<String> walks, Boolean isSequential) {
        walks = walks == null ? new HashSet<>() : walks;
        isSequential = isSequential == null || isSequential;

        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            isSequential = isSequentialToTarget(source.getSubProcess(), targetKsy, walks, isSequential);
        }

        // 获取入口连线
        List<SequenceFlow> sequenceFlows = getIncomingFlows(source);
        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            // 如果目标节点已被判断为并行，后面都不需要执行，直接返回
            if (!isSequential) {
                break;
            }
            // 这条线路存在目标节点，这条线路完成，进入下个线路
            if (targetKsy.equals(element.getSourceFlowElement().getId())) {
                continue;
            }
            if (element.getSourceFlowElement() instanceof StartEvent) {
                isSequential = false;
                break;
            }

            // 递归
            isSequential = isSequentialToTarget(element.getSourceFlowElement(), targetKsy, walks, isSequential);
        }

        return isSequential;
    }

    /**
     * 往前查找，获取所有上游用户任务路径
     * 不存在直接回退到子流程，但是存在回退到父级流程的情况
     *
     * @param source    起始节点
     * @param walkRoads 已经经过的点集合
     * @param collector     路线
     * @return
     */
    public static List<List<UserTask>> findParentUserTaskRoads(FlowElement source, List<UserTask> walkRoads, Set<String> walks, List<List<UserTask>> collector) {
        walkRoads = walkRoads == null ? new ArrayList<>() : walkRoads;
        walks = walks == null ? new HashSet<>() : walks;
        collector = collector == null ? new ArrayList<>() : collector;

        // 是开始节点且存在子流程中
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            collector = findParentUserTaskRoads(source.getSubProcess(), walkRoads, walks, collector);
        }

        // 根据类型，获取入口连线
        List<SequenceFlow> sequenceFlows = getIncomingFlows(source);
        if (sequenceFlows.isEmpty()) {
            collector.add(new ArrayList<>(walkRoads));
            return collector;
        }

        for (SequenceFlow element : sequenceFlows) {
            // 出现循环，跳过
            if (!walks.add(element.getId())) {
                continue;
            }

            Set<String> branchWalks = new HashSet<>(walks);
            branchWalks.add(element.getId());

            // 添加经过路线
            List<UserTask> branchRoads = new ArrayList<>(walkRoads);
            FlowElement prev = element.getSourceFlowElement();
            if (prev instanceof UserTask) {
                branchRoads.add((UserTask) prev);
            }

            // 递归
            collector = findParentUserTaskRoads(prev, branchRoads, branchWalks, collector);
        }

        return collector;
    }

    /**
     *  历史节点数据清洗，清洗掉又回滚导致的脏数据: <br/>
     *
     *  <p><b>1. 正常直线流程 </b></p>
     *  <div>流程：A -> B -> C </div>
     *  <div>历史：A, B, C </div>
     *  <div>返回：A, B, C </div>
     *  <div>没有回退或跳转，全部保留。 </div>
     *
     *  <p><b>2. 从 C 驳回到 A </b></p>
     *  <div>流程：A -> B -> C </div>
     *  <div>历史：A, B, C(删除原因：Change activity to A), A, B </div>
     *  <div>返回：A, B </div>
     *  <div>旧线路的 A、B、C 已经失效。方法根据 C 的删除原因，从 C 反向找到回到 A 前的脏线路并过滤；后面重新生成的 A、B 才是有效记录。 </div>
     *
     *  <p><b>3. 从 C 回退到 B </b></p>
     *  <div>流程：A -> B -> C </div>
     *  <div>历史：A, B, C(删除原因：Change activity to B), B, C </div>
     *  <div>返回：A, B, C </div>
     *  <div>第一次到达的 C 是被撤销的实例，旧的 B 仍是有效前置节点；回退后新创建的 B、C 保留。因此最终看起来仍是一条完整的 A -> B -> C。 </div>
     *
     *  <p><b>4. 分支只走其中一路 </b></p>
     *  <div>流程：A -> 网关 -> B 或 C -> D </div>
     *  <div>实际执行：A -> B -> D </div>
     *  <div>历史：A, B, D </div>
     *  <div>返回：A, B, D </div>
     *  <div>它不根据 BPMN 图补全未执行分支，而是以历史实例为准，仅清除跳转造成的无效记录。 </div>
     *
     *  <p><b>5. 会签节点回退 </b></p>
     *  <div>流程：A -> M(会签) -> C </div>
     *  <div>历史：A, M(用户1), M(用户2), C(删除原因：Change activity to M), M(用户1), M(用户2) </div>
     *  <div>返回：A, M, M </div>
     *  <div>会签节点会有多个历史任务实例。方法会将回退前那批会签实例和后续失效线路一起视为脏数据，并保留回退后重新创建的会签实例。返回的是节点 Key，因此两个会签实例都会表现为两个 M </div>
     *  <div>核心区别是：它不是简单按节点 Key 去重，而是通过 deleteReason 和 BPMN 连线判断“哪一次执行”已被回退废弃 </div>
     *
     * @param allElements 全部节点信息
     * @param historicTaskInstances 历史任务实例信息，数据采用开始时间升序
     * @return
     */
    public static List<String> cleanHistoricTaskInstanceKeys(Collection<FlowElement> allElements, List<HistoricTaskInstance> historicTaskInstances) {
        Set<String> multiTask = new HashSet<>();
        Map<String, FlowElement> flowElementMap = new HashMap<>();
        allElements.forEach(flowElement -> {
            flowElementMap.put(flowElement.getId(), flowElement);
            if (flowElement instanceof UserTask) {
                // 会签节点
                if (((UserTask) flowElement).getBehavior() instanceof ParallelMultiInstanceBehavior || ((UserTask) flowElement).getBehavior() instanceof SequentialMultiInstanceBehavior) {
                    multiTask.add(flowElement.getId());
                }
            }
        });

        //栈LIFO：后进先出
        Stack<HistoricTaskInstance> stack = new Stack<>();
        historicTaskInstances.forEach(stack::push);

        // 清洗后的历史任务实例 key
        List<String> pureHistoricTaskInstanceKeyList = new ArrayList<>();

        // 网关存在只走了部分分支，且还存在跳转废弃，及其他分支的干扰，因此需对历史节点数据进行清洗
        // 上次循环的用户任务 key
        String lastUserTaskKey = null;
        List<Set<String>> deleteKeyList = new ArrayList<>(); // 临时被删掉的任务 key，存在并行情况
        List<Set<String>> dirtyKeyList = new ArrayList<>(); // 临时脏数据线路

        int multiIndex = -1; // 会签特殊处理下标
        String multiKey = null; // 会签特殊处理 key
        boolean multiOpera = false; // 会签特殊处理操作标识

        while (!stack.empty()) {
            // 是否是脏线上的点
            boolean isDirty = false;
            HistoricTaskInstance top = stack.peek();
            for (Set<String> dirtyKey : dirtyKeyList) {
                if (dirtyKey.contains(top.getTaskDefinitionKey())) {
                    isDirty = true;
                }
            }

            // 删除原因不为空，说明从这条数据开始回跳或者回退的
            // MI_END：会签完成后，其他未签到节点的删除原因，不在处理范围内
            if (top.getDeleteReason() != null
                    && !"MI_END".equals(top.getDeleteReason())
                    && (top.getDeleteReason().contains("Change activity to ") || top.getDeleteReason().contains("Change parent activity to "))) {
                // 可以理解为脏线路起点
                String dirtyStartKeyStr = "";
                if (top.getDeleteReason().contains("Change activity to ")) {
                    dirtyStartKeyStr = top.getDeleteReason().replace("Change activity to ", "");
                }
                // 会签回退的删除原因有点不同
                if (top.getDeleteReason().contains("Change parent activity to ")) {
                    dirtyStartKeyStr = top.getDeleteReason().replace("Change parent activity to ", "");
                }

                List<String> dirtyStartKeys = Arrays.asList(dirtyStartKeyStr.split(","));
                FlowElement dirtyTask = flowElementMap.get(top.getTaskDefinitionKey());
                // 获取脏数据线路
                Set<String> dirtyKeys = FlowableUtils.findDirtyRoads(dirtyTask, null, null, dirtyStartKeys, null);
                dirtyKeys.add(top.getTaskDefinitionKey());  // 自己本身也是脏线上的点
                logger.debug("{} 点脏路线集合：{}", top.getTaskDefinitionKey(), dirtyKeys);

                // 是全新的需要添加的脏线路
                boolean isNewDirty = true;
                for (int i = 0; i < dirtyKeyList.size(); i++) {
                    // 如果上个节点也在脏线路内，当前点可能是并行或连续驳回节点
                    // 这时，都以之前的脏线路节点为准，合并脏线路，即路线补全
                    if (dirtyKeyList.get(i).contains(lastUserTaskKey)) {
                        dirtyKeyList.get(i).addAll(dirtyKeys);
                        isNewDirty = false;
                    }
                }

                // 全新脏线
                if (isNewDirty) {
                    // deleteKey 单一路线驳回到并行，这种同时生成多个新实例记录情况，这时 deleteKey 其实是由多个值组成
                    // 按照逻辑，回退后立刻生成的实例记录就是回退的记录
                    // 至于驳回所生成的 Key，直接从删除原因中获取，因为存在驳回到并行的情况
                    deleteKeyList.add(new HashSet<>(dirtyStartKeys));
                    dirtyKeyList.add(dirtyKeys);
                }
                isDirty = true;
            }

            // 非脏线上的有效节点数据，添加历史实例 Key
            if (!isDirty) {
                pureHistoricTaskInstanceKeyList.add(top.getTaskDefinitionKey());
            }


            /**
             * 会签脏数据范畴开始，校验脏线路是否结束
             * 由某个点跳到会签点, 此时出现多个会签实例对应一个跳转情况，需要把这些连续脏数据都找到
             */
            for (int i = 0; i < deleteKeyList.size(); i++) {
                // 如果发现脏数据属于会签，记录下标与对应 Key，以备后续比对
                if (multiKey == null
                        && multiTask.contains(top.getTaskDefinitionKey())
                        && deleteKeyList.get(i).contains(top.getTaskDefinitionKey())) {
                    multiIndex = i;
                    multiKey = top.getTaskDefinitionKey();
                }

                // 会签脏数据处理，节点退回会签清空
                // 如果在会签脏数据范畴中，发现 Key改变，说明会签脏数据在上个节点就结束了，可以把会签脏数据删掉
                if (multiKey != null && !multiKey.equals(top.getTaskDefinitionKey())) {
                    deleteKeyList.get(multiIndex).remove(top.getTaskDefinitionKey());
                    multiKey = null;
                    // 结束进行下校验删除
                    multiOpera = true;
                }

                // 其他脏数据处理
                // 发现该路线最后一条脏数据，说明这条脏数据线路处理完了，删除脏数据信息
                // 脏数据产生的新实例中是否包含这条数据
                if (multiKey == null && deleteKeyList.get(i).contains(top.getTaskDefinitionKey())) {
                    // 删除匹配到的部分
                    deleteKeyList.get(i).remove(top.getTaskDefinitionKey());
                }

                // 如果每组中的元素都以匹配过，说明脏数据结束
                if (deleteKeyList.get(i).isEmpty()) {
                    deleteKeyList.remove(i);
                    dirtyKeyList.remove(i);
                    break;
                }
            }

            // 会签数据处理需要在循环外处理，否则可能导致溢出
            // 会签的数据肯定是之前放进去的所以理论上不会溢出，但还是校验下
            if (multiOpera && deleteKeyList.size() > multiIndex && deleteKeyList.get(multiIndex).isEmpty()) {
                deleteKeyList.remove(multiIndex);
                dirtyKeyList.remove(multiIndex);
                multiIndex = -1;
                multiOpera = false;
            }

            // 注意：pop() 与 peek() 方法不同
            lastUserTaskKey = stack.pop().getTaskDefinitionKey();
        }

        return pureHistoricTaskInstanceKeyList;
    }

    /**
     * 从 flowElement 获取 指定名称的 拓展元素
     *
     * @param flowElement          元素
     * @param extensionElementName 拓展元素名称
     */
    public static ExtensionElement getExtensionElementByName(FlowElement flowElement, String extensionElementName) {
        if (flowElement == null) {
            return null;
        }
        Map<String, List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();
        for (Map.Entry<String, List<ExtensionElement>> stringEntry : extensionElements.entrySet()) {
            if (stringEntry.getKey().equals(extensionElementName)) {
                for (ExtensionElement extensionElement : stringEntry.getValue()) {
                    if (extensionElement.getName().equals(extensionElementName)) {
                        return extensionElement;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 获取当前任务节点扩展属性信息
     *
     * @param repositoryService
     * @param task 当前任务
     * @return 自定义属性列表
     */
    public static List<Map<String, String>> getPropertyElements(RepositoryService repositoryService, org.flowable.task.api.Task task) {
        FlowElement flowElement = getCurrentElement(repositoryService, task);
        ExtensionElement extensionElement = FlowableUtils.getExtensionElementByName(flowElement, "properties");
        if (extensionElement == null) {
            return Collections.emptyList();
        }
        return getPropertyAttributes(extensionElement, "property");
    }

    /**
     * 获取当前任务节点
     *
     * @param repositoryService
     * @param task
     * @return
     */
    public static FlowElement getCurrentElement(RepositoryService repositoryService, org.flowable.task.api.Task task) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        return bpmnModel.getFlowElement(task.getTaskDefinitionKey());
    }

    /**
     * 根据属性名获取扩展元素中的扩展属性列表
     *
     * @param extensionElement 扩展元素
     * @param attributesName   属性名
     * @return 扩展属性列表
     */
    public static List<Map<String, String>> getPropertyAttributes(ExtensionElement extensionElement, String attributesName) {
        if (extensionElement == null || extensionElement.getChildElements() == null) {
            return Collections.emptyList();
        }
        return extensionElement.getChildElements().getOrDefault(attributesName, Collections.emptyList())
                .stream()
                .map(element -> {
                    Map<String, String> properties = new LinkedHashMap<>();
                    element.getAttributes().forEach((name, attributes) -> {
                        if (attributes != null && !attributes.isEmpty()) {
                            properties.put(name, attributes.get(0).getValue());
                        }
                    });
                    return properties;
                })
                .collect(Collectors.toList());
    }

    /**
     * 驳回目标节点：若目标节点是多实例用户节点，直接 changeState 到该节点不会重新生成任务实例，
     * 需移动到该节点入口网关，由引擎按正常流转重新进入节点并创建(多实例)任务。
     *
     * @param allElements   流程全部节点
     * @param targetTaskKeys 目标用户节点ids
     * @return 实际移动的目标节点id（入口网关或原节点）
     */
    public static List<String> resolveTargetActivityIds(Collection<FlowElement> allElements,  List<String> targetTaskKeys) {
        return targetTaskKeys.stream().map(targetTaskKey -> {
            FlowElement element = allElements.stream()
                .filter(o -> targetTaskKey.equals(o.getId()))
                .findFirst()
                .orElse(null);
            if (element instanceof UserTask) {
                List<SequenceFlow> incomingFlows = ((UserTask) element).getIncomingFlows();
                if (incomingFlows.size() == 1) {
                    FlowElement source = incomingFlows.get(0).getSourceFlowElement();
                    if (source instanceof Gateway) {
                        boolean reachable = ((Gateway) source).getOutgoingFlows().stream()
                                .anyMatch(flow -> targetTaskKey.equals(flow.getTargetFlowElement().getId()));
                        if (reachable) {
                            return source.getId();
                        }
                    }
                }
            }
            return targetTaskKey;
        }).collect(Collectors.toList());
    }
}


