package com.nis.web.controller.restful;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.web.service.AuditLogThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DataDictionaryValue;
import com.nis.listener.SystemConfigListener;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DataDictionaryService;

@RestController
//@RequestMapping("${servicePath}")
public class DataDictionaryController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/cfg/v1/findAllDataDict", method = RequestMethod.POST)
	public Map findAllDataDict(DataDictionaryValue dataDictionaryValue, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				dataDictionaryValue);
		Page page = new Page();
		int no = dataDictionaryValue.getPagesNo() == null ? 1 : dataDictionaryValue.getPagesNo();
		int size = dataDictionaryValue.getPagesSize() == null ? 10 : dataDictionaryValue.getPagesSize();
		page.setPageNo(no);
		page.setPageSize(size);
		Page<DataDictionaryValue> dataDictPage = null;
		try {
			dataDictPage = dataDictionaryService.getDataDictList(page, dataDictionaryValue);
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "数据字典获取失败");
		}
		return serviceResponse(thread, start, request, response, "编译配置获取成功", dataDictPage);
	}

	@RequestMapping(value = "/cfg/v1/getAllDictName", method = RequestMethod.GET)
	public Map getAllDictName(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			return serviceResponse(thread, start, request, response, "编译配置获取成功",
					dataDictionaryService.getDataDictList());
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "数据字典获取失败");
		}

	}

	@RequestMapping(value = "/cfg/v1/addDictName", method = RequestMethod.POST)
	public Map addDictName(String dataDictId, String addDictName, String dictVal, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		try {
			String option = "";
			if (null != addDictName && !addDictName.equals("") && null != dictVal && !dictVal.equals("")) {
				option = "addNameAndValue";
			} else if (null != addDictName && !addDictName.equals("")) {
				option = "addName";
			} else if (null != dataDictId && !dataDictId.equals("") && null != dictVal && !dictVal.equals("")) {
				option = "addValue";
			}
			String saveDataDict = dataDictionaryService.saveDataDict(dataDictId, addDictName, dictVal, option);
			if (saveDataDict.equals("error")) {
				return serviceResponse(thread, start, request, response, "添加数据字典失败", "error");
			}
			return serviceResponse(thread, start, request, response, "添加数据字典成功", "ok");
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "添加数据字典失败");
		}

	}

	@RequestMapping(value = "/cfg/v1/delDictVal", method = RequestMethod.POST)
	public Map delDictVal(String dictValId, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		try {
			String saveDataDict = dataDictionaryService.delDictVal(dictValId);
			if (saveDataDict.equals("error")) {
				return serviceResponse(thread, start, request, response, "删除数据字典值失败", "error");
			}
			return serviceResponse(thread, start, request, response, "删除数据字典值成功", "ok");
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "删除数据字典值失败");
		}

	}

	@RequestMapping(value = "/cfg/v1/delDictName", method = RequestMethod.POST)
	public Map delDictName(String dictNameId, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		try {
			String saveDataDict = dataDictionaryService.delDictName(dictNameId);
			if (saveDataDict.equals("error")) {
				return serviceResponse(thread, start, request, response, "删除数据字典名称失败", "error");
			}
			return serviceResponse(thread, start, request, response, "删除数据字典名称成功", "ok");
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "删除数据字典名称失败");
		}
	}

	@RequestMapping(value = "/cfg/v1/getDict", method = RequestMethod.POST)
	public Map getDict(String dictValId, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		try {
			DataDictionaryValue dict = dataDictionaryService.getDict(dictValId);
			if (null == dict) {
				return serviceResponse(thread, start, request, response, "获取数据字典对象失败", "error");
			}
			return serviceResponse(thread, start, request, response, "获取数据字典对象成功", dict);
			// if (null == dict) {
			// map.put("dict", null);
			// } else {
			// map.put("dict", dict);
			// }
			// return "/test/page/dataDict/addOrUpdateDataDict.jsp";
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "获取数据字典对象失败");
		}
	}

	@RequestMapping(value = "/cfg/v1/updateDictName", method = RequestMethod.POST)
	public Map updateDictName(String dictVal, String dictId, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		try {
			String option = "updateVal";
			String saveDataDict = dataDictionaryService.updateDataDict(dictVal, dictId, option);
			if (saveDataDict.equals("error")) {
				return serviceResponse(thread, start, request, response, "修改数据字典失败", "error");
			}
			return serviceResponse(thread, start, request, response, "修改数据字典成功", "ok");
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "修改数据字典失败");
		}

	}

	@RequestMapping(value = "/cfg/v1/setDataDictEffective", method = RequestMethod.POST)
	public Map setDataDictEffective(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		try {
			Map<String, List<DataDictionaryValue>> allDataDict = dataDictionaryService.getAllDataDict();
			if (null != allDataDict && allDataDict.size() > 0) {
				SystemConfigListener.setDictionaryMap(allDataDict);
			}
			return serviceResponse(thread, start, request, response, "数据字典更新成功", "ok");
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, start, "数据字典更新失败");
		}

	}

}
