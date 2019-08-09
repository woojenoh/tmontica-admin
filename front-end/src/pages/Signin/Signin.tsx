import React, { PureComponent } from "react";
import Header from "../../components/Header";
import * as userTypes from "../../types/user";
import "./styles.scss";
import { Redirect } from "react-router";

interface Props {
  isSignin: boolean;
  isAdmin: boolean;
  fetchSignin(payload: userTypes.IUserSigninInfo): void;
}
export interface State {
  id: string;
  password: string;
}

export default class Signin extends PureComponent<Props, State> {
  state = { id: "", password: "" };

  handleInputChange = (e: React.FormEvent<HTMLInputElement>) => {
    this.setState({
      [e.currentTarget.name]: e.currentTarget.value
    } as { [K in keyof State]: State[K] });
  };

  handleSigninSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { id, password } = this.state;
    const { fetchSignin } = this.props;

    e.preventDefault();

    fetchSignin({ id: id, password: password });
  };

  render() {
    const { handleInputChange, handleSigninSubmit } = this;

    const { isSignin, isAdmin } = this.props;

    return !isSignin || !isAdmin ? (
      <>
        <Header hide={true} title="관리자 로그인" />
        <main className="main">
          <section className="signin">
            <h1 className="signin__title">티몽티카 관리자 로그인</h1>
            <form className="inner" onSubmit={e => handleSigninSubmit(e)}>
              <div className="input-group">
                <input
                  type="text"
                  name="id"
                  className="form-control"
                  placeholder="아이디"
                  onChange={e => handleInputChange(e)}
                  required
                />
              </div>
              <div className="input-group">
                <input
                  type="password"
                  name="password"
                  className="form-control"
                  placeholder="비밀번호"
                  onChange={e => handleInputChange(e)}
                  required
                />
              </div>
              <input type="submit" value="로그인" className="btn btn-primary btn-submit" />
            </form>
          </section>
        </main>
      </>
    ) : (
      <Redirect to={"/menus"} />
    );
  }
}
