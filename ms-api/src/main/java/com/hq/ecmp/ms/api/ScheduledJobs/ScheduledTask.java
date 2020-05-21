package com.hq.ecmp.ms.api.ScheduledJobs;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hq.ecmp.constant.ConfigTypeEnum;
import com.hq.ecmp.mscore.domain.EcmpConfig;
import com.hq.ecmp.mscore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.dto.dispatch.DispatchCountCarAndDriverDto;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Autowired
    private IEcmpUserService ecmpUserService;
	@Autowired
	private IEcmpOrgService ecmpOrgService;
    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IEcmpConfigService ecmpConfigService;
    @Autowired
    private IDispatchService dispatchService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
	private IRegimeInfoService regimeInfoService;
    @Autowired
	private ICarInfoService carInfoService;
    @Autowired
    private IEcmpNoticeService iEcmpNoticeService;

    @Value("${schedule.confirmTimeout}")
    private int timeout;

	@Autowired
	private IDriverWorkInfoService driverWorkInfoService;
	@Autowired
	private IDriverInfoService iDriverInfoService;

    @Scheduled(cron = "5 * * * * ?")
    public void testJob(){
        System.out.println("定时任务:testJob"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
    }

    //每天0点5分校验项目是否失效
    @Scheduled(cron = "0 5 0 * * ?")
    public void checkProject(){
        System.out.println("定时任务:checkProject:校验项目是否过期"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        iProjectInfoService.checkProject();
    }

    //每天0点0分校验员工是否已离职
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkDimissionEcmpUser(){
        System.out.println("定时任务:checkDimissionEcmpUser:校验员工是否离职"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        ecmpUserService.checkDimissionEcmpUser();
    }
    //每10分钟校验申请单是否过期
    @Scheduled(cron = "0 */10 * * * ?")
    public void checkApplyExpired(){
        System.out.println("定时任务:checkApplyExpired:校验申请单是否过期"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        applyInfoService.checkApplyExpired();
    }

	/**
	 * 每五分钟判断订单（自有车或者未派车的订单）是否过期,约车中状态不做判断，由云端回调过期来处理
	 */
	@Scheduled(cron = "0 0/5 * * * ? ")
    public void checkOrderIsExpired(){
        log.info("定时任务:checkOrderIsExpired:校验订单是否过期开始,{}",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		try {
			orderInfoService.checkOrderIsExpired();
		} catch (Exception e) {
			log.error("校验订单是否过期定时任务执行异常");
			e.printStackTrace();
		}
		log.info("定时任务:checkOrderIsExpired:校验订单是否过期结束,{}", DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
	}

	/**
	 * 每天0点10分校验制度是否已过期
	 */
	@Scheduled(cron = "0 10 0 * * ? ")
	public void checkRegimenExpired(){
		System.out.println("定时任务:checkRegimenExpired:校验制度是否过期开始"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		regimeInfoService.checkRegimenExpired();
		System.out.println("定时任务:checkRegimenExpired:校验制度是否过期结束"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
	}

	/**
	 * 每天0点15分检验车辆状态
	 */
	@Scheduled(cron = "0 15 0 * * ? ")
	public void checkCarState(){
		log.info("定时任务:checkCarState:校验车辆状态开始,{}",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		long start = System.currentTimeMillis();
		try {
			carInfoService.checkCarState();
		} catch (Exception e) {
			log.error("校验车辆状态异常");
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		log.info("定时任务:checkCarState:校验车辆状态结束,{}，耗时：{}",DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()),end-start);
	}


	/**
	 * 自动调度 每五分钟一次
	 *
	 * @throws Exception
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void autoDispatch() {
		log.info("定时任务:autoDispatch:自动调度" + DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT, new Date()));

		EcmpConfig ecmpConfig = new EcmpConfig(ConfigTypeEnum.DISPATCH_INFO.getConfigKey(),null);
		List<EcmpConfig> ecmpConfigs = ecmpConfigService.selectEcmpConfigList(ecmpConfig);
		for (EcmpConfig ecmpConfig1 :
				ecmpConfigs) {
			// 查询企业设置是否开启了自动调度
			if (ecmpConfigService.checkAutoDispatch(ecmpConfig1.getCompanyId())) {
				// 虚拟一个调度员来处理自动调度 编号 1
				Long autoDispatchUserId = new Long(1);
				// 查询所有处于待调度订单
				List<DispatchOrderInfo> queryAllWaitDispatchList = orderInfoService.queryAllWaitDispatchList(1L, true,ecmpConfig1.getCompanyId());
				if (null != queryAllWaitDispatchList && queryAllWaitDispatchList.size() > 0) {
					for (DispatchOrderInfo dispatchOrderInfo : queryAllWaitDispatchList) {
						Long orderId = dispatchOrderInfo.getOrderId();
						//订单用车提交时间时间是否大于五分钟
						Date createTime = dispatchOrderInfo.getCreateTime();
						if (DateFormatUtils.compareDateInterval(createTime, 5)) {
							continue;
						}
						log.info("订单【" + orderId + "】开始自动派单");
						String redisLockKey = "dispatch_" + orderId;
						// 查询是否存在调度员已经在进行操作
						Object o = redisUtil.get(redisLockKey);
						if (null != o && !autoDispatchUserId.equals(Long.parseLong(o.toString()))) {
							continue;
						}
						String useCarMode = dispatchOrderInfo.getUseCarMode();
						if (StringUtil.isNotEmpty(useCarMode)) {
							// 对自动调度上锁
							redisUtil.set(redisLockKey, autoDispatchUserId.toString());
							try {
								String[] split = useCarMode.split(",");
								List<String> asList = Arrays.asList(split);
								if (asList.contains(CarConstant.USR_CARD_MODE_HAVE)) {
									// 如果自有车 网约车都有 则先走自有车调度 自有车调度失败再走网约车调度
									DispatchCountCarAndDriverDto dispatchCountCarAndDriverDto = new DispatchCountCarAndDriverDto();
									dispatchCountCarAndDriverDto.setOrderNo(orderId.toString());
									ApiResponse<DispatchResultVo> autoDispatch = dispatchService
											.autoDispatch(dispatchCountCarAndDriverDto);
									log.info("订单[" + orderId + "]自动派单-自有车 返回可用车辆和可用驾驶员:" + JSONObject.toJSONString(autoDispatch));
									if (!autoDispatch.isSuccess()) {
										if (asList.contains(CarConstant.USR_CARD_MODE_NET)) {
											// 网约车派车
											log.info("订单[" + orderId + "]自动派单-网约车派车:订单编号:" + orderId);
											orderInfoService.platCallTaxiParamValid(orderId, String.valueOf(autoDispatchUserId),
													null);
										} else {
											redisUtil.delKey(redisLockKey);
										}
										continue;
									}
									DispatchResultVo data = autoDispatch.getData();
									// 自有车调度
									if (null == data.getDriverList() || null == data.getDriverList().get(0) || null == data.getCarList() || null == data.getCarList().get(0)) {
										//没有可用车辆和可用驾驶员
										log.info("订单[" + orderId + "]自动派单-自有车派车 没有可用车辆或驾驶员 ");
										if (asList.contains(CarConstant.USR_CARD_MODE_NET)) {
											orderInfoService.platCallTaxiParamValid(orderId, String.valueOf(autoDispatchUserId),
													null);
										} else {
											redisUtil.delKey(redisLockKey);
										}
										continue;
									}
									log.info("订单[" + orderId + "]自动派单-自有车派车 选择的驾驶员编号:" + data.getDriverList().get(0).getDriverId()
											+ "  选择的车辆编号:" + data.getCarList().get(0).getCarId());
									boolean ownCarSendCar = orderInfoService.ownCarSendCar(orderId,
											data.getDriverList().get(0).getDriverId(), data.getCarList().get(0).getCarId(),
											autoDispatchUserId);
									if (ownCarSendCar) {
										// 调度成功 释放自动调度锁
										redisUtil.delKey(redisLockKey);
										continue;
									}
								}

								if (asList.size() == 1 && CarConstant.USR_CARD_MODE_NET.equals(asList.get(0))) {
									// 只有网约车
									log.info("订单[" + orderId + "]自动派单-网约车派车:订单编号:" + orderId);
									orderInfoService.platCallTaxiParamValid(orderId, String.valueOf(autoDispatchUserId),
											null);
									continue;
								}

							} catch (Exception e) {
								log.error("订单【" + orderId + "】自动派单异常:" + JSONObject.toJSONString(e));
								//释放自动派单锁
								redisUtil.delKey(redisLockKey);
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

	}


    //后台公告管理通过发布时间与结束时间做状态修改
	@Scheduled(cron = "0 0/1 * * * ? ")
    public void  announcementManagementTimingTask (){
        log.info("定时任务:announcementManagementTimingTask:通过发布时间与结束时间做状态修改StartTime:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        try {
            iEcmpNoticeService.announcementTask();
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.info("定时任务:announcementManagementTimingTask:通过发布时间与结束时间做状态修改EndTime:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
    }

    //从云端获取一年的节假日修改本地数据的cloud_work_date_info表
	//@Scheduled(cron = "0 0/3 * * * ? ")
    @Scheduled(cron = "0 0 0 * * ?")
	public void  SchedulingTimingTask (){
		log.info("定时任务:SchedulingTimingTask:通过云端获取的时间修改本地cloud_work_date_info假期StartTime:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		try {
			driverWorkInfoService.SchedulingTimingTask();
		}catch (Exception e) {
			e.printStackTrace();
		}
		log.info("定时任务:SchedulingTimingTask:通过云端获取的时间修改本地cloud_work_date_info假期EndTime:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
	}

	/**
	 * 过12小时，自动确认行程,目前是一小时，测试完成要改成12小时
	 */
	@Scheduled(cron = "0 0/20 * * * ? ")
	public void confirmOrderJourneyAuto(){
		log.info("定时任务:confirmOrderJourneyAuto:自动确认行程开始,时间{}", DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		try {
			orderInfoService.confirmOrderJourneyAuto(timeout);
		} catch (Exception e) {
			log.error("自动确认行程定时任务执行异常");
			e.printStackTrace();
		}
		log.info("定时任务:confirmOrderJourneyAuto:自动确认行程结束,时间{}", DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
	}

	//定时任务独立审核
	@Scheduled(cron = "0 0/1 * * * ? ")
	public void  SchedulingIndependentTask (){
		log.info("定时任务:SchedulingIndependentTask:定时查询独立审核结果:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		try {
			ecmpOrgService.selectIndependentCompanyApplyState();
		}catch (Exception e) {
			e.printStackTrace();
		}
		log.info("定时任务:SchedulingIndependentTask:定时查询独立审核结果:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
	}


	/***
	 *定时更新外聘驾驶员，借调驾驶员状态
	 * add by liuzb
	 */
	@Scheduled(cron = "0 0/1 * * * ? ")
	public void  ScheduUpdateDriverStatusTask (){
		log.info("定时任务:ScheduUpdateDriverStatusTask:定时更新外聘驾驶员，借调驾驶员状态开始:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
		try {
			iDriverInfoService.updateDriverStatusService();
		}catch (Exception e) {
			log.error("ScheduUpdateDriverStatusTask error",e);
		}
		log.info("定时任务:ScheduUpdateDriverStatusTask:定时更新外聘驾驶员，借调驾驶员状态结果:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
	}
}