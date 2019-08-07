import axios, { AxiosRequestConfig } from "axios";
import { CommonError } from "./CommonError";

export const BASE_URL = "http://tmonticaadmin-idev.tmon.co.kr";
export const IMAGE_URL = `${BASE_URL}/images`;
export const API_URL = `${BASE_URL}/api`;

export function withJWT(header: AxiosRequestConfig = {}) {
  return { ...header, headers: { Authorization: localStorage.getItem("jwt") || "" } };
}

export async function get<SuccessDataType>(url: string, config?: AxiosRequestConfig) {
  try {
    const res = await axios.get(url, config);
    return res.data as SuccessDataType;
  } catch (error) {
    return new CommonError({
      ...error.response,
      ...error.response!.data
    });
  }
}

export async function post<SuccessDataType>(url: string, data?: any, config?: AxiosRequestConfig) {
  try {
    const res = await axios.post(url, data, config);
    return res.data as SuccessDataType;
  } catch (error) {
    return new CommonError({
      ...error.response,
      ...error.response!.data
    });
  }
}

export async function put<SuccessDataType>(url: string, data?: any, config?: AxiosRequestConfig) {
  try {
    const res = await axios.put(url, data, config);
    return res.data as SuccessDataType;
  } catch (error) {
    return new CommonError({
      ...error.response,
      ...error.response!.data
    });
  }
}

export async function del<SuccessDataType>(url: string, config?: AxiosRequestConfig) {
  try {
    const res = await axios.delete(url, config);
    return res.data as SuccessDataType;
  } catch (error) {
    return new CommonError({
      ...error.response,
      ...error.response!.data
    });
  }
}
