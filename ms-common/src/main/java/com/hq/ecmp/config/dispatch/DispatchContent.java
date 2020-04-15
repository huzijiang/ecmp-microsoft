package com.hq.ecmp.config.dispatch;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: zj.hu
 * @Date: 2020-03-23 14:17
 */
@Component
@Data
public class DispatchContent {


    public static int notBackCarGroup;

    public static int backCarGroup;

    @Value("${dispatch.notBackCarGroup}")
    public void setNotBackCarGroup(int notBackCarGroup) {
        notBackCarGroup = notBackCarGroup;
    }

    @Value("${dispatch.backCarGroup}")
    public void setBackCarGroup(int backCarGroup) {
        backCarGroup = backCarGroup;
    }
}
