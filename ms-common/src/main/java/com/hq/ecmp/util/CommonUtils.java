package com.hq.ecmp.util;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CharterTypeEnum;

import java.util.Date;

/**
 * 业务工具类
 *
 * @author crk
 */
public class CommonUtils {

    /**
     * 获取多日组中的当天是否为半日租或者整日租
     * @param startDate 行程开始时间
     * @param endDate 行程结束时间
     * @param userTime 用车时间，单位天
     * @return
     */
    public static String getCarType(Date startDate, Date endDate, double userTime) {
        String carType ;
        Date nowDate = new Date();
        //判断用车时间是包含0.5天
        if (userTime % 1 > 0) {
            //包含0.5天
            //判断当天是否为起始时间
            if (DateUtils.isSameDay(startDate, nowDate)) {
                if (startDate.compareTo(DateUtils.parseDate(DateUtils.formatDate(startDate, DateUtils.YYYY_MM_DD) + " 12:00:00")) > -1) {
                    carType = CharterTypeEnum.OVERALL_RENT_TYPE.getKey();
                } else {
                    carType = CharterTypeEnum.HALF_DAY_TYPE.getKey();
                }
            }else{
                if (startDate.compareTo(DateUtils.parseDate(DateUtils.formatDate(startDate, DateUtils.YYYY_MM_DD) + " 12:00:00")) > -1) {
                    carType = CharterTypeEnum.OVERALL_RENT_TYPE.getKey();
                } else {
                    if(DateUtils.isSameDay(endDate,nowDate)){
                        carType = CharterTypeEnum.HALF_DAY_TYPE.getKey();
                    }else{
                        carType = CharterTypeEnum.OVERALL_RENT_TYPE.getKey();
                    }
                }
            }

        } else {
            carType = CharterTypeEnum.OVERALL_RENT_TYPE.getKey();
        }

        return carType;
    }

}
