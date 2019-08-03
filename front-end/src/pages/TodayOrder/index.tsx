import * as React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import _ from "underscore";
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
  selectedTodayStatus: orderTypes.TOrderStatusKor | null;
  selectedSelectStatus: orderTypes.TOrderStatusKor;
  selectedOrderId: number | null;
  isCheckedAll: boolean;
  checkedOrderIds: number[];
  intervalId: NodeJS.Timeout | null;
}

class TodayOrder extends React.Component<ITodayOrderProps, ITodayOrderState> {
  componentDidMount() {
    // 처음에 전체내역을 한번 가져온다.
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

    // 실시간으로 주문내역을 확인하기 위해 반복.
    this.startInterval(1000);
  }

  // 컴포넌트가 언마운트될 때 인터벌 제거.
  componentWillUnmount() {
    if (this.state.intervalId) {
      clearInterval(this.state.intervalId);
    }
  }

  startInterval = (time: number) => {
    const intervalId = setInterval(() => {
      axios
        .get("http://tmonticaadmin-idev.tmon.co.kr/api/orders/today", {
          params: {
            page: 1,
            size: 30,
            status: this.state.selectedTodayStatus
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
    }, time);
    this.setState({
      intervalId: intervalId
    });
  };

  status = [
    "미결제",
    "결제완료",
    "제작중",
    "준비완료",
    "픽업완료",
    "주문취소"
  ] as orderTypes.TOrderStatusKor[];
  statusEng = [
    "beforePayment",
    "afterPayment",
    "inProduction",
    "ready",
    "pickUp",
    "cancel"
  ] as orderTypes.TOrderStatusEng[];
  statusToEng = {
    미결제: "beforePayment",
    결제완료: "afterPayment",
    제작중: "inProduction",
    준비완료: "ready",
    픽업완료: "pickUp",
    주문취소: "cancel"
  } as { [key in orderTypes.TOrderStatusKor]: orderTypes.TOrderStatusEng };

  state = {
    orders: null,
    statusCount: null,
    orderDetail: null,
    isModalOpen: false,
    selectedTodayStatus: null,
    selectedSelectStatus: this.status[0],
    selectedOrderId: null,
    isCheckedAll: false,
    checkedOrderIds: [],
    intervalId: null
  } as ITodayOrderState;

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

  handleClickTodayStatus = (statusName: orderTypes.TOrderStatusKor) => {
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
      selectedSelectStatus: e.currentTarget.value as orderTypes.TOrderStatusKor
    });
  };

  handleCheckRowAll = () => {
    const { orders } = this.state;
    if (orders) {
      this.setState({
        checkedOrderIds: orders.map(o => o.orderId),
        isCheckedAll: true
      });
    } else {
      alert("문제가 발생했습니다.");
    }
  };

  handleUncheckRowAll = () => {
    this.setState({
      checkedOrderIds: [],
      isCheckedAll: false
    });
  };

  handleCheckRow = (orderId: number) => {
    const { checkedOrderIds } = this.state;
    this.setState({
      checkedOrderIds: checkedOrderIds.concat(orderId)
    });
  };

  handleUncheckRow = (orderId: number) => {
    const { checkedOrderIds } = this.state;
    this.setState({
      checkedOrderIds: checkedOrderIds.filter(i => i !== orderId)
    });
  };

  handleChangeStatusSubmit = () => {
    const {
      orders,
      selectedSelectStatus,
      statusCount,
      selectedTodayStatus,
      checkedOrderIds
    } = this.state;
    const { statusToEng, handleUncheckRow, handleUncheckRowAll } = this;

    if (orders && statusCount) {
      axios
        .put("http://tmonticaadmin-idev.tmon.co.kr/api/orders/status", {
          orderIds: checkedOrderIds,
          status: selectedSelectStatus
        })
        .then(() => {
          const newStatusCount = _(statusCount).clone() as orderTypes.IOrderStatusCount;
          // 주문상태 변경에 따른 State 변경 로직.
          // 현재 보고있는 주문내역이 전체일 경우, 체크된 Row의 주문상태를 변경한다.
          if (selectedTodayStatus === null) {
            const newOrders = orders.map(o => {
              if (checkedOrderIds.indexOf(o.orderId) !== -1) {
                // 체크된 Row에 해당하는 StatusCount를 변경한다.
                if (o.status !== selectedSelectStatus) {
                  newStatusCount[statusToEng[selectedSelectStatus]] += 1;
                  newStatusCount[statusToEng[o.status]] -= 1;
                }
                o.status = selectedSelectStatus;
                handleUncheckRow(o.orderId);
                return o;
              } else {
                return o;
              }
            });
            handleUncheckRowAll();
            this.setState({
              orders: newOrders,
              statusCount: newStatusCount,
              // orderDetail의 새로고침을 위해 null로 초기화.
              orderDetail: null
            });
          } else {
            // 현재 보고있는 주문내역이 전체가 아닐 경우, 다른 상태로 이동한 Row를 지워준다.
            const newOrders = orders.filter(o => {
              if (checkedOrderIds.indexOf(o.orderId) !== -1) {
                // 체크된 Row에 해당하는 StatusCount를 변경한다.
                newStatusCount[statusToEng[selectedSelectStatus]] += 1;
                newStatusCount[statusToEng[o.status]] -= 1;
                return false;
              } else {
                handleUncheckRow(o.orderId);
                return true;
              }
            });
            handleUncheckRowAll();
            this.setState({
              orders: newOrders,
              statusCount: newStatusCount,
              // orderDetail의 새로고침을 위해 null로 초기화.
              orderDetail: null
            });
          }
          alert("주문상태가 변경되었습니다.");
        })
        .catch((err: AxiosError) => alert(err.response));
    } else {
      alert("문제가 발생했습니다.");
    }
  };

  render() {
    const {
      isModalOpen,
      selectedSelectStatus,
      selectedTodayStatus,
      statusCount,
      orders,
      orderDetail,
      isCheckedAll,
      checkedOrderIds
    } = this.state;
    const {
      status,
      statusEng,
      handleModalOpen,
      handleModalClose,
      handleChangeSelectStatus,
      handleClickTodayStatus,
      initializeTodayStatus,
      handleCheckRowAll,
      handleUncheckRowAll,
      handleCheckRow,
      handleUncheckRow,
      handleChangeStatusSubmit
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
                  <button className="btn btn-primary" onClick={() => handleChangeStatusSubmit()}>
                    적용
                  </button>
                </div>
              </div>

              {/* <!-- 주문내역 목록 --> */}
              <table className="content-table table table-striped table-sm mb-0">
                <thead>
                  <tr className="text-center">
                    <th>
                      <input
                        type="checkbox"
                        onChange={() =>
                          isCheckedAll ? handleUncheckRowAll() : handleCheckRowAll()
                        }
                        checked={isCheckedAll}
                      />
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
                          handleCheckRow={handleCheckRow}
                          handleUncheckRow={handleUncheckRow}
                          isChecked={checkedOrderIds.indexOf(o.orderId) !== -1}
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
