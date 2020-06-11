package com.hq.ecmp.ms.api.controller.questionnaire;


import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.EcmpQuestionnaire;
import com.hq.ecmp.mscore.dto.DriverAppraiseDto;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.IEcmpQuestionnaireService;
import com.hq.ecmp.mscore.service.StatisticsRankingService;
import com.hq.ecmp.mscore.vo.CarListVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.QuestionnaireVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/questionnaire")
public class EcmpQuestionnaireController {
    @Autowired
    private IEcmpQuestionnaireService ecmpQuestionnaireService;
    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "submit",notes = "提交反馈",httpMethod = "POST")
    @PostMapping("/submit")
    public ApiResponse ranking(@RequestBody EcmpQuestionnaire ecmpQuestionnaire){
        try {
            int temp = ecmpQuestionnaireService.insertEcmpQuestionnaire(ecmpQuestionnaire);
            if(temp>0){
                return ApiResponse.success("提交成功，感谢您的反馈");
            }else{
                return ApiResponse.error("提交失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("系统错误");
        }
    }

    @ApiOperation(value = "dispatcherDriverList",notes = "查询调度员所在车队的司机列表",httpMethod = "POST")
    @PostMapping("/dispatcherDriverList")
    public ApiResponse dispatcherDriverList(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            List<DriverQueryResult> drivers = ecmpQuestionnaireService.dispatcherDriverList(loginUser);
            return ApiResponse.success("查询成功",drivers);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation(value = "dispatcherCarList",notes = "查询调度员所在车队的车辆列表",httpMethod = "POST")
    @PostMapping("/dispatcherCarList")
    public ApiResponse dispatcherCarList(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            List<CarInfo> cars = ecmpQuestionnaireService.dispatcherCarList(loginUser);
            return ApiResponse.success("查询成功",cars);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }


    @ApiOperation(value = "dispatcherDriverAppraiseList",notes = "查询调度员所在车队的司机评价列表",httpMethod = "POST")
    @PostMapping("/dispatcherDriverAppraiseList")
    public ApiResponse dispatcherDriverAppraiseList(@RequestBody DriverAppraiseDto driverAppraiseDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            PageResult<QuestionnaireVo> result = ecmpQuestionnaireService.dispatcherDriverAppraiseList(loginUser,driverAppraiseDto);
            return ApiResponse.success("查询成功",result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }


}
