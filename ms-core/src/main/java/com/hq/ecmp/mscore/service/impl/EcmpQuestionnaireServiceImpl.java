package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DriverAppraiseDto;
import com.hq.ecmp.mscore.mapper.CarGroupDispatcherInfoMapper;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpQuestionnaireMapper;
import com.hq.ecmp.mscore.service.IEcmpQuestionnaireService;
import com.hq.ecmp.mscore.vo.CarListVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.QuestionnaireVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-06-11
 */
@Service
public class EcmpQuestionnaireServiceImpl implements IEcmpQuestionnaireService
{
    @Autowired
    private EcmpQuestionnaireMapper ecmpQuestionnaireMapper;
    @Autowired
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpQuestionnaire selectEcmpQuestionnaireById(Long id)
    {
        return ecmpQuestionnaireMapper.selectEcmpQuestionnaireById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpQuestionnaire> selectEcmpQuestionnaireList(EcmpQuestionnaire ecmpQuestionnaire)
    {
        return ecmpQuestionnaireMapper.selectEcmpQuestionnaireList(ecmpQuestionnaire);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpQuestionnaire(EcmpQuestionnaire ecmpQuestionnaire)
    {
        ecmpQuestionnaire.setCreateTime(DateUtils.getNowDate());
        return ecmpQuestionnaireMapper.insertEcmpQuestionnaire(ecmpQuestionnaire);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpQuestionnaire(EcmpQuestionnaire ecmpQuestionnaire)
    {
        ecmpQuestionnaire.setUpdateTime(DateUtils.getNowDate());
        return ecmpQuestionnaireMapper.updateEcmpQuestionnaire(ecmpQuestionnaire);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpQuestionnaireByIds(Long[] ids)
    {
        return ecmpQuestionnaireMapper.deleteEcmpQuestionnaireByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpQuestionnaireById(Long id)
    {
        return ecmpQuestionnaireMapper.deleteEcmpQuestionnaireById(id);
    }

    @Override
    public List<DriverQueryResult> dispatcherDriverList(LoginUser loginUser) {
        Long carGroupId = getCarGroupId(loginUser);
        //根据车队id 查询驾驶员列表
        DriverQuery query = new DriverQuery();
        //query.setCarGroupId(carGroupId);
        query.setState("V000");
        /**
         * This bug was fixed by Gandaif on 07/06/2020.
         */
        if (!carGroupId.equals(Long.valueOf(0l))) {
            query.setCarGroupId(carGroupId);
        }
        List<DriverQueryResult> driverQueryResults = driverInfoMapper.queryDriverList(query);
        return driverQueryResults;
    }

    private Long getCarGroupId(LoginUser loginUser) {
        //查询调度员所在车队
        Long userId = loginUser.getUser().getUserId();
        CarGroupDispatcherInfo carGroupDispatcherInfo = CarGroupDispatcherInfo.builder().userId(userId).build();
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        /**
         * This bug was fixed by Gandaif on 07/06/2020.
         */
        if (CollectionUtils.isEmpty(carGroupDispatcherInfos)) {
            //throw new RuntimeException("该用户不是调度员");
            return Long.valueOf(0L);
        }
        return carGroupDispatcherInfos.get(0).getCarGroupId();
    }

    @Override
    public List<CarInfo> dispatcherCarList(LoginUser loginUser) {
        //查询调度员所在车队
        Long carGroupId = getCarGroupId(loginUser);
        /**
         * This bug was fixed by Gandaif on 07/06/2020.
         */
        if (carGroupId.equals(Long.valueOf(0l))) {
            carGroupId = null;
        }
        //根据车队id查询车辆列表
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoListByGroupId(carGroupId, null, "S000", null);
        return carInfos;
    }

    @Override
    public PageResult<QuestionnaireVo> dispatcherDriverAppraiseList(LoginUser loginUser, DriverAppraiseDto driverAppraiseDto) {
        //获取调度员所在车队id
        Long carGroupId = getCarGroupId(loginUser);
        /**
         * This bug was fixed by Gandaif on 07/06/2020.
         */
        if (carGroupId.equals(Long.valueOf(0L))) {
            carGroupId = null;
        }
        //分页查询评价列表
        Integer pageNum = driverAppraiseDto.getPageNum();
        Integer pageSize = driverAppraiseDto.getPageSize();
        String orderNum = driverAppraiseDto.getOrderNum();
        Integer score = driverAppraiseDto.getScore();
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionnaireVo> list = ecmpQuestionnaireMapper.selectEcmpQuestionnaireListByCarGroup(carGroupId,
                driverAppraiseDto.getDriverId(), driverAppraiseDto.getCarId(), orderNum, score);
        PageInfo<EcmpQuestionnaire> pageInfo = new PageInfo(list);
        return new PageResult<QuestionnaireVo>(pageInfo.getTotal(), pageInfo.getPages(), list);
    }
}