import React, { Component } from "react";
import { Link } from "react-router-dom";

interface Props {}
interface State {}

export default class Nav extends Component<Props, State> {
  state = {};

  render() {
    return (
      <nav className="col-md-2 d-none d-md-block bg-light sidebar">
        <div className="sidebar-sticky">
          <ul className="nav flex-column">
            <li className="nav-item">
              <Link to="" className="nav-link">
                <i className="fas fa-shopping-cart feather" />
                주문 관리
              </Link>
            </li>
            <li className="nav-item">
              <Link to="" className="nav-link">
                <i className="fas fa-coffee feather" />
                메뉴 관리
              </Link>
            </li>
            <li className="nav-item">
              <Link to="" className="nav-link">
                <i className="far fa-flag feather" />
                배너 관리
              </Link>
            </li>
            <li className="nav-item">
              <Link to="" className="nav-link">
                <i className="fas fa-user feather" />
                사용자 관리
              </Link>
            </li>
            <li className="nav-item">
              <Link to="" className="nav-link">
                <i className="far fa-chart-bar feather" />
                통계
              </Link>
            </li>
          </ul>
        </div>
      </nav>
    );
  }
}
