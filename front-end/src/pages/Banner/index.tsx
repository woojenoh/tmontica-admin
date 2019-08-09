import React, { PureComponent, SyntheticEvent } from "react";
import Header from "../../components/Header";
import { Table } from "react-bootstrap";
import { BannerModal } from "../../components/BannerModal";
import { getBannerAll } from "../../api/banner";
import { CommonError } from "../../api/CommonError";
import { IBanner } from "../../types/banner";
import { IPagination } from "../../types/common";
import Pagination from "../../components/Pagination";
import { BannerUsePageDict } from "../../constants";
import { IMAGE_URL } from "../../api/common";
import "./styles.scss";
import moment from "moment";

interface Props {}
interface State {
  page: number;
  show: boolean;
  bannerId: number;
  banners: IBanner[];
  isReg: boolean;
  checkedRows: number[];
  isCheckedAll: boolean;
  pagination: IPagination | null;
}

export default class Banner extends PureComponent<Props, State> {
  state = {
    page: 1,
    show: false,
    bannerId: -1,
    banners: [] as IBanner[],
    isReg: true,
    checkedRows: [] as number[],
    isCheckedAll: false,
    pagination: null
  };

  showModal = ({ bannerId, show, isReg }: { bannerId: number; isReg: boolean; show: boolean }) => (
    e: SyntheticEvent
  ) => {
    this.setState({
      show,
      isReg,
      bannerId
    });
  };

  closeModal = () => {
    this.setState({
      show: false
    });
  };

  // 배너 목록 요청
  getBannerAll = async () => {
    try {
      const banners = await getBannerAll();

      if (banners instanceof CommonError) throw banners;
      if (banners.length === 0) {
        return;
      }

      this.setState({
        banners
      });
    } catch (error) {
      if (error instanceof CommonError) {
        error.alertMessage();
      } else {
        console.log(error);
      }
    }
  };

  componentDidMount() {
    this.getBannerAll();
  }

  render() {
    const { banners } = this.state;

    return (
      <>
        <Header title="메뉴 관리" />
        <div className="main-wrapper">
          <main id="menus" className="main">
            <section>
              <div className="content-head d-flex flex-wrap flex-md-nowrap align-items-center mb-4">
                <button
                  className="btn btn-outline-primary mr-1"
                  onClick={this.showModal({
                    show: true,
                    bannerId: -1,
                    isReg: true
                  })}
                >
                  배너 등록
                </button>
              </div>
              <Table striped size="sm" id="banner-table" className="content-table">
                <thead>
                  <tr>
                    <th>
                      <input
                        type="checkbox"
                        aria-label="Checkbox for following text input"
                        onChange={e => {}}
                      />
                    </th>
                    <th>위치</th>
                    <th>이미지</th>
                    <th>기간</th>
                    <th>링크</th>
                    <th>순서</th>
                    <th>사용여부</th>
                  </tr>
                </thead>
                <tbody className="table-body">
                  {banners.length > 0 ? (
                    banners.map((banner, i) => (
                      <tr
                        key={i}
                        className="banner"
                        onClick={this.showModal({
                          show: true,
                          bannerId: banner.id,
                          isReg: false
                        })}
                      >
                        <td>
                          <input type="checkbox" />
                        </td>
                        <td>{BannerUsePageDict[banner.usePage]}</td>
                        <td className="img">
                          <img src={`${IMAGE_URL}/${banner.imgUrl}`} alt="배너 미리보기" />
                        </td>
                        <td>
                          {moment(banner.startDate).format("YYYY.MM.DD")}~
                          {moment(banner.endDate).format("YYYY.MM.DD")}
                        </td>
                        <td>{banner.link}</td>
                        <td>{banner.number}</td>
                        <td>{banner.usable ? "Y" : "N"}</td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={7}>배너를 등록해주세요.</td>
                    </tr>
                  )}
                </tbody>
              </Table>
              <Pagination pagination={null} handleSelectPage={() => null} />
            </section>
            <BannerModal
              show={this.state.show}
              closeModal={this.closeModal}
              bannerId={this.state.bannerId}
              isReg={this.state.isReg}
              // getMenus={this.getMenus.bind(this, this.state.page)}
            />
          </main>
        </div>
      </>
    );
  }
}
