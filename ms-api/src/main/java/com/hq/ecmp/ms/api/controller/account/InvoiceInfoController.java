package com.hq.ecmp.ms.api.controller.account;


import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.DictTypeDto;
import com.hq.ecmp.ms.api.dto.invoice.InvoiceCodeDto;
import com.hq.ecmp.ms.api.dto.invoice.InvoiceDto;
import com.hq.ecmp.ms.api.vo.threeparty.LocationInfoVo;
import com.hq.ecmp.mscore.bo.InvoiceAbleItineraryData;
import com.hq.ecmp.mscore.domain.EcmpDictType;
import com.hq.ecmp.mscore.domain.InvoiceInfo;
import com.hq.ecmp.mscore.domain.OrderAccountInfo;
import com.hq.ecmp.mscore.domain.OrderInvoiceInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderAccountInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInvoiceInfoMapper;
import com.hq.ecmp.mscore.service.IInvoiceInfoService;
import com.hq.ecmp.mscore.vo.InvoiceDetailVO;
import com.hq.ecmp.mscore.vo.InvoiceHeaderVO;
import com.hq.ecmp.mscore.vo.InvoiceRecordVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.MailUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 发票地址
 * @author shixin
 * @date 2020-3-6
 *
 */
 @RestController
 @RequestMapping("/invoice")
public class InvoiceInfoController {

     private static final Logger logger =LoggerFactory.getLogger(InvoiceInfoController.class);

    @Autowired
    private IInvoiceInfoService invoiceInfoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OrderAccountInfoMapper orderAccountInfoMapper;
    @Autowired
    private OrderInvoiceInfoMapper orderInvoiceInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    /**
     * 企业编号
     */
    @Value("${thirdService.enterpriseId}")
    private String enterpriseId;
    /**
     * 企业证书信息
     */
    @Value("${thirdService.licenseContent}")
    private String licenseContent;
    /**
     * 三方平台的接口前地址
     */
    @Value("${thirdService.apiUrl}")
    private String apiUrl;
    /**I
     * 根据时间区间、开票状态查询发票记录信息
     * @param invoiceByTimeStateDTO
     * @return
     */
    @Log(title = "财务模块", content = "发票记录列表查询",businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceInfoList",notes = "发票记录列表查询",httpMethod = "POST")
    @PostMapping("/getInvoiceInfoList")
    public ApiResponse<PageResult<InvoiceRecordVO>> getInvoiceInfoList(@RequestBody InvoiceByTimeStateDTO invoiceByTimeStateDTO){
        Long  userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
        invoiceByTimeStateDTO.setCreateBy(userId);
        PageResult<InvoiceRecordVO> invoiceInfoList = invoiceInfoService.queryAllByTimeState(invoiceByTimeStateDTO);
        return ApiResponse.success(invoiceInfoList);
    }
    /**
     * 发票信息新增
     * @param invoiceDTO
     * @return
     */
    @Log(title = "财务模块",content = "新增发票信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "invoiceInfoCommit",notes = "新增发票信息",httpMethod = "POST")
    @PostMapping("/invoiceInfoCommit")
     public ApiResponse invoiceInfoCommit( InvoiceDTO invoiceDTO){
        Long  userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();

        //发票信息
        InvoiceInsertDTO invoiceInsertDTO = new InvoiceInsertDTO();
        invoiceInsertDTO.setType(invoiceDTO.getType());
        invoiceInsertDTO.setHeader(invoiceDTO.getHeader());
        invoiceInsertDTO.setTin(invoiceDTO.getTin());
        invoiceInsertDTO.setContent(invoiceDTO.getContent());
        invoiceInsertDTO.setAmount(invoiceDTO.getAmount());
        invoiceInsertDTO.setAcceptAddress(invoiceDTO.getAcceptAddress());
        invoiceInsertDTO.setEmail(invoiceDTO.getEmail());
        invoiceInsertDTO.setCreateBy(userId);
        try {
         invoiceInfoService.insertInvoiceInfo(invoiceInsertDTO);
         Long invoiceId = invoiceInsertDTO.getInvoiceId();
        //发票账期
        List<OrderInvoiceInfo> list = new ArrayList<OrderInvoiceInfo>();
/*
        Long[] periodIds =invoiceDTO.getPeriodId();
*/
            String attr []= invoiceDTO.getPeriodIds().split(",");
            Long[] periodIds = new Long[attr.length];
            for (int i = 0; i < periodIds.length; i++) {
                periodIds[i] = Long.valueOf(attr[i]);
            }
        for (Long periodId : periodIds)
           {
               OrderInvoiceInfo invoicePer = new OrderInvoiceInfo();
               invoicePer.setInvoiceId(invoiceId);
               invoicePer.setAccountId(periodId);
               list.add(invoicePer);
            }
            if(list.size()>0){
            invoiceInfoService.addInvoice(list);
            String str = updateInvoiceUrl(invoiceId,invoiceDTO,list);
            if("发票税务接口返回失败".equals(str)){
                return ApiResponse.error("开票失败");
            }
          //  return ApiResponse.success("成功开发票");
           }
        } catch (Exception e) {
            logger.error("业务处理异常", e);
            return ApiResponse.error("新增失败");
        }
        return ApiResponse.success("开发票成功");
    }


    /***
     * add by liuzb 2020/05/12
     *(发票完成后直接上送数据更新发票表url，后续发票重发使用)
     * @param invoiceId
     */
    private String updateInvoiceUrl(Long invoiceId,InvoiceDTO invoiceDTO,List<OrderInvoiceInfo> list)throws Exception{
        String  userPhone = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getPhonenumber();
        /**接口获取发票税务并返回图片*/
        String json = OkHttpUtil.postForm(apiUrl+"/service/v2/invoice/create", getMap(invoiceDTO,userPhone));
        JSONObject parseObject = JSONObject.parseObject(json);
        if(!"0".equals(parseObject.getString("code"))){
            logger.info("发票税务接口返回失败");
            return "发票税务接口返回失败";
        }
        updateInvoiceInfoOrder(invoiceId,parseObject,list);
        /**发送行程邮件*/
        if("1".equals(invoiceDTO.getToResend())){
            sendMail(invoiceId,invoiceDTO.getEmail());
        }
        return "开发票成功";
    }

    /***
     *
     * @param invoiceId
     * @param email
     * @throws Exception
     */
    private void sendMail(Long invoiceId,String email)throws Exception{
        String message=" <div>\n" +
                "         <span>您的行程单：感谢关注红旗智行</span>\n" +
                "    </div><br>";
        OrderInvoiceInfo orderInvoiceInfo = new OrderInvoiceInfo();
        orderInvoiceInfo.setInvoiceId(invoiceId);
        List<OrderInvoiceInfo> orderInvoiceInfoList  = orderInvoiceInfoMapper.selectOrderInvoiceInfoList(orderInvoiceInfo);
        for(OrderInvoiceInfo data :orderInvoiceInfoList ){
            List<InvoiceAbleItineraryData> key =  journeyInfoMapper.getInvoiceAbleItineraryHistoryKey(data.getAccountId());
            message ="<div>\n" +
                    "    <div>\n" +
                    "         <span>"+message+key.get(0).getActionTime()+" &nbsp;&nbsp;&nbsp;&nbsp;订单金额"+key.get(0).getAmount()+"元</span>\n" +
                    "    </div>\n" +
                    "    <div>\n" +
                    "         <span>"+ key.get(0).getAddress()+"</span>\n" +
                    "    </div>\n" +
                    "    <div>\n" +
                    "         <span>"+key.get(1).getAddress()+"</span>\n" +
                    "    </div>\n" +
                    "<div><br><br>";
        }
        MailUtils.sendMail(email,message,"您有一张发票请查收");
    }
    /***
     *
     * @param invoiceId
     * @param parseObject
     * @param list
     * @throws Exception
     */
    private void updateInvoiceInfoOrder(Long invoiceId,JSONObject parseObject,List<OrderInvoiceInfo> list)throws Exception{
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setInvoiceId(invoiceId);
        invoiceInfo.setInvoiceUrl(((JSONObject) parseObject.get("data")).get("pdfUrl").toString());
        invoiceInfo.setStatus("00");
        invoiceInfoService.updateInvoiceInfo(invoiceInfo);
        for(int i =0;i<list.size();i++){
            OrderAccountInfo orderAccountInfo = new OrderAccountInfo();
            orderAccountInfo.setAccountId(list.get(i).getAccountId());
            orderAccountInfo.setState("S009");
            orderAccountInfoMapper.updateOrderAccountInfo(orderAccountInfo);
        }
    }




    /***
     *
     * @param invoiceDTO
     * @param userPhone
     * @return
     * @throws Exception
     */
    private Map<String,Object> getMap(InvoiceDTO invoiceDTO,String userPhone)throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("enterpriseId",enterpriseId);
        map.put("licenseContent",licenseContent);
        map.put("mac", MacTools.getMacList().get(0));
        map.put("email",invoiceDTO.getEmail());
        map.put("address",null==invoiceDTO.getAcceptAddress() ||"".equals(invoiceDTO.getAcceptAddress())?"0":invoiceDTO.getAcceptAddress());
        map.put("bankAccount",null==invoiceDTO.getContent()||"".equals(invoiceDTO.getContent())?"0":invoiceDTO.getContent());
        map.put("bankName",null==invoiceDTO.getBankName()||"".equals(invoiceDTO.getBankName())?"0":invoiceDTO.getBankName());
        map.put("amount",invoiceDTO.getAmount());
        map.put("phone",null==invoiceDTO.getTelephone()||"".equals(invoiceDTO.getTelephone())?"0":invoiceDTO.getTelephone());
        map.put("taxNum",null==invoiceDTO.getTin()||"".equals(invoiceDTO.getTin())?"0":invoiceDTO.getTin());
        map.put("title",invoiceDTO.getHeader());
        map.put("userPhone",null==userPhone ||"".equals(userPhone)? "0":userPhone);
        return map;
    }


    /**
     * 发票信息详情
     * @param
     * @return
     */
    @Log(title = "财务模块",content = "发票信息详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceInfoDetail",notes = "发票信息详情",httpMethod ="POST")
    @PostMapping("/getInvoiceInfoDetail")
    public ApiResponse<InvoiceDetailVO> getInvoiceInfoDetail(Long invoiceId){
        InvoiceDetailVO invoiceInfoDetail = invoiceInfoService.getInvoiceDetail(invoiceId);
        return  ApiResponse.success(invoiceInfoDetail);
    }



    /**
     * 新增发票抬头
     * @param invoiceHeaderDTO
     * @return
     */
    @Log(title = "财务模块",content = "新增发票抬头", businessType = BusinessType.INSERT)
    @ApiOperation(value = "invoiceHeaderCommit",notes = "新增发票抬头",httpMethod = "POST")
    @PostMapping("/invoiceHeaderCommit")
    public ApiResponse invoiceHeaderCommit(@RequestBody InvoiceHeaderDTO invoiceHeaderDTO){

        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        invoiceHeaderDTO.setCompanyId(companyId);
        try {
            List<InvoiceHeaderVO> invoiceHeaderList = invoiceInfoService.queryInvoiceHeader(companyId);
            if(invoiceHeaderList.size()>0){
                invoiceInfoService.deleteInvoiceHeader();
            }
            invoiceInfoService.insertInvoiceHeader(invoiceHeaderDTO);
        } catch (Exception e) {
            logger.error("业务处理异常", e);
            return ApiResponse.error("新增失败");
        }
        return ApiResponse.success("新增成功");

    }
    /**
     * 发票抬头查询
     * @param
     * @return
     */
    @Log(title = "财务模块",content = "发票抬头查询", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvoiceHeaderList",notes = "发票抬头查询",httpMethod = "POST")
    @PostMapping("/getInvoiceHeaderList")
    public ApiResponse<List<InvoiceHeaderVO>> getInvoiceHeaderList(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        List<InvoiceHeaderVO> invoiceHeaderList = invoiceInfoService.queryInvoiceHeader(companyId);
        return ApiResponse.success(invoiceHeaderList);
    }
    @Log(title = "发票模块",content = "发票重发", businessType = BusinessType.OTHER)
    @ApiOperation(value = "reissueofInvoice",notes = "发票重发",httpMethod = "POST")
    @PostMapping("/reissueofInvoice")
    @RequestMapping(value = "/reissueofInvoice", method = RequestMethod.POST)
    public ApiResponse reissueofInvoice(Long invoiceId,String mailboxes,String toResend) {
        try{
            invoiceInfoService.reissueofInvoice(invoiceId,mailboxes,toResend);
            return ApiResponse.success("发票重发成功");
        }catch(Exception e){
            logger.error("发票重发异常",e);
        }
        return ApiResponse.error("发票重发异常");
    }
 }
