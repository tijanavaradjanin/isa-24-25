import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../css/Prijavljivanje.css'; 
import {jwtDecode} from 'jwt-decode';
import { Typography} from '@mui/material';

export default function Prijavljivanje() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const korisnik = { email, password };

    fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(korisnik),
    })
      .then((response) => {
        return response.text().then((text) => {
          if (!response.ok) {
            throw new Error(text); // Vraća backend poruku umesto samo HTTP statusa
          }
          return JSON.parse(text); // Ako je uspešno, parsira JSON
        });
      })
      .then((data) => {
        const jwtToken = data.accessToken;
        if (jwtToken) {
          localStorage.setItem("token", jwtToken);
          const decodedToken = jwtDecode(jwtToken);
          const role = decodedToken.uloga.naziv;
          if (role === "ADMIN") {
            navigate("/adminSistemPocetna", { state: { token: jwtToken } });
          } else {
            navigate("/prijavljeniKorisnikPocetna", { state: { token: jwtToken } });
          }
        }
      })
      .catch((error) => {
        console.error("Error logging in:", error);
        setError(error.message); // Prikazuje backend poruku
      })
    }
    
  return (
    <div className="prijavljivanje-container">
      <form className="prijavljivanje-form" onSubmit={handleSubmit}>
        <h1 className="prijavljivanje-title">Dobrodošli nazad!</h1>
        <div className={`error-container ${error ? "show" : ""}`}>
          {error && <p className="error-message">{error}</p>}
        </div>

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
          <Typography component="div" style={{ display: "flex", alignItems: "center", marginTop: "5px" }}>
            <span>Nemaš nalog?</span>
              <Link to="/registracija" className="prijavljivanje-link" style={{ marginLeft: "5px" }}>
                Registruj se
              </Link>
          </Typography>
      </form>
    </div>
  );
}



