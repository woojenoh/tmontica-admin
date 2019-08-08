import React, { PureComponent } from "react";
import { Link } from "react-router-dom";

interface Props {}
interface State {}

export default class Nav extends PureComponent<Props, State> {
  state = {};

  render() {
    return (
      <nav className="col-md-2 d-none d-md-block bg-light border-right sidebar pt-3">
        <div className="sidebar-sticky">
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
              <Link to="/statistics" className="nav-link">
                <h5>통계</h5>
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/banner" className="nav-link">
                <h5>배너 관리</h5>
              </Link>
            </li>
          </ul>
        </div>
      </nav>
    );
  }
}
