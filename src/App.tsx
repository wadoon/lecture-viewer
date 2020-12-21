import { ReactComponent } from '*.svg';
import React, { useState, Component } from 'react';
import './App.css';

type AppProps = {
}

type AppState = {
  rootHeight: number
}

class App extends Component<{}, AppState> {
  componentWillMount() {
    this.setState({
      rootHeight: window.innerHeight
    });

    window.onresize = (evt: Event) => {
      console.log("resize!", window.innerHeight);
      this.setState({ rootHeight: window.innerHeight });
    };
  }

  render() {
    return (
      <div className="App" style={{ height: this.state.rootHeight + "px" }}>
        <header className="App-header"></header>
        <div id="pdf">
          <canvas id="slides" width="800" height="500"></canvas>
          <canvas id="mouse" width="800" height="500"></canvas>
          <canvas id="social" width="800" height="500"></canvas>
        </div>
        <div id="video">
          <video id="player" controls>
            <source src="big_buck_bunny.webm" />
          </video>
        </div>
        <div id="chapters"></div>
      </div>
    );
  }
}

export default App;
