package org.dh.notification.channel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.dh.notification.channel.ChannelEnum;
import org.dh.notification.channel.TemplateContentTypeEnum;
import org.dh.notification.channel.entity.Views.NEEventsView;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="ne_template")
public class NETemplate extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@JsonView(NEEventsView.class)
	@Column(name="name")
	private String name;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="event_id",referencedColumnName="id")
	private NEEvent event;
	
	@JsonView(NEEventsView.class)
	private ChannelEnum channel;
	
	@JsonView(NEEventsView.class)
	private String tenant;
	
	@JsonView(NEEventsView.class)
	private String subject;
	
	@JsonView(NEEventsView.class)
	@Column(name="content_type",nullable=false)
	private  TemplateContentTypeEnum contentType;
	
	@JsonView(NEEventsView.class)
	@Column(name="content",length=100000)
	@Lob
	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NEEvent getEvent() {
		return event;
	}

	public void setEvent(NEEvent event) {
		this.event = event;
	}	

	public ChannelEnum getChannel() {
		return channel;
	}

	public void setChannel(ChannelEnum channel) {
		this.channel = channel;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public TemplateContentTypeEnum getContentType() {
		return contentType;
	}

	public void setContentType(TemplateContentTypeEnum contentType) {
		this.contentType = contentType;
	}

	@PrePersist
	void preInsert() {
		if(this.getSubject() ==null ||"".equals(this.getSubject())|| this.getSubject().length()<=0)
			this.setSubject(this.getEvent().getName());
	}
}
