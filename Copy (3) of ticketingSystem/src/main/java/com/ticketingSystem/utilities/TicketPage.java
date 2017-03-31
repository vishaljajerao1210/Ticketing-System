package com.ticketingSystem.utilities;

import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.TicketView;

public class TicketPage<T> implements Page<T> {

	
	
	
	private Page<T> pageObj;

    public TicketPage(Page<T> pageObj) {
        this.pageObj = pageObj;
    }
	
	@JsonView({TicketView.class})
	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return pageObj.getNumber();
	}

	
	@JsonView({TicketView.class})
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return pageObj.getSize();
	}

	
	@JsonView({TicketView.class})
	@Override
	public int getNumberOfElements() {
		// TODO Auto-generated method stub
		return pageObj.getNumberOfElements();
	}

	
	@JsonView({TicketView.class})
	@Override
	public List<T> getContent() {
		// TODO Auto-generated method stub
		return pageObj.getContent();
	}

	
	@JsonView({TicketView.class})
	@Override
	public boolean hasContent() {
		// TODO Auto-generated method stub
		return pageObj.hasContent();
	}

	
	@JsonView({TicketView.class})
	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return pageObj.getSort();
	}

	@JsonView({TicketView.class})
	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return pageObj.isFirst();
	}

	
	@JsonView({TicketView.class})
	@Override
	public boolean isLast() {
		// TODO Auto-generated method stub
		return pageObj.isLast();
	}

	@JsonView({TicketView.class})
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return pageObj.hasNext();
	}

	@JsonView({TicketView.class})
	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return pageObj.hasPrevious();
	}

	
	@JsonView({TicketView.class})
	@Override
	public Pageable nextPageable() {
		// TODO Auto-generated method stub
		return pageObj.nextPageable();
	}

	@JsonView({TicketView.class})
	@Override
	public Pageable previousPageable() {
		// TODO Auto-generated method stub
		return pageObj.previousPageable();
	}

	@JsonView({TicketView.class})
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return pageObj.iterator();
	}

	@JsonView({TicketView.class})
	@Override
	public int getTotalPages() {
		// TODO Auto-generated method stub
		return pageObj.getTotalPages();
	}

	@JsonView({TicketView.class})
	@Override
	public long getTotalElements() {
		// TODO Auto-generated method stub
		return pageObj.getTotalElements();
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		// TODO Auto-generated method stub
		return pageObj.map(converter);
	}

}
