package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.DictDto;
import com.hq.ecmp.ms.api.dto.base.DictTypeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpDictData;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.service.IEcmpDictDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 字典信息
 * @Author: ShiXin
 * @Date: 2020-3-4 12:00
 */
@RestController
@RequestMapping("/dict")
public class DictDataController {
    @Autowired
    private IEcmpDictDataService iEcmpDictDataService;
    /**
     * 通过 数据字典类型获取-提供前端调用
     * @param  ecmpDictData  数据字典数据
     * @return ecmpDictDataList
     */
    @ApiOperation(value = "getDictDataByType",notes = "通过数据字典类型获取 数据字典值 ",httpMethod ="POST")
    @PostMapping("/getDictDataByType")
    public ApiResponse<List<EcmpDictData>> getDictDataByType(EcmpDictData ecmpDictData){
        List<EcmpDictData> ecmpDictDataList = iEcmpDictDataService.selectEcmpDictDataByType(ecmpDictData.getDictType());
        return ApiResponse.success(ecmpDictDataList);
    }

    /**
     * 通过数据字典获取 数据字典
     * @param  ecmpDictData  数据字典类型信息
     * @return ecmpDictDataList
     */
    @ApiOperation(value = "getDictList",notes = "数据字典获取",httpMethod ="POST")
    @PostMapping("/getDictList")
    public ApiResponse<List<EcmpDictData>> getDictList(EcmpDictData ecmpDictData){
        List<EcmpDictData> ecmpDictDataList = iEcmpDictDataService.selectEcmpDictDataList(ecmpDictData);
        return ApiResponse.success(ecmpDictDataList);
    }
    /**
     * 通过 数据字典dictId查询
     * @param  ecmpDictData  数据字典类型详情
     * @return  ecmpDictDa
     */
    @ApiOperation(value = "getDictDetail",notes = "数据字典获取",httpMethod ="POST")
    @PostMapping("/getDictDetail")
    public ApiResponse<EcmpDictData> getDictDetail(EcmpDictData ecmpDictData){

        EcmpDictData ecmpDictDa = iEcmpDictDataService.selectEcmpDictDataById(ecmpDictData.getDictCode());
        return  ApiResponse.success(ecmpDictDa);
    }
    /**
     * 通过 数据字典提交
     * @param  ecmpDictData  数据字典新增
     * @return
     */
    @ApiOperation(value = "applyDictCommit",notes = "数据字典新增 ",httpMethod ="POST")
    @PostMapping("/applyDictCommit")
    public ApiResponse  applyDictCommit(@RequestBody EcmpDictData ecmpDictData){
        //新增数据字典类型
        try {
            iEcmpDictDataService.insertEcmpDictData(ecmpDictData);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增失败");
        }
        return ApiResponse.success("新增成功");
    }
    /**
     * 通过 数据字典类型修改
     * @param  ecmpDictData  数据字典类型修改
     * @return
     */
    @ApiOperation(value = "applyDicUpdate",notes = "数据字典修改",httpMethod ="POST")
    @PostMapping("/applyDicUpdate")
    public ApiResponse  applyDicUpdate(@RequestBody EcmpDictData ecmpDictData){
        //新增数据字典类型
        try {
            iEcmpDictDataService.updateEcmpDictData(ecmpDictData);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改失败");
        }
        return ApiResponse.success("修改成功");
    }

    /**
     * 通过 数据字典类型删除
     * @param  ecmpDictData  数据字典类型删除
     * @return
     */
    @ApiOperation(value = "applyDicDelete",notes = "数据字典删除 ",httpMethod ="POST")
    @PostMapping("/applyDicDelete")
    public ApiResponse  applyDicDelete(@RequestBody EcmpDictData ecmpDictData){
        //新增数据字典类型
        try {
            iEcmpDictDataService.deleteEcmpDictDataById(ecmpDictData.getDictCode());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除失败");
        }
        return ApiResponse.success("删除成功");
    }
}
