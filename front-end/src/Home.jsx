import React from 'react';
import { Container } from 'react-bootstrap';

const Home = () => {
  return (
    <div className="d-flex justify-content-center align-items-center">
        <Container fluid className="bg-primary text-white py-5">
          <Container className="text-center">
            <h1>Welcome to the Home Page</h1>
            <p>
              This is a simple example of a home page using Bootstrap for styling.
            </p>
          </Container>
        </Container>
    </div>
  );
};

export default Home;
