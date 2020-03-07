package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.DictTypeDto;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.dto.ApplyTravelRequest;
import com.hq.ecmp.mscore.service.IEcmpDictTypeService;
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
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/dict")
public class DictTypeController {
    @Autowired
    private IEcmpDictTypeService iEcmpDictTypeService;

    /**
     * 通过数据字典类型获取 数据字典类型
     * @param  ecmpDictType  数据字典类型信息
     * @return
     */
    @ApiOperation(value = "getDictTypeList",notes = "数据字典类型获取",httpMethod ="POST")
    @PostMapping("/getDictTypeList")
    public ApiResponse<List<EcmpDictType>> getDictTypeList(EcmpDictType ecmpDictType){
        List<EcmpDictType> ecmpDictTypeList = iEcmpDictTypeService.selectEcmpDictTypeList(ecmpDictType);
        return ApiResponse.success(ecmpDictTypeList);
    }
    /**
     * 通过 数据字典类型dictId查询
     * @param  dictTypeDto  数据字典类型详情
     * @return
     */
    @ApiOperation(value = "getDictTypeDetail",notes = "数据字典类型获取",httpMethod ="POST")
    @PostMapping("/getDictTypeDetail")
     public ApiResponse<EcmpDictType> getDictTypeDetail(DictTypeDto dictTypeDto){

         EcmpDictType ecmpDictType = iEcmpDictTypeService.selectEcmpDictTypeById(dictTypeDto.getDictId());
         return  ApiResponse.success(ecmpDictType);
     }
    /**
     * 通过 数据字典类型提交
     * @param  ecmpDictType  数据字典类型新增
     * @return
     */
    @ApiOperation(value = "applyDictTypeCommit",notes = "数据字典新增 ",httpMethod ="POST")
    @PostMapping("/applyDictTypeCommit")
         public ApiResponse  applyDictTypeCommit(@RequestBody EcmpDictType ecmpDictType){
              //新增数据字典类型
        try {
               iEcmpDictTypeService.insertEcmpDictType(ecmpDictType);
              } catch (Exception e) {
                  e.printStackTrace();
                  return ApiResponse.error("新增失败");
              }
              return ApiResponse.success("新增成功");
          }
    /**
     * 通过 数据字典类型修改
     * @param  ecmpDictType  数据字典类型修改
     * @return
     */
     @ApiOperation(value = "applyDicTypeUpdate",notes = "数据字典修改",httpMethod ="POST")
     @PostMapping("/applyDicTypeUpdate")
         public ApiResponse  applyDicTypeUpdate(@RequestBody EcmpDictType ecmpDictType){
        //新增数据字典类型
        try {
            iEcmpDictTypeService.updateEcmpDictType(ecmpDictType);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改失败");
        }
        return ApiResponse.success("修改成功");
    }

    /**
     * 通过 数据字典类型删除
     * @param  ecmpDictType  数据字典类型删除
     * @return
     */
    @ApiOperation(value = "applyDicTypeDelete",notes = "数据字典删除 ",httpMethod ="POST")
    @PostMapping("/applyDicTypeDelete")
    public ApiResponse  applyDicTypeDelete(@RequestBody EcmpDictType ecmpDictType){
        //新增数据字典类型
        try {
            iEcmpDictTypeService.deleteEcmpDictTypeById(ecmpDictType.getDictId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除失败");
        }
        return ApiResponse.success("删除成功");
    }



}
