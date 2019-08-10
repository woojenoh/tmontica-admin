import { withJWT, get, API_URL } from "./common";
import * as orderTypes from "../types/order";
import * as commonTypes from "../types/common";

interface ITodayOrder {
  orders: orderTypes.IOrder[] | null;
  statusCount: orderTypes.IOrderStatusCount | null;
  pagination: commonTypes.IPagination | null;
}

export function getTodayOrder(
  page: number,
  size: number,
  status: orderTypes.TOrderStatusKor | null = null
) {
  return get<ITodayOrder>(
    `${API_URL}/orders/today`,
    withJWT({
      params: {
        page,
        size,
        status
      }
    })
  );
}
