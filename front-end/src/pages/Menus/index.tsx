import React, { Component, MouseEvent } from "react";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import { Table } from "react-bootstrap";
import { handleChange } from "../../utils";
import "react-datepicker/dist/react-datepicker.css";
import "./styles.scss";
import { MenuModal } from "../../components/MenuModal";

interface IMenusProps {}
interface IMenusState {
  show: boolean;
  menuId: number;
}

export default class Menus extends Component<IMenusProps, IMenusState> {
  state = {
    show: false,
    menuId: -1
  };

  componentDidMount() {
    const result = /\/menus\/([0-9+])\??/.exec(window.location.href);
    const menuId = result && result.length > 1 ? parseInt(result[1]) : -1;

    if (menuId > 0) {
      this.setState({
        show: true,
        menuId
      });
    }
  }

  handleClose = () => {
    this.setState({
      show: false
    });
  };

  handleShow = (e: MouseEvent<HTMLButtonElement>) => {
    this.setState({
      show: true
    });
  };

  render() {
    const { handleShow, handleClose } = this;
    const { show, menuId } = this.state;

    return (
      <>
        <Header title="메뉴 관리" />
        <div className="main-wrapper">
          <Nav />
          <main className="col-md-10">
            <section>
              <div className="content-head d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <button className="btn btn-primary" onClick={handleShow}>
                  메뉴 추가
                </button>
                <button className="btn btn-primary">이달의 메뉴 보기</button>
              </div>
              <Table striped size="sm" className="content-table">
                <thead>
                  <tr>
                    <th>
                      <input
                        type="checkbox"
                        aria-label="Checkbox for following text input"
                        onChange={handleChange.bind(this)}
                      />
                    </th>
                    <th>미리보기</th>
                    <th>카테고리</th>
                    <th>메뉴명</th>
                    <th>설명</th>
                    <th>이달의 메뉴</th>
                    <th>상품가</th>
                    <th>할인율</th>
                    <th>판매가</th>
                    <th>재고</th>
                    <th>등록일</th>
                    <th>등록인</th>
                    <th>수정일</th>
                    <th>수정인</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td className="menu__td check">
                      <input type="checkbox" aria-label="Checkbox for following text input" />
                    </td>
                    <td className="menu__td preview" />
                    <td className="menu__td category" />
                    <td className="menu__td name" />
                    <td className="menu__td description" />
                    <td className="menu__td monthly" />
                    <td className="menu__td product-price" />
                    <td className="menu__td discount-rate" />
                    <td className="menu__td sales-price" />
                    <td className="menu__td quantity" />
                    <td className="menu__td create-date" />
                    <td className="menu__td creator" />
                    <td className="menu__td edit-date" />
                    <td className="menu__td editor" />
                  </tr>
                </tbody>
              </Table>
            </section>
            <MenuModal show={show} handleClose={handleClose} menuId={menuId} />
          </main>
        </div>
      </>
    );
  }
}
