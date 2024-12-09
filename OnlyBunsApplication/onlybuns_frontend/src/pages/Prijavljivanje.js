// Importuj potrebne biblioteke
/*import React, { useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import { Link, useNavigate } from 'react-router-dom'; // Dodaj useNavigate
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';

const defaultTheme = createTheme();

export default function Prijavljivanje() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const korisnik = { email, password };
    console.log(korisnik);

    fetch("http://localhost:8080/api/auth/login", {
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
        const jwtToken = data.token; // Pretpostavlja se da API vraća token pod ključem "token"
        if (jwtToken) {
          // Sačuvan token u localStorage
          localStorage.setItem('token', jwtToken);
        console.log(data.message);
        navigate('/prijavljeniKorisnikPregled', { state: { token: jwtToken } }); // Prosledjuje podatke kroz rutiranje
      }})
      .catch((error) => {
        console.error("Error logging in:", error);
        setError('Pogrešan email ili lozinka'); // Postavite grešku kada prijava nije uspešna
      });
  };

  return (
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
            Prijavljivanje
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              error={!!error} // Ako postoji greška, obeleži polje kao grešku
              helperText={error ? 'Pogrešan email ili lozinka' : ''} // Prikazivanje poruke greške
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Lozinka"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Prijavi se
            </Button>
            <Link to="/registracija"> Registruj se </Link>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}*/

import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../css/Prijavljivanje.css'; // Povezivanje sa CSS-om
import {jwtDecode} from 'jwt-decode';

export default function Prijavljivanje() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const korisnik = { email, password };

    fetch("http://localhost:8080/api/auth/login", {
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
        const jwtToken = data.token; // Pretpostavlja se da API vraća token pod ključem "token"
        if (jwtToken) {
          // Sačuvan token u localStorage
          localStorage.setItem('token', jwtToken);
          const decodedToken = jwtDecode(jwtToken);
          console.log('Decoded Token:', decodedToken);
          // Prikaz tokena i dodatnih informacija u konzoli
          console.log('Success login');
          console.log('Token:', jwtToken);
          console.log('User info:', data); // Ovo ispisuje sve podatke koje vraća API
          const role=decodedToken.uloga;

          if (role === 'ADMIN') {
            navigate('/adminSistemPocetna', { state: { token: jwtToken } });
          } else {
            navigate('/prijavljeniKorisnikPocetna', { state: { token: jwtToken } });
          }
        }
      })
      .catch((error) => {
        console.error("Error logging in:", error);
        setError('Pogrešan email ili lozinka'); // Postavite grešku kada prijava nije uspešna
      });
  };

  return (
    <div className="prijavljivanje-container">
      <form className="prijavljivanje-form" onSubmit={handleSubmit}>
        <h1 className="prijavljivanje-title">Prijavljivanje</h1>
        {error && <p className="error-message">{error}</p>}
        <input
          type="email"
          placeholder="Email"
          className="prijavljivanje-input"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Lozinka"
          className="prijavljivanje-input"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" className="prijavljivanje-button">Prijavi se</button>
        <Link to="/registracija" className="prijavljivanje-link">Registruj se</Link>
      </form>
    </div>
  );
}



