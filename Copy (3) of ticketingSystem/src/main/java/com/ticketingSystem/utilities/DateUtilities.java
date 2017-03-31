package com.ticketingSystem.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DateUtilities {

	
	static Logger logger = LoggerFactory.getLogger(DateUtilities.class);

	public static final SimpleDateFormat SEARCH_DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");

	

	public static Date getCurrentDateAndTime() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		Date dateTime = dateFormat.parse(dateFormat.format(date));
		return dateTime;
	}
	
	public static Date getCurrentDateAndTime(Date date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = dateFormat.parse(dateFormat.format(date));
		return dateTime;
	}
	
	
	public static Date getCurrentDateAndTimeFromEpoch(Long epoch) throws ParseException
	{
		Date date = new Date(epoch);
	
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dateTime = format.parse(format.format(date));
		return dateTime;
		
	}
	
	
	
	
	
	
	public static Date getCurrentDate() throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		
		Date date1 = dateFormat.parse(dateFormat.format(date));
		return date1;
	}
	
	public static String getCurrentTime() throws ParseException
	{
		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date=new Date();
		String time = localDateFormat.format(date);
		return time;
	}
	
	public static Date getCurrentDateAndTimeAfterHour() throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		
		Calendar cal = new GregorianCalendar();
	    cal.setTime(date);

	    //Adding 1 Hours to your Date
	    cal.add(Calendar.HOUR_OF_DAY, 1);
	    
	    Date dateTime = dateFormat.parse(dateFormat.format(cal.getTime()));
	    return dateTime;
	}
	
	public static Date getCurrentDateAndTimeAfterFiveMinutes() throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		
		Calendar cal = new GregorianCalendar();
	    cal.setTime(date);

	    //Adding 5 minutes to your Date
	    cal.add(Calendar.MINUTE, 5);
	    
	    Date dateTime = dateFormat.parse(dateFormat.format(cal.getTime()));
	    return dateTime;
	}
	
	public static Date getCurrentDateAfterOneDay(Date date)
	{
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		return date;
	}
	
	
	
	
}
