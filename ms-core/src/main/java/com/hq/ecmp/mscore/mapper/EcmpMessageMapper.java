package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpMessage;
import com.hq.ecmp.mscore.dto.EcmpMessageDto;
import com.hq.ecmp.mscore.dto.MessageDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * (EcmpMessage)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
@Repository
public interface EcmpMessageMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EcmpMessage queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpMessage> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ecmpMessage 实例对象
     * @return 对象列表
     */
    List<EcmpMessage> queryAll(EcmpMessage ecmpMessage);
    List<EcmpMessage> queryList(EcmpMessageDto ecmpMessage);

    /**
     * 新增数据
     *
     * @param ecmpMessage 实例对象
     * @return 影响行数
     */
    int insert(EcmpMessage ecmpMessage);

    /**
     * 修改数据
     *
     * @param ecmpMessage 实例对象
     * @return 影响行数
     */
    int update(EcmpMessage ecmpMessage);
    int updateList(@Param("list") List<EcmpMessage> ecmpMessage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    List<EcmpMessage> queryMessageList(Map map);

    int queryMessageCount(Map map);

    List<MessageDto> getMessagesForPassenger(@Param("userId") Long userId, @Param("categorys") String categorys);

    List<MessageDto> getRunMessageForDrive(@Param("driverId") Long driverId,@Param("categorys") String categorys);
    List<MessageDto> getRunMessageForDispatcher(@Param("ecmpId") Long ecmpId,@Param("categorys") String category);

    void insertList(@Param("msgList")List<EcmpMessage> msgList);

    void updateByCategoryId(@Param("orderId") Long orderId,@Param("status")  String status,@Param("applyId") Long applyId);
}