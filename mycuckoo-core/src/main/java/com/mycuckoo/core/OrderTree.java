package com.mycuckoo.core;

public class OrderTree extends SimpleTree<OrderTree> {
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
