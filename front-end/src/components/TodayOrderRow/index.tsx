import * as React from "react";
import moment from "moment";
import { numberCommaRegex } from "../../utils";
import * as orderTypes from "../../types/order";
import "./styles.scss";

export interface ITodayOrderRowProps {
  id: number;
  user: string;
  status: string;
  menus: orderTypes.IOrderMenu[];
  payment: string;
  price: number;
  date: string;
  handleModalOpen(orderId: number): void;
}

function TodayOrderRow(props: ITodayOrderRowProps) {
  const { id, user, status, menus, payment, price, date, handleModalOpen } = props;
  return (
    <tr className="text-center">
      <td className="order__td check">
        <input type="checkbox" />
      </td>
      <td className="order__td order-id" onClick={() => handleModalOpen(id)}>
        {id}
      </td>
      <td className="order__td order-user">{user}</td>
      <td className="order__td order-menus">
        {menus.length > 1 ? `${menus[0].nameKo} 외 ${menus.length - 1}개` : `${menus[0].nameKo}`}
      </td>
      <td className="order__td order-payment">{payment}</td>
      <td className="order__td order-price">{numberCommaRegex(price)}</td>
      <td className="order__td order-date">{moment(date).format("hh시 mm분")}</td>
      <td className="order__td order-status">{status}</td>
    </tr>
  );
}

export default TodayOrderRow;
