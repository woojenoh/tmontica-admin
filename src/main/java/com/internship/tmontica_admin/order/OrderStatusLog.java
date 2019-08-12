package com.internship.tmontica_admin.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class OrderStatusLog {
    private int id;
    @NotEmpty
    private String status;
    @NotEmpty
    private String editorId;
    @NotNull
    private int orderId;

    private Date modifiedDate;

    public OrderStatusLog(@NotEmpty String status, @NotEmpty String editorId, @NotNull int orderId) {
        this.status = status;
        this.editorId = editorId;
        this.orderId = orderId;
    }
}
