package com.hq.ecmp.vo;//ZimgResult
import lombok.Data;

/**
 * @author xueyong
 */
@Data
public class ZimgResult {

    private boolean ret;
    private ZimgResultInfo info;
    private ZimgResultError error;
}