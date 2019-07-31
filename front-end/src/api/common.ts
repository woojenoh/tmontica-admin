import { TMessageError } from "../types/error";

export const API_URL = "http://tmontica-idev.tmon.co.kr/api";

async function fetchTMON<SuccessDataType, ErrorType extends TMessageError>(
  url: string,
  options: RequestInit
) {
  const res = await fetch(url, options);

  const json = await res.json();
  if (res.ok) {
    return json as SuccessDataType;
  }

  const err = json as ErrorType;

  error(err.message);
}

function error(message: string | undefined): never {
  throw new Error(message);
}

export function get<T, E extends TMessageError>(
  reqURL: string,
  params?: Map<string, string> | null,
  jwt?: string
) {
  if (typeof params !== "undefined" && params !== null && !/[?]/.test(reqURL)) {
    reqURL += `?${Array.from(params.entries())
      .map(x => {
        return `${x[0]}=${x[1]}`;
      })
      .join("&")}`;
  }

  return fetchTMON<T, E>(reqURL, {
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: jwt || ""
    },
    method: "GET"
  });
}

export function getWithJWT<T, E extends TMessageError>(
  reqURL: string,
  params?: Map<string, string> | null
) {
  const jwt = localStorage.getItem("jwt") || "";

  return get<T, E>(reqURL, params, jwt);
}

export function postWithJWT<T, E extends TMessageError>(reqURL: string, data: any) {
  const jwt = localStorage.getItem("jwt") || "";

  return fetchTMON<T, E>(reqURL, {
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: jwt
    },
    method: "POST",
    body: JSON.stringify(data)
  });
}
