/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * dnbData.java
 * 2015-1-27
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package model;

import java.util.List;

/**
 * 实现功能： 
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2015-1-27	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public class DnbData {
	private String period;
	private List<String> ids;
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
}

