import { get, withJWT, API_URL, post, put, del } from "./common";
import { IBanner } from "../types/banner";

const multipartHeader = {
  headers: { "content-type": "multipart/form-data" }
};

export function getBannerById(bannerId: number) {
  return get<IBanner>(`${API_URL}/banners/${bannerId}`, withJWT());
}

export function getBannerAll() {
  return get<IBanner[]>(`${API_URL}/banners`, withJWT());
}

export function addBanner(data: FormData) {
  return post<true>(`${API_URL}/banners`, data, withJWT(multipartHeader));
}

export function updateBanner(data: FormData) {
  return put<true>(`${API_URL}/banners`, data, withJWT(multipartHeader));
}

export function deleteBanner(bannerId: number) {
  return del(`${API_URL}/banners/${bannerId}`, withJWT());
}
