package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.order.exception.OrderExceptionType;
import com.internship.tmontica_admin.order.exception.OrderValidException;
import com.internship.tmontica_admin.order.model.request.OrderStatusReq;
import com.internship.tmontica_admin.order.model.response.OrderDetailResp;
import com.internship.tmontica_admin.order.model.response.OrderHistoryResp;
import com.internship.tmontica_admin.order.model.response.OrderResp;
import com.internship.tmontica_admin.order.model.response.OrdersByStatusResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /** 오늘의 상태별 주문 현황 가져오기(관리자) */
    @GetMapping("/today")
    public ResponseEntity<OrdersByStatusResp> getTodayOrderByStatus(@RequestParam(value = "status", defaultValue = "ALL")String status,
                                                                    @RequestParam(value = "size", required = false)int size,
                                                                    @RequestParam(value = "page", required = false)int page){
        OrdersByStatusResp ordersByStatusResp = orderService.getTodayOrderByStatusApi(status, size, page);
        return new ResponseEntity<>(ordersByStatusResp, HttpStatus.OK);
    }

    /** 주문 상세 정보 가져오기(관리자) */
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderDetailResp> getOrderDetail(@PathVariable("orderId")int orderId){
        OrderDetailResp orderDetailResp = orderService.getOrderDetailApi(orderId);
        return new ResponseEntity<>(orderDetailResp, HttpStatus.OK);
    }

    /** 주문 내역 검색(관리자) */
    @GetMapping("/history")
    public ResponseEntity<OrderHistoryResp> getOrderHistory(@RequestParam(value = "searchType", required = false)String searchType,
                                                            @RequestParam(value = "searchValue", required = false)String searchValue,
                                                            @RequestParam(value = "startDate", required = false) String startDate,
                                                            @RequestParam(value = "endDate", required = false)String endDate,
                                                            @RequestParam(value = "size", required = false)int size,
                                                            @RequestParam(value = "page", required = false)int page){
        OrderHistoryResp orderHistoryResp = null;
        if(searchType.equals("") && searchValue.equals("") && startDate.equals("") && endDate.equals("")){
            // 전체 내역 보내기
        }else if(searchType.equals("") && searchValue.equals("") && !startDate.equals("") && !endDate.equals("")){
            // 날짜만 적용
        }else {
            // 검색 조건, 날짜 모두 적용
            orderHistoryResp = orderService.getOrderHistory(searchType, searchValue,startDate, endDate, size, page);
        }
        return new ResponseEntity<>(orderHistoryResp, HttpStatus.OK);
    }
}
