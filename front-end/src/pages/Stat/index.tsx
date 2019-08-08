import * as React from "react";
import axios from "axios";
import { Bar, Doughnut, Pie } from "react-chartjs-2";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import * as menuTypes from "../../types/menu";
import * as statTypes from "../../types/stat";
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

export interface IStatProps {}

export interface IStatState {
  menus: menuTypes.IMenu[] | null;
  chartMenus: statTypes.IStatMenu[] | null;
  menuId: string;
  menuStartDate: string;
  menuEndDate: string;
  capturedMenuDate: {
    menuStartDate: string;
    menuEndDate: string;
  };

  chartAges: statTypes.IStatAge[] | null;
  ageGroup: string;
  ageStartDate: string;
  ageEndDate: string;
  capturedAgeDate: {
    ageStartDate: string;
    ageEndDate: string;
  };

  chartUsers: statTypes.IStatUser[] | null;
  userAgent: string;
  userStartDate: string;
  userEndDate: string;
  capturedUserDate: {
    userStartDate: string;
    userEndDate: string;
  };
}

export interface IInputState {
  menuId: string;
  menuStartDate: string;
  menuEndDate: string;

  ageGroup: string;
  ageStartDate: string;
  ageEndDate: string;

  userAgent: string;
  userStartDate: string;
  userEndDate: string;
}

class Stat extends React.PureComponent<IStatProps, IStatState> {
  state = {
    menus: null,
    chartMenus: null,
    menuId: "",
    menuStartDate: "",
    menuEndDate: "",
    capturedMenuDate: {
      menuStartDate: "",
      menuEndDate: ""
    },

    chartAges: null,
    ageGroup: "",
    ageStartDate: "",
    ageEndDate: "",
    capturedAgeDate: {
      ageStartDate: "",
      ageEndDate: ""
    },

    chartUsers: null,
    userAgent: "",
    userStartDate: "",
    userEndDate: "",
    capturedUserDate: {
      userStartDate: "",
      userEndDate: ""
    }
  } as IStatState;

  componentDidMount() {
    // 현재 등록돼있는 메뉴를 모두 불러온다.
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

  // 차트에 메뉴 추가
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
            // 10개 이하인지 체크
            if (chartMenus.length <= 10) {
              // 같은 항목이 있는지
              if (chartMenus.map(m => m.menuId).indexOf(Number(menuId)) < 0) {
                this.setState({
                  chartMenus: chartMenus.concat(res.data.dataList)
                });
              } else {
                alert("이미 차트에 존재하는 메뉴입니다.");
              }
            } else {
              alert("차트의 항목은 10개를 초과할 수 없습니다.");
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
      if (chartMenus.length) {
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
    } else {
      alert("메뉴를 먼저 추가하세요.");
    }
  };

  // 메뉴 차트에 기간 전체 적용
  handleMenuPeriodAllSubmit = () => {
    const { chartMenus } = this.state;
    if (chartMenus) {
      if (chartMenus.length) {
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
    } else {
      alert("메뉴를 먼저 추가하세요.");
    }
  };

  // 차트에 연령 추가
  handleAgeSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { ageGroup, chartAges, capturedAgeDate } = this.state;
    e.preventDefault();
    if (ageGroup) {
      axios
        .post(
          `${API_URL}/statistic/age`,
          {
            ageGroups: [ageGroup],
            startDate: capturedAgeDate.ageStartDate,
            endDate: capturedAgeDate.ageEndDate
          },
          withJWT()
        )
        .then(res => {
          // null 체크
          if (chartAges) {
            // 10개 이하인지 체크
            if (chartAges.length <= 10) {
              // 같은 항목이 있는지
              if (chartAges.map(m => m.ageGroup).indexOf(ageGroup) < 0) {
                this.setState({
                  chartAges: chartAges.concat(res.data.dataList)
                });
              } else {
                alert("이미 차트에 존재하는 연령입니다.");
              }
            } else {
              alert("차트의 항목은 10개를 초과할 수 없습니다.");
            }
          } else {
            this.setState({
              chartAges: res.data.dataList
            });
          }
        })
        .catch(err => alert(err.response.data.message));
    } else {
      alert("추가할 연령을 선택하세요.");
    }
  };

  // 차트에 들어간 연령 삭제
  handleAgeRemove = (chartLabel: string) => {
    const { chartAges } = this.state;
    if (chartAges) {
      // 길이가 있을 경우에만
      if (chartAges.length !== 0) {
        this.setState({
          chartAges: chartAges.filter((m: statTypes.IStatAge) => {
            return m.ageGroup !== chartLabel;
          })
        });
      } else {
        alert("연령을 추가하세요.");
      }
    } else {
      alert("연령을 추가하세요.");
    }
  };

  // 연령 차트에 기간 적용
  handleAgePeriodSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { chartAges, ageStartDate, ageEndDate } = this.state;
    e.preventDefault();
    if (chartAges) {
      if (chartAges.length) {
        axios
          .post(
            `${API_URL}/statistic/age`,
            {
              ageGroups: chartAges.map(a => a.ageGroup),
              startDate: ageStartDate,
              endDate: ageEndDate
            },
            withJWT()
          )
          .then(res =>
            this.setState({
              chartAges: res.data.dataList,
              // 적용된 날짜를 기억해둔다.
              capturedAgeDate: {
                ageStartDate: ageStartDate,
                ageEndDate: ageEndDate
              }
            })
          )
          .catch(err => alert(err.response.data.message));
      } else {
        alert("연령을 먼저 추가하세요.");
      }
    } else {
      alert("연령을 먼저 추가하세요.");
    }
  };

  // 연령 차트에 기간 전체 적용
  handleAgePeriodAllSubmit = () => {
    const { chartAges } = this.state;
    if (chartAges) {
      if (chartAges.length) {
        axios
          .post(
            `${API_URL}/statistic/age`,
            {
              ageGroups: chartAges.map(a => a.ageGroup),
              startDate: "",
              endDate: ""
            },
            withJWT()
          )
          .then(res =>
            this.setState({
              chartAges: res.data.dataList,
              ageStartDate: "",
              ageEndDate: "",
              capturedAgeDate: {
                ageStartDate: "",
                ageEndDate: ""
              }
            })
          )
          .catch(err => alert(err.response.data.message));
      } else {
        alert("연령을 먼저 추가하세요.");
      }
    } else {
      alert("연령을 먼저 추가하세요.");
    }
  };

  // 차트에 기기별 유저 추가
  handleUserSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { userAgent, chartUsers, capturedUserDate } = this.state;
    e.preventDefault();
    if (userAgent) {
      axios
        .post(
          `${API_URL}/statistic/useragent`,
          {
            userAgentList: [userAgent],
            startDate: capturedUserDate.userStartDate,
            endDate: capturedUserDate.userEndDate
          },
          withJWT()
        )
        .then(res => {
          // null 체크
          if (chartUsers) {
            // 10개 이하인지 체크
            if (chartUsers.length <= 10) {
              // 같은 항목이 있는지
              if (chartUsers.map(m => m.userAgent).indexOf(userAgent) < 0) {
                this.setState({
                  chartUsers: chartUsers.concat(res.data.dataList)
                });
              } else {
                alert("이미 차트에 존재하는 기기입니다.");
              }
            } else {
              alert("차트의 항목은 10개를 초과할 수 없습니다.");
            }
          } else {
            this.setState({
              chartUsers: res.data.dataList
            });
          }
        })
        .catch(err => alert(err.response.data.message));
    } else {
      alert("추가할 기기를 선택하세요.");
    }
  };

  // 차트에 들어간 기기별 유저 삭제
  handleUserRemove = (chartLabel: string) => {
    const { chartUsers } = this.state;
    if (chartUsers) {
      // 길이가 있을 경우에만
      if (chartUsers.length !== 0) {
        this.setState({
          chartUsers: chartUsers.filter((u: statTypes.IStatUser) => {
            return u.userAgent !== chartLabel;
          })
        });
      } else {
        alert("기기를 추가하세요.");
      }
    } else {
      alert("기기를 추가하세요.");
    }
  };

  // 기기별 유저 차트에 기간 적용
  handleUserPeriodSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    const { chartUsers, userStartDate, userEndDate } = this.state;
    e.preventDefault();
    if (chartUsers) {
      if (chartUsers.length) {
        axios
          .post(
            `${API_URL}/statistic/useragent`,
            {
              userAgentList: chartUsers.map(u => u.userAgent),
              startDate: userStartDate,
              endDate: userEndDate
            },
            withJWT()
          )
          .then(res =>
            this.setState({
              chartUsers: res.data.dataList,
              // 적용된 날짜를 기억해둔다.
              capturedUserDate: {
                userStartDate: userStartDate,
                userEndDate: userEndDate
              }
            })
          )
          .catch(err => alert(err.response.data.message));
      } else {
        alert("기기를 먼저 추가하세요.");
      }
    } else {
      alert("기기를 먼저 추가하세요.");
    }
  };

  // 기기별 유저 차트에 기간 전체 적용
  handleUserPeriodAllSubmit = () => {
    const { chartUsers } = this.state;
    if (chartUsers) {
      if (chartUsers.length) {
        axios
          .post(
            `${API_URL}/statistic/useragent`,
            {
              userAgentList: chartUsers.map(u => u.userAgent),
              startDate: "",
              endDate: ""
            },
            withJWT()
          )
          .then(res =>
            this.setState({
              chartUsers: res.data.dataList,
              userStartDate: "",
              userEndDate: "",
              capturedUserDate: {
                userStartDate: "",
                userEndDate: ""
              }
            })
          )
          .catch(err => alert(err.response.data.message));
      } else {
        alert("기기를 먼저 추가하세요.");
      }
    } else {
      alert("기기를 먼저 추가하세요.");
    }
  };

  render() {
    const {
      menus,
      chartMenus,
      menuId,
      menuStartDate,
      menuEndDate,
      chartAges,
      ageGroup,
      ageStartDate,
      ageEndDate,
      chartUsers,
      userAgent,
      userStartDate,
      userEndDate
    } = this.state;
    const {
      handleInputChange,
      handleSelectChange,
      handleMenuSubmit,
      handleMenuRemove,
      handleMenuPeriodSubmit,
      handleMenuPeriodAllSubmit,
      handleAgeSubmit,
      handleAgeRemove,
      handleAgePeriodSubmit,
      handleAgePeriodAllSubmit,
      handleUserSubmit,
      handleUserRemove,
      handleUserPeriodSubmit,
      handleUserPeriodAllSubmit
    } = this;

    return (
      <>
        <Header title="통계" />
        <div className="main-wrapper">
          <Nav />
          <main className="main">
            <section className="stat p-4">
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
                        ? ({
                            datasets: chartMenus.map((m: statTypes.IStatMenu, index: number) => {
                              return {
                                label: m.menuName,
                                backgroundColor: colors[index],
                                hoverBackgroundColor: hoverColors[index],
                                data: [m.totalPrice]
                              };
                            })
                          } as statTypes.IBarData)
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
                  <form className="chart-add-form" onSubmit={e => handleAgeSubmit(e)}>
                    <select
                      className="form-control"
                      name="ageGroup"
                      value={ageGroup}
                      onChange={e => handleSelectChange(e)}
                      required
                    >
                      <option value="">선택</option>
                      <option value="영유아">영유아</option>
                      <option value="10대">10대</option>
                      <option value="20대">20대</option>
                      <option value="30대">30대</option>
                      <option value="40대">40대</option>
                      <option value="50대">50대</option>
                      <option value="60대">60대 이상</option>
                    </select>
                    <input className="btn btn-primary w-100 mt-2 mb-4" type="submit" value="추가" />
                  </form>
                  <form className="chart-period-form" onSubmit={e => handleAgePeriodSubmit(e)}>
                    <input
                      className="form-control mb-2"
                      type="date"
                      name="ageStartDate"
                      value={ageStartDate}
                      onChange={e => handleInputChange(e)}
                      required
                    />
                    <input
                      className="form-control mb-2"
                      type="date"
                      name="ageEndDate"
                      value={ageEndDate}
                      onChange={e => handleInputChange(e)}
                      required
                    />
                    <input className="btn btn-primary w-100 mb-4" type="submit" value="적용" />
                  </form>
                  <button
                    className="btn btn-outline-primary w-100"
                    onClick={() => handleAgePeriodAllSubmit()}
                  >
                    기간 전체 보기
                  </button>
                </div>
                <div className="chart-wrapper pl-4">
                  <Pie
                    data={
                      chartAges
                        ? ({
                            labels: chartAges.map(a => a.ageGroup),
                            datasets: [
                              {
                                backgroundColor: [...colors],
                                hoverBackgroundColor: [...hoverColors],
                                data: chartAges.map(a => a.totalPrice)
                              }
                            ]
                          } as statTypes.IPieData)
                        : {}
                    }
                    options={{
                      responsive: true,
                      maintainAspectRatio: false
                    }}
                    getElementAtEvent={elem =>
                      // 선택된 파이가 있을 경우에만 해당 파이 삭제.
                      elem.length ? handleAgeRemove(elem[0]._model.label) : {}
                    }
                  />
                </div>
              </div>

              <div className="device-chart">
                <div className="chart-form-container">
                  <h1 className="mb-4 text-center">기기별 구매자수</h1>
                  <form className="chart-add-form" onSubmit={e => handleUserSubmit(e)}>
                    <select
                      className="form-control"
                      name="userAgent"
                      value={userAgent}
                      onChange={e => handleSelectChange(e)}
                      required
                    >
                      <option value="">선택</option>
                      <option value="PC">PC</option>
                      <option value="TABLET">TABLET</option>
                      <option value="MOBILE">MOBILE</option>
                    </select>
                    <input className="btn btn-primary w-100 mt-2 mb-4" type="submit" value="추가" />
                  </form>
                  <form className="chart-period-form" onSubmit={e => handleUserPeriodSubmit(e)}>
                    <input
                      className="form-control mb-2"
                      type="date"
                      name="userStartDate"
                      value={userStartDate}
                      onChange={e => handleInputChange(e)}
                      required
                    />
                    <input
                      className="form-control mb-2"
                      type="date"
                      name="userEndDate"
                      value={userEndDate}
                      onChange={e => handleInputChange(e)}
                      required
                    />
                    <input className="btn btn-primary w-100 mb-4" type="submit" value="적용" />
                  </form>
                  <button
                    className="btn btn-outline-primary w-100"
                    onClick={() => handleUserPeriodAllSubmit()}
                  >
                    기간 전체 보기
                  </button>
                </div>
                <div className="chart-wrapper pl-4">
                  <Doughnut
                    data={
                      chartUsers
                        ? ({
                            labels: chartUsers.map(u => u.userAgent),
                            datasets: [
                              {
                                backgroundColor: [...colors],
                                hoverBackgroundColor: [...hoverColors],
                                data: chartUsers.map(u => u.count)
                              }
                            ]
                          } as statTypes.IPieData)
                        : {}
                    }
                    options={{
                      responsive: true,
                      maintainAspectRatio: false
                    }}
                    getElementAtEvent={elem =>
                      // 선택된 파이가 있을 경우에만 해당 파이 삭제.
                      elem.length ? handleUserRemove(elem[0]._model.label) : {}
                    }
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

export default Stat;
