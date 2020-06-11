package com.hq.ecmp.ms.api.controller.questionnaire;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpQuestionnaire;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.service.IEcmpQuestionnaireService;
import com.hq.ecmp.mscore.service.StatisticsRankingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questionnaire")
public class EcmpQuestionnaireController {
    @Autowired
    private IEcmpQuestionnaireService ecmpQuestionnaireService;

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
}
