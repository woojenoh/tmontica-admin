import * as React from "react";
import _ from "underscore";
import * as commonTypes from "../../types/common";

export interface IPaginationProps {
  pagination: commonTypes.IPagination | null;
  handleSelectPage(pageNumber: number): void;
}

export interface IPaginationState {}

export default class Pagination extends React.Component<IPaginationProps, IPaginationState> {
  shouldComponentUpdate(nextProps: IPaginationProps) {
    if (_.isEqual(this.props.pagination, nextProps.pagination)) {
      return false;
    } else {
      return true;
    }
  }

  // 페이지 버튼 만드는 함수.
  buildPageButtons = () => {
    const { pagination, handleSelectPage } = this.props;

    if (pagination) {
      var arr = [];
      for (
        // 현재 range부터 현재 range의 끝까지.
        let i = (pagination.range - 1) * 10 + 1;
        i <= (pagination.next ? pagination.range * pagination.rangeSize : pagination.pageCnt);
        i++
      ) {
        arr.push(
          <li
            key={i}
            className={`page-item cursor-pointer ${i === pagination.page && "active"}`}
            // 현재 페이지와 같은 페이지가 아닐 때만
            onClick={() => (pagination.page !== i ? handleSelectPage(i) : {})}
          >
            <span className="page-link">{i}</span>
          </li>
        );
      }
      return arr;
    } else {
      return null;
    }
  };

  render() {
    const { pagination, handleSelectPage } = this.props;
    const { buildPageButtons } = this;

    return (
      <>
        {pagination ? (
          <ul className="pagination d-flex justify-content-center mb-0">
            <li
              className={`page-item ${!pagination.prev && "disabled"}`}
              onClick={() =>
                pagination.prev && handleSelectPage((pagination.range - 1) * pagination.rangeSize)
              }
            >
              <span className="page-link cursor-pointer">이전</span>
            </li>
            {buildPageButtons()}
            <li
              className={`page-item ${!pagination.next && "disabled"}`}
              onClick={() =>
                pagination.next && handleSelectPage(pagination.range * pagination.rangeSize + 1)
              }
            >
              <span className="page-link cursor-pointer">다음</span>
            </li>
          </ul>
        ) : (
          ""
        )}
      </>
    );
  }
}
