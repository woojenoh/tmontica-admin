import * as userTypes from "./user";

export interface IRootState {
  user: userTypes.IUserState;
}

export interface Indexable {
  [key: string]: any;
}
