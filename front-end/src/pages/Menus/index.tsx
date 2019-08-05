import React, { Component, MouseEvent } from "react";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import { Table } from "react-bootstrap";
import { handleChange } from "../../utils";
import "react-datepicker/dist/react-datepicker.css";
import "./styles.scss";
import { MenuModal } from "../../components/MenuModal";
import axios from "axios";
import { API_URL } from "../../api/common";
import { IMenus, IMenu } from "../../types/menu";
import MenuRow from "../../components/MenuRow";

interface IMenusProps {}
interface IMenusState {
  show: boolean;
  menuId: number;
  menus: IMenus;
  isReg: boolean;
  checkedRows: number[];
  isCheckedAll: boolean;
}

export default class Menus extends Component<IMenusProps, IMenusState> {
  state = {
    show: false,
    menuId: -1,
    menus: [],
    isReg: true,
    checkedRows: [] as number[],
    isCheckedAll: false
  };

  componentDidMount() {
    const result = /\/menus\/([0-9+])\??/.exec(window.location.href);
    const menuId = result && result.length > 1 ? parseInt(result[1]) : -1;

    this.getMenus();

    if (menuId > 0) {
      this.setState({
        show: true,
        menuId
      });
    }
  }

  handleCheckRowAll = () => {
    const { menus } = this.state;
    if (menus) {
      this.setState({
        checkedRows: menus.map((menu: IMenu) => menu.id),
        isCheckedAll: true
      });
    } else {
      alert("문제가 발생했습니다.");
    }
  };

  handleUncheckRowAll = () => {
    this.setState({
      checkedRows: [],
      isCheckedAll: false
    });
  };

  handleCheckRow = (menuId: number) => {
    const { checkedRows } = this.state;
    this.setState({
      checkedRows: checkedRows.concat(menuId)
    });
  };

  handleUncheckRow = (menuId: number) => {
    const { checkedRows } = this.state;
    this.setState({
      checkedRows: checkedRows.filter(i => i !== menuId)
    });
  };

  handleClose = () => {
    this.setState({
      show: false
    });
  };

  handleShowRegModal = (e: MouseEvent<HTMLButtonElement>) => {
    this.setState({
      show: true,
      isReg: true,
      menuId: -1
    });
  };

  handleShowUpdateModal = (menuId: number) => {
    this.setState({
      show: true,
      isReg: false,
      menuId
    });
  };

  async getMenus() {
    try {
      const res = await axios.get(`${API_URL}/menus`);
      if (res.status === 200) {
        const { menus } = res.data;
        this.setState({
          menus
        });
      }
    } catch (err) {
      console.log(err);
    }
  }

  render() {
    const { handleShowRegModal, handleShowUpdateModal, handleClose } = this;
    const { show, menuId, isReg, menus } = this.state;

    return (
      <>
        <Header title="메뉴 관리" />
        <div className="main-wrapper">
          <Nav />
          <main id="menus" className="col-md-10">
            <section>
              <div className="content-head d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <button className="btn btn-primary" onClick={handleShowRegModal}>
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
                        onChange={e => {
                          if (e.target.checked) {
                            this.handleCheckRowAll();
                          } else {
                            this.handleUncheckRowAll();
                          }
                        }}
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
                    <th>사용여부</th>
                    <th>등록일</th>
                    <th>등록인</th>
                    <th>수정일</th>
                    <th>수정인</th>
                  </tr>
                </thead>
                <tbody>
                  {menus.length >= 1 &&
                    menus.map((menu: IMenu, i) => (
                      <MenuRow
                        key={i}
                        isChecked={this.state.checkedRows.includes(menu.id) ? true : false}
                        menu={menu}
                        handleShowUpdateModal={handleShowUpdateModal}
                        handleCheckRow={this.handleCheckRow.bind(this)}
                        handleUncheckRow={this.handleUncheckRow.bind(this)}
                      />
                    ))}
                </tbody>
              </Table>
            </section>
            <MenuModal
              show={show}
              handleClose={handleClose}
              menuId={menuId}
              isReg={isReg}
              getMenus={this.getMenus.bind(this)}
            />
          </main>
        </div>
      </>
    );
  }
}
