import * as React from "react";
import axios from "axios";
import { Bar, Doughnut } from "react-chartjs-2";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import * as menuTypes from "../../types/menu";
import * as statTypes from "../../types/statistics";
import { API_URL } from "../../api/common";
import { withJWT } from "../../utils";
import "./styles.scss";

const colors = [
  "#ff6b6b",
  "#cc5de8",
  "#5c7cfa",
  "#22b8cf",
  "#51cf66",
  "#fcc419",
  "#adb5bd",
  "#f06595",
  "#845ef7",
  "#339af0"
];
const hoverColors = [
  "#fa5252",
  "#be4bdb",
  "#4c6ef5",
  "#15aabf",
  "#40c057",
  "#fab005",
  "#868e96",
  "#e64980",
  "#7950f2",
  "#228be6"
];

const data2 = {
  labels: ["10대", "20대", "30대"],
  datasets: [
    {
      data: [200, 100, 50],
      backgroundColor: [colors[0], colors[1], colors[2]],
      hoverBackgroundColor: [hoverColors[0], hoverColors[1], hoverColors[2]]
    }
  ]
};

const data3 = {
  labels: ["MOBILE", "PC", "TABLET"],
  datasets: [
    {
      data: [200, 100, 50],
      backgroundColor: [colors[0], colors[1], colors[2]],
      hoverBackgroundColor: [hoverColors[0], hoverColors[1], hoverColors[2]]
    }
  ]
};

export interface IStatisticsProps {}

export interface IStatisticsState {
  menus: menuTypes.IMenu[] | null;
  chartMenus: statTypes.IStatMenu[] | null;
  menuId: string;
  menuStartDate: string;
  menuEndDate: string;
  // 날짜 적용 당시의 날짜를 기억하기 위한 객체
  capturedMenuDate: {
    menuStartDate: string;
    menuEndDate: string;
  };
}

export interface IInputState {
  menuId: string;
  menuStartDate: string;
  menuEndDate: string;
}

class Statistics extends React.Component<IStatisticsProps, IStatisticsState> {
  state = {
    menus: null,
    chartMenus: null,
    menuId: "",
    menuStartDate: "",
    menuEndDate: "",
    capturedMenuDate: {
      menuStartDate: "",
      menuEndDate: ""
    }
  } as IStatisticsState;

  componentDidMount() {
    axios.get(`${API_URL}/menus`, withJWT({ params: { size: 100 } })).then(res => {
      this.setState({
        menus: res.data.menus
      });
    });
  }

  handleInputChange = (e: React.FormEvent<HTMLInputElement>) => {
    this.setState({
      [e.currentTarget.name]: e.currentTarget.value
    } as { [K in keyof IInputState]: IInputState[K] });
  };

  handleSelectChange = (e: React.FormEvent<HTMLSelectElement>) => {
    this.setState({
      [e.currentTarget.name]: e.currentTarget.value
    } as { [K in keyof IInputState]: IInputState[K] });
  };

  // 메뉴 차트에 추가
  handleMenuSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { menuId, chartMenus, capturedMenuDate } = this.state;
    e.preventDefault();
    if (menuId) {
      axios
        .post(
          `${API_URL}/statistic/menu`,
          {
            menuIds: [Number(menuId)],
            startDate: capturedMenuDate.menuStartDate,
            endDate: capturedMenuDate.menuEndDate
          },
          withJWT()
        )
        .then(res => {
          // null 체크
          if (chartMenus) {
            // 메뉴가 10개 이하인지 체크
            if (chartMenus.length <= 10) {
              // 차트에 같은 메뉴가 있는지
              if (chartMenus.map(m => m.menuId).indexOf(Number(menuId)) < 0) {
                this.setState({
                  chartMenus: chartMenus.concat(res.data.dataList)
                });
              } else {
                alert("이미 차트에 존재하는 메뉴입니다.");
              }
            } else {
              alert("통계 메뉴는 10개를 초과할 수 없습니다.");
            }
          } else {
            this.setState({
              chartMenus: res.data.dataList
            });
          }
        })
        .catch(err => alert(err.response.data.message));
    } else {
      alert("추가할 메뉴를 선택하세요.");
    }
  };

  // 차트에 들어간 메뉴 삭제
  handleMenuRemove = (chartIndex: number) => {
    const { chartMenus } = this.state;
    if (chartMenus) {
      // 길이가 있을 경우에만
      if (chartMenus.length !== 0) {
        this.setState({
          chartMenus: chartMenus.filter((m, index) => {
            return index !== chartIndex;
          })
        });
      } else {
        alert("메뉴를 추가하세요.");
      }
    } else {
      alert("메뉴를 추가하세요.");
    }
  };

  // 메뉴 차트에 기간 적용
  handleMenuPeriodSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { chartMenus, menuStartDate, menuEndDate } = this.state;
    e.preventDefault();
    if (chartMenus) {
      axios
        .post(
          `${API_URL}/statistic/menu`,
          {
            menuIds: chartMenus.map(m => m.menuId),
            startDate: menuStartDate,
            endDate: menuEndDate
          },
          withJWT()
        )
        .then(res =>
          this.setState({
            chartMenus: res.data.dataList,
            // 적용된 날짜를 기억해둔다.
            capturedMenuDate: {
              menuStartDate: menuStartDate,
              menuEndDate: menuEndDate
            }
          })
        )
        .catch(err => alert(err.response.data.message));
    } else {
      alert("메뉴를 먼저 추가하세요.");
    }
  };

  handleMenuPeriodAllSubmit = () => {
    const { chartMenus } = this.state;
    if (chartMenus) {
      axios
        .post(
          `${API_URL}/statistic/menu`,
          {
            menuIds: chartMenus.map(m => m.menuId),
            startDate: "",
            endDate: ""
          },
          withJWT()
        )
        .then(res =>
          this.setState({
            chartMenus: res.data.dataList,
            menuStartDate: "",
            menuEndDate: "",
            capturedMenuDate: {
              menuStartDate: "",
              menuEndDate: ""
            }
          })
        )
        .catch(err => alert(err.response.data.message));
    } else {
      alert("메뉴를 먼저 추가하세요.");
    }
  };

  render() {
    const { menus, chartMenus, menuId, menuStartDate, menuEndDate } = this.state;
    const {
      handleInputChange,
      handleSelectChange,
      handleMenuSubmit,
      handleMenuRemove,
      handleMenuPeriodSubmit,
      handleMenuPeriodAllSubmit
    } = this;

    return (
      <>
        <Header title="통계" />
        <div className="main-wrapper">
          <Nav />
          <main className="main">
            <section className="statistics p-4">
              <div className="menu-chart">
                <div className="chart-form-container">
                  <h1 className="mb-4 text-center">메뉴별 매출액</h1>
                  <form className="chart-add-form" onSubmit={e => handleMenuSubmit(e)}>
                    <select
                      className="form-control"
                      name="menuId"
                      value={menuId}
                      onChange={e => handleSelectChange(e)}
                      required
                    >
                      <option value="">선택</option>
                      {menus &&
                        menus.map(m => {
                          return (
                            <option key={m.id} value={m.id}>
                              {m.nameKo}
                            </option>
                          );
                        })}
                    </select>
                    <input className="btn btn-primary w-100 mt-2 mb-4" type="submit" value="추가" />
                  </form>
                  <form className="chart-period-form" onSubmit={e => handleMenuPeriodSubmit(e)}>
                    <input
                      className="form-control mb-2"
                      name="menuStartDate"
                      type="date"
                      value={menuStartDate}
                      onChange={e => handleInputChange(e)}
                      required
                    />
                    <input
                      className="form-control mb-2"
                      name="menuEndDate"
                      type="date"
                      value={menuEndDate}
                      onChange={e => handleInputChange(e)}
                      required
                    />
                    <input className="btn btn-primary w-100 mb-4" type="submit" value="적용" />
                  </form>
                  <button
                    className="btn btn-outline-primary w-100"
                    onClick={() => handleMenuPeriodAllSubmit()}
                  >
                    기간 전체 보기
                  </button>
                </div>
                <div className="chart-wrapper pl-4">
                  <Bar
                    data={
                      chartMenus
                        ? {
                            datasets: chartMenus.map((m: statTypes.IStatMenu, index: number) => {
                              return {
                                label: m.menuName,
                                backgroundColor: colors[index],
                                hoverBackgroundColor: hoverColors[index],
                                data: [m.totalPrice]
                              };
                            })
                          }
                        : {}
                    }
                    options={{
                      responsive: true,
                      maintainAspectRatio: false,
                      tooltips: {
                        callbacks: {
                          title: function() {}
                        }
                      }
                    }}
                    getElementAtEvent={elem =>
                      // 선택된 바가 있을 경우에만 해당 바 삭제.
                      elem.length ? handleMenuRemove(elem[0]._datasetIndex) : {}
                    }
                  />
                </div>
              </div>

              <div className="age-chart">
                <div className="chart-form-container">
                  <h1 className="mb-4 text-center">연령별 매출액</h1>
                  <form className="chart-add-form">
                    <select className="form-control">
                      <option value="10대">10대</option>
                      <option value="20대">20대</option>
                    </select>
                    <input className="btn btn-primary w-100 mt-2 mb-4" type="submit" value="추가" />
                  </form>
                  <form className="chart-period-form">
                    <input className="form-control mb-2" type="date" />
                    <input className="form-control mb-2" type="date" />
                    <input className="btn btn-primary w-100 mb-4" type="submit" value="적용" />
                    <button className="btn btn-outline-primary w-100">기간 전체 보기</button>
                  </form>
                </div>
                <div className="chart-wrapper pl-4">
                  <Doughnut
                    data={data2}
                    options={{
                      responsive: true,
                      maintainAspectRatio: false,
                      tooltips: {
                        enabled: false
                      }
                    }}
                    getElementAtEvent={elem => console.log(elem)}
                  />
                </div>
              </div>

              <div className="device-chart">
                <div className="chart-form-container">
                  <h1 className="mb-4 text-center">기기별 접속자수</h1>
                  <form className="chart-add-form">
                    <select className="form-control">
                      <option value="10대">10대</option>
                      <option value="20대">20대</option>
                    </select>
                    <input className="btn btn-primary w-100 mt-2 mb-4" type="submit" value="추가" />
                  </form>
                  <form className="chart-period-form">
                    <input className="form-control mb-2" type="date" />
                    <input className="form-control mb-2" type="date" />
                    <input className="btn btn-primary w-100 mb-4" type="submit" value="적용" />
                    <button className="btn btn-outline-primary w-100">기간 전체 보기</button>
                  </form>
                </div>
                <div className="chart-wrapper pl-4">
                  <Doughnut
                    data={data3}
                    options={{
                      responsive: true,
                      maintainAspectRatio: false,
                      tooltips: {
                        enabled: false
                      }
                    }}
                    getElementAtEvent={elem => console.log(elem)}
                  />
                </div>
              </div>
            </section>
          </main>
        </div>
      </>
    );
  }
}

export default Statistics;
