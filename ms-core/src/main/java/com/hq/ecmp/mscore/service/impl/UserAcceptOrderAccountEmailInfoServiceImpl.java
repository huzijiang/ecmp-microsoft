package com.hq.ecmp.mscore.service.impl;


import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.hq.ecmp.mscore.dto.EmailDTO;
import com.hq.ecmp.mscore.dto.EmailUpdateDTO;
import com.hq.ecmp.mscore.mapper.UserAcceptOrderAccountEmailInfoMapper;
import com.hq.ecmp.mscore.service.UserAcceptOrderAccountEmailInfoService;
import com.hq.ecmp.mscore.vo.EmailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;

/**
 * (UserAcceptOrderAccountEmailInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-12 12:09:12
 */
@Service("userAcceptOrderAccountEmailInfoService")
public class UserAcceptOrderAccountEmailInfoServiceImpl implements UserAcceptOrderAccountEmailInfoService {

    @Autowired
    private UserAcceptOrderAccountEmailInfoMapper userAcceptOrderAccountEmailInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param Id 主键
     * @return 实例对象
     */
    @Override
    public  UserAcceptOrderAccountEmailInfo queryById(Integer Id) {
        return userAcceptOrderAccountEmailInfoMapper.queryById(Id);
    }

    /**
     * 查询指定行数据
     *
     * @param userId 用户Id
     * @return 对象列表
     */
    @Override
   public  List<EmailVO> queryEmailByUserId(@Param("用户ID") Long userId){
        return userAcceptOrderAccountEmailInfoMapper.queryEmailByUserId(userId);
    }

    /**
     * 新增数据
     *
     * @param emailDTO 实例对象
     * @return 影响行数
     */
    @Override
    public int insertEmail(EmailDTO emailDTO){
        emailDTO.setCreateTime(DateUtils.getNowDate());
        return userAcceptOrderAccountEmailInfoMapper.insertEmail(emailDTO);
    }

    /**
     * 修改数据
     *
     * @param emailUpdateDTO 实例对象
     * @return 影响行数
     */
    @Override
    public int updateEmail(EmailUpdateDTO emailUpdateDTO){
        emailUpdateDTO.setUpdateTime(DateUtils.getNowDate());
        return userAcceptOrderAccountEmailInfoMapper.updateEmail(emailUpdateDTO);
    }

    /**
     * 通过主键删除数据
     *
     * @param Id 主键
     * @return 影响行数
     */
    @Override
   public  int deleteEmailById(Long Id){
        return userAcceptOrderAccountEmailInfoMapper.deleteEmailById(Id);
    }


}