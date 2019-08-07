import * as React from "react";
import { Bar, Doughnut } from "react-chartjs-2";
import Header from "../../components/Header";
import Nav from "../../components/Nav";
import "./styles.scss";

export interface IStatisticsProps {}

export interface IStatisticsState {}

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

const data1 = {
  datasets: [
    {
      label: "아메리카노",
      backgroundColor: colors[0],
      hoverBackgroundColor: hoverColors[0],
      data: [5520]
    },
    {
      label: "카페라떼",
      backgroundColor: colors[1],
      hoverBackgroundColor: hoverColors[1],
      data: [2310]
    },
    {
      label: "바닐라라떼",
      backgroundColor: colors[2],
      hoverBackgroundColor: hoverColors[2],
      data: [3420]
    }
  ]
};

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

class Statistics extends React.Component<IStatisticsProps, IStatisticsState> {
  render() {
    return (
      <>
        <Header title="통계" />
        <div className="main-wrapper">
          <Nav />
          <main className="main">
            <section className="statistics p-4">
              <div className="menu-chart">
                <div className="chart-form-container">
                  <h1 className="mb-4 text-center">메뉴별 판매량</h1>
                  <form className="chart-add-form">
                    <select className="form-control">
                      <option value="아메리카노">아메리카노</option>
                      <option value="카페라떼">카페라떼</option>
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
                  <Bar
                    data={data1}
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

              <div className="age-chart">
                <div className="chart-form-container">
                  <h1 className="mb-4 text-center">연령별 분포량</h1>
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
                  <h1 className="mb-4 text-center">기기별 접속량</h1>
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
