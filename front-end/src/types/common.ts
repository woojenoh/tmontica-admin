export interface IPagination {
  page: number;
  range: number;
  totalCnt: number;
  pageCnt: number;
  size: number;
  rangeSize: number;
  startPage: number;
  startList: number;
  endPage: number;
  prev: boolean;
  next: boolean;
}
