import * as actionTypes from "../actionTypes/user";
import * as userTypes from "../../types/user";
import { AxiosError } from "axios";

export function signout() {
  return {
    type: actionTypes.SIGNOUT
  };
}

export function fetchSignin(payload: userTypes.IUserSigninInfo) {
  return {
    type: actionTypes.FETCH_SIGNIN,
    payload
  };
}

export function fetchSigninFulfilled(payload: { user: userTypes.IUser; isAdmin: boolean }) {
  return {
    type: actionTypes.FETCH_SIGNIN_FULFILLED,
    payload
  };
}

export function fetchSigninRejected(error: AxiosError) {
  return {
    type: actionTypes.FETCH_SIGNIN_REJECTED,
    error
  };
}
