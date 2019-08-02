import * as React from "react";

export interface ITodayOrderStatusProps {
  index: number;
  statusName: string;
  statusCount: Number;
  isActive?: boolean;
  initializeTodayStatus(): void;
  handleClickTodayStatus(statusName: string): void;
}

function TodayOrderStatus(props: ITodayOrderStatusProps) {
  const {
    statusName,
    statusCount,
    isActive,
    handleClickTodayStatus,
    initializeTodayStatus
  } = props;

  return (
    <div
      className={`order-circle cursor-pointer border btn ${
        isActive ? "btn-success text-white" : "btn-light"
      }`}
      onClick={() => (isActive ? initializeTodayStatus() : handleClickTodayStatus(statusName))}
    >
      <div className="order-circle-name">{statusName}</div>
      <div>
        <span className="order-cnt">{statusCount}건</span>
      </div>
    </div>
  );
}

export default TodayOrderStatus;
