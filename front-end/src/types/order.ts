import * as commonTypes from "./common";

export interface IOrder {
  orderId: number;
  orderDate: string;
  payment: string;
  totalPrice: number;
  usedPoint: number;
  realPrice: number;
  status: TOrderStatusKor;
  userId: string;
  menus: IOrderMenu[];
  checked?: boolean;
}

export interface IOrderMenu {
  menuId: number;
  nameEng: string;
  nameKo: string;
  option: string;
  imgUrl: string;
  quantity: number;
  price: number;
}

export interface IOrderStatusCount {
  beforePayment: number;
  afterPayment: number;
  inProduction: number;
  ready: number;
  pickUp: number;
  cancel: number;
}

export interface IOrderDetail {
  menus: IOrderMenu[];
  orderId: number;
  orderStatusLogs: IOrderStatusLog[];
  totalPrice: number;
  usedPoint: number;
  realPrice: number;
  userId: string;
}

export interface IOrderStatusLog {
  editorId: string;
  modifiedDate: string;
  status: TOrderStatusKor;
}

export interface IOrderFetchData {
  pagination: commonTypes.IPagination;
  statusCount: IOrderStatusCount;
  orders: IOrder[];
}

export type TOrderStatusKor =
  | "미결제"
  | "결제완료"
  | "준비중"
  | "준비완료"
  | "픽업완료"
  | "주문취소";

export type TOrderStatusEng =
  | "beforePayment"
  | "afterPayment"
  | "inProduction"
  | "ready"
  | "pickUp"
  | "cancel";

export type TOrderSearchType = "주문자" | "주문번호" | "주문상태" | "결제방법";
