export interface IMenu {
  id: number; // 8;
  nameEng: string; // "Cacao Pan Brownie";
  nameKo: string; // "카카오 팬 브라우니";
  creatorId: string; // "123qwe";
  categoryEng: string; // "bread";
  categoryKo: string; // "빵";
  description: string; // "진한맛의 초콜릿의 풍미가 느껴지는 촉촉한 브라우니";
  productPrice: number; // 2500;
  sellPrice: number; // 2500;
  discountRate: number; // 0;
  imgUrl: string; // "imagefile/2019/7/26/Cacao Pan Brownie_1764d079-4cbb-4093-91a8-52c8608aa1c5.png";
  monthlyMenu: boolean; // true;
  stock: number; // 96;
  startDate: string | null;
  endDate: string | null; // null;
  createdDate: string; // "2019-07-26T05:33:23.000+0000";
  updatedDate: string | null; // null;
  updaterId: string | null; // null;
  usable: boolean;
}

export type IMenus = Array<IMenu>;
