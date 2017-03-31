package com.ticketingSystem.Constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class URLConstants {
	
	@Value("${entrypoint.url}")
	private String URL;

	private String personalTaskURL= URL + "/personaltasks";

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getPersonalTaskURL() {
		return personalTaskURL;
	}

	public void setPersonalTaskURL(String personalTaskURL) {
		this.personalTaskURL = personalTaskURL;
	}
}
