package cello.papertable.event;

import cello.papertable.model.Page;

/**
 * Page event class contains a page
 * @author Marcello
 *
 */
public class PageEvent {
	
	private Page page;
	
	/**
	 * 
	 * @param page
	 */
	public PageEvent(Page page) {
		this.page = page;
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}

}
