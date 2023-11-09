package com.vam.model;

public class PageDTO {

	/* 페이지 시작 끝*/
	private int pageStart;
	private int pageEnd;
	
	/* 이전, 다음 버튼 존재 유무 */
	private boolean next, prev;
	
	/* 행 전체 개수 */
	private int total;
	
	/* 현재 페이지 번호(pageNum), 행 표시 수(amount) , 검색 키워드(keyword), 검색 종류(type) */
	private Criteria cri;
	
	/* 생성자(클래스 호출 시 각 변수 값 초기화) */
	public PageDTO(Criteria cri, int total) {
		
		/* cri, total init */
		this.cri = cri;
		this.total = total;
		
		/* end of page */
		this.pageEnd = (int) (Math.ceil(cri.getPageNum()/10.0)) * 10;
		
		/* start of page */
		this.pageStart = this.pageEnd - 9;
		
		/* last page num of all */
		int realEnd = (int)(Math.ceil(total *  1.0/ cri.getAmount()));
		
		/* check end of page num */
		if(realEnd < pageEnd) {
			this.pageEnd = realEnd;
		}
		
		/* prev btn value init */
		this.prev = this.pageStart > 1;
		
		/* next btn value init */
		this.next = this.pageEnd < realEnd;
	}

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageEnd() {
		return pageEnd;
	}

	public void setPageEnd(int pageEnd) {
		this.pageEnd = pageEnd;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Criteria getCri() {
		return cri;
	}

	public void setCri(Criteria cri) {
		this.cri = cri;
	}

	@Override
	public String toString() {
		return "PageDTO [pageStart=" + pageStart + ", pageEnd=" + pageEnd + ", next=" + next + ", prev=" + prev
				+ ", total=" + total + ", cri=" + cri + "]";
	}
	
	
}
