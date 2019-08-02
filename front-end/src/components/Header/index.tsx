import React, { Component } from "react";
import "bootstrap/dist/css/bootstrap.css";
import "./styles.scss";

interface Props {
  hide?: boolean;
  title: string;
}
interface State {}

export default class Header extends Component<Props, State> {
  state = {};

  render() {
    const { title, hide } = this.props;
    return (
      <header className={`bg-dark text-light flex-row ${hide ? "hide" : ""}`}>
        <i className="fas fa-bars menu-icon" />
        <h1 className="title">{title}</h1>
      </header>
    );
  }
}
