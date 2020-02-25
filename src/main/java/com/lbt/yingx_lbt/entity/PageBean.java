package com.lbt.yingx_lbt.entity;

public class PageBean {
	private int pageNo;//页面
	private int pageRow;//每页的行数
	private int totalRow;//需要显示的总行数
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageRow() {
		return pageRow;
	}
	public void setPageRow(int pageRow) {
		this.pageRow = pageRow;
	}
	public int getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public PageBean(int pageNo, int pageRow, int totalRow) {
		super();
		this.pageNo = pageNo;
		this.pageRow = pageRow;
		this.totalRow = totalRow;
	}
	public PageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PageBean [pageNo=" + pageNo + ", pageRow=" + pageRow
				+ ", totalRow=" + totalRow + "]";
	}
	//计算总页码
	public int getTotalPage(){
		int num = totalRow/pageRow;
		if(totalRow%pageRow!=0){
			num++;
		}
		return num;
	}
	//开始页
	public int getBegin(){
		return (pageNo-1)*pageRow+1;
	}
	//结束页
	public int getEnd(){
		return pageNo*pageRow;
	}
	//使用通用mapper要忽略的数据
	public int getIngore(){return (pageNo-1)*pageRow;}
}
