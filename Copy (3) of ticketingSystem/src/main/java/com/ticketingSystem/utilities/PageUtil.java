package com.ticketingSystem.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class PageUtil {

	public PageRequest getPageRequest(int index,int size,Direction direction,String property)
	{
		PageRequest pagerequest=new PageRequest(index,size,direction,property);
		return pagerequest;
	}
	
	
	public PageRequest getPageRequest(int index,int size)
	{
		PageRequest pagerequest=new PageRequest(index,size);
		return pagerequest;
	}
	
}
