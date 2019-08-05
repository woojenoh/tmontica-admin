import * as React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import OrderRow from "../../components/OrderRow";
import OrderModal from "../../components/OrderModal";
import Pagination from "../../components/Pagination";
import * as orderTypes from "../../types/order";
import * as commonTypes from "../../types/common";
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
}

class Order extends React.Component<IOrderProps, IOrderState> {
  componentDidMount() {
    this.handleFetchAll();
  }

  state = {
    orders: null,
    isModalOpen: false,
    orderDetail: null,
    selectedOrderId: null,
    currentPage: 1,
    pageSize: 10,
    pagination: null
  } as IOrderState;

  handleFetchAll = () => {
    axios
      .get("http://tmonticaadmin-idev.tmon.co.kr/api/orders/today", {
        params: {
          page: 1,
          size: this.state.pageSize
        }
      })
      .then((res: AxiosResponse) => {
        this.setState({
          orders: res.data.orders,
          pagination: res.data.pagination
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
        .get(`http://tmonticaadmin-idev.tmon.co.kr/api/orders/detail/${orderId}`)
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
        axios
          .get("http://tmonticaadmin-idev.tmon.co.kr/api/orders/today", {
            params: {
              page: this.state.currentPage,
              size: this.state.pageSize
            }
          })
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
    );
  };

  render() {
    const { orders, isModalOpen, orderDetail, pagination } = this.state;
    const { handleFetchAll, handleModalOpen, handleModalClose, handleSelectPage } = this;

    return (
      <>
        <Header title="주문 내역" />
        <div className="main-wrapper">
          <Nav />
          <main className="main col-md-10 p-4">
            <section className="order">
              <div className="order-top mb-4 d-flex justify-content-between">
                <div className="order-title d-flex flex-column justify-content-between">
                  <h1 className="m-0">주문 내역 보기</h1>
                  <button className="btn btn-outline-primary" onClick={() => handleFetchAll()}>
                    전체 내역 보기
                  </button>
                </div>
                <form className="order-search text-right">
                  <div className="order-period d-flex mb-2">
                    <input type="date" className="form-control mr-2" required />
                    <input type="date" className="form-control" required />
                  </div>
                  <div className="order-search d-flex">
                    <select className="custom-select mr-2 w-25" required>
                      <option value="주문자">주문자</option>
                      <option value="주문번호">주문번호</option>
                      <option value="주문상태">주문상태</option>
                      <option value="결제방법">결제방법</option>
                    </select>
                    <input
                      type="text"
                      className="form-control mr-2 w-50"
                      placeholder="검색할 내용"
                      required
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
