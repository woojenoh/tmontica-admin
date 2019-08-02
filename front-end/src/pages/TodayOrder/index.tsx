import * as React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import TodayOrderRow from "../../components/TodayOrderRow";
import TodayOrderModal from "../../components/TodayOrderModal";
import TodayOrderStatus from "../../components/TodayOrderStatus";
import * as orderTypes from "../../types/order";

export interface ITodayOrderProps {}

export interface ITodayOrderState {
  orders: orderTypes.IOrder[] | null;
  statusCount: orderTypes.IOrderStatusCount | null;
  orderDetail: orderTypes.IOrderDetail | null;
  isModalOpen: boolean;
  selectedTodayStatus: string | null;
  selectedSelectStatus: string;
  selectedOrderId: number | null;
}

class TodayOrder extends React.Component<ITodayOrderProps, ITodayOrderState> {
  componentDidMount() {
    axios
      .get("http://tmonticaadmin-idev.tmon.co.kr/api/orders/today", {
        params: {
          page: 1,
          size: 30
        }
      })
      .then((res: AxiosResponse) => {
        this.setState({
          orders: res.data.orders,
          statusCount: res.data.statusCount
        });
      })
      .catch((err: AxiosError) => {
        alert(err);
      });
  }

  status = ["미결제", "결제완료", "제작중", "준비완료", "픽업완료", "주문취소"];
  statusEng = [
    "beforePayment",
    "afterPayment",
    "inProduction",
    "ready",
    "pickUp",
    "cancel"
  ] as orderTypes.TOrderStatusEng[];

  state = {
    orders: null,
    statusCount: null,
    orderDetail: null,
    isModalOpen: false,
    selectedTodayStatus: null,
    selectedSelectStatus: this.status[0],
    selectedOrderId: null
  } as ITodayOrderState;

  handleModalOpen = (orderId: number) => {
    const { selectedOrderId } = this.state;
    if (orderId !== selectedOrderId) {
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

  handleClickTodayStatus = (statusName: string) => {
    axios
      .get("http://tmonticaadmin-idev.tmon.co.kr/api/orders/today", {
        params: {
          page: 1,
          size: 30,
          status: statusName
        }
      })
      .then((res: AxiosResponse) => {
        this.setState({
          orders: res.data.orders,
          statusCount: res.data.statusCount,
          selectedTodayStatus: statusName
        });
      })
      .catch((err: AxiosError) => {
        alert(err);
      });
  };

  initializeTodayStatus = () => {
    axios
      .get("http://tmonticaadmin-idev.tmon.co.kr/api/orders/today", {
        params: {
          page: 1,
          size: 30
        }
      })
      .then((res: AxiosResponse) => {
        this.setState({
          orders: res.data.orders,
          statusCount: res.data.statusCount,
          selectedTodayStatus: null
        });
      })
      .catch((err: AxiosError) => {
        alert(err);
      });
  };

  handleChangeSelectStatus = (e: React.FormEvent<HTMLSelectElement>) => {
    this.setState({
      selectedSelectStatus: e.currentTarget.value
    });
  };

  public render() {
    const {
      isModalOpen,
      selectedSelectStatus,
      selectedTodayStatus,
      statusCount,
      orders,
      orderDetail
    } = this.state;
    const {
      status,
      statusEng,
      handleModalOpen,
      handleModalClose,
      handleChangeSelectStatus,
      handleClickTodayStatus,
      initializeTodayStatus
    } = this;

    return (
      <>
        <Header title="주문 관리" />
        <div className="main-wrapper">
          <Nav />
          <main className="col-md-10">
            {/* <!-- 오늘의 현황 --> */}
            <section className="today-board__section">
              <div className="content-head d-flex flex-wrap flex-md-nowrap align-items-center pt-3 mb-3 border-bottom">
                <h4 className="mb-3">오늘의 현황</h4>
              </div>
              <div className="today-board-list">
                {statusCount
                  ? status.map((s: string, index: number) => {
                      return (
                        <TodayOrderStatus
                          key={index}
                          index={index}
                          statusName={s}
                          statusCount={statusCount[statusEng[index]]}
                          handleClickTodayStatus={handleClickTodayStatus}
                          initializeTodayStatus={initializeTodayStatus}
                          isActive={s === selectedTodayStatus}
                        />
                      );
                    })
                  : "로딩 중입니다."}
              </div>
            </section>

            {/* <!-- 주문내역 --> */}
            <section className="order-list__section">
              <div className="content-head d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 mt-4 mb-3">
                <h4>주문내역({selectedTodayStatus ? selectedTodayStatus : "전체"})</h4>
                <div className="order-select d-flex">
                  <select
                    className="mr-2"
                    value={selectedSelectStatus}
                    onChange={e => handleChangeSelectStatus(e)}
                  >
                    {status.map((s: string, index: number) => {
                      return (
                        <option key={index} value={s}>
                          {s}
                        </option>
                      );
                    })}
                  </select>
                  <button className="btn btn-primary">적용</button>
                </div>
              </div>

              {/* <!-- 주문내역 목록 --> */}
              <table className="content-table table table-striped table-sm mb-0">
                <thead>
                  <tr className="text-center">
                    <th>
                      <input type="checkbox" aria-label="Checkbox for following text input" />
                    </th>
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
                        <TodayOrderRow
                          key={o.orderId}
                          order={o}
                          handleModalOpen={handleModalOpen}
                        />
                      );
                    })
                  ) : (
                    <tr>
                      <td>로딩 중입니다.</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </section>

            {/* <!-- 주문 상세 모달 --> */}
            <TodayOrderModal
              isModalOpen={isModalOpen}
              handleModalClose={handleModalClose}
              orderDetail={orderDetail}
            />
          </main>
        </div>
      </>
    );
  }
}

export default TodayOrder;
