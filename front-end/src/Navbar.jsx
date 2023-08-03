import React from 'react';
import { Link } from 'react-router-dom';
import { Nav, Button } from 'react-bootstrap';

const Navbar = () => {
  return (
    <Nav className="justify-content-center my-3">
      <Link to="/login" className="mx-2">
        <Button variant="btn btn-primary">Login</Button>
      </Link>
      <Link to="/signup" className="mx-2">
        <Button variant="btn btn-primary">Sign Up</Button>
      </Link>
      <Link to="/" className="mx-2">
        <Button variant="btn btn-primary">Home</Button>
      </Link>
      <Link to="/loginBackdoored" className="mx-2">
        <Button variant="btn btn-primary btn-danger">Login Backdoor</Button>
      </Link>
    </Nav>
  );
};

export default Navbar;
