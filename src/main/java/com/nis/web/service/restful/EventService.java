package com.nis.web.service.restful;

import com.nis.domain.Page;
import com.nis.domain.restful.EventKeyProtection;
import com.nis.domain.restful.EventMonitorOrBlock;
import com.nis.util.CalendarUtils;
import com.nis.util.DateUtils;
import com.nis.web.dao.EventServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GouGe
 * @data 2018/12/24 15:30
 */
@Service
public class EventService {
    @Autowired
    EventServiceDao eventServiceDao;

    protected final Logger logger = Logger.getLogger(SystemHomePageService.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 重点保障事件
     *
     * @param eventKeyProtection
     * @return
     * @throws ParseException
     */
    public Page<EventKeyProtection> getEventsKeyProtection(EventKeyProtection eventKeyProtection) throws ParseException {
        List<EventKeyProtection> list = new ArrayList();
        Page<EventKeyProtection> page = new Page<>();
        String searchReportStartTime = eventKeyProtection.getSearchReportStartTime();
        String searchReportEndTime = eventKeyProtection.getSearchReportEndTime();
        String searchTaskStartTime = eventKeyProtection.getSearchTaskStartTime();
        String searchTaskEndTime = eventKeyProtection.getSearchTaskEndTime();
        String taskName = eventKeyProtection.getTaskName();
        long taskId = eventKeyProtection.getTaskId();
        int countParamEmpty = 0;
        try{
        logger.info("开始获取重点保障事件信息,参数为{"
                + ((null == searchReportStartTime && !"".equals(searchReportStartTime)) ? "无"+countParamEmpty++ : format.parse(searchReportStartTime))
                + "},{"
                + ((null == searchReportEndTime && !"".equals(searchReportEndTime)) ? "无"+countParamEmpty++ : format.parse(searchReportEndTime))
                + "},{"
                + ((null == searchTaskStartTime && !"".equals(searchTaskStartTime)) ? "无"+countParamEmpty++ : format.parse(searchTaskStartTime))
                + "},{"
                + ((null == searchTaskEndTime && !"".equals(searchTaskEndTime)) ? "无"+countParamEmpty++ : format.parse(searchTaskEndTime))
                + "},{"
                + ((null == taskName && !"".equals(taskName)) ? "无"+countParamEmpty++ : taskName)
                + "},{"
                + ((null == String.valueOf(taskId) && !"".equals(String.valueOf(taskId))) ? "无"+countParamEmpty++ : (long)taskId));
        }catch(Exception ex){
        	logger.error("时间参数处理转换异常,{}", ex);
        	 String rangeOfday = CalendarUtils.getRangeOfday(DateUtils.getDateTime());
             String startTemp = CalendarUtils.getRangeNhourOfTime(rangeOfday, -24);
             eventKeyProtection = new EventKeyProtection();
             eventKeyProtection.setSearchReportStartTime(startTemp);
             logger.info("获取重点保障事件API请求参数转换异常,默认查询昨天"+startTemp+"至今数据");
        }
        if(countParamEmpty == 6){
        	String rangeOfday = CalendarUtils.getRangeOfday(DateUtils.getDateTime());
            String startTemp = CalendarUtils.getRangeNhourOfTime(rangeOfday, -24);
            eventKeyProtection = new EventKeyProtection();
            eventKeyProtection.setSearchReportStartTime(startTemp);
            logger.info("获取重点保障事件API请求参数为空,默认查询昨天"+startTemp+"至今数据");
        }
        try {
            list = eventServiceDao.getEventsKeyProtection(eventKeyProtection);
        } catch (Exception ex) {
            logger.error("重点保障事件查询异常,{}", ex);
        }
        page.setList(list);
        return page;
    }

    /**
     * 事件监测、封堵
     *
     * @param eventMonitorOrBlock
     * @return
     */
    public Page<EventMonitorOrBlock> getEventsMonitorOrBlock(EventMonitorOrBlock eventMonitorOrBlock,String logMark) {
        List<EventMonitorOrBlock> list = new ArrayList();
        Page<EventMonitorOrBlock> page = new Page<>();
        String searchReportStartTime = eventMonitorOrBlock.getSearchReportStartTime();
        String searchReportEndTime = eventMonitorOrBlock.getSearchReportEndTime();
        String searchTaskStartTime = eventMonitorOrBlock.getSearchTaskStartTime();
        String searchTaskEndTime = eventMonitorOrBlock.getSearchTaskEndTime();
        String taskName = eventMonitorOrBlock.getTaskName();
        long taskId = eventMonitorOrBlock.getTaskId();    
        int countParamEmpty = 0;
        if (taskName != null) {
            eventMonitorOrBlock.setTaskName(taskName.trim());
        }
        try{
        logger.info("开始获取"+logMark+",参数为{"
                + ((null == searchReportStartTime && !"".equals(searchReportStartTime)) ? "无"+countParamEmpty++ : format.parse(searchReportStartTime))
                + "},{"
                + ((null == searchReportEndTime && !"".equals(searchReportEndTime)) ? "无"+countParamEmpty++ : format.parse(searchReportEndTime))
                + "},{"
                + ((null == searchTaskStartTime && !"".equals(searchTaskStartTime)) ? "无"+countParamEmpty++ : format.parse(searchTaskStartTime))
                + "},{"
                + ((null == searchTaskEndTime && !"".equals(searchTaskEndTime)) ? "无"+countParamEmpty++ : format.parse(searchTaskEndTime))
                + "},{"
                + ((null == taskName && !"".equals(taskName)) ? "无"+countParamEmpty++ : taskName)
                + "},{"
                + ((null == String.valueOf(taskId) && !"".equals(String.valueOf(taskId))) ? "无"+countParamEmpty++ : (long)taskId));
        }catch(Exception ex){
        	 logger.error("时间参数处理转换异常,{}", ex);
        	 String rangeNhourOfTime = CalendarUtils.getRangeOfhour(DateUtils.getDateTime());
             String startTemp = CalendarUtils.getRangeNhourOfTime(rangeNhourOfTime, -1);
             int typeId = eventMonitorOrBlock.getReportType();
             eventMonitorOrBlock = new EventMonitorOrBlock();
             eventMonitorOrBlock.setSearchReportStartTime(startTemp);
             eventMonitorOrBlock.setReportType(typeId);
             logger.info("获取"+logMark+"API请求参数转换异常,默认查询上一小时"+startTemp+"到现在的数据");
        }
        if(countParamEmpty==6){
        	 String rangeNhourOfTime = CalendarUtils.getRangeOfhour(DateUtils.getDateTime());
             String startTemp = CalendarUtils.getRangeNhourOfTime(rangeNhourOfTime, -1);
             int typeId = eventMonitorOrBlock.getReportType();
             eventMonitorOrBlock = new EventMonitorOrBlock();
             eventMonitorOrBlock.setSearchReportStartTime(startTemp);
             eventMonitorOrBlock.setReportType(typeId);
             logger.info("获取"+logMark+"API请求参数为空,默认查询上一小时"+startTemp+"到现在的数据");
        }
        try {
            list = eventServiceDao.getEventsMonitorOrBlock(eventMonitorOrBlock);
        } catch (Exception ex) {
            logger.error(logMark+"查询异常,{}", ex);
        }
        page.setList(list);
        return page;
    }
}
