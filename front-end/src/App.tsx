import React, { lazy, Suspense } from "react";
import {
  Switch,
  Route,
  Redirect,
  withRouter,
  RouteComponentProps,
  RouteProps
} from "react-router-dom";
import { connect } from "react-redux";
import * as rootTypes from "./types/index";
import "./assets/scss/admin.scss";

export interface IAppProps extends RouteComponentProps {
  isSignin: boolean;
  isAdmin: boolean;
}

class App extends React.Component<IAppProps> {
  AdminRoute = ({ component: Component, ...rest }: RouteProps) => {
    const { isSignin, isAdmin } = this.props;

    if (Component) {
      return (
        <Route
          {...rest}
          render={props =>
            isSignin && isAdmin ? (
              <Component {...props} />
            ) : (
              <Redirect
                to={{
                  pathname: "/signin",
                  state: { from: props.location }
                }}
              />
            )
          }
        />
      );
    } else {
      return null;
    }
  };

  PublicRoute = ({ component: Component, ...rest }: RouteProps) => {
    const { isSignin } = this.props;

    if (Component) {
      return (
        <Route
          {...rest}
          render={props =>
            // 로그인이 돼있는 상태에서 로그인 페이지로 이동하면 리다이렉트한다.
            isSignin && (rest.path === "/signin" || rest.path === "/find") ? (
              <Redirect
                to={{
                  pathname: "/",
                  state: { from: props.location }
                }}
              />
            ) : (
              <Component {...props} />
            )
          }
        />
      );
    } else {
      return null;
    }
  };

  render() {
    const { PublicRoute, AdminRoute } = this;
    const Signin = lazy(() => import("./pages/Signin"));
    const TodayOrder = lazy(() => import("./pages/TodayOrder"));
    const Banner = lazy(() => import("./pages/Banner"));
    const Menus = lazy(() => import("./pages/Menus"));
    const Order = lazy(() => import("./pages/Order"));
    const Stat = lazy(() => import("./pages/Stat"));

    return (
      <>
        <Suspense fallback={<div>로딩 중입니다.</div>}>
          <Switch>
            <AdminRoute exact path="/stat" component={Stat} />
            <AdminRoute exact path="/todayOrder" component={TodayOrder} />
            <AdminRoute exact path="/order" component={Order} />
            <AdminRoute exact path="/menus" component={Menus} />
            <AdminRoute exact path="/menus/:menuId([0-9]+)" component={Menus} />
            <AdminRoute exact path="/banner" component={Banner} />
            <AdminRoute exact path="/" component={TodayOrder} />
            <PublicRoute exact path="/signin" component={Signin} />
          </Switch>
        </Suspense>
      </>
    );
  }
}

const mapStateToProps = (state: rootTypes.IRootState) => ({
  isSignin: state.user.isSignin,
  isAdmin: state.user.isAdmin
});

export default connect(mapStateToProps)(withRouter(App));
