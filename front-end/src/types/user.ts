import * as userActionTypes from "../redux/actionTypes/user";
import { AxiosError } from "axios";

export interface IUserState {
  user: IUser | null;
  isSignin: boolean;
  isAdmin: boolean;
}

export interface IUser {
  id: number;
  name: string;
  email: string;
  birthDate: string;
  point: number;
}

export interface IUserSigninInfo {
  id: string;
  password: string;
  role?: "USER" | "ADMIN";
}

export interface IJwtToken {
  sub: string;
  exp: number;
  userInfo: string;
}

export interface ISignout {
  type: typeof userActionTypes.SIGNOUT;
}

export interface IFetchSignin {
  type: typeof userActionTypes.FETCH_SIGNIN;
  payload: IUserSigninInfo;
}

export interface IFetchSigninFulfilled {
  type: typeof userActionTypes.FETCH_SIGNIN_FULFILLED;
  payload: {
    user: IUser;
    isAdmin: boolean;
  };
}

export interface IFetchSigninRejected {
  type: typeof userActionTypes.FETCH_SIGNIN_REJECTED;
  error: AxiosError;
}

export type TUserAction = ISignout | IFetchSignin | IFetchSigninFulfilled | IFetchSigninRejected;
