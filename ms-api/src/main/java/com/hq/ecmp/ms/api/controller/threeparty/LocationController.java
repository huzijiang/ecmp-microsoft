package com.hq.ecmp.ms.api.controller.threeparty;

import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.ms.api.dto.threeparty.LocationDto;
import com.hq.ecmp.ms.api.vo.threeparty.LocationInfoVo;
import com.hq.ecmp.mscore.domain.DriverHeartbeatInfo;
import com.hq.ecmp.mscore.domain.JourneyNodeInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.service.IDriverHeartbeatInfoService;
import com.hq.ecmp.mscore.service.IJourneyNodeInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;
import com.hq.ecmp.util.MacTools;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/location")
public class LocationController {

	String enterpriseId;
	String licenseContent;
	@Resource
	private IOrderInfoService orderInfoService;
	@Resource
	private IJourneyNodeInfoService journeyNodeInfoService;
	@Autowired
	private IDriverHeartbeatInfoService driverHeartbeatInfoService;

	 @ApiOperation(value = "queryByShortAddress",notes = "地址反查查询 ",httpMethod ="POST")
	    @PostMapping("/queryByShortAddress")
	    public ApiResponse<List<LocationInfoVo>> queryByShortAddress(Map<String, Object> map){
		 //调用云端接口  通过短地址反查地址详情 TODO
		 List<LocationInfoVo> list=new ArrayList<LocationInfoVo>();
	     return ApiResponse.success(list);
	    }


	    @ApiOperation(value = "queryAddress",notes = "司机获取乘客/司机位置信息 ",httpMethod ="POST")
	    @PostMapping("/queryAddress")
	    public ApiResponse<LocationInfoVo> queryAddress(LocationDto locationDto){
			try{
				Map<String,Object> formMap = new HashMap<>();
				List<String> macList = MacTools.getMacList();
				formMap.put("mac",macList.get(0));
				formMap.put("enterpriseId",enterpriseId);
				formMap.put("licenseContent",licenseContent);
				String url = "/service/locateSearchByLongitudeAndLatitude";
				OrderInfo orderInfo = orderInfoService.selectOrderInfoById(locationDto.getOrderId());
				String latitude=null;
				String longitude=null;
				if (locationDto.getUserRole()==0){//司机
					List<DriverHeartbeatInfo> driverHeartbeatInfos = driverHeartbeatInfoService.selectDriverHeartbeatInfoList(new DriverHeartbeatInfo(orderInfo.getDriverId(), orderInfo.getOrderId()));
					if (CollectionUtils.isEmpty(driverHeartbeatInfos)){
						return ApiResponse.error("获取司机位置超时");
					}
					Collections.sort(driverHeartbeatInfos, new Comparator<DriverHeartbeatInfo>() {
						@Override
						public int compare(DriverHeartbeatInfo o1, DriverHeartbeatInfo o2) {
							long i = o2.getCreateTime().getTime()- o1.getCreateTime().getTime();
							return (int)i;
						}
					});
					latitude=driverHeartbeatInfos.get(0).getLatitude()+"";
					longitude=driverHeartbeatInfos.get(0).getLongitude()+"";
				}else{
					JourneyNodeInfo nodeInfo = journeyNodeInfoService.selectJourneyNodeInfoById(orderInfo.getNodeId());
					latitude=nodeInfo.getPlanBeginLatitude();
					longitude=nodeInfo.getPlanBeginLongitude();
				}
				formMap.put("latitude",latitude);
				formMap.put("longitude",longitude);
				String  returnJson = OkHttpUtil.postJson(url,formMap);
				ApiResponse<LocationInfoVo> result = JSONObject.parseObject(returnJson, new TypeToken<ApiResponse<LocationInfoVo>>() {
				}.getType());
				if(ApiResponse.SUCCESS_CODE ==result.getCode()){
					return ApiResponse.success(result.getData());
				}else {
					return ApiResponse.error(result.getMsg());
				}
			}catch (Exception e){
				e.printStackTrace();
				return ApiResponse.error("获取审批详情异常");
			}
	    }

}
