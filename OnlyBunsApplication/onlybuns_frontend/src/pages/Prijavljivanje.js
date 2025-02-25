import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../css/Prijavljivanje.css'; 
import {jwtDecode} from 'jwt-decode';

export default function Prijavljivanje() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const korisnik = { email, password };
    console.log("Korisnik:", korisnik); // Pre fetch-a

    fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(korisnik),
    })
      .then((response) => {
        return response.text().then((text) => {
          if (!response.ok) {
            throw new Error(text); // Sada vraća backend poruku umesto samo HTTP statusa
          }
          return JSON.parse(text); // Ako je uspešno, parsira JSON
        });
      })
      .then((data) => {
        console.log("Pokrenuta funkcija handleLogin");
        console.log("Sta vraca bekend: ", data);
        console.log("Sta vraca bekend: ", data.accessToken);
        const jwtToken = data.accessToken;
        if (jwtToken) {
          localStorage.setItem("token", jwtToken);
          const decodedToken = jwtDecode(jwtToken);
          console.log("Decoded Token:", decodedToken);
          console.log("Success login");
          console.log("Token:", jwtToken);
          console.log("User info:", data);
          const role = decodedToken.uloga;
    
          if (role === "ADMIN") {
            navigate("/adminSistemPocetna", { state: { token: jwtToken } });
          } else {
            navigate("/prijavljeniKorisnikPocetna", { state: { token: jwtToken } });
          }
        }
      })
      .catch((error) => {
        console.error("Error logging in:", error);
        setError(error.message); // Sada prikazuje backend poruku (npr. "Previše neuspešnih pokušaja. Pokušajte ponovo kasnije.")
      })
    }
    
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



