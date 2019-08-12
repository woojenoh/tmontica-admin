package com.internship.tmontica_admin.order.model.response;

import com.internship.tmontica_admin.order.OrderStatusType;
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

    public boolean isPickUp(){

        return status.equals(OrderStatusType.PICK_UP.getStatus());
    }
}
