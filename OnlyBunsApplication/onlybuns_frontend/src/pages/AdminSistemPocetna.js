import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import {
  MDBContainer
} from 'mdb-react-ui-kit';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import { useNavigate } from 'react-router-dom'; // Dodaj useNavigate
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';

const defaultTheme = createTheme();

const AdminSistemPocetna = () => {
  const location = useLocation();
  const { korisnik } = location.state || {};
  const [email] = useState(korisnik?.email || ''); // Set the initial value of email
  const [password, setPassword] = useState('');
  const [password2, setPassword2] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    const korisnik = { email, password };
    console.log(korisnik);

    fetch("http://localhost:8080/adminsistem/updatepassword", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(korisnik),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log(data.message);
        navigate('/adminSistemView', { state: { korisnik: data } }); // Prosledi podatke kroz rutiranje
      })
      .catch((error) => {
        console.error("Error logging in:", error);
      });
  };
  return (
    <MDBContainer fluid>
      <div>
        {korisnik ? (
          <>
            <h1>
              Dobrodošli, AdminSistem {korisnik.ime} {korisnik.prezime}!
            </h1>
          </>
        ) : (
          <p>Nije pronađen prijavljeni korisnik.</p>
        )}
      </div>

    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Promena lozinke
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Nova lozinka"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Nova lozinka"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password2}
              onChange={(e) => setPassword2(e.target.value)}
            />
            {password !== password2 && (
            <Typography variant="body2" color="error">
              Passwords do not match
            </Typography>
          )}
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={password !== password2}
            >
              Sacuvaj
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
    </MDBContainer>
    
  );
};

export default AdminSistemPocetna;
