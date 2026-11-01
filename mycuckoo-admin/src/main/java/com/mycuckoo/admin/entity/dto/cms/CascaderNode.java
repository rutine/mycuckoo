package com.mycuckoo.admin.entity.dto.cms;

import java.util.List;

/**
 * 级联节点
 */
public class CascaderNode {

    private String value;               // 节点值
    private String label;               // 节点显示文本
    private List<CascaderNode> children; // 子节点

    public CascaderNode() {}

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public List<CascaderNode> getChildren() { return children; }
    public void setChildren(List<CascaderNode> children) { this.children = children; }
}