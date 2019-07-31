import history from "./history";

export const goToSignin = (message = "로그인이 필요합니다.") => {
  alert(message);
  history.push("/signin");
};

// 숫자를 천 단위로 콤마 찍어주는 함수.
export const numberCommaRegex = (number: number | string): string => {
  return Number(number).toLocaleString();
};
