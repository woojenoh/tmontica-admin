import * as React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import _ from "underscore";
import Header from "../../components/Header";
import OrderRow from "../../components/OrderRow";
import OrderModal from "../../components/OrderModal";
import TodayOrderStatus from "../../components/TodayOrderStatus";
import Pagination from "../../components/Pagination";
import * as orderTypes from "../../types/order";
import * as commonTypes from "../../types/common";
import { API_URL, handleError } from "../../api/common";
import { withJWT } from "../../utils";
import "./styles.scss";
import { getTodayOrder } from "../../api/order";
import { CommonError } from "../../api/CommonError";

export interface ITodayOrderProps {
  signout(): void;
}

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
  currentPage: number;
  pageSize: number;
  pagination: commonTypes.IPagination | null;
}

class TodayOrder extends React.PureComponent<ITodayOrderProps, ITodayOrderState> {
  async getTodayOrder(
    page: number,
    size: number,
    status: orderTypes.TOrderStatusKor | null = null,
    afterSetStates: Pick<ITodayOrderState, "selectedTodayStatus" | "currentPage"> = {} as Pick<
      ITodayOrderState,
      "selectedTodayStatus" | "currentPage"
    >
  ) {
    try {
      const result = await getTodayOrder(page, size, status);

      if (result instanceof CommonError) throw result;

      this.setState({
        orders: result.orders,
        statusCount: result.statusCount,
        pagination: result.pagination,
        ...afterSetStates
      });
    } catch (error) {
      const result = await handleError(error);
      if (result === "signout") {
        this.props.signout();
      }

      if (this.state.intervalId) {
        clearInterval(this.state.intervalId);
      }
    }
  }

  componentDidMount() {
    // 처음에 전체내역을 한번 가져온다.
    this.getTodayOrder(1, this.state.pageSize);

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
      this.getTodayOrder(
        this.state.currentPage,
        this.state.pageSize,
        this.state.selectedTodayStatus
      );
    }, time);
    this.setState({
      intervalId: intervalId
    });
  };

  status = [
    "미결제",
    "결제완료",
    "준비중",
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
    준비중: "inProduction",
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
    intervalId: null,
    currentPage: 1,
    pageSize: 10,
    pagination: null
  } as ITodayOrderState;

  handleModalOpen = (orderId: number) => {
    const { selectedOrderId, orderDetail } = this.state;
    if (orderId !== selectedOrderId || orderDetail === null) {
      axios
        .get(`API_URL/orders/detail/${orderId}`, withJWT())
        .then((res: AxiosResponse) => {
          this.setState({
            orderDetail: res.data,
            selectedOrderId: orderId,
            isModalOpen: true
          });
        })
        .catch((err: AxiosError) => {
          alert(err.response && err.response.data.message);
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
    this.getTodayOrder(this.state.currentPage, this.state.pageSize, statusName, {
      selectedTodayStatus: statusName,
      currentPage: 1
    });
  };

  initializeTodayStatus = () => {
    this.getTodayOrder(this.state.currentPage, this.state.pageSize, null, {
      selectedTodayStatus: null,
      currentPage: 1
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
        .put(
          `${API_URL}/orders/status`,
          {
            orderIds: checkedOrderIds,
            status: selectedSelectStatus
          },
          withJWT()
        )
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
        .catch((err: AxiosError) => alert(err.response && err.response.data.message));
    } else {
      alert("문제가 발생했습니다.");
    }
  };

  handleSelectPage = (pageNumber: number) => {
    this.setState(
      {
        currentPage: pageNumber
      },
      () => {
        this.setState({
          checkedOrderIds: [],
          isCheckedAll: false
        });
        this.getTodayOrder(
          this.state.currentPage,
          this.state.pageSize,
          this.state.selectedTodayStatus
        );
      }
    );
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
      checkedOrderIds,
      pagination
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
      handleChangeStatusSubmit,
      handleSelectPage
    } = this;

    return (
      <>
        <Header title="오늘의 주문" />
        <div className="main-wrapper">
          <main className="main">
            {/* <!-- 오늘의 현황 --> */}
            <section className="today-order">
              <div className="mb-3 border-bottom">
                <h1 className="mb-3">주문 현황</h1>
              </div>
              <div className="today-order-circles">
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
            <section className="today-order-list pt-3 mt-3">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h1>주문 내역({selectedTodayStatus ? selectedTodayStatus : "전체"})</h1>
                <div className="order-select d-flex">
                  <select
                    className="custom-select mr-2"
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
                  <button
                    className="btn btn-primary"
                    style={{ width: "100px" }}
                    onClick={() => handleChangeStatusSubmit()}
                  >
                    적용
                  </button>
                </div>
              </div>

              {/* <!-- 주문내역 목록 --> */}
              <table className="content-table table table-striped mb-4">
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
                        <OrderRow
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

              <Pagination pagination={pagination} handleSelectPage={handleSelectPage} />
            </section>

            {/* <!-- 주문 상세 모달 --> */}
            <OrderModal
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
