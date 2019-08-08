import * as React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import OrderRow from "../../components/OrderRow";
import OrderModal from "../../components/OrderModal";
import Pagination from "../../components/Pagination";
import * as orderTypes from "../../types/order";
import * as commonTypes from "../../types/common";
import { API_URL } from "../../api/common";
import { withJWT } from "../../utils";
import "./styles.scss";

export interface IOrderProps {}

export interface IOrderState {
  orders: orderTypes.IOrder[] | null;
  isModalOpen: boolean;
  orderDetail: orderTypes.IOrderDetail | null;
  selectedOrderId: number | null;
  currentPage: number;
  pageSize: number;
  pagination: commonTypes.IPagination | null;
  startDate: string;
  endDate: string;
  searchType: string;
  searchValue: string;
  capturedInput: {
    startDate: string;
    endDate: string;
    searchType: string;
    searchValue: string;
  };
}

export interface InputState {
  startDate: string;
  endDate: string;
  searchType: string;
  searchValue: string;
}

class Order extends React.PureComponent<IOrderProps, IOrderState> {
  componentDidMount() {
    this.handleFetchAll();
  }

  searchTypes = [
    "선택",
    "주문자",
    "주문번호",
    "주문상태",
    "결제방법"
  ] as orderTypes.TOrderSearchType[];

  state = {
    orders: null,
    isModalOpen: false,
    orderDetail: null,
    selectedOrderId: null,
    currentPage: 1,
    pageSize: 10,
    pagination: null,
    startDate: "",
    endDate: "",
    searchType: this.searchTypes[0],
    searchValue: "",
    // 검색 버튼을 눌렀을 당시의 인풋 상태를 저장하기 위한 상태값.
    capturedInput: {
      startDate: "",
      endDate: "",
      searchType: this.searchTypes[0],
      searchValue: ""
    }
  } as IOrderState;

  handleInputChange = (e: React.FormEvent<HTMLInputElement>) => {
    this.setState({
      [e.currentTarget.name]: e.currentTarget.value
    } as { [K in keyof InputState]: InputState[K] });
  };

  handleSearchTypeChange = (e: React.FormEvent<HTMLSelectElement>) => {
    this.setState({
      searchType: e.currentTarget.value as orderTypes.TOrderSearchType
    });
  };

  handleSearchSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // 검색 버튼을 누르면 우선 인풋에 있는 값들을 CapturedInput에 저장한다.
    this.setState(
      {
        capturedInput: {
          startDate: this.state.startDate,
          endDate: this.state.endDate,
          searchType: this.state.searchType,
          searchValue: this.state.searchValue
        }
      },
      () => {
        if (
          (this.state.capturedInput.searchType !== "선택" &&
            this.state.capturedInput.searchValue === "") ||
          (this.state.capturedInput.searchType === "선택" &&
            this.state.capturedInput.searchValue !== "")
        ) {
          alert("검색 타입에 해당하는 검색어를 입력하세요.");
        } else {
          axios
            .get(
              `${API_URL}/orders/history`,
              withJWT({
                params: {
                  page: this.state.currentPage,
                  size: this.state.pageSize,
                  startDate: this.state.capturedInput.startDate,
                  endDate: this.state.capturedInput.endDate,
                  searchType:
                    this.state.capturedInput.searchType === "선택" ? "" : this.state.searchType,
                  searchValue: this.state.capturedInput.searchValue
                }
              })
            )
            .then((res: AxiosResponse) => {
              this.setState({
                orders: res.data.orders,
                pagination: res.data.pagination
              });
            })
            .catch((err: AxiosError) => {
              alert(err);
            });
        }
      }
    );
  };

  handleFetchAll = () => {
    axios
      .get(
        `${API_URL}/orders/history`,
        withJWT({
          params: {
            page: 1,
            size: this.state.pageSize,
            startDate: "",
            endDate: "",
            searchType: "",
            searchValue: ""
          }
        })
      )
      .then((res: AxiosResponse) => {
        this.setState({
          orders: res.data.orders,
          pagination: res.data.pagination,
          startDate: "",
          endDate: "",
          searchType: this.searchTypes[0],
          searchValue: "",
          capturedInput: {
            startDate: "",
            endDate: "",
            searchType: this.searchTypes[0],
            searchValue: ""
          }
        });
      })
      .catch((err: AxiosError) => {
        alert(err);
      });
  };

  handleModalOpen = (orderId: number) => {
    const { selectedOrderId, orderDetail } = this.state;
    if (orderId !== selectedOrderId || orderDetail === null) {
      axios
        .get(`http://tmonticaadmin-idev.tmon.co.kr/api/orders/detail/${orderId}`, withJWT())
        .then((res: AxiosResponse) => {
          this.setState({
            orderDetail: res.data,
            selectedOrderId: orderId,
            isModalOpen: true
          });
        })
        .catch((err: AxiosError) => {
          alert(err);
        });
    } else {
      this.setState({
        isModalOpen: true
      });
    }
  };

  handleModalClose = () => {
    this.setState({
      isModalOpen: false
    });
  };

  handleSelectPage = (pageNumber: number) => {
    this.setState(
      {
        currentPage: pageNumber
      },
      () => {
        // 페이지를 넘길 때 CapturedInput 상태를 사용한다.
        // 계속 컨트롤 중인 인풋 상태를 사용하면 검색 버튼을 누르지 않았는데도, 내역이 바뀔 수 있기 때문.
        axios
          .get(
            `${API_URL}/orders/history`,
            withJWT({
              params: {
                page: this.state.currentPage,
                size: this.state.pageSize,
                startDate: this.state.capturedInput.startDate,
                endDate: this.state.capturedInput.endDate,
                searchType:
                  this.state.capturedInput.searchType === "선택" ? "" : this.state.searchType,
                searchValue: this.state.capturedInput.searchValue
              }
            })
          )
          .then((res: AxiosResponse) => {
            this.setState({
              orders: res.data.orders,
              pagination: res.data.pagination,
              currentPage: 1
            });
          })
          .catch((err: AxiosError) => {
            alert(err);
          });
      }
    );
  };

  render() {
    const {
      orders,
      isModalOpen,
      orderDetail,
      pagination,
      startDate,
      endDate,
      searchType,
      searchValue
    } = this.state;
    const {
      handleFetchAll,
      handleModalOpen,
      handleModalClose,
      handleSelectPage,
      handleInputChange,
      handleSearchTypeChange,
      searchTypes,
      handleSearchSubmit
    } = this;

    return (
      <>
        <Header title="주문 내역" />
        <div className="main-wrapper">
          <Nav />
          <main className="main col-md-10 p-4">
            <section className="order">
              <div className="order-top mb-4 d-flex justify-content-between">
                <div className="order-title d-flex flex-column justify-content-between">
                  <h1 className="m-0">주문 내역 검색</h1>
                  <button className="btn btn-outline-primary" onClick={() => handleFetchAll()}>
                    전체 내역 보기
                  </button>
                </div>
                <form className="order-search text-right" onSubmit={e => handleSearchSubmit(e)}>
                  <div className="order-period d-flex mb-2">
                    <input
                      type="date"
                      className="form-control mr-2"
                      name="startDate"
                      value={startDate}
                      onChange={e => handleInputChange(e)}
                    />
                    <input
                      type="date"
                      className="form-control"
                      name="endDate"
                      value={endDate}
                      onChange={e => handleInputChange(e)}
                    />
                  </div>
                  <div className="order-search d-flex">
                    <select
                      className="custom-select mr-2 w-25"
                      value={searchType}
                      onChange={e => handleSearchTypeChange(e)}
                    >
                      {searchTypes.map((s: string, index: number) => {
                        return (
                          <option key={index} value={s}>
                            {s}
                          </option>
                        );
                      })}
                    </select>
                    <input
                      type="text"
                      className="form-control mr-2 w-50"
                      placeholder="검색할 내용"
                      name="searchValue"
                      value={searchValue}
                      onChange={e => handleInputChange(e)}
                    />
                    <input type="submit" value="검색" className="btn btn-primary w-25" />
                  </div>
                </form>
              </div>
              <div className="order-table">
                <table className="table table-striped mb-4">
                  <thead>
                    <tr className="text-center">
                      <th>주문번호</th>
                      <th>주문자</th>
                      <th>주문메뉴</th>
                      <th>결제방법</th>
                      <th>결제금액</th>
                      <th>주문일시</th>
                      <th>주문상태</th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders ? (
                      orders.map(o => {
                        return (
                          <OrderRow key={o.orderId} order={o} handleModalOpen={handleModalOpen} />
                        );
                      })
                    ) : (
                      <tr>
                        <td>로딩 중입니다.</td>
                      </tr>
                    )}
                  </tbody>
                </table>

                <Pagination pagination={pagination} handleSelectPage={handleSelectPage} />
              </div>
            </section>

            <OrderModal
              orderDetail={orderDetail}
              isModalOpen={isModalOpen}
              handleModalClose={handleModalClose}
            />
          </main>
        </div>
      </>
    );
  }
}

export default Order;
