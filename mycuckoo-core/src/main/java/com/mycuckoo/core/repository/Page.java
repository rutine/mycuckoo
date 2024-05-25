package com.mycuckoo.core.repository;

import java.util.Iterator;
import java.util.List;

/**
 * 功能说明: 分页接口
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:45:52 PM
 */
public interface Page<T> extends Iterable<T> {

    /**
     * 返回当前页. 返回值总是正值且不大于 {@code Page#getTotalPages()}.
     *
     * @return 当前页
     */
    int getNumber();

    /**
     * 返回页面大小.
     *
     * @return 页面大小
     */
    int getSize();

    /**
     * 返回总页数.
     *
     * @return 总页数
     */
    int getTotalPages();

    /**
     * 返回当前页面所包含得元素数量.
     *
     * @return 当前页面所包含得元素数量
     */
    int getNumberOfElements();

    /**
     * 返回元素总数量.
     *
     * @return 元素总数量
     */
    long getTotalElements();

    /**
     * 返回是否存在上一页.
     *
     * @return 是否存在上一页
     */
    boolean hasPreviousPage();

    /**
     * 返回是否是第一页.
     *
     * @return
     */
    boolean isFirstPage();

    /**
     * 返回是否存在下一页.
     *
     * @return 是否存在下一页
     */
    boolean hasNextPage();

    /**
     * 返回是否是最后一页.
     *
     * @return 是否是最后一页
     */
    boolean isLastPage();

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    Iterator<T> iterator();

    /**
     * Returns the page content as {@link List}.
     *
     * @return
     */
    List<T> getContent();

    /**
     * Returns whether the {@link Page} has content at all.
     *
     * @return
     */
    boolean hasContent();
}
