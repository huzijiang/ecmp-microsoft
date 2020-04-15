package com.hq.ecmp.mscore.mapper;
import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.hq.ecmp.mscore.dto.EmailDTO;
import com.hq.ecmp.mscore.dto.EmailUpdateDTO;
import com.hq.ecmp.mscore.vo.EmailVO;
import com.sun.jna.platform.win32.WinDef;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (UserAcceptOrderAccountEmailInfo)表数据库访问层
 *
 * @author shixin
 * @since 2020-03-12 12:04:31
 */
@Repository
public interface UserAcceptOrderAccountEmailInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserAcceptOrderAccountEmailInfo queryById(Integer id);



    /**
     * 通过实体作为筛选条件查询
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 对象列表
     */
    List<UserAcceptOrderAccountEmailInfo> queryAll(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo);
    /**
     * 查询指定行数据
     *
     * @param userId 用户Id
     * @return 对象列表
     */
    List<EmailVO> queryEmailByUserId(Long userId);

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

    Long queryEmailByUserIdCount(Long userId);
}