package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.hq.ecmp.mscore.dto.EmailDTO;
import com.hq.ecmp.mscore.dto.EmailUpdateDTO;
import com.hq.ecmp.mscore.vo.EmailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (UserAcceptOrderAccountEmailInfo)表服务接口
 *
 * @author shixin
 * @since 2020-03-12 12:08:07
 */
public interface UserAcceptOrderAccountEmailInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserAcceptOrderAccountEmailInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param userId 用户Id
     * @return 对象列表
     */
    List<EmailVO> queryEmailByUserId(@Param("用户ID") Long userId);

    /**
     * 新增数据
     *
     * @param emailDTO 实例对象
     * @return 影响行数
     */
    int insertEmail(EmailDTO emailDTO);

    /**
     * 修改数据
     *
     * @param emailUpdateDTO 实例对象
     * @return 影响行数
     */
    int updateEmail(EmailUpdateDTO emailUpdateDTO);

    /**
     * 通过主键删除数据
     *
     * @param Id 主键
     * @return 影响行数
     */
    int deleteEmailById(Long Id);
}