import { TMessageError, TExceptionError, TCommonError } from "../types/error";

export const ADMIN_API_URL = "http://tmonticaadmin-idev.tmon.co.kr/api";
export const API_URL = "http://tmontica-idev.tmon.co.kr/api";

const defaultRequestConfig = {
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json"
  }
};

async function fetchTMON<SuccessDataType, ErrorType extends TCommonError>(
  url: string,
  options: RequestInit
) {
  const res = await fetch(url, options);

  const text = await res.text();
  const data = transformData(text);

  if (res.ok) {
    return data as SuccessDataType;
  }

  const originErr = data as ErrorType;
  const err = Object.assign(originErr, { status: res.status });
  throw err;
}

function error(message: string | undefined): never {
  throw new Error(message);
}

function getHeaders(jwt?: string) {
  return jwt
    ? Object.assign(defaultRequestConfig["headers"], { Authorization: jwt })
    : defaultRequestConfig["headers"];
}

export function get<T, E extends TCommonError>(
  reqURL: string,
  params?: Map<string, string> | Object | null,
  jwt?: string
) {
  reqURL = attchParamsToURL(reqURL, params);

  const headers = getHeaders(jwt);
  const requestConfig = Object.assign(defaultRequestConfig, { headers }, { method: "GET" });

  return fetchTMON<T, E>(reqURL, requestConfig);
}

export function del<T, E extends TCommonError>(
  reqURL: string,
  params?: Map<string, string> | Object | null,
  jwt?: string
) {
  reqURL = attchParamsToURL(reqURL, params);
  const headers = getHeaders(jwt);
  const requestConfig = Object.assign(defaultRequestConfig, { headers }, { method: "DELETE" });

  return fetchTMON<T, E>(reqURL, requestConfig);
}

export function post<T, E extends TCommonError>(reqURL: string, data: any, jwt?: string) {
  const headers = getHeaders(jwt);
  const requestConfig = Object.assign(
    defaultRequestConfig,
    { headers },
    { method: "POST" },
    { body: JSON.stringify(data) }
  );
  return fetchTMON<T, E>(reqURL, requestConfig);
}

export function put<T, E extends TCommonError>(reqURL: string, data: any, jwt?: string) {
  const headers = getHeaders(jwt);
  const requestConfig = Object.assign(
    defaultRequestConfig,
    { headers },
    { method: "PUT" },
    { body: JSON.stringify(data) }
  );
  return fetchTMON<T, E>(reqURL, requestConfig);
}

function withJWT(this: void, f: Function) {
  const jwt = localStorage.getItem("jwt") || "";

  return f.call(f, jwt);
}

export function getWithJWT<T, E extends TCommonError>(
  this: void,
  reqURL: string,
  params?: Map<string, string> | null
) {
  return withJWT(get.bind(this, reqURL, params));
}

export function postWithJWT<T, E extends TCommonError>(this: void, reqURL: string, data: any) {
  return withJWT(post.bind(this, reqURL, data));
}

export function putWithJWT<T, E extends TCommonError>(this: void, reqURL: string, data: any) {
  return withJWT(put.bind(this, reqURL, data));
}

export function delWithJWT<T, E extends TCommonError>(this: void, reqURL: string) {
  return withJWT(del.bind(this, reqURL, ""));
}

function transformData(data: any) {
  if (typeof data === "string") {
    try {
      data = JSON.parse(data);
    } catch (e) {
      /* Ignore */
    }
  }
  return data;
}

// 쿼리 파라미터 맵/객체를 URL에 붙여주는 함수
function attchParamsToURL(url: string, params?: Map<string, string> | Object | null) {
  if (typeof params !== "undefined" && params !== null && !/[?]/.test(url)) {
    if (params instanceof Map) {
      url += `?${Array.from(params.entries())
        .map(x => {
          return `${x[0]}=${x[1]}`;
        })
        .join("&")}`;
    } else {
      url += `?${Object.entries(params)
        .map(([key, val]) => `${key}=${val}`)
        .join("&")}`;
    }
  }
  return url;
}
