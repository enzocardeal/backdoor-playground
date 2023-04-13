import React from 'react';
import { Link } from 'react-router-dom';
import { Nav, Button } from 'react-bootstrap';

const Navbar = () => {
  return (
    <Nav className="justify-content-center my-3">
      <Link to="/login" className="mx-2">
        <Button variant="outline-primary">Login</Button>
      </Link>
      <Link to="/signup" className="mx-2">
        <Button variant="outline-primary">Sign Up</Button>
      </Link>
      <Link to="/" className="mx-2">
        <Button variant="outline-primary">Home</Button>
      </Link>
    </Nav>
  );
};

export default Navbar;
