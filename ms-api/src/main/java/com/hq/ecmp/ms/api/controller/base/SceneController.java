package com.hq.ecmp.ms.api.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.domain.SceneRegimeRelation;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.SceneDTO;
import com.hq.ecmp.mscore.dto.SceneSortDTO;
import com.hq.ecmp.mscore.service.IEcmpDictDataService;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.service.ISceneInfoService;
import com.hq.ecmp.mscore.service.ISceneRegimeRelationService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.SceneDetailVO;
import com.hq.ecmp.mscore.vo.SceneListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户用车场景
 * @author cm
 *
 */
@RestController
@RequestMapping("/scene")
public class SceneController {

    @Autowired
    private ISceneInfoService           sceneInfoService;
    @Autowired
    private TokenService                tokenService;
    @Autowired
    private IRegimeInfoService          regimeInfoService;
    @Autowired
    private ISceneRegimeRelationService sceneRegimeRelationService;
    @Autowired
    private IEcmpDictDataService        iEcmpDictDataService;
    @Value("${thirdService.enterpriseId}") // 企业编号
    private String                      enterpriseId;
    @Value("${thirdService.licenseContent}") // 企业证书信息
    private String                      licenseContent;
    @Value("${thirdService.apiUrl}") // 三方平台的接口前地址
    private String                      apiUrl;

    /**
     * 获取用户的用车场景
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "查询用户所有场景", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getAll", notes = "获取用户的所有可用用车场景", httpMethod = "GET")
    @GetMapping("/getAll")
    public ApiResponse<List<SceneInfo>> getAllScene() {
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<SceneInfo> selectAllSceneSort = sceneInfoService
            .selectAllSceneSort(loginUser.getUser().getUserId());
        return ApiResponse.success(selectAllSceneSort);
    }

    /**
     * 创建用车场景
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "创建用车场景", businessType = BusinessType.INSERT)
    @ApiOperation(value = "saveScene", notes = "创建用车场景", httpMethod = "POST")
    @RequestMapping("/saveScene")
    public ApiResponse saveScene(@RequestBody SceneDTO sceneDTO) {
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        Long ownerCompany = loginUser.getUser().getOwnerCompany();
        try {
            sceneInfoService.saveScene(sceneDTO, userId, ownerCompany);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success("保存失败");
        }
        return ApiResponse.success("保存用车场景成功");
    }

    /**
     * 删除用车场景
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "删除场景", businessType = BusinessType.DELETE)
    @ApiOperation(value = "deleteScene", notes = "删除用车场景", httpMethod = "POST")
    @RequestMapping("/deleteScene")
    public ApiResponse deleteScene(@RequestBody Long sceneId) {
        try {
            SceneDTO sceneDTO = new SceneDTO();
            sceneDTO.setSceneId(sceneId);
            int i = sceneInfoService.deleteSceneInfoById(sceneDTO.getSceneId());
            if (i == 1) {
                return ApiResponse.success("删除成功");
            } else {
                return ApiResponse.error("请先删除该用车场景下的所有制度");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success("删除失败");
        }
    }

    /**
     * 修改用车场景
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "修改场景", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "updateScene", notes = "修改用车场景", httpMethod = "POST")
    @PostMapping("/updateScene")
    public ApiResponse updateScene(@RequestBody SceneDTO sceneDTO) {
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            sceneInfoService.updateScene(sceneDTO, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success("修改场景失败");
        }
        return ApiResponse.success("修改用车场景成功");
    }

    /**
     * 用车场景详情
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "场景详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getSceneDetail", notes = "查询用车场景详情", httpMethod = "POST")
    @PostMapping("/getSceneDetail")
    public ApiResponse<SceneDetailVO> getSceneDetail(@RequestBody Long sceneId) {
        try {
            SceneDTO sceneDTO = new SceneDTO();
            sceneDTO.setSceneId(sceneId);
            //查询场景详情
            SceneDetailVO sceneDetailVO = sceneInfoService.selectSceneDetail(sceneDTO);
            if (ObjectUtils.isEmpty(sceneDetailVO)) {
                return ApiResponse.error("暂无数据");
            } else {
                return ApiResponse.success(sceneDetailVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询场景详情失败");
        }
    }

    /**
     * 分页查询用车场景列表信息（带搜索功能）
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "场景列表分页", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getSceneList", notes = "查询用车场景列表信息", httpMethod = "POST")
    @PostMapping("/getSceneList")
    public ApiResponse<PageResult<SceneListVO>> getSceneList(@RequestBody PageRequest pageRequest) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long ownerCompany = loginUser.getUser().getOwnerCompany();
            System.out.println(":::" + ownerCompany);
            PageResult<SceneListVO> list = sceneInfoService.seleSceneByPage(pageRequest,
                ownerCompany);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询场景列列表信息失败");
        }
    }

    /**
     * 场景排序 上移/下移
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "场景排序", businessType = BusinessType.OTHER)
    @ApiOperation(value = "sortScene", notes = "场景排序 上移/下移", httpMethod = "POST")
    @PostMapping("/sortScene")
    public ApiResponse sortScene(@RequestBody SceneSortDTO sceneSortDTO) {
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            sceneInfoService.sortScene(sceneSortDTO, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改场景顺序失败");
        }
        return ApiResponse.success("修改场景顺序成功");
    }

    /**
     * 获取所有可用的用车场景
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "查询所有场景", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getAllUseScene", notes = "获取所有可用的用车场景", httpMethod = "POST")
    @PostMapping("/getAllUseScene")
    public ApiResponse<List<SceneInfo>> getAllUseScene() {
        try {
            Long companyId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser()
                .getOwnerCompany();
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setEffectStatus("0");
            sceneInfo.setCompanyId(companyId);
            List<SceneInfo> selectSceneInfoList = sceneInfoService.selectSceneInfoList(sceneInfo);
            return ApiResponse.success(selectSceneInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询所有可用用车场景失败");
        }
    }

    /**
     * 场景可选制度
     * @param regimeQueryPo
     * @return
     */
    @Log(title = "场景模块", content = "场景可选制度", businessType = BusinessType.OTHER)
    @ApiOperation(value = "scenarioSelectionSystem", notes = "场景可选制度", httpMethod = "POST")
    @PostMapping("/scenarioSelectionSystem")
    public ApiResponse<List<RegimeVo>> queryRegimeList(@RequestBody RegimeQueryPo regimeQueryPo) {
        RegimeQueryPo regimeQuery = new RegimeQueryPo();
        regimeQuery.setSceneId(regimeQueryPo.getSceneId());
        //所有用车制度
        List<RegimeVo> regimeVoList = regimeInfoService.queryRegimeList(new RegimeQueryPo());
        //场景制度中已经用过的制度
        List<SceneRegimeRelation> sceneRegimeRelation = sceneRegimeRelationService
            .selectSceneRegimeRelationList(new SceneRegimeRelation());
        //选出可以使用的制度
        for (int i = 0; i < regimeVoList.size(); i++) {
            for (int s = 0; s < sceneRegimeRelation.size(); s++) {
                if (regimeVoList.get(i).getRegimeId()
                    .equals(sceneRegimeRelation.get(s).getRegimenId())) {
                    regimeVoList.remove(i);
                    i--;
                    break;
                }
            }
        }
        //如果传了sceneId  则是返回增加时候的放进来的制度数据
        if (regimeQueryPo.getSceneId() != null) {
            //根据sceneId查询对应的制度id集合
            List<RegimeVo> regimeVo = regimeInfoService.queryRegimeList(regimeQuery);
            //把增加的制度数据放进去regimeVoList中
            if (!regimeVo.isEmpty()) {
                for (RegimeVo regime : regimeVo) {
                    regimeVoList.add(regime);
                }
            }
        }
        return ApiResponse.success(regimeVoList);
    }

    /**
     * 场景可选图标
     * @param
     * @return
     */
    @Log(title = "场景模块", content = "场景可选图标", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getSceneIcon", notes = "场景可选图标", httpMethod = "POST")
    @PostMapping("/getSceneIcon")
    public ApiResponse<List<SceneListVO>> queryRegimeList(@RequestBody SceneListVO sceneListVO) {
        SceneListVO sceneLists = new SceneListVO();
        sceneListVO.setDictType("icon");
        //所有场景图标
        List<SceneListVO> ecmpDictDataList = iEcmpDictDataService
            .selectEcmpDictByType(sceneListVO.getDictType());
        //场景图标中已经用过的图标
        List<SceneListVO> sceneList = sceneInfoService.seleSceneByIcon(sceneLists);
        //选出可以使用的制度
        for (int i = 0; i < ecmpDictDataList.size(); i++) {
            for (int s = 0; s < sceneList.size(); s++) {
                if (ecmpDictDataList.get(i).getIcon().equals(sceneList.get(s).getIcon())) {
                    ecmpDictDataList.remove(i);
                    i--;
                    break;
                }
            }
        }
        //如果传了sceneId  则是返回增加时候的放进来的制度数据
        if (sceneListVO.getSceneId() != null) {
            //根据sceneId查询对应的制度id集合
            List<SceneListVO> scene = sceneInfoService.seleSceneByIcon(sceneListVO);
            //把增加的制度数据放进去regimeVoList中
            if (!scene.isEmpty()) {
                for (SceneListVO sceneId : scene) {
                    ecmpDictDataList.add(sceneId);
                }
            }
        }
        return ApiResponse.success(ecmpDictDataList);
    }

    /**
     * 从云端获取车型
     * @return
     */
    @Log(title = "从云端获取车型", content = "从云端获取车型", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getVehicleGrade", notes = "获取平台车型级别：公务级，行政级 等", httpMethod = "POST")
    @PostMapping("/getVehicleGrade")
    public ApiResponse<List<?>> getVehicleGrade() {
        try {
            // MAC地址
            String macAdd = MacTools.getMacList().get(0);
            // 调用云端接口
            Map<String, Object> map = new HashMap<>();
            //企业编号  勇哥说是1000  先写死方便测试 后期做变量
            map.put("enterpriseId", enterpriseId);
            //企业证书信息
            map.put("licenseContent", licenseContent);
            //物理地址
            map.put("mac", macAdd);
            //去云端的路径
            String postJson = OkHttpUtil.postForm(apiUrl + "/basic/carTypes", map);

            JSONObject jsonObjectQuery = JSONObject.parseObject(postJson);
            if (!"0".equals(jsonObjectQuery.getString("code"))) {
                return ApiResponse.error("调用云端接口获取平台车型级别：公务级，行政级 =等失败");
            }
            Object object = jsonObjectQuery.get("data");
            if (null == object) {
                return ApiResponse.success("调用云端接口获取平台车型级别：公务级，行政级 =等为空");
            }
            List<?> list = JSONObject.parseArray(object.toString());
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("调用云端接口获取平台车型级别：公务级，行政级 =等失败");
        }
    }
}
