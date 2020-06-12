package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.IOrderAccountInfoService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import com.hq.ecmp.mscore.vo.OrderAccountVO;
import com.hq.ecmp.mscore.vo.OrderAccountViewVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.format.*;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 账务信息
 * @author shixin
 * @date 2020-3-7
 *
 */
 @RestController
 @RequestMapping("/account")
public class AccountInfoController {

    @Autowired
    private IOrderAccountInfoService iOrderAccountInfoService;

    @Autowired
    private TokenService tokenService;
    /**
     * 网约车账务订单分页
     * @param
     * @return list
     */
    @Log(title = "财务模块",content = "查询账务订单信息", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getAccountViewList",notes = "查询账务订单信息",httpMethod = "POST")
    @PostMapping("/getAccountViewList")
    public ApiResponse<PageResult<OrderAccountViewVO>> getAccountViewList(@RequestBody PageRequest pageRequest){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        PageResult<OrderAccountViewVO> invoiceViewList = iOrderAccountInfoService.getAccountViewList(pageRequest,companyId);
        return ApiResponse.success(invoiceViewList);
    }
    /**
     * 开发票下拉列表
     * @param
     * @return list
     */
    @Log(title = "财务模块",content = "获取为开发票的订单统计列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getAccountList",notes = "获取为开发票的订单统计列表",httpMethod = "POST")
    @PostMapping("/getAccountList")
    public ApiResponse<List<OrderAccountVO>> getAccountList(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        List<OrderAccountVO> invoiceInfoList = iOrderAccountInfoService.getAccountList(companyId);
        return ApiResponse.success(invoiceInfoList);
    }

    /**
     * 网约车财务管理  下载账单
     * @return
     */
    @ApiOperation(value = "downloadBill",notes = "网约车  下载账单",httpMethod ="POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/downloadBill")
    public ApiResponse downloadBill(@RequestHeader("token") String token,
                                    @RequestBody PageRequest pageRequest){

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        HttpServletRequest request = requestAttributes.getRequest();
        // 文件名
        String filename = "*************************.xls";
        try {
            // 写到服务器上
            String path = request.getSession().getServletContext().getRealPath("") + "/" + filename;
            File name = new File(path);
            // 创建写工作簿对象
            WritableWorkbook workbook = Workbook.createWorkbook(name);
            // 工作表
            WritableSheet sheet = workbook.createSheet("地址列表", 0);
            // 设置字体;
            WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat cellFormat = new WritableCellFormat(font);
            // 设置背景颜色;
            cellFormat.setBackground(Colour.WHITE);
            // 设置边框;
            cellFormat.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
            // 设置文字居中对齐方式;
            cellFormat.setAlignment(Alignment.CENTRE);
            // 设置垂直居中;
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 分别给1,5,6列设置不同的宽度;
//            sheet.setColumnView(0, 15);
//            sheet.setColumnView(4, 60);
//            sheet.setColumnView(5, 35);
            // 给sheet电子版中所有的列设置默认的列的宽度;
            sheet.getSettings().setDefaultColumnWidth(20);
            // 给sheet电子版中所有的行设置默认的高度，高度的单位是1/20个像素点,但设置这个貌似就不能自动换行了
            // sheet.getSettings().setDefaultRowHeight(30 * 20);
            // 设置自动换行;
            cellFormat.setWrap(true);
            // 单元格
            Label label0 = new Label(0, 0, "ID", cellFormat);
            Label label1 = new Label(1, 0, "省", cellFormat);
            Label label2 = new Label(2, 0, "市", cellFormat);
            Label label3 = new Label(3, 0, "区", cellFormat);
            Label label4 = new Label(4, 0, "详细地址", cellFormat);
            Label label5 = new Label(5, 0, "创建时间", cellFormat);

            sheet.addCell(label0);
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);
            sheet.addCell(label4);
            sheet.addCell(label5);

            // 给第二行设置背景、字体颜色、对齐方式等等;
            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
            // 设置文字居中对齐方式;
            cellFormat2.setAlignment(Alignment.CENTRE);
            // 设置垂直居中;
            cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat2.setBackground(Colour.WHITE);
            cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
            cellFormat2.setWrap(true);

            // 记录行数
            int n = 1;

            // 查询数据库


            //开始执行写入操作
            workbook.write();
            //关闭流
            workbook.close();


            //下载excel文件
            OutputStream out = null;

            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(filename, "utf-8"));

            // 2.下载
            out = response.getOutputStream();
            String path3 = request.getSession().getServletContext().getRealPath("") + "/" + filename;

            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(path3);

            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return null;
    }

}
