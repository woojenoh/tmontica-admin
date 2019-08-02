import Signin from "./Signin";
import { connect } from "react-redux";
import * as userActionCreators from "../../redux/actionCreators/user";
import * as userTypes from "../../types/user";
import { Dispatch } from "redux";
import * as rootTypes from "../../types/index";

const mapStateToProps = (state: rootTypes.IRootState) => ({
  isSignin: state.user.isSignin,
  isAdmin: state.user.isAdmin
});

const mapDispatchToProps = (dispatch: Dispatch) => {
  return {
    fetchSignin: (payload: userTypes.IUserSigninInfo) =>
      dispatch(userActionCreators.fetchSignin(payload))
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Signin);
