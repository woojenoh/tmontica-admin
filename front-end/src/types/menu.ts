export interface IMenuBasic {
  categoryEng: string;
  categoryKo: string;
  description: string;
  discountRate: number;
  id: number;
  imgUrl: string | ArrayBuffer | null;
  monthlyMenu: boolean;
  nameEng: string;
  nameKo: string;
  productPrice: number;
  sellPrice: number;
  stock: number;
}

export interface IMenu extends IMenuBasic {
  creatorId: string; // "123qwe";
  description: string; // "진한맛의 초콜릿의 풍미가 느껴지는 촉촉한 브라우니";
  imgFile?: Blob | string;
  startDate: string | null;
  endDate: string | null; // null;
  createdDate: string; // "2019-07-26T05:33:23.000+0000";
  updatedDate: string | null; // null;
  updaterId: string | null; // null;
  usable: boolean;
}
export interface IOptionObject {
  id: number;
  name: string;
  price: number;
  type: string;
  quantity: number;
}

export interface IOptionObjectArray {
  option: Array<IOptionObject>;
}

export interface IOptionSet {
  optionIds: Set<number>;
}

export interface IOptionIdArray {
  option: Array<{ id: number }>;
}

// 메뉴 모달에서 데이터 불러올 때 타입
export interface IMenuModalResponse {
  categoryEng: string;
  categoryKo: string;
  description: string;
  discountRate: number;
  id: number;
  imgUrl: string | ArrayBuffer | null;
  monthlyMenu: boolean;
  nameEng: string;
  nameKo: string;
  productPrice: number;
  sellPrice: number;
  stock: number;
  option: Array<IOptionObject>;
  startDate: string | null;
  endDate: string | null;
  usable: boolean;
}

export interface IMenuResponse extends IOptionIdArray {}

export type IMenus = Array<IMenu>;
