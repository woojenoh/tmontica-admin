import axios, { AxiosRequestConfig } from "axios";
import { CommonError } from "./CommonError";
import { BASE_URL } from "../constants";

export const API_URL = `${BASE_URL}/api`;

export function handleError(error: CommonError | string) {
  if (error instanceof CommonError) {
    if (!error.status) {
      alert("네트워크 오류 발생");
    } else if (error["status"] === 401 || (error.message && /JWT/.test(error.message))) {
      alert("권한이 필요한 요청입니다. 다시 로그인 해주세요.");
      return Promise.resolve("signout");
    } else if (error.message && /No message/.test(error.message)) {
      // 알 수 없는 오류
      console.log(error);
    } else {
      error.alertMessage();
    }
  } else {
    console.dir(error);
  }
  return Promise.resolve(error);
}

export function withJWT(header: AxiosRequestConfig = {}) {
  return { ...header, headers: { Authorization: localStorage.getItem("jwt") || "" } };
}

export async function get<SuccessDataType>(url: string, config?: AxiosRequestConfig) {
  try {
    const res = await axios.get(url, config);
    return res.data as SuccessDataType;
  } catch (error) {
    if (!error.response || /JWT/.test(error.response.message)) {
      return new CommonError({
        status: 401,
        message: "다시 로그인 해주시기 바랍니다."
      });
    }

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
    if (!error.response || /JWT/.test(error.response.message)) {
      return new CommonError({
        status: 401,
        message: "다시 로그인 해주시기 바랍니다."
      });
    }

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
    if (!error.response || /JWT/.test(error.response.message)) {
      return new CommonError({
        status: 401,
        message: "다시 로그인 해주시기 바랍니다."
      });
    }

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
    if (!error.response || /JWT/.test(error.response.message)) {
      return new CommonError({
        status: 401,
        message: "다시 로그인 해주시기 바랍니다."
      });
    }

    return new CommonError({
      ...error.response,
      ...error.response!.data
    });
  }
}
