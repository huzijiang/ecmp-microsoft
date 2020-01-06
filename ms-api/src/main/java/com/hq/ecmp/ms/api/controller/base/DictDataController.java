package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.DictDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpDictData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
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
public class DictDataController {

    /**
     * 通过数据字典类型获取 数据字典值
     * @param  dictDto  数据字典信息
     * @return
     */
    @ApiOperation(value = "getDictDataByType",notes = "通过数据字典类型获取 数据字典值 ",httpMethod ="POST")
    @PostMapping("/getDictDataByType")
    public ApiResponse<List<EcmpDictData>> getDictDataByType(DictDto dictDto){

        return null;
    }


}
