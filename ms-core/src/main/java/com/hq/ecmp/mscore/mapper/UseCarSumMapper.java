package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UseCarSumMapper：
 *
 * @author: ll
 * @date: 2020/9/4 16:14
 */
@Repository
public interface UseCarSumMapper {

    List<UseCarSumExportVo> getUseCarSumExportVoList(UseCarSumVo useCarSumVo);

}
