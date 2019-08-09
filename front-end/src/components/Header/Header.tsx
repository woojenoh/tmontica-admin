import React, { PureComponent } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";
import Nav from "../Nav";
import "bootstrap/dist/css/bootstrap.css";
import "./styles.scss";

interface Props {
  hide?: boolean;
  title: string;
  isSignin: boolean;
  isAdmin: boolean;
  fetchSignin(): void;
}
interface State {
  isSidebarOpen: boolean;
}

export default class Header extends PureComponent<Props, State> {
  state = {
    isSidebarOpen: false
  };

  handleSidebarOpen = () => {
    this.setState({
      isSidebarOpen: true
    });
  };

  handleSidebarClose = () => {
    this.setState({
      isSidebarOpen: false
    });
  };

  render() {
    const { isSidebarOpen } = this.state;
    const { title, isSignin, isAdmin, fetchSignin } = this.props;
    const { handleSidebarOpen, handleSidebarClose } = this;

    return (
      <>
        <header className="bg-dark text-light header-container">
          <div className="header">
            <div className="d-flex align-items-center">
              <div
                className="bar-button cursor-pointer"
                onClick={() => (isSidebarOpen ? handleSidebarClose() : handleSidebarOpen())}
              >
                {isSignin && isAdmin && (
                  <FontAwesomeIcon className="mr-4" icon={faBars} size="2x" />
                )}
              </div>
              <h1 className="title">{title}</h1>
            </div>
            {isSignin && isAdmin && (
              <button className="btn btn-primary" onClick={() => fetchSignin()}>
                로그아웃
              </button>
            )}
          </div>
        </header>
        {isSignin && isAdmin && <Nav isSidebarOpen={isSidebarOpen} />}
      </>
    );
  }
}
