import { get, withJWT, API_URL, post, put } from "./common";
import { IMenus, IMenuModalResponse } from "../types/menu";
import { IPagination } from "../types/common";

interface IMenuPaging {
  menus: IMenus;
  pagination: IPagination;
}

const multipartHeader = {
  headers: { "content-type": "multipart/form-data" }
};

export function getMenuById(menuId: number) {
  return get<IMenuModalResponse>(`${API_URL}/menus/${menuId}`, withJWT());
}

export function getMenuPaging(page: number = 1) {
  return get<IMenuPaging>(`${API_URL}/menus?page=${page}`, withJWT());
}

export function addMenu(data: FormData) {
  return post<true>(`${API_URL}/menus`, data, withJWT(multipartHeader));
}

export function updateMenu(data: FormData) {
  return put<true>(`${API_URL}/menus`, data, withJWT(multipartHeader));
}
