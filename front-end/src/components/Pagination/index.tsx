import * as React from "react";
import * as commonTypes from "../../types/common";

export interface IPaginationProps {
  pagination: commonTypes.IPagination | null;
  handleSelectPage(pageNumber: number): void;
}

function Pagination(props: IPaginationProps) {
  const { pagination, handleSelectPage } = props;

  // 페이지 버튼 만드는 함수.
  const buildPageButtons = () => {
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
            className={`page-item cursor-pointer ${i === pagination.page && "active"}`}
            onClick={() => handleSelectPage(i)}
          >
            <a className="page-link">{i}</a>
          </li>
        );
      }
      return arr;
    } else {
      return null;
    }
  };

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
            <a className="page-link cursor-pointer">이전</a>
          </li>
          {buildPageButtons()}
          <li
            className={`page-item ${!pagination.next && "disabled"}`}
            onClick={() =>
              pagination.next && handleSelectPage(pagination.range * pagination.rangeSize + 1)
            }
          >
            <a className="page-link cursor-pointer">다음</a>
          </li>
        </ul>
      ) : (
        ""
      )}
    </>
  );
}

export default Pagination;
