import * as React from "react";

export interface ITodayOrderRowProps {
  handleModalOpen(): void;
}

function TodayOrderRow(props: ITodayOrderRowProps) {
  const { handleModalOpen } = props;

  return (
    <tr onClick={() => handleModalOpen()}>
      <td className="order__td check">
        <input type="checkbox" aria-label="Checkbox for following text input" />
      </td>
      <td className="order__td order-id">1</td>
      <td className="order__td order-user">2</td>
      <td className="order__td order-status">3</td>
      <td className="order__td order-menus">4</td>
      <td className="order__td order-payment">5</td>
      <td className="order__td order-price">6</td>
      <td className="order__td order-date">7</td>
    </tr>
  );
}

export default TodayOrderRow;
