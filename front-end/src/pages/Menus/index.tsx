import React, { Component, FormEvent } from "react";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import { Table, Modal, Dropdown } from "react-bootstrap";
import DatePicker from "react-datepicker";

interface IMenusProps {}
interface IMenusState {
  show: boolean;
  menuId: number;
}

interface IMenuModalProps {
  show: boolean;
  menuId: number;
  handleClose(): void;
}

interface IMenuModalState {
  isReg: boolean;
  nameKo: string;
  nameEng: string;
  description: string;
  monthlyMenu: boolean;
  categoryKo: string;
  categoryEng: string;
  productPrice: number;
  sellPrice: number;
  discountRate: number;
  stock: number;
  optionIds: Array<number>;
  usable: boolean;
  startDate: string;
  endDate: string;
}

class MenuModal extends Component<IMenuModalProps, IMenuModalState> {
  state = {
    isReg: true,
    nameKo: "",
    nameEng: "",
    description: "",
    monthlyMenu: false,
    categoryKo: "",
    categoryEng: "",
    productPrice: 0,
    sellPrice: 0,
    discountRate: 0,
    stock: 0,
    optionIds: [],
    usable: false,
    startDate: "",
    endDate: ""
  };

  input = React.createRef();

  handleUpdate() {}

  handleReg(e: FormEvent) {}

  handleChange() {}

  render() {
    const { menuId, show, handleClose } = this.props;

    const {
      nameKo,
      nameEng,
      description,
      monthlyMenu,
      categoryKo,
      categoryEng,
      productPrice,
      sellPrice,
      discountRate,
      stock,
      optionIds,
      usable,
      startDate,
      endDate
    } = this.state;

    return (
      <Modal id="menuModal" show={show} onHide={handleClose}>
        <form name="menuForm" onSubmit={e => e.preventDefault()}>
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="input-group-wrap">
                <div className="input-group name">
                  <div className="input-group-prepend">
                    <span className="input-group-text">메뉴명</span>
                  </div>
                  <input value={nameKo} type="text" className="form-control" placeholder="메뉴명" />
                </div>
                <div className="input-group en-name">
                  <div className="input-group-prepend">
                    <span className="input-group-text">영문명</span>
                  </div>
                  <input
                    value={nameEng}
                    type="text"
                    className="form-control"
                    placeholder="영문명"
                  />
                </div>
                <div className="input-group category">
                  <div className="input-group-prepend">
                    <span className="input-group-text">카테고리</span>
                  </div>
                  <div className="input-group-append">
                    <Dropdown>
                      <Dropdown.Toggle
                        variant="secondary"
                        id="category-dropdown"
                        className="btn btn-outline-secondary"
                      >
                        카테고리
                      </Dropdown.Toggle>
                      <Dropdown.Menu>
                        <Dropdown.Item as="button" eventKey="coffee" active>
                          커피
                        </Dropdown.Item>
                        <Dropdown.Item as="button" eventKey="ade">
                          에이드
                        </Dropdown.Item>
                        <Dropdown.Item as="button" eventKey="bread">
                          빵
                        </Dropdown.Item>
                      </Dropdown.Menu>
                    </Dropdown>
                  </div>
                </div>
                <div className="input-group description">
                  <div className="input-group-prepend">
                    <span className="input-group-text">설명</span>
                  </div>
                  <textarea className="form-control" placeholder="설명입니다." />
                </div>
                <div className="input-group monthly">
                  <div className="input-group-prepend">
                    <span className="input-group-text">이달의 메뉴</span>
                  </div>
                  <div className="form-control">
                    <div className="input-group">
                      <input type="radio" name="monthlymenu" checked={monthlyMenu ? true : false} />
                      <label className="choice yes">예</label>
                    </div>
                    <div className="input-group">
                      <input
                        type="radio"
                        name="monthlymenu"
                        checked={!monthlyMenu ? true : false}
                      />
                      <label className="choice no">아니오</label>
                    </div>
                  </div>
                </div>
                <div className="row">
                  <div className="input-group product-price half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">상품가</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="0,000(원)"
                      value={productPrice}
                    />
                  </div>
                  <div className="input-group discount-rate half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">할인율</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="00(%)"
                      value={discountRate}
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="input-group sales-price half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">판매가</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="0,000(원)"
                      value={sellPrice}
                    />
                  </div>
                  <div className="input-group quantity half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">재고</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="0(수량)"
                      value={stock}
                    />
                  </div>
                </div>

                <div className="input-group monthly">
                  <div className="input-group-prepend">
                    <span className="input-group-text">기간</span>
                  </div>
                  <div className="form-control">
                    <DatePicker
                      selected={new Date(startDate)}
                      onChange={this.handleChange}
                      showTimeSelect
                      timeFormat="HH:mm"
                      timeIntervals={15}
                      dateFormat="MMMM d, yyyy h:mm aa"
                      timeCaption="time"
                    />

                    <input type="datetime" name="startDate" value={startDate} />
                    <input type="datetime" name="endDate" value={endDate} />
                  </div>
                </div>

                <div className="input-group options">
                  <div className="input-group-prepend">
                    <span className="input-group-text">옵션</span>
                  </div>
                  <div className="form-control">
                    <div className="input-group align-items-center pr-1">
                      <input type="checkbox" className="option__checkbox mr-1" />
                      <label className="option-name m-0">HOT</label>
                    </div>
                    <div className="input-group align-items-center pr-1">
                      <input type="checkbox" className="option__checkbox mr-1" />
                      <label className="option-name m-0">ICE</label>
                    </div>
                    <div className="input-group align-items-center pr-1">
                      <input type="checkbox" className="option__checkbox mr-1" />
                      <label className="option-name m-0">샷추가</label>
                    </div>
                    <div className="input-group align-items-center">
                      <input type="checkbox" className="option__checkbox mr-1" />
                      <label className="option-name m-0">시럽추가</label>
                    </div>
                  </div>
                </div>

                <div className="input-group monthly">
                  <div className="input-group-prepend">
                    <span className="input-group-text">사용여부</span>
                  </div>
                  <div className="form-control">
                    <div className="input-group">
                      <input type="radio" name="usable" checked={usable ? true : false} />
                      <label className="choice yes">사용</label>
                    </div>
                    <div className="input-group">
                      <input type="radio" name="usable" checked={!usable ? true : false} />
                      <label className="choice no">미사용</label>
                    </div>
                  </div>
                </div>
              </div>
              <div className="left-wrap">
                <div className="menu-image">
                  <img
                    src="https://dummyimage.com/600x400/ffffff/ff7300.png&text=tmontica"
                    alt="메뉴 이미지"
                  />
                </div>
                <button className="reg-image__button btn btn-warning">이미지 등록</button>
                <input type="file" className="hide" hidden />
              </div>
            </div>
            <div className="modal-footer">
              <input
                type="submit"
                className="reg-menu__button btn btn-primary"
                value="등록"
                onSubmit={e => e.preventDefault()}
              />

              <button
                type="button"
                className="cancle-menu__button btn btn-danger"
                data-dismiss="modal"
              >
                취소
              </button>
            </div>
          </div>
        </form>
      </Modal>
    );
  }
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

  handleShow = () => {
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
                      <input type="checkbox" aria-label="Checkbox for following text input" />
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
