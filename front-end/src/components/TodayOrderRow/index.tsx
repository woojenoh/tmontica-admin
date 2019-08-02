import * as React from "react";
import moment from "moment";
import { numberCommaRegex } from "../../utils";
import * as orderTypes from "../../types/order";
import "./styles.scss";

export interface ITodayOrderRowProps {
  order: orderTypes.IOrder;
  handleModalOpen(orderId: number): void;
}

function TodayOrderRow(props: ITodayOrderRowProps) {
  const { order, handleModalOpen } = props;
  return (
    <tr className="text-center">
      <td className="order__td check">
        <input type="checkbox" />
      </td>
      <td className="order__td order-id" onClick={() => handleModalOpen(order.orderId)}>
        {order.orderId}
      </td>
      <td className="order__td order-user">{order.userId}</td>
      <td className="order__td order-menus">
        {order.menus.length > 1
          ? `${order.menus[0].nameKo} 외 ${order.menus.length - 1}개`
          : `${order.menus[0].nameKo}`}
      </td>
      <td className="order__td order-payment">{order.payment}</td>
      <td className="order__td order-price">{numberCommaRegex(order.totalPrice)}</td>
      <td className="order__td order-date">{moment(order.orderDate).format("hh시 mm분")}</td>
      <td className="order__td order-status">{order.status}</td>
    </tr>
  );
}

export default TodayOrderRow;
