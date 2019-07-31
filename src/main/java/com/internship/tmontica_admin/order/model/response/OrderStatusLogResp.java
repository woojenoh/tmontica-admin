package com.internship.tmontica_admin.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusLogResp {
    private String status;
    private String editorId;
    private Date modifiedDate;

}
