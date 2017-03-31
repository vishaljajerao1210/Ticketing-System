package com.ticketingSystem.utilities;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.TicketView;

public class JsonPage<T> extends org.springframework.data.domain.PageImpl<T> {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public JsonPage(final List<T> content, final Pageable pageable, final long total) {
    super(content, pageable, total);
}

public JsonPage(final List<T> content) {
    super(content);
}



public JsonPage(final Page<T> page, final Pageable pageable) {
    super(page.getContent(), pageable, page.getTotalElements());
}

@JsonView(TicketView.PageView.class)
public int getTotalPages() {
    return super.getTotalPages();
}

@JsonView(TicketView.PageView.class)
public long getTotalElements() {
    return super.getTotalElements();
}

@JsonView(TicketView.PageView.class)
public boolean hasNext() {
    return super.hasNext();
	}
	
@JsonView(TicketView.PageView.class)
public boolean isLast() {
    return super.isLast();

}

@JsonView(TicketView.PageView.class)
public boolean hasContent() {
    return super.hasContent();
}

@JsonView(TicketView.PageView.class)
public List<T> getContent() {
    return super.getContent();
}
}