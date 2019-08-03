import * as React from "react";
import { Modal } from "react-bootstrap";

export interface IOrdersModalProps {
  isModalOpen: boolean;
  handleModalClose(): void;
}

function OrdersModal(props: IOrdersModalProps) {
  const { isModalOpen, handleModalClose } = props;

  return (
    <Modal show={isModalOpen} onHide={handleModalClose}>
      <Modal.Header>
        <h3>주문내역(00015)</h3>
      </Modal.Header>
      <Modal.Body>
        <div id="order-detail" className="input-group-wrap">
          {/* <!-- 주문인 --> */}
          <div className="input-group orderer mb-2">
            <div className="input-group-prepend">
              <span className="input-group-text">주문인</span>
            </div>
            <span className="form-control" id="basic-url" />
          </div>
          {/* <!-- 설명 --> */}
          <div className="input-group description mb-3">
            <div className="input-group-prepend">
              <span className="input-group-text">설명</span>
            </div>
            <span className="form-control" id="basic-url" />
          </div>
          <table className="table table-sm bg-white table-bordered mb-0">
            <thead className="thead-dark">
              <tr>
                <th>메뉴명</th>
                <th>옵션</th>
                <th>결제금액</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>아메리카노</td>
                <td>HOT/시럽추가(1개)</td>
                <td>1,300원</td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <td colSpan={3} className="text-right">
                  총 결제금액: <span className="total-price">1,300</span>원
                </td>
              </tr>
            </tfoot>
          </table>
        </div>
        <h4 className="mt-3 mb-3">주문기록</h4>
        <table className="table table-sm bg-white table-bordered mb-0 border-bottom-0">
          <thead className="thead-dark">
            <tr>
              <th>번호</th>
              <th>주문상태</th>
              <th>변경인</th>
              <th>변경일</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>결제완료</td>
              <td>티몽이</td>
              <td>2019.07.15 11:13:02</td>
            </tr>
          </tbody>
        </table>
      </Modal.Body>
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

export default OrdersModal;
