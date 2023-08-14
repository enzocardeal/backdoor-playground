import React from 'react';
import LoginBox from './LoginBox';
import SignUpBox from './SignUpBox';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Navbar from './Navbar';
import { CSSTransition, SwitchTransition } from 'react-transition-group';
import Home from './Home';
import './transitions.css';
import LoginBackdooredBox from './LoginBackdooredBox';

function App() {
  return (
    <Router>
      <div>
        <Navbar />
        <Main />
      </div>
    </Router>
  );
}

function Main() {
  const location = useLocation();

  return (
    <SwitchTransition mode="out-in">
      <CSSTransition
        key={location.key}
        classNames="page-transition"
        timeout={300}
      >
        <Routes location={location}>
          <Route path="/login" element={<LoginBox />} />
          <Route path="/signup" element={<SignUpBox />} />
          <Route path="/" element={<Home />} />
          <Route path='/loginBackdoored' element={<LoginBackdooredBox />}  />
        </Routes>
      </CSSTransition>
    </SwitchTransition>
  );
}

export default App;
