import React, { Component } from "react";
import { Link } from "react-router-dom";

interface Props {}
interface State {}

export default class Nav extends Component<Props, State> {
  state = {};

  render() {
    return (
      <nav className="col-md-2 d-none d-md-block bg-light border-right sidebar pt-3">
        <div className="sidebar-sticky">
          <ul className="nav flex-column">
            <li className="nav-item">
              <Link to="/" className="nav-link">
                <i className="fas fa-shopping-cart feather" />
                <h5>오늘의 주문</h5>
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/order" className="nav-link">
                <i className="fas fa-shopping-cart feather" />
                <h5>주문 내역 검색</h5>
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/menus" className="nav-link">
                <i className="fas fa-coffee feather" />
                <h5>메뉴 관리</h5>
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/" className="nav-link">
                <i className="far fa-flag feather" />
                <h5>배너 관리</h5>
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/" className="nav-link">
                <i className="fas fa-user feather" />
                <h5>사용자 관리</h5>
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/" className="nav-link">
                <i className="far fa-chart-bar feather" />
                <h5>통계</h5>
              </Link>
            </li>
          </ul>
        </div>
      </nav>
    );
  }
}
