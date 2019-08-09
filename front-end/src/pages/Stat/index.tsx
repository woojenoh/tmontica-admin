import * as React from "react";
import axios from "axios";
import Header from "../../components/Header";
import StatItem from "../../components/StatItem";
import * as menuTypes from "../../types/menu";
import * as statTypes from "../../types/stat";
import { TCapturedDate, TSetCapturedDate } from "../../components/StatItem";
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

export interface IStatProps {}

export interface IStatState {
  menus: menuTypes.IMenu[] | null;
  chartMenus: statTypes.IStatMenu[] | null;
  chartAges: statTypes.IStatAge[] | null;
  chartUsers: statTypes.IStatUser[] | null;
}

class Stat extends React.PureComponent<IStatProps, IStatState> {
  state = {
    menus: null,
    chartMenus: null,
    chartAges: null,
    chartUsers: null
  } as IStatState;

  componentDidMount() {
    // 현재 등록돼있는 메뉴를 모두 불러온다.
    axios.get(`${API_URL}/menus`, withJWT({ params: { size: 100 } })).then(res => {
      this.setState({
        menus: res.data.menus
      });
    });
  }

  // 차트에 메뉴 추가
  handleMenuSubmit = (
    e: React.FormEvent<HTMLFormElement>,
    menuId: string,
    capturedDate: TCapturedDate
  ) => {
    const { chartMenus } = this.state;
    e.preventDefault();
    if (menuId) {
      axios
        .post(
          `${API_URL}/statistic/menu`,
          {
            menuIdList: [Number(menuId)],
            startDate: capturedDate.startDate,
            endDate: capturedDate.endDate
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
  handleMenuRemove = (chartIndex: string) => {
    const { chartMenus } = this.state;
    if (chartMenus) {
      // 길이가 있을 경우에만
      if (chartMenus.length !== 0) {
        this.setState({
          chartMenus: chartMenus.filter(m => {
            return m.menuName !== chartIndex;
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
  handleMenuPeriodSubmit = (
    e: React.FormEvent<HTMLFormElement>,
    menuStartDate: string,
    menuEndDate: string,
    setCapturedDate: TSetCapturedDate
  ) => {
    const { chartMenus } = this.state;
    e.preventDefault();
    if (chartMenus) {
      if (chartMenus.length) {
        axios
          .post(
            `${API_URL}/statistic/menu`,
            {
              menuIdList: chartMenus.map(m => m.menuId),
              startDate: menuStartDate,
              endDate: menuEndDate
            },
            withJWT()
          )
          .then(res =>
            this.setState(
              {
                chartMenus: res.data.dataList
              },
              () => setCapturedDate(menuStartDate, menuEndDate)
            )
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
  handleMenuPeriodAllSubmit = (initializeDate: { (): void }) => {
    const { chartMenus } = this.state;
    if (chartMenus) {
      if (chartMenus.length) {
        axios
          .post(
            `${API_URL}/statistic/menu`,
            {
              menuIdList: chartMenus.map(m => m.menuId),
              startDate: "",
              endDate: ""
            },
            withJWT()
          )
          .then(res =>
            this.setState(
              {
                chartMenus: res.data.dataList
              },
              () => initializeDate()
            )
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
  handleAgeSubmit = (
    e: React.FormEvent<HTMLFormElement>,
    ageGroup: string,
    capturedDate: TCapturedDate
  ) => {
    const { chartAges } = this.state;
    e.preventDefault();
    if (ageGroup) {
      axios
        .post(
          `${API_URL}/statistic/age`,
          {
            ageGroupList: [ageGroup],
            startDate: capturedDate.startDate,
            endDate: capturedDate.endDate
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
  handleAgePeriodSubmit = (
    e: React.FormEvent<HTMLFormElement>,
    ageStartDate: string,
    ageEndDate: string,
    setCapturedDate: TSetCapturedDate
  ) => {
    const { chartAges } = this.state;
    e.preventDefault();
    if (chartAges) {
      if (chartAges.length) {
        axios
          .post(
            `${API_URL}/statistic/age`,
            {
              ageGroupList: chartAges.map(a => a.ageGroup),
              startDate: ageStartDate,
              endDate: ageEndDate
            },
            withJWT()
          )
          .then(res =>
            this.setState(
              {
                chartAges: res.data.dataList
              },
              () => setCapturedDate(ageStartDate, ageEndDate)
            )
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
  handleAgePeriodAllSubmit = (initializeDate: { (): void }) => {
    const { chartAges } = this.state;
    if (chartAges) {
      if (chartAges.length) {
        axios
          .post(
            `${API_URL}/statistic/age`,
            {
              ageGroupList: chartAges.map(a => a.ageGroup),
              startDate: "",
              endDate: ""
            },
            withJWT()
          )
          .then(res =>
            this.setState(
              {
                chartAges: res.data.dataList
              },
              () => initializeDate()
            )
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
  handleUserSubmit = (
    e: React.FormEvent<HTMLFormElement>,
    userAgent: string,
    capturedDate: TCapturedDate
  ) => {
    const { chartUsers } = this.state;
    e.preventDefault();
    if (userAgent) {
      axios
        .post(
          `${API_URL}/statistic/useragent`,
          {
            userAgentList: [userAgent],
            startDate: capturedDate.startDate,
            endDate: capturedDate.endDate
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
  handleUserPeriodSubmit = (
    e: React.FormEvent<HTMLFormElement>,
    userStartDate: string,
    userEndDate: string,
    setCapturedDate: TSetCapturedDate
  ) => {
    const { chartUsers } = this.state;
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
            this.setState(
              {
                chartUsers: res.data.dataList
              },
              () => setCapturedDate(userStartDate, userEndDate)
            )
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
  handleUserPeriodAllSubmit = (initializeDate: { (): void }) => {
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
            this.setState(
              {
                chartUsers: res.data.dataList
              },
              () => initializeDate()
            )
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
    const { menus, chartMenus, chartAges, chartUsers } = this.state;
    const {
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
          <main className="main">
            <section className="stat">
              <StatItem
                statTitle="메뉴별 매출액"
                chartType="Bar"
                optionArray={
                  menus
                    ? menus.map(m => {
                        return {
                          id: String(m.id),
                          value: m.nameKo
                        };
                      })
                    : null
                }
                chartData={
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
                    : null
                }
                handleSubmit={handleMenuSubmit}
                handleRemove={handleMenuRemove}
                handlePeriodSubmit={handleMenuPeriodSubmit}
                handlePeriodAllSubmit={handleMenuPeriodAllSubmit}
              />
              <StatItem
                statTitle="연령별 매출액"
                chartType="Pie"
                optionArray={[
                  { id: "영유아", value: "영유아" },
                  { id: "10대", value: "10대" },
                  { id: "20대", value: "20대" },
                  { id: "30대", value: "30대" },
                  { id: "40대", value: "40대" },
                  { id: "50대", value: "50대" },
                  { id: "60대 이상", value: "60대 이상" }
                ]}
                chartData={
                  chartAges
                    ? {
                        labels: chartAges.map(a => a.ageGroup),
                        datasets: [
                          {
                            backgroundColor: [...colors],
                            hoverBackgroundColor: [...hoverColors],
                            data: chartAges.map(a => a.totalPrice)
                          }
                        ]
                      }
                    : null
                }
                handleSubmit={handleAgeSubmit}
                handleRemove={handleAgeRemove}
                handlePeriodSubmit={handleAgePeriodSubmit}
                handlePeriodAllSubmit={handleAgePeriodAllSubmit}
              />
              <StatItem
                statTitle="기기별 주문건수"
                chartType="Doughnut"
                optionArray={[
                  { id: "PC", value: "PC" },
                  { id: "TABLET", value: "TABLET" },
                  { id: "MOBILE", value: "MOBILE" }
                ]}
                chartData={
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
                    : null
                }
                handleSubmit={handleUserSubmit}
                handleRemove={handleUserRemove}
                handlePeriodSubmit={handleUserPeriodSubmit}
                handlePeriodAllSubmit={handleUserPeriodAllSubmit}
              />
            </section>
          </main>
        </div>
      </>
    );
  }
}

export default Stat;
