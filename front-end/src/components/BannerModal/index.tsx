import React, { ChangeEvent, PureComponent, BaseSyntheticEvent, FormEvent } from "react";
import { Modal } from "react-bootstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./styles.scss";
import { Indexable } from "../../types/index";
import moment from "moment";
import { IBanner } from "../../types/banner";
import { setImagePreview } from "../../utils";
import { BASE_URL } from "../../constants";
import { CommonError } from "../../api/CommonError";
import { addBanner, updateBanner, getBannerById, deleteBanner } from "../../api/banner";
import { handleError } from "../../api/common";

interface IBannerModalProps {
  show: boolean;
  bannerId: number;
  isReg: boolean;
  closeModal(): void;
  getBanners(): void;
  signout(): void;
}

interface IBannerModalState extends IBanner, Indexable {
  imgFile?: Blob | string;
  imgUrl?: string | null;
}

const dateFormat = "YYYY.MM.DD HH:mm:ss";

const initState = {
  id: -1,
  link: "",
  usePage: "main_top",
  startDate: moment().format(dateFormat),
  endDate: moment().format(dateFormat),
  number: 1,
  imgFile: "",
  imgUrl: "",
  usable: false
} as IBanner;

export default class BannerModal extends PureComponent<IBannerModalProps, IBannerModalState>
  implements Indexable {
  [key: string]: any;
  form?: HTMLFormElement;

  state = {
    ...(initState as IBanner)
  };

  setRef = (name: string) => (el: any) => {
    this[name] = el;
  };

  clickFileInput = () => {
    if (this.fileInput) {
      this.fileInput.click();
    }
  };

  getRegName() {
    return this.props.isReg ? "등록" : "수정";
  }

  isValidForm(): boolean {
    const { link, number, imgFile } = this.state;
    const { isReg } = this.props;

    if (!link) {
      alert("링크를 입력해주세요.");
      this.link.focus();
      return false;
    }

    if (number < 1) {
      alert("순서는 0보다 커야 합니다.");
      this.number.focus();
      return false;
    }

    if (!imgFile && isReg) {
      alert("이미지를 등록해주세요.");
      return false;
    }

    return true;
  }

  // 타겟 value를 state에 넣을 때 사용하는 onChange 핸들러
  handleChangeValue = (name: string) => (e: BaseSyntheticEvent) => {
    this.setState({
      [name]: e.currentTarget.value
    });
  };

  // 체크하면 Ture/False 일 때 사용하는 onChange 핸들러
  handleChangeRadio = (name: string, isTrue: boolean) => (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      this.setState({
        [name]: isTrue
      });
    }
  };

  // 배너 등록/수정
  handleSubmit(e: FormEvent<HTMLInputElement>) {
    e.preventDefault();

    // 유효성검사
    if (!this.isValidForm()) {
      return;
    }
    const data = this.getFormData();

    const isConfirm = window.confirm(`${this.getRegName()}하시겠습니까?`);
    if (!isConfirm) return;

    if (this.props.isReg) {
      this.addBanner(data);
    } else {
      this.updateBanner(data);
    }
  }

  async addBanner(data: FormData) {
    try {
      const res = await addBanner(data);
      if (res instanceof CommonError) throw res;

      alert("배너가 등록되었습니다.");
      this.setState({ ...initState });
      this.props.getBanners();
    } catch (error) {
      const result = await handleError(error);
      if (result === "signout") {
        this.props.signout();
      }
    }
  }

  async updateBanner(data: FormData) {
    try {
      const res = await updateBanner(data);
      if (res instanceof CommonError) throw res;

      alert("배너가 수정되었습니다.");
      // this.setState({ ...initState });
      this.props.getBanners();
    } catch (error) {
      const result = await handleError(error);
      if (result === "signout") {
        this.props.signout();
      }
    }
  }

  getFormData() {
    const data = new FormData();

    if (!this.props.isReg) {
      data.append("id", `${this.props.bannerId}`);
    }
    data.append("link", this.state.link);
    data.append("usePage", this.state.usePage);
    data.append("startDate", moment(this.state.startDate).format(dateFormat));
    data.append("endDate", moment(this.state.endDate).format(dateFormat));
    data.append("number", this.state.number.toString());
    if (typeof this.state.imgFile !== "undefined" && this.state.imgFile) {
      data.append("imgFile", this.state.imgFile);
    }
    data.append("usable", `${this.state.usable}`);

    return data;
  }

  componentDidUpdate(prevProps: IBannerModalProps) {
    if (prevProps.bannerId !== this.props.bannerId) {
      this.getBannerById();
    }
  }

  async getBannerById() {
    if (this.props.isReg) {
      this.setState({
        ...initState
      });
      return;
    }
    try {
      const data = await getBannerById(this.props.bannerId);
      if (data instanceof CommonError) throw data;

      this.setState(
        Object.assign({}, data, {
          imgFile: ""
        } as IBannerModalState)
      );
    } catch (error) {
      if (error instanceof CommonError) {
        error.alertMessage();
      } else {
        console.dir(error);
      }
    }
  }

  render() {
    const { show, isReg, closeModal } = this.props;

    const { link, startDate, endDate, imgUrl, usable, number } = this.state;

    return (
      <Modal id="bannerModal" show={show} onHide={this.props.closeModal}>
        <form
          name="bannerForm"
          ref={el => (this.form = el ? el : undefined)}
          onSubmit={e => {
            e.preventDefault();
          }}
        >
          <Modal.Body>
            <div className="banner-image-wrap">
              <div className="banner-image">
                <img
                  src={
                    imgUrl
                      ? typeof imgUrl === "string" && /^data/.test(imgUrl)
                        ? imgUrl
                        : `${BASE_URL}/${imgUrl}`
                      : "https://dummyimage.com/600x400/ffffff/ff7300.png&text=tmontica"
                  }
                  alt="배너 이미지"
                />
              </div>
              <button
                className="mb-3 reg-image__button btn btn-warning"
                onClick={this.clickFileInput}
              >
                이미지 등록
              </button>

              <input
                type="file"
                ref={this.setRef("fileInput")}
                className="hide"
                hidden
                onChange={e => {
                  e.preventDefault();

                  // https://hyunseob.github.io/2018/06/24/debounce-react-synthetic-event/
                  e.persist();
                  setImagePreview(e.target.files, (imgUrl: any) => {
                    if (e.target.files![0].size >= 1024 * 1024 * 5) {
                      alert("이미지 크기는 5MB를 넘을 수 없습니다.");
                      return false;
                    }

                    this.setState({
                      imgUrl,
                      imgFile: e.target.files !== null ? (e.target.files[0] as Blob) : ""
                    });
                  });
                }}
              />
            </div>

            <div className="mb-1 input-group category">
              <div className="input-group-prepend">
                <span className="input-group-text">위치</span>
              </div>
              <div className="input-group-append">
                <select
                  name="usePage"
                  ref={this.setRef("usePage")}
                  value={this.state.usePage}
                  onChange={this.handleChangeValue("usePage")}
                >
                  <option value="main_top">메인-상단</option>
                  <option value="main_bottom">메인-하단</option>
                </select>
              </div>
            </div>

            <div className="mb-1 input-group period">
              <div className="input-group-prepend">
                <span className="input-group-text">기간</span>
              </div>
              <div className="form-control">
                <DatePicker
                  selected={startDate ? new Date(startDate) : new Date()}
                  name="startDate"
                  onChange={date =>
                    this.setState({
                      startDate: moment(date ? date : undefined).format(dateFormat)
                    })
                  }
                  showTimeSelect
                  timeFormat="HH:mm"
                  timeIntervals={30}
                  dateFormat="yyyy.MM.dd HH:mm:ss"
                  timeCaption="time"
                />
                <DatePicker
                  selected={endDate ? new Date(endDate) : new Date()}
                  name="endDate"
                  onChange={date =>
                    this.setState({
                      endDate: moment(date ? date : undefined).format(dateFormat)
                    })
                  }
                  showTimeSelect
                  timeFormat="HH:mm"
                  timeIntervals={15}
                  dateFormat="yyyy.MM.dd HH:mm:ss"
                  timeCaption="time"
                />
              </div>
            </div>

            <div className="mb-1 input-group name">
              <div className="input-group-prepend">
                <span className="input-group-text">링크</span>
              </div>
              <input
                value={link}
                name="link"
                ref={this.setRef("link")}
                type="text"
                className="form-control"
                placeholder="예) http://tmontica.co.kr/"
                onChange={this.handleChangeValue("link")}
              />
            </div>

            <div className="tmon-row">
              <div className="input-group number half">
                <div className="input-group-prepend">
                  <span className="input-group-text">순서</span>
                </div>
                <input
                  value={number}
                  name="number"
                  ref={this.setRef("number")}
                  type="number"
                  min="1"
                  className="form-control"
                  onChange={this.handleChangeValue("number")}
                />
              </div>

              <div className="input-group usable half">
                <div className="input-group-prepend">
                  <span className="input-group-text">사용여부</span>
                </div>
                <div className="form-control">
                  <div className="input-group mr-3">
                    <input
                      type="radio"
                      name="usable"
                      className="mr-2"
                      checked={usable ? true : false}
                      onChange={this.handleChangeRadio("usable", true)}
                    />
                    <label className="choice yes">예</label>
                  </div>
                  <div className="input-group">
                    <input
                      type="radio"
                      name="usable"
                      className="mr-2"
                      checked={!usable ? true : false}
                      onChange={this.handleChangeRadio("usable", false)}
                    />
                    <label className="choice no">아니오</label>
                  </div>
                </div>
              </div>
            </div>
          </Modal.Body>
          <Modal.Footer>
            <input
              type="submit"
              className="reg-menu__button btn btn-outline-primary"
              value={this.getRegName()}
              onClick={this.handleSubmit.bind(this)}
            />
            {!isReg ? (
              <input
                type="submit"
                className="reg-menu__button btn btn-outline-primary"
                value="삭제"
                onClick={async e => {
                  e.preventDefault();

                  try {
                    await deleteBanner(this.props.bannerId);
                    alert("배너가 삭제되었습니다.");
                    this.props.getBanners();
                    closeModal();
                  } catch (error) {}
                }}
              />
            ) : (
              ""
            )}

            <button className="cancle-menu__button btn btn-danger" onClick={closeModal}>
              취소
            </button>
          </Modal.Footer>
        </form>
      </Modal>
    );
  }
}
