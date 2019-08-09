import React, { PureComponent } from "react";
import { Link } from "react-router-dom";
import "./styles.scss";

interface Props {
  isSidebarOpen: boolean;
}
interface State {}

export default class Nav extends PureComponent<Props, State> {
  render() {
    const { isSidebarOpen } = this.props;

    return (
      <nav className={`bg-light border-right sidebar ${isSidebarOpen && `sidebar--open`}`}>
        <ul className="nav flex-column">
          <li className="nav-item">
            <Link to="/" className="nav-link">
              <h5>오늘의 주문</h5>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/order" className="nav-link">
              <h5>주문 내역 검색</h5>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/menus" className="nav-link">
              <h5>메뉴 관리</h5>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/banner" className="nav-link">
              <h5>배너 관리</h5>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/stat" className="nav-link">
              <h5>통계</h5>
            </Link>
          </li>
        </ul>
      </nav>
    );
  }
}
