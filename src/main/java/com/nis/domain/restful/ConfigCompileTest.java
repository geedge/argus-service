/**   
* @Title: DfConfigCompile.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午9:36:28 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.nis.domain.Page;

/**
 * @ClassName: DfConfigCompile
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (darnell)
 * @date 2016年8月29日 下午9:36:28
 * @version V1.0
 */
public class ConfigCompileTest extends ConfigCompile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3662236229622771720L;
	private Integer pagesNo; // 当前页码
	private Integer pagesSize;// 每页显示条数
	private Long pagesCount;// 总记录数，设置为“-1”表示不查询总数
	private Page<ConfigCompileTest> page;
	private String searchStartTime;
	private String searchEndTime;

	public Integer getPagesNo() {
		return pagesNo;
	}

	public void setPagesNo(Integer pagesNo) {
		this.pagesNo = pagesNo;
	}

	public Integer getPagesSize() {
		return pagesSize;
	}

	public void setPagesSize(Integer pagesSize) {
		this.pagesSize = pagesSize;
	}

	public Long getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(Long pagesCount) {
		this.pagesCount = pagesCount;
	}

	public Page<ConfigCompileTest> getPage() {
		return page;
	}

	public void setPage(Page<ConfigCompileTest> page) {
		this.page = page;
	}

	public String getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(String searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public String getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}


}
