import { Form, Button, Card } from 'react-bootstrap';
import React, { useState } from 'react';

const LoginBox = () =>  {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  function handleSubmit(event) {
    event.preventDefault();

    fetch('http://localhost:5555/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: email,
        password: password
      })
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.log(error));
  }

  return (
    <div className="d-flex justify-content-center align-items-center" >
      <Card className="border border-primary shadow w-25" style={{ minWidth: "300px" }}>
        <Card.Body>
          <h2 className="text-center mb-4">Login Page</h2>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" placeholder="Enter email" value={email} onChange={(e) => {setEmail(e.target.value)}} required />
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" placeholder="Password" value={password} onChange={(e) => {setPassword(e.target.value)}} required />
            </Form.Group>

            <Button variant="primary" type="submit" className="w-100 my-2">
              Submit
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
}

export default LoginBox;
