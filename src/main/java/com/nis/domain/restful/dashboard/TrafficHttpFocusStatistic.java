package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficHttpFocusStatistic {

	private Long stat_id;
	private Integer web_id;
	private Long unique_num;
	private Long link_num;
	private Long c2s_pkt_num;
	private Long s2c_pkt_num;
	private Long c2s_byte_len;
	private Long s2c_byte_len;
	private Integer entrance_id;
	private Date stat_time;
	
	private String time;
	private Long count;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getStat_id() {
		return stat_id;
	}
	public void setStat_id(Long stat_id) {
		this.stat_id = stat_id;
	}
	public Integer getWeb_id() {
		return web_id;
	}
	public void setWeb_id(Integer web_id) {
		this.web_id = web_id;
	}
	public Long getUnique_num() {
		return unique_num;
	}
	public void setUnique_num(Long unique_num) {
		this.unique_num = unique_num;
	}
	public Long getLink_num() {
		return link_num;
	}
	public void setLink_num(Long link_num) {
		this.link_num = link_num;
	}
	public Long getC2s_pkt_num() {
		return c2s_pkt_num;
	}
	public void setC2s_pkt_num(Long c2s_pkt_num) {
		this.c2s_pkt_num = c2s_pkt_num;
	}
	public Long getS2c_pkt_num() {
		return s2c_pkt_num;
	}
	public void setS2c_pkt_num(Long s2c_pkt_num) {
		this.s2c_pkt_num = s2c_pkt_num;
	}
	public Long getC2s_byte_len() {
		return c2s_byte_len;
	}
	public void setC2s_byte_len(Long c2s_byte_len) {
		this.c2s_byte_len = c2s_byte_len;
	}
	public Long getS2c_byte_len() {
		return s2c_byte_len;
	}
	public void setS2c_byte_len(Long s2c_byte_len) {
		this.s2c_byte_len = s2c_byte_len;
	}
	public Integer getEntrance_id() {
		return entrance_id;
	}
	public void setEntrance_id(Integer entrance_id) {
		this.entrance_id = entrance_id;
	}
	public Date getStat_time() {
		return stat_time;
	}
	public void setStat_time(Date stat_time) {
		this.stat_time = stat_time;
	}
	
	
}
