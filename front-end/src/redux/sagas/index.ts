import { all } from "redux-saga/effects";
import userSagas from "./user";

export default function* rootSaga() {
  yield all([userSagas()]);
}
