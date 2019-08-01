import * as React from "react";
import { DropdownButton, Dropdown } from "react-bootstrap";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import OrdersRow from "../../components/OrdersRow";
import OrdersModal from "../../components/OrdersModal";

export interface IOrdersProps {}

export interface IOrdersState {
  isModalOpen: boolean;
  selectedStatus: string | null;
}

export default class Orders extends React.Component<IOrdersProps, IOrdersState> {
  status = ["미결제", "결제완료", "제작중", "준비완료", "픽업완료", "주문취소"];

  state = {
    isModalOpen: false,
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

  handleChangeStatus = (e: React.FormEvent<HTMLSelectElement>) => {
    this.setState({
      selectedStatus: e.currentTarget.value
    });
  };

  public render() {
    const { isModalOpen, selectedStatus } = this.state;
    const { handleModalOpen, handleModalClose, handleChangeStatus } = this;

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
                <div className="order-circle btn-warning">
                  <div className="order-circle-name">미결제</div>
                  <div>
                    <span className="order-cnt">3</span>건
                  </div>
                </div>
                <div className="order-circle btn-info">
                  <div className="order-circle-name">결제완료</div>
                  <div>
                    <span className="order-cnt">10</span>건
                  </div>
                </div>
                <div className="order-circle btn-warning">
                  <div className="order-circle-name">제작중</div>
                  <div>
                    <span className="order-cnt">3</span>건
                  </div>
                </div>
                <div className="order-circle btn-info">
                  <div className="order-circle-name">준비완료</div>
                  <div>
                    <span className="order-cnt">3</span>건
                  </div>
                </div>
                <div className="order-circle btn-warning">
                  <div className="order-circle-name">픽업완료</div>
                  <div>
                    <span className="order-cnt">3</span>건
                  </div>
                </div>
                <div className="order-circle btn-danger">
                  <div className="order-circle-name">주문취소</div>
                  <div>
                    <span className="order-cnt">2</span>건
                  </div>
                </div>
              </div>
            </section>

            {/* <!-- 주문내역 --> */}
            <section className="order-list__section">
              <div className="content-head d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 mt-4 mb-3">
                <h4>주문내역</h4>
                <div className="order-select d-flex">
                  <select
                    className="mr-2"
                    value={selectedStatus}
                    onChange={e => handleChangeStatus(e)}
                  >
                    {this.status.map((s, index) => {
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
              <table className="content-table table table-striped table-sm mb-0">
                <thead>
                  <tr>
                    <th>
                      <input type="checkbox" aria-label="Checkbox for following text input" />
                    </th>
                    <th>주문번호</th>
                    <th>주문자</th>
                    <th>주문상태</th>
                    <th>결제방법</th>
                    <th>결제금액</th>
                    <th>주문일시</th>
                  </tr>
                </thead>
                <tbody>
                  <OrdersRow handleModalOpen={handleModalOpen} />
                </tbody>
              </table>
            </section>

            {/* <!-- 주문 상세 모달 --> */}
            <OrdersModal isModalOpen={isModalOpen} handleModalClose={handleModalClose} />
          </main>
        </div>
      </>
    );
  }
}
