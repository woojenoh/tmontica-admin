import * as React from "react";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import TodayOrderRow from "../../components/TodayOrderRow";
import TodayOrderModal from "../../components/TodayOrderModal";
import TodayOrderStatus from "../../components/TodayOrderStatus";
import * as orderTypes from "../../types/order";

export interface ITodayOrderProps {}

export interface ITodayOrderState {
  orders: orderTypes.IOrder[] | null;
  ordersStatus: orderTypes.IOrderStatusCount | null;
  isModalOpen: boolean;
  selectedTodayStatus: string | null;
  selectedStatus: string;
}

class TodayOrder extends React.Component<ITodayOrderProps, ITodayOrderState> {
  status = ["미결제", "결제완료", "제작중", "준비완료", "픽업완료", "주문취소"];
  statusToEnglish = {
    미결제: "beforePayment",
    결제완료: "afterPayment",
    제작중: "inProduction",
    준비완료: "ready",
    픽업완료: "pickUp",
    주문취소: "cancel"
  };

  state = {
    orders: null,
    ordersStatus: null,
    isModalOpen: false,
    selectedTodayStatus: null,
    selectedStatus: this.status[0]
  };

  handleModalOpen = () => {
    this.setState({
      isModalOpen: true
    });
  };

  handleModalClose = () => {
    this.setState({
      isModalOpen: false
    });
  };

  handleClickTodayStatus = (statusName: string) => {
    this.setState({
      selectedTodayStatus: statusName
    });
  };

  initializeTodayStatus = () => {
    this.setState({
      selectedTodayStatus: null
    });
  };

  handleChangeStatus = (e: React.FormEvent<HTMLSelectElement>) => {
    this.setState({
      selectedStatus: e.currentTarget.value
    });
  };

  public render() {
    const { isModalOpen, selectedStatus, selectedTodayStatus } = this.state;
    const {
      status,
      handleModalOpen,
      handleModalClose,
      handleChangeStatus,
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
                {status.map((s: string, index: number) => {
                  return (
                    <TodayOrderStatus
                      key={index}
                      statusName={s}
                      statusCount={index}
                      handleClickTodayStatus={handleClickTodayStatus}
                      initializeTodayStatus={initializeTodayStatus}
                      isActive={s === selectedTodayStatus}
                    />
                  );
                })}
              </div>
            </section>

            {/* <!-- 주문내역 --> */}
            <section className="order-list__section">
              <div className="content-head d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 mt-4 mb-3">
                <h4>주문내역 - {selectedTodayStatus ? selectedTodayStatus : "전체"}</h4>
                <div className="order-select d-flex">
                  <select
                    className="mr-2"
                    value={selectedStatus}
                    onChange={e => handleChangeStatus(e)}
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
                  <tr>
                    <th>
                      <input type="checkbox" aria-label="Checkbox for following text input" />
                    </th>
                    <th>주문번호</th>
                    <th>주문자</th>
                    <th>주문상태</th>
                    <th>주문메뉴</th>
                    <th>결제방법</th>
                    <th>결제금액</th>
                    <th>주문일시</th>
                  </tr>
                </thead>
                <tbody>
                  <TodayOrderRow handleModalOpen={handleModalOpen} />
                </tbody>
              </table>
            </section>

            {/* <!-- 주문 상세 모달 --> */}
            <TodayOrderModal isModalOpen={isModalOpen} handleModalClose={handleModalClose} />
          </main>
        </div>
      </>
    );
  }
}

export default TodayOrder;
