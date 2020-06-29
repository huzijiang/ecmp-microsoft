package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.ApplyStateConstant;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyNodeDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.bo.InvoiceAbleItineraryData;
import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.dto.config.PowerDTO;
import com.hq.ecmp.mscore.service.IApplyApproveResultInfoService;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:17
 */
@RestController
@RequestMapping("/journey")
public class JourneyController {

    private static final Logger logger = LoggerFactory.getLogger(JourneyController.class);

    @Autowired
    private IJourneyInfoService journeyInfoService;
    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IApplyApproveResultInfoService resultInfoService;
    /**
     * 创建行程
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "createJourneyApply",notes = "创建行程",httpMethod ="POST")
    @PostMapping("/createJourneyApply")
    public ApiResponse createJourneyApply(JourneyApplyDto journeyApplyDto, UserDto userDto){

        return null;
    }

    /**
     * 撤消行程
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "行程模块:撤销申请", businessType = BusinessType.OTHER)
    @ApiOperation(value = "cancelJourney",notes = "撤消行程",httpMethod ="POST")
    @PostMapping("/cancelJourney")
    public ApiResponse cancelJourneyApply(@RequestBody JourneyApplyDto journeyApplyDto){
        //撤销行程申请
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            int i = applyInfoService.updateApplyState(journeyApplyDto.getApplyId(), ApplyStateConstant.CANCEL_APPLY,ApproveStateEnum.CANCEL_APPROVE_STATE.getKey(),userId);

            if(i == 1){
                return ApiResponse.success("撤销成功");
            }else {
                return ApiResponse.error("撤销申请失败，请重试");
            }
        } catch (Exception e) {
            logger.error("业务处理异常", e);
            return ApiResponse.error("撤销申请失败，请重试");
        }
    }

    /**
     * 查询用户当前进行中的行程信息
     * @param  userDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserRunningJourneyNumbers",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserRunningJourneyNumbers")
    public ApiResponse getUserRunningJourneyNumbers(@RequestBody(required = false) UserDto userDto){
        //查询用户进行中的行程个数及申请id
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);

        return null;
    }
    /**
     * 查询用户所有行程信息
     * @param  userDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserAllJourneyNumbers",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserAllJourneyNumbers")
    public ApiResponse getUserAllJourneyNumbers(UserDto userDto){
        //查询用户所有行程信息
        JourneyInfo journeyInfo = new JourneyInfo();
      //  journeyInfo.setUserId(userDto.getUserId());
        List<JourneyInfo> journeyInfoList = journeyInfoService.selectJourneyInfoList(journeyInfo);
        return ApiResponse.success(journeyInfoList);
    }

    /**
     *   @author caobj
     *   @Description 根据用车权限获取行程详情
     *   @Date 10:11 2020/3/4
     *   @Param  []
     *   @return com.hq.common.core.api.ApiResponse
     **/
    @ApiOperation(value = "根据用车权限获取行程详情",httpMethod = "POST")
    @RequestMapping("/getItineraryDetail")
    public ApiResponse<JourneyDetailVO> getItineraryDetail(@RequestBody PowerDTO powerDTO){
        try {
            JourneyDetailVO  orderVO = journeyInfoService.getItineraryDetail(powerDTO.getPowerId());
            return ApiResponse.success(orderVO);
        }catch (Exception e){
            logger.error("业务处理异常", e);
            return  ApiResponse.error(e.getMessage());
        }
    }


    /**
     * 查询用户当前进行中的行程列表
     * @param  userDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserJourneys",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserJourneys")
    public ApiResponse<List<JourneyInfo>> getUserJourneys(UserDto userDto){

        return null;
    }

    /**
     * 查询用户当前进行中的行程详细信息
     * @param  journeyApplyDto  用户信息
     * @return
     */
     @ApiOperation(value = "getUserJourneysDetail",notes = "查询用户当前进行中的行程详细信息 ",httpMethod ="POST")
     @PostMapping("/getUserJourneysDetail")
     public ApiResponse<JourneyInfo> getUserJourneysDetail(JourneyApplyDto journeyApplyDto){
        //根据行程id查询行程信息
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
        return ApiResponse.success(journeyInfo);
    }

    /**
     * 获取行程的 分享的链接
     * @param  journeyApplyDto  用户信息
     * @return
     */
    @ApiOperation(value = "getSharedJourneyUrl",notes = "查询用户当前进行中的行程详细信息 ",httpMethod ="POST")
    @PostMapping("/getSharedJourneyUrl")
    public ApiResponse<JourneyInfo> getSharedJourneyUrl(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 获取行程的用车权限
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getJourneyUserCarPower",notes = "获取行程的用车权限 ",httpMethod ="POST")
    @PostMapping("/getJourneyUserCarPower")
    public ApiResponse<JourneyInfo> getJourneyUserCarPower(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 获取行程对应的订单信息
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getJourneyOrders",notes = "获取行程的用车权限 ",httpMethod ="POST")
    @PostMapping("/getJourneyOrders")
    public ApiResponse<JourneyInfo> getJourneyOrders(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 获取行程-行程节点的 的司车调派信息
     * @param  journeyNodeDto  行程-行程节点-信息
     * @return
     */
    @ApiOperation(value = "getJourneyDispatchedInfo",notes = "获取行程-行程节点的 的司车调派信息 ",httpMethod ="POST")
    @PostMapping("/getJourneyDispatchedInfo")
    public ApiResponse<JourneyInfo> getJourneyDispatchedInfo(JourneyNodeDto journeyNodeDto){

        return null;
    }

    /**@author
     * @Date 10:11 2020/3/9
     * @Description 查询用户当前进行中的行程列
     * @return
     */
    @ApiOperation(value = "",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserJourneysList")
    public ApiResponse<List<JourneyVO>> getUserJourneysList(Long userId){
        List<JourneyVO>  journeyList = journeyInfoService.getJourneyList(userId);
        return ApiResponse.success(journeyList);
    }
    /** @author
     *  @Date 10:11 2020/3/9
     *  @Description 查询用户当前进行中的行程个数
     */
    @ApiOperation(value = "getJourneyListCount",notes = "获取当前进行中的行程个数",httpMethod ="POST")
    @PostMapping("/getJourneyListCount")
    public ApiResponse<String> getJourneyListCount(Long userId){
        int count= journeyInfoService.getJourneyListCount(userId);
        return ApiResponse.success("查询成功",count+"");
    }
    /** @author
     *  @Date 10:11 2020/3/9
     *  @Description 判断是否有待确认的行程
     */
    @ApiOperation(value = "getWhetherJourney",notes = "判断是否有待确认的行程",httpMethod ="POST")
    @PostMapping("/getWhetherJourney")
    public ApiResponse<String> getWhetherJourney(){

        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            int count = journeyInfoService.getWhetherJourney(userId);
            if(count!=0){
                return ApiResponse.success("有待确认的行程","1");
            }
            return ApiResponse.success("无待确认的行程","0");
        } catch (Exception e) {
            logger.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }

    }


    /***
     * 当前登陆人支付完成的订单行程（已开发票，未开发票）
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "getInvoiceAbleItinerary",notes = "当前可以开发票的行程",httpMethod ="POST")
    @PostMapping("/getInvoiceAbleItinerary")
    public ApiResponse<PageResult<InvoiceAbleItineraryData>> invoiceAbleItinerary(int pageNum, int pageSize){
        try{
            Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
            return ApiResponse.success(journeyInfoService.getInvoiceAbleItinerary(userId,pageNum,pageSize));
        }catch(Exception e){
            logger.error("当前可以开发票的行程异常",e);
        }
        return null;
    }

    /***
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "历史发票",notes = "历史发票",httpMethod ="POST")
    @PostMapping("/getInvoiceAbleItineraryHistory")
    public ApiResponse<PageResult<InvoiceAbleItineraryData>> getInvoiceAbleItineraryHistory(int pageNum, int pageSize){
        try{
            Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
            return ApiResponse.success(journeyInfoService.getInvoiceAbleItineraryHistory(userId,pageNum,pageSize));
        }catch(Exception e){
            logger.error("历史发票异常",e);
        }
        return ApiResponse.error("ApiResponse");
    }

    @ApiOperation(value = "当前发票的所有行程",notes = "当前发票的所有行程",httpMethod ="POST")
    @PostMapping("/invoiceTripList")
    public ApiResponse<List<InvoiceAbleItineraryData>> invoiceTripList(Long invoiceId){
        try{
            return ApiResponse.success(journeyInfoService.invoiceTripList(invoiceId));
        }catch(Exception e){
            logger.error("当前发票的所有行程异常",e);
        }
        return ApiResponse.error("当前发票的所有行程异常");
    }

    /***
     * 当前发票行程总数
     * @return
     */
    @ApiOperation(value = "getInvoiceItineraryCount",notes = "当前发票行程总数",httpMethod ="POST")
    @PostMapping("/getInvoiceItineraryCount")
    public ApiResponse<Integer> getInvoiceItineraryCount(Long invoiceId){
        try{
            return ApiResponse.success(journeyInfoService.getInvoiceItineraryCount(invoiceId));
        }catch(Exception e){
            logger.error("当前发票行程总数异常",e);
        }
        return ApiResponse.error("ApiResponse");
    }

}
