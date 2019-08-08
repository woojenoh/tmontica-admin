import React, { PureComponent, SyntheticEvent } from "react";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import { Table } from "react-bootstrap";
import Pagination from "../../components/Pagination";
import { BannerModal } from "../../components/BannerModal";
import { getBannerAll } from "../../api/banner";
import { CommonError } from "../../api/CommonError";

interface Props {}
interface State {}

export default class Banner extends PureComponent<Props, State> {
  state = {
    page: 1,
    show: false,
    bannerId: -1,
    banners: [],
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
          <Nav />
          <main id="menus" className="col-md-10">
            <section>
              <div className="content-head d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
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
                <button className="btn btn-outline-primary">이달의 메뉴 보기</button>
              </div>
              <Table striped size="sm" className="content-table">
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
                <tbody>
                  {banners.length > 0 ? (
                    <></>
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
