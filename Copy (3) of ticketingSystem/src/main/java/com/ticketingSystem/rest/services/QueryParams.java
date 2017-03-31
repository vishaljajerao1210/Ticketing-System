package com.ticketingSystem.rest.services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryParams{
		
		public String searchStr;
		
		public Long startDate;
		 
		public Long endDate;
		
		public boolean isSearched;

		public String getSearchStr() {
			return searchStr;
		}

		public void setSearchStr(String searchStr) {
			this.searchStr = searchStr;
		}

		public Long getStartDate() {
			return startDate;
		}

		public void setStartDate(Long startDate) {
			this.startDate = startDate;
		}

		public Long getEndDate() {
			return endDate;
		}

		public void setEndDate(Long endDate) {
			this.endDate = endDate;
		}

		public static QueryParams returnObject(String queryParams) throws JsonParseException, JsonMappingException, IOException{					
			
			ObjectMapper obj = new ObjectMapper();
			return obj.readValue(queryParams, QueryParams.class);
			
		}
		
}
