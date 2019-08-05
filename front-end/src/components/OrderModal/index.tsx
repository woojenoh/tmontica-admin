import * as React from "react";
import { Modal } from "react-bootstrap";
import moment from "moment";
import { numberCommaRegex } from "../../utils";
import * as orderTypes from "../../types/order";

export interface IOrderModalProps {
  isModalOpen: boolean;
  handleModalClose(): void;
  orderDetail: orderTypes.IOrderDetail | null;
}

function OrderModal(props: IOrderModalProps) {
  const { isModalOpen, handleModalClose, orderDetail } = props;

  return (
    <Modal show={isModalOpen} onHide={handleModalClose}>
      {orderDetail ? (
        <>
          <Modal.Header>
            <h3>주문내역({orderDetail.orderId})</h3>
          </Modal.Header>
          <Modal.Body>
            <div id="order-detail" className="input-group-wrap">
              <div className="input-group orderer mb-2">
                <div className="input-group-prepend">
                  <span className="input-group-text">주문자</span>
                </div>
                <span className="form-control" id="basic-url">
                  {orderDetail.userId}
                </span>
              </div>
              <table className="table table-sm bg-white table-bordered mb-0">
                <thead className="thead-dark">
                  <tr className="text-center">
                    <th>메뉴명</th>
                    <th>옵션</th>
                    <th>결제금액</th>
                  </tr>
                </thead>
                <tbody>
                  {orderDetail.menus.map((m, index) => {
                    return (
                      <tr key={index} className="text-center">
                        <td>{m.nameKo}</td>
                        <td>{m.option}</td>
                        <td>{numberCommaRegex(m.price)}원</td>
                      </tr>
                    );
                  })}
                </tbody>
                <tfoot>
                  <tr>
                    <td colSpan={3} className="text-right">
                      총 결제금액:{" "}
                      <span className="total-price">
                        {numberCommaRegex(orderDetail.totalPrice)}
                      </span>
                      원
                    </td>
                  </tr>
                </tfoot>
              </table>
            </div>
            <h4 className="mt-3 mb-3">주문기록</h4>
            <table className="table table-sm bg-white table-bordered mb-0 border-bottom-0">
              <thead className="thead-dark">
                <tr className="text-center">
                  <th>번호</th>
                  <th>주문상태</th>
                  <th>변경인</th>
                  <th>변경일</th>
                </tr>
              </thead>
              <tbody>
                {orderDetail.orderStatusLogs.map((l, index) => {
                  return (
                    <tr key={index} className="text-center">
                      <td>{index}</td>
                      <td>{l.status}</td>
                      <td>{l.editorId === orderDetail.userId ? "주문자" : l.editorId}</td>
                      <td>{moment(l.modifiedDate).format("YYYY년 MM월 DD일 HH시 mm분")}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </Modal.Body>
        </>
      ) : (
        "로딩 중입니다."
      )}
      <Modal.Footer>
        <button
          className="close__button btn btn-primary pl-4 pr-4"
          onClick={() => handleModalClose()}
        >
          닫기
        </button>
      </Modal.Footer>
    </Modal>
  );
}

export default OrderModal;
