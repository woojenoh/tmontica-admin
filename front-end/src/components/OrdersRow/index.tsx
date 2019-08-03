import * as React from "react";

export interface IOrdersRowProps {
  handleModalOpen(): void;
}

function OrdersRow(props: IOrdersRowProps) {
  const { handleModalOpen } = props;

  return (
    <tr data-toggle="modal" onClick={() => handleModalOpen()}>
      <td className="menu__td check">
        <input type="checkbox" aria-label="Checkbox for following text input" />
      </td>
      <td className="menu__td order-num">1</td>
      <td className="menu__td orderer">2</td>
      <td className="menu__td order-status">3</td>
      <td className="menu__td payment">4</td>
      <td className="menu__td order-price">5</td>
      <td className="menu__td order-date">6</td>
    </tr>
  );
}

export default OrdersRow;
