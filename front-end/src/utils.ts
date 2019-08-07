import history from "./history";
import { BaseSyntheticEvent } from "react";
import { AxiosRequestConfig } from "axios";

export const goToSignin = (message = "로그인이 필요합니다.") => {
  alert(message);
  history.push("/signin");
};

// 숫자를 천 단위로 콤마 찍어주는 함수.
export const numberCommaRegex = (number: number | string): string => {
  return Number(number).toLocaleString();
};

export function handleChange(this: any, e: BaseSyntheticEvent) {
  let change = {
    [e.target.name]: e.target.value
  };
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
  var datetime;
  if (date instanceof Date) {
    datetime = date;
  } else {
    datetime = new Date();
  }
  return isValidDate(datetime)
    ? _formatDatetime(datetime, typeof format === "string" ? format : "yyyy.mm.dd hh:ii:ss")
    : "";
}

// 이미지 미리보기 업로드 > IE10
export function setImagePreview(files: FileList | null, callback: Function) {
  if (files && files.length > 0 && /(\.jpg|\.jpeg|\.png)$/.test(files[0].name)) {
    const reader = new FileReader();
    reader.onload = (e: ProgressEvent) => {
      const target = e.target as FileReader;

      callback(target.result);
    };

    reader.readAsDataURL(files[0]);
  } else {
    alert("이미지 파일을 등록해 주세요.");
  }
}

export function withJWT(header: AxiosRequestConfig = {}) {
  return { ...header, headers: { Authorization: localStorage.getItem("jwt") || "" } };
}
