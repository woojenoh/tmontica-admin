import React, { PureComponent } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";
import "bootstrap/dist/css/bootstrap.css";
import "./styles.scss";

interface Props {
  hide?: boolean;
  title: string;
  isSignin: boolean;
  isAdmin: boolean;
  fetchSignin(): void;
}
interface State {}

export default class Header extends PureComponent<Props, State> {
  state = {};

  render() {
    const { title, hide, isSignin, isAdmin, fetchSignin } = this.props;
    return (
      <header
        className={`bg-dark text-light flex-row d-flex justify-content-between align-items-center${
          hide ? "hide" : ""
        }`}
      >
        <div className="d-flex align-items-center">
          {isSignin && isAdmin && <FontAwesomeIcon className="mr-3" icon={faBars} size="2x" />}
          <h1 className="title">{title}</h1>
        </div>
        {isSignin && isAdmin && (
          <button className="btn btn-primary" onClick={() => fetchSignin()}>
            로그아웃
          </button>
        )}
      </header>
    );
  }
}
