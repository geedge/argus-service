package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nis.domain.Page;
import com.nis.domain.restful.DataDictionaryName;
import com.nis.domain.restful.DataDictionaryValue;
import com.nis.web.dao.DataDictionaryDao;

@Service
public class DataDictionaryService {
	protected final Logger logger1 = Logger.getLogger(this.getClass());
	@Autowired
	private DataDictionaryDao dataDictionaryDao;

	public Map<String, List<DataDictionaryValue>> getAllDataDict() {
		Map<String, List<DataDictionaryValue>> map = new HashMap<String, List<DataDictionaryValue>>();
		List<DataDictionaryValue> allDictName = dataDictionaryDao.getAllDictName(new DataDictionaryValue());
		for (DataDictionaryValue dictVal : allDictName) {
			if (map.containsKey(dictVal.getDataDictName())) {
				map.get(dictVal.getDataDictName()).add(dictVal);
			} else {
				List<DataDictionaryValue> list = new ArrayList<DataDictionaryValue>();
				list.add(dictVal);
				map.put(dictVal.getDataDictName(), list);
			}
		}
		return map;
	}

	public Page<DataDictionaryValue> getDataDictList(Page<DataDictionaryValue> page, DataDictionaryValue entity) {
		entity.setPage(page);
		page.setList(dataDictionaryDao.getAllDictName(entity));
		return page;
	}

	public List<DataDictionaryName> getDataDictList() {
		return dataDictionaryDao.selAllDictName();
	}

	public String saveDataDict(String dataDictId, String addDictName, String dictVal, String option) {
		if (null != option && option.equals("addName")) {
			DataDictionaryName dataDictionaryName = new DataDictionaryName(addDictName);
			dataDictionaryDao.addDictName(dataDictionaryName);
			return "ok";
		} else if (null != option && option.equals("addValue")) {
			String[] split = dictVal.split(",");
			for (int i = 0; i < split.length; i++) {
				dataDictionaryDao.addDictValue(new DataDictionaryValue(Long.parseLong(dataDictId), split[i]));
			}
			return "ok";
		} else if (null != option && option.equals("addNameAndValue")) {
			DataDictionaryName dataDictionaryName = new DataDictionaryName(addDictName);
			dataDictionaryDao.addDictName(dataDictionaryName);
			Long dictNameId = dataDictionaryName.getDictNameId();
			String[] split = dictVal.split(",");
			for (int i = 0; i < split.length; i++) {
				dataDictionaryDao.addDictValue(new DataDictionaryValue(dictNameId, split[i]));
			}
			return "ok";
		} else {
			return "error";
		}
	}

	public String delDictVal(String id) {
		if (id.contains(",")) {
			id = id.replace("[", "").replace("]", "");
			String[] idArr = id.split(",");
			for (String str : idArr) {
				if (null != str && !str.equals("")) {
					dataDictionaryDao.updateDictValue(new DataDictionaryValue(Long.parseLong(str), 0l));
				}
			}
			return "ok";
		} else {
			return "error";
		}
	}

	public String delDictName(String id) {
		if (id.contains(",")) {
			id = id.replace("[", "").replace("]", "");
			String[] idArr = id.split(",");
			for (String str : idArr) {
				if (null != str && !str.equals("")) {
					dataDictionaryDao.updateDictName(new DataDictionaryName(Long.parseLong(str), 0l));
					DataDictionaryValue dictVal = new DataDictionaryValue();
					dictVal.setDictNameId(Long.parseLong(str));
					dictVal.setIsValid(0l);
					dataDictionaryDao.updateDictValue(dictVal);
				}
			}
			return "ok";
		} else {
			return "error";
		}
	}

	public DataDictionaryValue getDict(String id) {
		Long dictId = null;
		if (id.contains(",")) {
			id = id.replace("[", "").replace("]", "");
			String[] idArr = id.split(",");
			for (String str : idArr) {
				if (null != str && !str.equals("")) {
					dictId = Long.parseLong(str);
				}
			}
			if (null == dictId) {
				return null;
			}
			List<DataDictionaryValue> allDictName = dataDictionaryDao.getAllDictName(new DataDictionaryValue(dictId));
			return allDictName.get(0);
		} else {
			return null;
		}
	}

	public String updateDataDict(String dictVal, String dictId, String option) {
		if (null != option && option.equals("updateVal")) {// 修改值
			dataDictionaryDao.updateDictValue(new DataDictionaryValue(Long.parseLong(dictId), null, dictVal));
			return "ok";
		} else {
			return "error";
		}
	}
}
