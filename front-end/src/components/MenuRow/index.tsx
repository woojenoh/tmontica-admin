import React, { Component, SyntheticEvent } from "react";
import _ from "underscore";
import { IMenu } from "../../types/menu";
import { IMAGE_URL } from "../../constants";
import "./styles.scss";
import { formatDate } from "../../utils";

interface Props {
  menu: IMenu;
  isChecked: boolean;
  handleShowUpdateModal(menuId: number): void;
  handleCheckRow(menuId: number): void;
  handleUncheckRow(menuId: number): void;
}
interface State {}

export default class MenuRow extends Component<Props, State> {
  shouldComponentUpdate(nextProps: Props) {
    if (
      _.isEqual(this.props.menu, nextProps.menu) &&
      this.props.isChecked === nextProps.isChecked
    ) {
      return false;
    } else {
      return true;
    }
  }

  render() {
    const { menu, handleShowUpdateModal } = this.props;

    return (
      <tr
        onClick={(e: SyntheticEvent) => {
          const target = e.target as HTMLElement;
          if (target.closest(".check")) return;
          handleShowUpdateModal(menu.id);
        }}
      >
        {
          // 전체삭제 기능 미지원으로 숨김
          /* <td className="menu__td check">
            <input
              type="checkbox"
              aria-label="Checkbox for following text input"
              checked={isChecked}
              onChange={e => {
                if (e.target.checked) {
                  this.props.handleCheckRow(this.props.menu.id);
                } else {
                  this.props.handleUncheckRow(this.props.menu.id);
                }
              }}
            />
          </td>*/
        }
        <td className="menu__td preview">
          <img src={`${IMAGE_URL}/${menu.imgUrl}`} alt={menu.nameEng} />
        </td>
        <td className="menu__td category">{menu.categoryKo}</td>
        <td className="menu__td name">{menu.nameKo}</td>
        <td className="menu__td monthly">{menu.monthlyMenu ? "○" : "Ⅹ"}</td>
        <td className="menu__td product-price">{menu.productPrice.toLocaleString()}</td>
        <td className="menu__td discount-rate">{menu.discountRate}</td>
        <td className="menu__td sales-price">{menu.sellPrice.toLocaleString()}</td>
        <td className="menu__td quantity">{menu.stock}</td>
        <td className="menu__td usable">{menu.usable ? "○" : "Ⅹ"}</td>
        <td className="menu__td create-date">
          {menu.createdDate ? formatDate(menu.createdDate).substr(0, 10) : ""}
        </td>
        <td className="menu__td creator">{menu.creatorId}</td>
        <td className="menu__td edit-date">
          {menu.updatedDate ? formatDate(menu.updatedDate).substr(0, 10) : ""}
        </td>
        <td className="menu__td editor">{menu.updaterId}</td>
      </tr>
    );
  }
}
