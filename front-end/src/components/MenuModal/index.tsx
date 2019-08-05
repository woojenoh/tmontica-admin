import React, { ChangeEvent, PureComponent } from "react";
import { Modal } from "react-bootstrap";
import DatePicker from "react-datepicker";
import { handleChange, formatDate, setImagePreview, withJWT } from "../../utils";
import "react-datepicker/dist/react-datepicker.css";
import "./styles.scss";
import { API_URL, BASE_URL } from "../../api/common";
import axios from "axios";
import { Indexable } from "../../types/index";
import jwt_decode from "jwt-decode";

interface IMenuModalProps {
  show: boolean;
  menuId: number;
  isReg: boolean;
  handleClose(): void;
  getMenus(): void;
}

interface IMenuModalState {
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
  optionIds: Set<number>;
  usable: boolean;
  startDate: string;
  endDate: string;
  imgFile: Blob | string;
  imgUrl: string | ArrayBuffer | null;
}

const initState = {
  nameKo: "",
  nameEng: "",
  description: "",
  monthlyMenu: false,
  categoryKo: "에이드",
  categoryEng: "",
  productPrice: 0,
  sellPrice: 0,
  discountRate: 0,
  stock: 0,
  optionIds: new Set([]) as Set<number>,
  usable: false,
  startDate: formatDate(new Date()),
  endDate: formatDate(new Date()),
  imgFile: "",
  imgUrl: ""
};

export class MenuModal extends PureComponent<IMenuModalProps, IMenuModalState>
  implements Indexable {
  [key: string]: any;
  fileInput: React.RefObject<HTMLInputElement> = React.createRef();
  form?: HTMLFormElement;

  state = {
    ...initState
  };

  constructor(props: IMenuModalProps, state: IMenuModalState) {
    super(props, state);

    this.clickFileInput = this.clickFileInput.bind(this);
  }

  async getMenuById() {
    if (this.props.isReg) {
      this.setState({
        ...initState
      });
      return;
    }
    try {
      const res = await axios.get(`${API_URL}/menus/${this.props.menuId}`, {
        headers: { Authorization: jwt_decode(localStorage.getItem("jwt") || "") }
      });

      const optionIds = res.data.option.map((o: { id: number }) => o.id);
      this.setState({
        ...res.data,
        optionIds: new Set(optionIds)
      });
    } catch (err) {
      alert(err);
    }
  }

  clickFileInput() {
    if (this.fileInput.current) {
      this.fileInput.current.click();
    }
  }

  componentDidUpdate(prevProps: IMenuModalProps) {
    if (prevProps.menuId !== this.props.menuId) {
      this.getMenuById();
    }
  }

  // 이미지 파일 미리보기
  handleImageFileChange(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files) {
      this.setState({
        imgUrl: URL.createObjectURL(e.target.files[0])
      });
    }
  }

  render() {
    const { menuId, show, handleClose, isReg, getMenus } = this.props;
    const regName = isReg ? "등록" : "수정";

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
      endDate,
      imgUrl
    } = this.state;

    return (
      <Modal id="menuModal" show={show} onHide={handleClose} size="lg">
        <form
          name="menuForm"
          ref={el => (this.form = el ? el : undefined)}
          onSubmit={e => {
            e.preventDefault();
          }}
        >
          <Modal.Body>
            <div className="modal-content">
              <div className="input-group-wrap">
                <div className="input-group name">
                  <div className="input-group-prepend">
                    <span className="input-group-text">메뉴명</span>
                  </div>
                  <input
                    value={nameKo}
                    name="nameKo"
                    ref={el => {
                      this["nameKo"] = el;
                    }}
                    type="text"
                    className="form-control"
                    placeholder="메뉴명"
                    onChange={handleChange.bind(this)}
                  />
                </div>
                <div className="input-group en-name">
                  <div className="input-group-prepend">
                    <span className="input-group-text">영문명</span>
                  </div>
                  <input
                    value={nameEng}
                    name="nameEng"
                    ref={el => {
                      this["nameEng"] = el;
                    }}
                    type="text"
                    className="form-control"
                    placeholder="영문명"
                    onChange={handleChange.bind(this)}
                  />
                </div>
                <div className="input-group category">
                  <div className="input-group-prepend">
                    <span className="input-group-text">카테고리</span>
                  </div>
                  <div className="input-group-append">
                    <select
                      name="categoryEng"
                      ref={el => {
                        this["categoryEng"] = el;
                      }}
                      value={categoryEng}
                      onChange={e => {
                        this.setState({
                          categoryEng: e.target.value
                        });
                      }}
                    >
                      <option value="">카테고리</option>
                      <option value="coffee">커피</option>
                      <option value="ade">에이드</option>
                      <option value="bread">빵</option>
                    </select>
                  </div>
                </div>
                <div className="input-group description">
                  <div className="input-group-prepend">
                    <span className="input-group-text">설명</span>
                  </div>
                  <textarea
                    className="form-control"
                    placeholder="설명입니다."
                    name="description"
                    ref={el => {
                      this["description"] = el;
                    }}
                    value={description}
                    onChange={handleChange.bind(this)}
                  />
                </div>
                <div className="input-group monthly">
                  <div className="input-group-prepend">
                    <span className="input-group-text">이달의 메뉴</span>
                  </div>
                  <div className="form-control">
                    <div className="input-group">
                      <input
                        type="radio"
                        name="monthlyMenu"
                        checked={monthlyMenu ? true : false}
                        onChange={e => {
                          if (e.target.checked) {
                            this.setState({
                              monthlyMenu: true
                            });
                          }
                        }}
                      />
                      <label className="choice yes">예</label>
                    </div>
                    <div className="input-group">
                      <input
                        type="radio"
                        name="monthlyMenu"
                        checked={!monthlyMenu ? true : false}
                        onChange={e => {
                          if (e.target.checked) {
                            this.setState({
                              monthlyMenu: false
                            });
                          }
                        }}
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
                      name="productPrice"
                      ref={el => {
                        this["productPrice"] = el;
                      }}
                      className="form-control"
                      placeholder="0,000(원)"
                      value={Number(productPrice).toLocaleString()}
                      onChange={e => {
                        const numericValue = parseInt(e.target.value.replace(/\,/g, ""));
                        const productPrice = !Number.isNaN(numericValue) ? numericValue : 0;
                        this.setState({
                          productPrice,
                          sellPrice: Number(
                            productPrice > 0
                              ? productPrice * ((100 - discountRate) / 100)
                              : productPrice
                          )
                        });
                      }}
                    />
                  </div>
                  <div className="input-group discount-rate half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">할인율</span>
                    </div>
                    <input
                      value={Number(discountRate).toLocaleString()}
                      name="discountRate"
                      ref={el => {
                        this["discountRate"] = el;
                      }}
                      type="text"
                      className="form-control"
                      placeholder="00(%)"
                      onChange={e => {
                        const numericValue = parseInt(e.target.value);
                        const discountRate = !Number.isNaN(numericValue) ? numericValue : 0;
                        if (discountRate > 100) {
                          alert("할인율은 100을 넘을 수 없습니다.");
                          return;
                        }
                        this.setState({
                          discountRate,
                          sellPrice: Number(
                            productPrice > 0
                              ? productPrice * ((100 - discountRate) / 100)
                              : productPrice
                          )
                        });
                      }}
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="input-group sales-price half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">판매가</span>
                    </div>
                    <input
                      value={sellPrice.toLocaleString()}
                      name="sellPrice"
                      ref={el => {
                        this["sellPrice"] = el;
                      }}
                      type="text"
                      className="form-control"
                      placeholder="0,000(원)"
                      readOnly
                    />
                  </div>
                  <div className="input-group quantity half">
                    <div className="input-group-prepend">
                      <span className="input-group-text">재고</span>
                    </div>
                    <input
                      value={stock}
                      name="stock"
                      ref={el => {
                        this["stock"] = el;
                      }}
                      type="text"
                      className="form-control"
                      placeholder="0(수량)"
                      onChange={e => {
                        const numericValue = parseInt(e.target.value.replace(/\,/g, ""));
                        const stock = !Number.isNaN(numericValue) ? numericValue : 0;
                        this.setState({
                          stock
                        });
                      }}
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
                      name="ㄹ"
                      onChange={date =>
                        this.setState({
                          startDate: formatDate(date)
                        })
                      }
                      showTimeSelect
                      timeFormat="HH:mm"
                      timeIntervals={30}
                      dateFormat="yyyy.MM.dd HH:mm:ss"
                      timeCaption="time"
                    />
                    <DatePicker
                      selected={new Date(endDate)}
                      name="endDate"
                      onChange={date =>
                        this.setState({
                          endDate: formatDate(date)
                        })
                      }
                      showTimeSelect
                      timeFormat="HH:mm"
                      timeIntervals={15}
                      dateFormat="yyyy.MM.dd HH:mm:ss"
                      timeCaption="time"
                    />
                  </div>
                </div>

                {/coffee|ade/.test(categoryEng) ? (
                  <div className="input-group options">
                    <div className="input-group-prepend">
                      <span className="input-group-text">옵션</span>
                    </div>
                    <div className="form-control">
                      <div className="input-group align-items-center pr-1">
                        <input
                          name="optionIds[]"
                          value="1"
                          checked={optionIds.has(1) ? true : false}
                          type="checkbox"
                          className="option__checkbox mr-1"
                          onChange={e => {
                            const value = parseInt(e.target.value);
                            if (e.target.checked) {
                              optionIds.add(value);
                            } else {
                              optionIds.delete(value);
                            }
                            this.setState({
                              optionIds: new Set(optionIds)
                            });
                          }}
                        />
                        <label className="option-name m-0">HOT</label>
                      </div>
                      <div className="input-group align-items-center pr-1">
                        <input
                          name="optionIds[]"
                          value="2"
                          checked={optionIds.has(2) ? true : false}
                          type="checkbox"
                          className="option__checkbox mr-1"
                          onChange={e => {
                            const value = parseInt(e.target.value);
                            if (e.target.checked) {
                              optionIds.add(value);
                            } else {
                              optionIds.delete(value);
                            }
                            this.setState({
                              optionIds: new Set(optionIds)
                            });
                          }}
                        />
                        <label className="option-name m-0">ICE</label>
                      </div>
                      <div className="input-group align-items-center pr-1">
                        <input
                          name="optionIds[]"
                          value="3"
                          checked={optionIds.has(3) ? true : false}
                          type="checkbox"
                          className="option__checkbox mr-1"
                          onChange={e => {
                            const value = parseInt(e.target.value);
                            if (e.target.checked) {
                              optionIds.add(value);
                            } else {
                              optionIds.delete(value);
                            }
                            this.setState({
                              optionIds: new Set(optionIds)
                            });
                          }}
                        />
                        <label className="option-name m-0">샷추가</label>
                      </div>
                      <div className="input-group align-items-center">
                        <input
                          name="optionIds[]"
                          value="4"
                          checked={optionIds.has(4) ? true : false}
                          type="checkbox"
                          className="option__checkbox mr-1"
                          onChange={e => {
                            const value = parseInt(e.target.value);
                            if (e.target.checked) {
                              optionIds.add(value);
                            } else {
                              optionIds.delete(value);
                            }
                            this.setState({
                              optionIds: new Set(optionIds)
                            });
                          }}
                        />
                        <label className="option-name m-0">시럽추가</label>
                      </div>

                      <div className="input-group align-items-center">
                        <input
                          name="optionIds[]"
                          value="5"
                          checked={optionIds.has(5) ? true : false}
                          type="checkbox"
                          className="option__checkbox mr-1"
                          onChange={e => {
                            const value = parseInt(e.target.value);
                            if (e.target.checked) {
                              optionIds.add(value);
                            } else {
                              optionIds.delete(value);
                            }
                            this.setState({
                              optionIds: new Set(optionIds)
                            });
                          }}
                        />
                        <label className="option-name m-0">사이즈업</label>
                      </div>
                    </div>
                  </div>
                ) : (
                  ""
                )}

                <div className="input-group monthly">
                  <div className="input-group-prepend">
                    <span className="input-group-text">사용여부</span>
                  </div>
                  <div className="form-control">
                    <div className="input-group">
                      <input
                        type="radio"
                        name="usable"
                        checked={usable ? true : false}
                        onChange={e => {
                          if (e.target.checked) {
                            this.setState({
                              usable: true
                            });
                          }
                        }}
                      />
                      <label className="choice yes">사용</label>
                    </div>
                    <div className="input-group">
                      <input
                        type="radio"
                        name="usable"
                        checked={!usable ? true : false}
                        onChange={e => {
                          if (e.target.checked) {
                            this.setState({
                              usable: false
                            });
                          }
                        }}
                      />
                      <label className="choice no">미사용</label>
                    </div>
                  </div>
                </div>
              </div>
              <div className="left-wrap">
                <div className="menu-image">
                  <img
                    src={
                      imgUrl
                        ? /^data/.test(imgUrl)
                          ? imgUrl
                          : `${BASE_URL}/${imgUrl}`
                        : "https://dummyimage.com/600x400/ffffff/ff7300.png&text=tmontica"
                    }
                    alt="메뉴 이미지"
                  />
                </div>
                <button className="reg-image__button btn btn-warning" onClick={this.clickFileInput}>
                  이미지 등록
                </button>
                <input
                  type="file"
                  ref={this.fileInput}
                  className="hide"
                  hidden
                  onChange={e => {
                    e.preventDefault();

                    // https://hyunseob.github.io/2018/06/24/debounce-react-synthetic-event/
                    e.persist();
                    setImagePreview(e.target.files, (imgUrl: any) => {
                      this.setState({
                        imgUrl,
                        imgFile: e.target.files !== null ? (e.target.files[0] as Blob) : ""
                      });
                    });
                  }}
                />
              </div>
            </div>
            <div className="modal-footer">
              <input
                type="submit"
                className="reg-menu__button btn btn-primary"
                value={regName}
                onClick={e => {
                  e.preventDefault();

                  const data = new FormData();
                  if (!this.state.nameKo) {
                    alert("메뉴명을 입력해주세요.");
                    this.nameKo.focus();
                    return;
                  }
                  if (!this.state.nameEng) {
                    alert("영문명을 입력해주세요.");
                    this.nameEng.focus();
                    return;
                  }
                  if (!this.state.categoryEng) {
                    alert("카테고리를 선택해주세요.");
                    this.categoryEng.focus();
                    return;
                  }
                  if (!this.state.imgFile && isReg) {
                    alert("이미지를 등록해주세요.");
                    return;
                  }
                  if (!isReg) {
                    data.append("menuId", `${this.props.menuId}`);
                  }
                  data.append("nameKo", this.state.nameKo);
                  data.append("nameEng", this.state.nameEng);
                  data.append("description", this.state.description);
                  data.append("monthlyMenu", `${this.state.monthlyMenu}`);
                  data.append("categoryKo", this.state.categoryKo);
                  data.append("categoryEng", this.state.categoryEng);
                  data.append("productPrice", this.state.productPrice.toString());
                  data.append("sellPrice", this.state.sellPrice.toString());
                  data.append("discountRate", this.state.discountRate.toString());
                  data.append("stock", this.state.stock.toString());
                  this.state.optionIds.forEach(v => {
                    data.append("optionIds", v.toString());
                  });
                  data.append("usable", `${this.state.usable}`);
                  data.append("startDate", this.state.startDate);
                  data.append("endDate", this.state.endDate);
                  if (this.state.imgFile) {
                    data.append("imgFile", this.state.imgFile);
                  }
                  const options = {
                    headers: { "content-type": "multipart/form-data" }
                  };

                  const isConfirm = window.confirm(`${regName}하시겠습니까?`);
                  if (!isConfirm) return;

                  if (isReg) {
                    axios.post(`${API_URL}/menus`, data, withJWT(options)).then(res => {
                      alert("메뉴가 등록되었습니다.");
                      this.setState({ ...initState });
                      getMenus();
                    });
                  } else {
                    axios.put(`${API_URL}/menus`, data, withJWT(options)).then(res => {
                      alert("메뉴가 수정되었습니다.");
                      getMenus();
                    });
                  }
                }}
              />
              {!isReg ? (
                <input
                  type="submit"
                  className="reg-menu__button btn btn-primary"
                  value="삭제"
                  onClick={e => {
                    e.preventDefault();

                    axios.delete(`${API_URL}/menus/${menuId}`, withJWT()).then(res => {
                      alert("메뉴가 삭제되었습니다.");
                      handleClose();
                      getMenus();
                    });
                  }}
                />
              ) : (
                ""
              )}

              <button className="cancle-menu__button btn btn-danger" onClick={handleClose}>
                취소
              </button>
            </div>
          </Modal.Body>
        </form>
      </Modal>
    );
  }
}
