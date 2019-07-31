package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.order.exception.OrderExceptionType;
import com.internship.tmontica_admin.order.exception.OrderValidException;
import com.internship.tmontica_admin.order.model.request.OrderStatusReq;
import com.internship.tmontica_admin.order.model.response.OrderDetailResp;
import com.internship.tmontica_admin.order.model.response.OrdersByStatusResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /** 주문 상태 바꾸기(관리자) */
    @PutMapping("/status")
    public ResponseEntity updateOrderStatus(@RequestBody @Valid OrderStatusReq orderStatusReq, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new OrderValidException(OrderExceptionType.INVALID_STATUS_FORM, bindingResult);
        }
        orderService.updateOrderStatusApi(orderStatusReq);
        return new ResponseEntity(HttpStatus.OK);
    }

    /** 오늘의 주문 상태별로 주문 정보 가져오기(관리자) */
    @GetMapping("/{status:[A-Z]+_?[A-Z]+}")
    public ResponseEntity<List<OrdersByStatusResp>> getOrderByStatus(@PathVariable("status")String status){
        List<OrdersByStatusResp> ordersByStatusResps = orderService.getOrderByStatusApi(status);
        return new ResponseEntity<>(ordersByStatusResps, HttpStatus.OK);
    }

    /** 주문 상세 정보 가져오기(관리자) */
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderDetailResp> getOrderDetail(@PathVariable("orderId")int orderId){
        OrderDetailResp orderDetailResp = orderService.getOrderDetailApi(orderId);
        return new ResponseEntity<>(orderDetailResp, HttpStatus.OK);
    }
}
