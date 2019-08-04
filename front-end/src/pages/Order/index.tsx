import * as React from "react";
import Header from "../../components/Header";
import Nav from "../../components/Nav";

export interface IOrderProps {}

export interface IOrderState {}

class Order extends React.Component<IOrderProps, IOrderState> {
  render() {
    return (
      <>
        <Header title="주문 내역" />
        <div className="main-wrapper">
          <Nav />
          <main className="main col-md-10">
            <section className="order p-4">
              <div className="order-title mb-4 d-flex justify-content-between">
                <h1>주문 내역 보기</h1>
                <form className="order-top text-right">
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
                <table className="table table-striped">
                  <thead>
                    <tr className="text-center">
                      <th>
                        <input type="checkbox" />
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
                    <tr className="text-center">
                      <td>
                        <input type="checkbox" />
                      </td>
                      <td>1</td>
                      <td>qwe123</td>
                      <td>아메리카노</td>
                      <td>현금결제</td>
                      <td>1,000</td>
                      <td>2019년 8월 4일</td>
                      <td>미결제</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </section>
          </main>
        </div>
      </>
    );
  }
}

export default Order;
