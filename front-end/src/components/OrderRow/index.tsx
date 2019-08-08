import * as React from "react";
import _ from "underscore";
import moment from "moment";
import { numberCommaRegex } from "../../utils";
import * as orderTypes from "../../types/order";
import "./styles.scss";

export interface IOrderRowProps {
  order: orderTypes.IOrder;
  isChecked?: boolean;
  handleModalOpen(orderId: number): void;
  handleCheckRow?(orderId: number): void;
  handleUncheckRow?(orderId: number): void;
}

export interface IOrderRowState {}

export default class OrderRow extends React.Component<IOrderRowProps, IOrderRowState> {
  shouldComponentUpdate(nextProps: IOrderRowProps) {
    if (
      _.isEqual(this.props.order, nextProps.order) &&
      this.props.isChecked === nextProps.isChecked
    ) {
      return false;
    } else {
      return true;
    }
  }

  render() {
    const { order, isChecked, handleModalOpen, handleCheckRow, handleUncheckRow } = this.props;
    return (
      <>
        {isChecked !== undefined &&
        handleUncheckRow !== undefined &&
        handleCheckRow !== undefined ? (
          <tr className="text-center">
            <td className="order__td check">
              <input
                type="checkbox"
                checked={isChecked}
                onChange={() =>
                  isChecked ? handleUncheckRow(order.orderId) : handleCheckRow(order.orderId)
                }
              />
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
            <td className="order__td order-date">{moment(order.orderDate).format("HH시 mm분")}</td>
            <td className="order__td order-status">{order.status}</td>
          </tr>
        ) : (
          <tr className="text-center">
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
            <td className="order__td order-date">
              {moment(order.orderDate).format("YYYY년 MM월 DD일 HH시 mm분")}
            </td>
            <td className="order__td order-status">{order.status}</td>
          </tr>
        )}
      </>
    );
  }
}
