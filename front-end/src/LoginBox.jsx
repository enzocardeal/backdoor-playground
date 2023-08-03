import { Form, Button, Card } from 'react-bootstrap';
import React, { useState } from 'react';

const LoginBox = () =>  {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [apiResponse, setApiResponse] = useState(null);

  function handleSubmit(event) {
    event.preventDefault();

    fetch('http://localhost:8000/api/user/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    })
    .then(response => response.json())
    .then(data => {
      console.log(data);
      setApiResponse(JSON.stringify(data, null, 2)); // Pretty print JSON string
    })
    .catch(error => console.log(error));
  }

  return (
    <div className="d-flex justify-content-center align-items-center" >
      <Card className="border border-primary shadow w-25" style={{ minWidth: "300px" }}>
        <Card.Body>
          <h2 className="text-center mb-4">Login Page</h2>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formBasicUsername">
              <Form.Label>Username</Form.Label>
              <Form.Control type="text" placeholder="Enter username" value={username} onChange={(e) => {setUsername(e.target.value)}} required />
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" placeholder="Password" value={password} onChange={(e) => {setPassword(e.target.value)}} required />
            </Form.Group>

            <Button variant="primary" type="submit" className="w-100 my-2">
              Submit
            </Button>
          </Form>
          {apiResponse && (
            <div className="mt-3">
              <h5>API Response:</h5>
              <pre>{apiResponse}</pre>
            </div>
          )}
        </Card.Body>
      </Card>
    </div>
  );
}

export default LoginBox;
