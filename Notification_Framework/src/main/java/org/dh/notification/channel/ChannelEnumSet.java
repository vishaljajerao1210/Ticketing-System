package org.dh.notification.channel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ChannelEnumSet {

	public static EnumSet<ChannelEnum> EMAIL =EnumSet.of(ChannelEnum.EMAIL);
	public static EnumSet<ChannelEnum> SMS =EnumSet.of(ChannelEnum.SMS);
	public static EnumSet<ChannelEnum> MOBILE =EnumSet.of(ChannelEnum.APN,ChannelEnum.GCM);
	public static EnumSet<ChannelEnum> ALL =EnumSet.of(ChannelEnum.EMAIL,ChannelEnum.SMS,ChannelEnum.APN,ChannelEnum.GCM);
	
	
	
	
	/**
	 * This method will give you the list of channels
	 * 
	 * @return {@link List} 
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static List<String> values() throws IllegalArgumentException, IllegalAccessException{
		Class<?> channel = ChannelEnumSet.class;
		Field[] fields =channel.getFields();
		List<String> devicesName = new ArrayList<>();
		
		for (Field field : fields) {
			devicesName.add(field.getName());
		}
		
		return devicesName;
	}

}
