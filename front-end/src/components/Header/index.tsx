import Header from "./Header";
import { connect } from "react-redux";
import * as userActionCreators from "../../redux/actionCreators/user";
import { Dispatch } from "redux";
import * as rootTypes from "../../types/index";

const mapStateToProps = (state: rootTypes.IRootState) => ({
  isSignin: state.user.isSignin,
  isAdmin: state.user.isAdmin
});

const mapDispatchToProps = (dispatch: Dispatch) => {
  return {
    fetchSignin: () => dispatch(userActionCreators.signout())
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header);
