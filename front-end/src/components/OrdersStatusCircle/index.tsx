import * as React from "react";

export interface IOrderStatusCircleProps {
  statusName: string;
  statusCount: number;
  isActive?: boolean;
}

function OrderStatusCircle(props: IOrderStatusCircleProps) {
  const { statusName, statusCount, isActive } = props;

  return (
    <div className={`order-circle border ${isActive ? "btn-success text-white" : "btn-light"}`}>
      <div className="order-circle-name">{statusName}</div>
      <div>
        <span className="order-cnt">{statusCount}건</span>
      </div>
    </div>
  );
}

export default OrderStatusCircle;
