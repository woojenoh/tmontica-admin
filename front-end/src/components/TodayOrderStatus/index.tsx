import * as React from "react";
import * as orderTypes from "../../types/order";

export interface ITodayOrderStatusProps {
  index: number;
  statusName: string;
  statusCount: orderTypes.IOrderStatusCount | null;
  isActive?: boolean;
  initializeTodayStatus(): void;
  handleClickTodayStatus(statusName: string): void;
}

const statusEng = [
  "beforePayment",
  "afterPayment",
  "inProduction",
  "ready",
  "pickUp",
  "cancel"
] as orderTypes.TOrderStatusEng[];

function TodayOrderStatus(props: ITodayOrderStatusProps) {
  const {
    index,
    statusName,
    statusCount,
    isActive,
    handleClickTodayStatus,
    initializeTodayStatus
  } = props;

  return (
    <div
      className={`order-circle border ${isActive ? "btn-success text-white" : "btn-light"}`}
      onClick={() => (isActive ? initializeTodayStatus() : handleClickTodayStatus(statusName))}
    >
      <div className="order-circle-name">{statusName}</div>
      <div>
        <span className="order-cnt">{statusCount ? statusCount[statusEng[index]] : 0}건</span>
      </div>
    </div>
  );
}

export default TodayOrderStatus;
