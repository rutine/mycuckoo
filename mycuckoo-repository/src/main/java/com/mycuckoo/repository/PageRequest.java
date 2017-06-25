package com.mycuckoo.repository;

import java.io.Serializable;

/**
 * Basic Java Bean implementation of {@code Pageable}.
 * @author rutine
 */
public class PageRequest  implements Pageable, Serializable {
	private static final long serialVersionUID = 8280485938848398236L;

	private final int page;
	private final int size;

	/**
	 * Creates a new {@link PageRequest}.
	 * 
	 * @param page
	 * @param size
	 */
	public PageRequest(int page, int size) {
		if (0 > page) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}

		if (0 >= size) {
			throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
		}

		this.page = page;
		this.size = size;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.mycuckoo.repository.Pageable#getPageSize()
	 */
	public int getPageSize() {
		return size;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.mycuckoo.repository.Pageable#getPageNumber()
	 */
	public int getPageNumber() {
		return page;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.mycuckoo.repository.Pageable#getFirstItem()
	 */
	public int getOffset() {
		return page * size;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PageRequest)) {
			return false;
		}

		PageRequest that = (PageRequest) obj;

		boolean pageEqual = this.page == that.page;
		boolean sizeEqual = this.size == that.size;

		return pageEqual && sizeEqual;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;

		result = 31 * result + page;
		result = 31 * result + size;

		return result;
	}
}
