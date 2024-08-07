package com.mycuckoo.core.repository;

/**
 * 功能说明: 为分页功能提供接口支持
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:46:25 PM
 */
public interface Pageable {

    /**
     * 返回页数
     *
     * @return the page to be returned.
     */
    @Deprecated
    int getPageNumber();

    /**
     * 返回条目数量
     *
     * @return the number of items of that page
     */
    int getPageSize();

    /**
     * 按当前页码和每页记录数返回下一个页码的相对位置
     *
     * @return the offset to be taken
     */
    int getOffset();
}
