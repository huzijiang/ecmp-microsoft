package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.mapper.UserRegimeRelationInfoMapper;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class RegimeInfoServiceImpl implements IRegimeInfoService {

    @Autowired
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
  

    /**
     * 根据用车制度id查询用车值得详细信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RegimeInfo selectRegimeInfoById(Long regimenId) {
        return regimeInfoMapper.selectRegimeInfoById(regimenId);
    }

    /**
     * 查询所有用车制度信息
     * @return
     */
    @Override
    public List<RegimeInfo> selectAll() {
        return regimeInfoMapper.selectAll();
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RegimeInfo> selectRegimeInfoList(RegimeInfo regimeInfo) {
        return regimeInfoMapper.selectRegimeInfoList(regimeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRegimeInfo(RegimeInfo regimeInfo) {
        regimeInfo.setCreateTime(DateUtils.getNowDate());
        return regimeInfoMapper.insertRegimeInfo(regimeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRegimeInfo(RegimeInfo regimeInfo) {
        regimeInfo.setUpdateTime(DateUtils.getNowDate());
        return regimeInfoMapper.updateRegimeInfo(regimeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param regimenIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeInfoByIds(Long[] regimenIds) {
        return regimeInfoMapper.deleteRegimeInfoByIds(regimenIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeInfoById(Long regimenId) {
        return regimeInfoMapper.deleteRegimeInfoById(regimenId);
    }

    /**
     * 根据用户id查询用车制度集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<RegimeInfo> findRegimeInfoListByUserId(Long userId) {
        //根据userId查询regimeId集合
        List<Long> regimeIds = userRegimeRelationInfoMapper.selectIdsByUserId(userId);
        //根据regimeId集合查询RegimeInfo集合
        List<RegimeInfo> regimeInfoList = regimeIds.stream().map(regimeId->regimeInfoMapper.selectRegimeInfoById(regimeId)).collect(Collectors.toList());
        return regimeInfoList;
    }
}
