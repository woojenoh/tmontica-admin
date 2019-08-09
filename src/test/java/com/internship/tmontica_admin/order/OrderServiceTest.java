package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.option.Option;
import com.internship.tmontica_admin.option.OptionDao;
import com.internship.tmontica_admin.order.model.request.OrderStatusReq;
import com.internship.tmontica_admin.order.model.response.*;
import com.internship.tmontica_admin.point.PointService;
import com.internship.tmontica_admin.security.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OptionDao optionDao;
    @Mock
    private PointService pointService;
    @Mock
    private JwtService jwtService;

    private Order order1;
    private Order order2;
    List<OrderMenusResp> menus;
    List<OrderMenusResp> menus2;

    @Before
    public void setUp() throws Exception {
        order1 = new Order(1, new Date(),"현장결제", 3400, 1000, 2400, "미결제", "testid","");
        order2 = new Order(2, new Date(),"현장결제", 3000, 0, 3000, "준비중", "testid","");

        menus = new ArrayList<>();
        menus.add(new OrderMenusResp(1,"americano","아메리카노","2__1/3__1","asdf/asdf.png",1,1300));
        menus.add(new OrderMenusResp(2,"latte","카페라떼","1__1/3__1","asdf/asdf.png",1,1800));

        menus2 = new ArrayList<>();
        menus2.add(new OrderMenusResp(1,"americano","아메리카노","2__1/3__1","asdf/asdf.png",1,1300));
        menus2.add(new OrderMenusResp(2,"latte","카페라떼","1__1/3__1","asdf/asdf.png",1,1800));

    }

    @Test
    public void 주문상태변경_포인트적립(){
        // given
        List<Integer> orderIds = new ArrayList<>();
        orderIds.add(1);
        orderIds.add(2);
        OrderStatusReq orderStatusReq = new OrderStatusReq(orderIds, "픽업완료");

        when(jwtService.getUserInfo("userInfo")).thenReturn("{\"id\":\"testid\"}");

        List<OrderStatusLogResp> orderStatusLogRespList = new ArrayList<>();
        orderStatusLogRespList.add(new OrderStatusLogResp("미결제", "admin", new Date()));
        orderStatusLogRespList.add(new OrderStatusLogResp("결제완료", "admin", new Date()));

        when(orderDao.getOrderStatusLogByOrderId(anyInt())).thenReturn(orderStatusLogRespList);
        when(orderDao.getOrderByOrderId(anyInt())).thenReturn(order1);

        // when
        orderService.updateOrderStatusApi(orderStatusReq);

        // then
        verify(pointService, atLeastOnce()).updateUserPoint(any());
        verify(orderDao, times(orderStatusReq.getOrderIds().size())).updateOrderStatus(anyInt(), anyString());
        verify(orderDao, times(orderStatusReq.getOrderIds().size())).addOrderStatusLog(any());
    }

    @Test
    public void 주문상태변경(){
        // given
        List<Integer> orderIds = new ArrayList<>();
        orderIds.add(1);
        orderIds.add(2);
        OrderStatusReq orderStatusReq = new OrderStatusReq(orderIds, "준비중");

        when(jwtService.getUserInfo("userInfo")).thenReturn("{\"id\":\"testid\"}");

        List<OrderStatusLogResp> orderStatusLogRespList = new ArrayList<>();
        orderStatusLogRespList.add(new OrderStatusLogResp("미결제", "admin", new Date()));
        orderStatusLogRespList.add(new OrderStatusLogResp("결제완료", "admin", new Date()));

        when(orderDao.getOrderStatusLogByOrderId(anyInt())).thenReturn(orderStatusLogRespList);

        // when
        orderService.updateOrderStatusApi(orderStatusReq);

        // then
        verify(orderDao, times(orderStatusReq.getOrderIds().size())).updateOrderStatus(anyInt(), anyString());
        verify(orderDao, times(orderStatusReq.getOrderIds().size())).addOrderStatusLog(any());
    }

    @Test
    public void 오늘의_상태별_주문현황_가져오기_상태문자열없을때ALL(){
        // given
        StatusCountResp statusCountResp = new StatusCountResp(2,3,4,5,6,1);
        when(orderDao.getTodayStatusCount()).thenReturn(statusCountResp);

        when(orderDao.getTodayOrderCnt()).thenReturn(1);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        when(orderDao.getTodayOrders(anyInt(),anyInt())).thenReturn(orders);

        menus리스트_옵션문자열_이미지url_셋팅();

        when(orderDao.getOrderDetailByOrderId(anyInt())).thenReturn(menus);

        // when
        OrdersByStatusResp ordersByStatusResp = orderService.getTodayOrderByStatusApi("ALL",10,1);

        // then
        verify(orderDao, times(1)).getTodayOrderCnt();
        verify(orderDao,times(1)).getTodayOrders(anyInt(),anyInt());
        System.out.println(ordersByStatusResp);
    }

    @Test
    public void 오늘의_상태별_주문현황_가져오기_상태문자열있을때(){
        // given
        StatusCountResp statusCountResp = new StatusCountResp(2,3,4,5,6,1);
        when(orderDao.getTodayStatusCount()).thenReturn(statusCountResp);

        when(orderDao.getTodayOrderCntByStatus("미결제")).thenReturn(1);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        when(orderDao.getTodayOrderByStatus(anyString(), anyInt(), anyInt())).thenReturn(orders);

        menus리스트_옵션문자열_이미지url_셋팅();

        when(orderDao.getOrderDetailByOrderId(anyInt())).thenReturn(menus);

        // when
        OrdersByStatusResp ordersByStatusResp = orderService.getTodayOrderByStatusApi("미결제",10,1);

        // then
        verify(orderDao, times(1)).getTodayOrderCntByStatus(anyString());
        verify(orderDao,times(1)).getTodayOrderByStatus(anyString(),anyInt(),anyInt());
        System.out.println(ordersByStatusResp);
    }

    @Test
    public void 주문상세정보_가져오기(){
        // given
        when(orderDao.getOrderByOrderId(1)).thenReturn(order1);
        when(orderDao.getOrderDetailByOrderId(1)).thenReturn(menus);
        menus리스트_옵션문자열_이미지url_셋팅();

        List<OrderStatusLogResp> orderStatusLogRespList = new ArrayList<>();
        orderStatusLogRespList.add(new OrderStatusLogResp("미결제", "admin", new Date()));
        orderStatusLogRespList.add(new OrderStatusLogResp("결제완료", "admin", new Date()));
        when(orderDao.getOrderStatusLogByOrderId(1)).thenReturn(orderStatusLogRespList);

        // when
        OrderDetailResp orderDetailResp = orderService.getOrderDetailApi(1);
        System.out.println(orderDetailResp);
    }

    @Test
    public void 주문내역검색_전체내역(){
        // given
        when(orderDao.getSearchAllOrderCnt()).thenReturn(2);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        when(orderDao.searchAllOrder(anyInt(),anyInt())).thenReturn(orders);
        when(orderDao.getOrderDetailByOrderId(1)).thenReturn(menus);
        when(orderDao.getOrderDetailByOrderId(2)).thenReturn(menus2);
        menus리스트_옵션문자열_이미지url_셋팅();

        // when
        OrderHistoryResp orderHistoryResp = orderService.getOrderHistory("","","","",10,1);

        // then
        verify(orderDao,times(1)).getSearchAllOrderCnt();
        verify(orderDao,times(1)).searchAllOrder(anyInt(),anyInt());
        System.out.println(orderHistoryResp);
    }

    @Test
    public void 주문내역검색_날짜만(){
        // given
        when(orderDao.getSearchOrderCntByDate(anyString(),anyString())).thenReturn(2);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        when(orderDao.searchOrderByDate(anyString(),anyString(),anyInt(),anyInt())).thenReturn(orders);
        when(orderDao.getOrderDetailByOrderId(1)).thenReturn(menus);
        when(orderDao.getOrderDetailByOrderId(2)).thenReturn(menus2);
        menus리스트_옵션문자열_이미지url_셋팅();

        // when
        OrderHistoryResp orderHistoryResp = orderService.getOrderHistory("","","2019-08-02","2019-08-09",10,1);

        // then
        verify(orderDao,times(1)).getSearchOrderCntByDate(anyString(),anyString());
        verify(orderDao,times(1)).searchOrderByDate(anyString(),anyString(),anyInt(),anyInt());
        System.out.println(orderHistoryResp);
    }


    

    @Test
    public void DB옵션문자열변환() {
        //given
        when(optionDao.getOptionById(1)).thenReturn(new Option("HOT", 0, "Temperature"));
        when(optionDao.getOptionById(2)).thenReturn(new Option("ICE", 0, "Temperature"));
        when(optionDao.getOptionById(3)).thenReturn(new Option("AddShot", 300, "Shot"));
        when(optionDao.getOptionById(4)).thenReturn(new Option("AddSyrup", 300, "Syrup"));
        when(optionDao.getOptionById(5)).thenReturn(new Option("SizeUp", 500, "Size"));

        // when
        String str = orderService.convertOptionStringToCli("1__1/3__2/4__1");
        assertEquals(str, "HOT/샷추가(2개)/시럽추가(1개)");
    }

    @Test
    public void menus리스트_옵션문자열_이미지url_셋팅(){
        // given
        List<OrderMenusResp> menus = new ArrayList<>();
        menus.add(new OrderMenusResp(1,"americano","아메리카노","1__1/3__1","asdf/asdf.png",1,1300));
        menus.add(new OrderMenusResp(2,"latte","카페라떼","1__1/3__1","asdf/asdf.png",1,1800));
        DB옵션문자열변환();

        // when
        orderService.setMenuOptionAndImgurl(menus);

        // then
        assertEquals(menus.get(0).getOption(), "HOT/샷추가(1개)");
        assertEquals(menus.get(1).getOption(), "HOT/샷추가(1개)");
        assertEquals(menus.get(0).getImgUrl(), "/images/asdf/asdf.png");
        assertEquals(menus.get(1).getImgUrl(), "/images/asdf/asdf.png");
    }
}
