import { get, withJWT, API_URL, post, put } from "./common";
import { IMenuModalResponse } from "../types/menu";
import { IBanner } from "../types/banner";

const multipartHeader = {
  headers: { "content-type": "multipart/form-data" }
};

export function getMenuById(menuId: number) {
  return get<IMenuModalResponse>(`${API_URL}/menus/${menuId}`, withJWT());
}

export function getBannerAll() {
  return get<IBanner[]>(`${API_URL}/banners`, withJWT());
}

export function addBanner(data: FormData) {
  return post<true>(`${API_URL}/banners`, data, withJWT(multipartHeader));
}

export function updateMenu(data: FormData) {
  return put<true>(`${API_URL}/menus`, data, withJWT(multipartHeader));
}
