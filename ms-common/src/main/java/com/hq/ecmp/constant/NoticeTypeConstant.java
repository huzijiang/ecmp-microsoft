package com.hq.ecmp.constant;
import lombok.Getter;


/**
 * @author crk
 */
@Getter
public enum NoticeTypeConstant {

    NOTICET_001("通知","1"),
    NOTICET_002("公告","2");


    private String desp;
    private String type;
    NoticeTypeConstant(String desp, String type){
        this.desp = desp;
        this.type = type;
    }
}
