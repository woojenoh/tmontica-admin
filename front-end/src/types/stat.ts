import * as commonTypes from "./common";
import * as menuTypes from "./menu";

export interface IStatMenu {
  menuId: number;
  menuName: string;
  totalPrice: number;
}

export interface IStatAge {
  ageGroup: string;
  totalPrice: number;
}

export interface IStatUser {
  userAgent: string;
  count: number;
}

export interface IBarData {
  datasets: {
    label: string;
    backgroundColor: string;
    hoverBackgroundColor: string;
    data: number[];
  }[];
}

export interface IPieData {
  labels: string[];
  datasets: {
    backgroundColor: string[];
    hoverBackgroundColor: string[];
    data: number[];
  }[];
}

export interface IStatFetchMenu {
  menus: menuTypes.IMenu[];
  pagination: commonTypes.IPagination;
}
