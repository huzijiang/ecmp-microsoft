package com.hq.ecmp.ms.api.dto.invoice;

import lombok.Data;

@Data
public class InvoiceCodeDto {


    String invoiceCode;

    String invoiceNum;

    String pdfUrl;

    String checkCode;
}
