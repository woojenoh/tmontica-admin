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
  beforePaymane: number;
  afterPayment: number;
  inProduction: number;
  ready: number;
  pickUp: number;
  cancel: number;
}
