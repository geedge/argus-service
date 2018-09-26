package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.TrafficNmsServerStatistic;
import com.nis.domain.restful.TrafficNmsServerStatisticList;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.TrafficNmsServerStatisticService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${servicePath}")
@Api(value = "NmsInfoController", description = "接收nms提交的系统状态等数据,将数据入库供服务推送给大屏")
public class NmsInfoController extends BaseRestController {
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@Autowired
	TrafficNmsServerStatisticService trafficNmsServerStatisticService;

	@RequestMapping(value = "/nms/v1/saveServerStatus", method = RequestMethod.POST)
	@ApiOperation(value = "存储NMS系统上报的服务器状态接口", httpMethod = "POST", response = Map.class, notes = "接收NMS系统上报的服务器状态信息")
	@ApiParam(value = "存储NMS系统上报的服务器状态接口", name = "saveServerStatus", required = true)
	public Map<String, Object> saveServerStatus(
			@RequestBody TrafficNmsServerStatisticList trafficNmsServerStatisticList, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				trafficNmsServerStatisticList);
		try {

			if (trafficNmsServerStatisticList != null && trafficNmsServerStatisticList.getTrafficNmsServerList() != null
					&& trafficNmsServerStatisticList.getTrafficNmsServerList().size() > 0) {
				trafficNmsServerStatisticService.saveNmsInfo(trafficNmsServerStatisticList.getTrafficNmsServerList());
			} else {
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "参数trafficNmsServerList不能为空",
						RestBusinessCode.missing_args.getValue());
			}

		} catch (Exception e) {
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "上报服务器状态信息异常:" + e.getMessage(),
					RestBusinessCode.unknow_error.getValue());
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "上报服务器状态信息成功",
				Constants.IS_DEBUG ? trafficNmsServerStatisticList : null);
	}

}
