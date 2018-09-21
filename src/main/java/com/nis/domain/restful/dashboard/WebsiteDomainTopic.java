package com.nis.domain.restful.dashboard;

import java.util.Date;

public class WebsiteDomainTopic {

	private Long id;			//	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	private Long websiteServiceId;			//	  `website_service_id` bigint(20) DEFAULT NULL COMMENT '网站服务Id',
	private String domain;			//	  `domain` varchar(200) DEFAULT NULL COMMENT '域名',
	private Long topicId;			//	  `topic_id` bigint(20) DEFAULT NULL COMMENT '主题Id',
	private	Date createTime;		//	  `create_time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
	private Integer creatorId;			//	  `creator_id` int(11) DEFAULT NULL,
	private Integer isValid;			//	  `is_valid` int(1) DEFAULT NULL,
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWebsiteServiceId() {
		return websiteServiceId;
	}
	public void setWebsiteServiceId(Long websiteServiceId) {
		this.websiteServiceId = websiteServiceId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	
}
