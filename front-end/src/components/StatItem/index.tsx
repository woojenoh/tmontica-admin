import * as React from "react";
import { Bar, Pie, Doughnut } from "react-chartjs-2";
import * as statTypes from "../../types/stat";
import "./styles.scss";

export interface IStatItemProps {
  statTitle: string;
  chartType: "Bar" | "Pie" | "Doughnut";
  optionArray:
    | {
        value: string;
        id: string;
      }[]
    | null;
  chartData: statTypes.IBarData | statTypes.IPieData | null;
  handleSubmit(
    e: React.FormEvent<HTMLFormElement>,
    selectId: string,
    capturedDate: TCapturedDate
  ): void;
  handleRemove(index: string): void;
  handlePeriodSubmit(
    e: React.FormEvent<HTMLFormElement>,
    startDate: string,
    endDate: string,
    setCapturedDate: TSetCapturedDate
  ): void;
  handlePeriodAllSubmit(initializeDate: { (): void }): void;
}

export type TCapturedDate = {
  startDate: string;
  endDate: string;
};

export interface TSetCapturedDate {
  (startDate?: string, endDate?: string): void;
}

export interface IStatItemState {
  selectName: string;
  startDate: string;
  endDate: string;
  capturedDate: {
    startDate: string;
    endDate: string;
  };
}

export interface IInputState {
  selectName: string;
  startDate: string;
  endDate: string;
}

export default class StatItem extends React.PureComponent<IStatItemProps, IStatItemState> {
  state = {
    selectName: "",
    startDate: "",
    endDate: "",
    capturedDate: {
      startDate: "",
      endDate: ""
    }
  };

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

  setCapturedDate = (startDate?: string, endDate?: string) => {
    this.setState({
      capturedDate: {
        startDate: startDate ? startDate : "",
        endDate: endDate ? endDate : ""
      }
    });
  };

  initializeDate = () => {
    this.setState({
      startDate: "",
      endDate: "",
      capturedDate: {
        startDate: "",
        endDate: ""
      }
    });
  };

  render() {
    const { selectName, startDate, endDate, capturedDate } = this.state;
    const {
      statTitle,
      chartType,
      optionArray,
      chartData,
      handleSubmit,
      handleRemove,
      handlePeriodSubmit,
      handlePeriodAllSubmit
    } = this.props;
    const { handleInputChange, handleSelectChange, setCapturedDate, initializeDate } = this;

    return (
      <div className="stat-item">
        <div className="stat-item-form-container">
          <h1 className="mb-4 text-center">{statTitle}</h1>
          <form
            className="stat-item-add-form"
            onSubmit={e => handleSubmit(e, selectName, capturedDate)}
          >
            <select
              className="form-control"
              name="selectName"
              value={selectName}
              onChange={e => handleSelectChange(e)}
              required
            >
              <option value="">선택</option>
              {optionArray &&
                optionArray.map(o => {
                  return (
                    <option key={o.id} value={o.id}>
                      {o.value}
                    </option>
                  );
                })}
            </select>
            <input className="btn btn-primary w-100 mt-2 mb-4" type="submit" value="추가" />
          </form>
          <form
            className="stat-item-period-form"
            onSubmit={e => handlePeriodSubmit(e, startDate, endDate, setCapturedDate)}
          >
            <input
              className="form-control mb-2"
              name="startDate"
              type="date"
              value={startDate}
              onChange={e => handleInputChange(e)}
              required
            />
            <input
              className="form-control mb-2"
              name="endDate"
              type="date"
              value={endDate}
              onChange={e => handleInputChange(e)}
              required
            />
            <input className="btn btn-primary w-100 mb-4" type="submit" value="적용" />
          </form>
          <button
            className="btn btn-outline-primary w-100"
            onClick={() => handlePeriodAllSubmit(initializeDate)}
          >
            기간 전체 보기
          </button>
        </div>
        <div className="stat-item-wrapper pl-4">
          {chartType === "Bar" && (
            <Bar
              data={chartData ? chartData : {}}
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
                elem.length ? handleRemove(elem[0]._model.datasetLabel) : {}
              }
            />
          )}
          {chartType === "Pie" && (
            <Pie
              data={chartData ? chartData : {}}
              options={{
                responsive: true,
                maintainAspectRatio: false,
                tooltips: {
                  callbacks: {
                    title: function() {}
                  }
                }
              }}
              getElementAtEvent={elem => (elem.length ? handleRemove(elem[0]._model.label) : {})}
            />
          )}
          {chartType === "Doughnut" && (
            <Doughnut
              data={chartData ? chartData : {}}
              options={{
                responsive: true,
                maintainAspectRatio: false,
                tooltips: {
                  callbacks: {
                    title: function() {}
                  }
                }
              }}
              getElementAtEvent={elem => (elem.length ? handleRemove(elem[0]._model.label) : {})}
            />
          )}
        </div>
      </div>
    );
  }
}
