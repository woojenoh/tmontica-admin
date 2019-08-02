export interface IOrder {
  orderId: number;
  orderDate: string;
  payment: string;
  totalPrice: number;
  usedPoint: number;
  realPrice: number;
  status: string;
  userId: string;
  menus: IOrderMenu[];
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

export type TOrderStatusEng =
  | "beforePayment"
  | "afterPayment"
  | "inProduction"
  | "ready"
  | "pickUp"
  | "cancel";
