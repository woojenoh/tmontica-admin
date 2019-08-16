import { call, put, takeLatest } from "redux-saga/effects";
import axios from "axios";
import history from "../../history";
import jwt from "jwt-decode";
import * as userActionTypes from "../actionTypes/user";
import * as userActionCreators from "../actionCreators/user";
import * as userTypes from "../../types/user";
import { API_URL } from "../../api/common";

export function* signout() {
  yield localStorage.removeItem("jwt");
  yield history.push("/signin");
}

function* fetchSigninSagas(action: userTypes.IFetchSignin) {
  try {
    const response = yield call(axios.post, `${API_URL}/users/signin`, action.payload);
    // 토큰을 로컬스토리지에 저장하고, 토큰에서 유저 정보를 가져와 상태를 업데이트한다.
    yield localStorage.setItem("jwt", response.data.authorization);
    const jwtToken = jwt(response.data.authorization) as userTypes.IJwtToken;
    const parsedUserInfo = JSON.parse(jwtToken.userInfo);
    alert("환영합니다!");
    yield history.push("/admin");
    yield put(
      userActionCreators.fetchSigninFulfilled({
        user: parsedUserInfo,
        isAdmin: parsedUserInfo.role === "ADMIN"
      })
    );
  } catch (error) {
    alert(error.response.data.message);
    yield put(userActionCreators.fetchSigninRejected(error.response));
  }
}

export default function* userSagas() {
  yield takeLatest(userActionTypes.SIGNOUT, signout);
  yield takeLatest(userActionTypes.FETCH_SIGNIN, fetchSigninSagas);
}
