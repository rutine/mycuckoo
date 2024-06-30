package com.mycuckoo.core.util;

import com.mycuckoo.core.OrderTree;
import com.mycuckoo.core.SimpleTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 功能说明: 树形帮助类
 *
 * @author rutine
 * @version 4.0.0
 * @time Oct 29, 2020 15:31:29 PM
 */
public abstract class TreeHelper {
    private TreeHelper() {}


    /**
     * 构造树形, 返回rootId下的子树
     */
    public static <T extends SimpleTree> List<T> buildTree(List<T> trees, String rootId) {
        //1. 先分组
        Map<String, List<T>> group = trees.stream().collect(Collectors.groupingBy(SimpleTree::getParentId));

        //2. 将对应的分组设置到对应的parent
        for (T node : trees) {
            List<T> subNodes = group.get(node.getId());
            if (subNodes != null && node instanceof OrderTree) {
                Collections.sort(subNodes, (o1, o2) -> ((OrderTree) o1).getOrder() - ((OrderTree) o2).getOrder());
            }
            node.setChildren(subNodes);
        }
        List<T> result = group.get(rootId);

        return result != null ? result : new ArrayList<>(0);
    }

    /**
     * 收集树节点ID
     */
    public static void collectNodeIds(List<String> nodeIds, List<? extends SimpleTree> tree) {
        if (tree == null) {
            return;
        }

        for (SimpleTree vo : tree) {
            nodeIds.add(vo.getId());
            collectNodeIds(nodeIds, vo.getChildren());
        }
    }

    public static List<? extends SimpleTree> treeFilter(List<? extends SimpleTree> trees, String keyword, BiFunction<SimpleTree, String, Boolean> filter) {
        if (CommonUtils.isEmpty(keyword)) {
            //无关键词, 无需匹配
            return trees;
        }
        if (trees == null || trees.isEmpty()) {
            //无节点, 匹配失败
            return trees;
        }

        trees = trees.stream()
                .filter(node -> {
                    if (filter.apply(node, keyword)) {
                        return true;
                    }
                    node.setChildren(treeFilter(node.getChildren(), keyword, filter));
                    return node.getChildren() != null && !node.getChildren().isEmpty();
                })
                .collect(Collectors.toList());

        return trees;
    }
    public static boolean filterNode(SimpleTree node, String keyword) {
        return node.getText().contains(keyword);
    }
}
