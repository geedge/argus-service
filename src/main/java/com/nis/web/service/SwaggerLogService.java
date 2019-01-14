package com.nis.web.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.SwaggerLog;
import com.nis.web.dao.SwaggerLogDao;

@Service
public class SwaggerLogService extends CrudService<SwaggerLogDao, SwaggerLog> {

	public Page<SwaggerLog> findSwaggerLogPage(Page<SwaggerLog> page, SwaggerLog sysLog) { // 设置默认时间范围，默认当前月
		if (sysLog.getBeginDate() == null) {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			date = calendar.getTime();
			sysLog.setBeginDate(date);
		}
		if (sysLog.getEndDate() == null) {
			sysLog.setEndDate(new Date());
		}
		return super.findPage(page, sysLog);
	}

}
