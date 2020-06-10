package com.hq.ecmp.mscore.service;

import com.hq.api.system.domain.SysUser;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.vo.ApplyInfoDetailVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.UserApplySingleVo;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ApplyInfoBackService
{

    PageResult<UserApplySingleVo> getApplyListPage(SysUser user, Integer pageNum, Integer pageSize);

    ApplyInfoDetailVO getApplyInfoDetail(Long applyId,LoginUser loginUser) throws Exception;
}
