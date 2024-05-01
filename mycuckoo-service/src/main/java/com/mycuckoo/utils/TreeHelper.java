package com.mycuckoo.utils;

import com.mycuckoo.vo.SimpleTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public static <T extends SimpleTree<T>> List<T> buildTree(List<T> trees, String rootId) {
        //1. 先分组
        Map<String, List<T>> group = trees.stream().collect(Collectors.groupingBy(SimpleTree::getParentId));

        //2. 将对应的分组设置到对应的parent
        for (T node : trees) {
            List<T> subNodes = group.get(node.getId());
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
}
