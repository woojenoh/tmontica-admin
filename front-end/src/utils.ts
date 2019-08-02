import history from "./history";
import React from "react";

export const goToSignin = (message = "로그인이 필요합니다.") => {
  alert(message);
  history.push("/signin");
};

// 숫자를 천 단위로 콤마 찍어주는 함수.
export const numberCommaRegex = (number: number | string): string => {
  return Number(number).toLocaleString();
};

export function handleChange(this: any, e: React.BaseSyntheticEvent) {
  let change = { [e.target.name]: e.target.value };
  this.setState(change);
}

function _formatDatetime(date: Date, format: string) {
  const _padStart = (value: number): string => value.toString().padStart(2, "0");
  return format
    .replace(/yyyy/g, _padStart(date.getFullYear()))
    .replace(/dd/g, _padStart(date.getDate()))
    .replace(/mm/g, _padStart(date.getMonth() + 1))
    .replace(/hh/g, _padStart(date.getHours()))
    .replace(/ii/g, _padStart(date.getMinutes()))
    .replace(/ss/g, _padStart(date.getSeconds()));
}
function isValidDate(d: Date): boolean {
  return !isNaN(d.getTime());
}
export function formatDate(date: any, format?: string): string {
  var datetime = new Date(date);
  return isValidDate(datetime)
    ? _formatDatetime(datetime, typeof format === "string" ? format : "yyyy-mm-dd hh:ii:ss")
    : "";
}
