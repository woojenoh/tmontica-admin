import { IPagination } from "./common";

export interface IBanner {
  id: number;
  creatorId: string;
  link: string;
  number: number;
  startDate: string;
  endDate: string;
  usePage: string;
  imgUrl?: string | null;
  imgFile?: Blob | string;
  usable: boolean;
}

export interface IBannerPaging {
  banners: IBanner[];
  pagination: IPagination;
}
