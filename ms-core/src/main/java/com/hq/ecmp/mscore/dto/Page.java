package com.hq.ecmp.mscore.dto;

import java.io.Serializable;

import lombok.Data;


/**
 * 分页插件
 * @author cm
 *
 */

@Data
public class Page implements Serializable{
	private static final long serialVersionUID = 749308593922343564L;
	private Integer pageSize;
	private Long offset;
	private Integer pageNum;
	private Integer pageCount;
	private Integer totalCount;

	public Page() {
	}

	public Page(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getOffset() {
		if (offset != null) {
			return offset;
		} else if (pageNum != null && pageSize != null) {
			return Long.valueOf((pageNum - 1) * pageSize);
		}
		return null;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageCount() {
		if (pageCount != null) {
			return pageCount;
		} else if (totalCount != null && pageSize != null && pageSize != 0) {
			return totalCount % pageSize == 0 ? totalCount / pageSize
					: totalCount / pageSize + 1;
		}
		return null;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	
}
