package com.internship.tmontica_admin.order.model.response;

import lombok.Data;

@Data
public class StatusCountResp {

    private int beforePayment;
    private int afterPayment;
    private int inProduction;
    private int ready;
    private int pickUp;
    private int cancel;
}
