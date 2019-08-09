package com.internship.tmontica_admin.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCountResp {

    private int beforePayment;
    private int afterPayment;
    private int inProduction;
    private int ready;
    private int pickUp;
    private int cancel;
}
