import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import '../css/Prijavljivanje.css'; 
import '../css/Registrovanje.css';
import { Typography} from '@mui/material';

export default function Registrovanje() {
  const [ime, setIme] = useState("");
  const [prezime, setPrezime] = useState("");
  const [korisnickoIme, setKorisnickoIme] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [potvrdaLozinke, setPotvrdaLozinke] = useState("");
  const [grad, setGrad] = useState(""); 
  const [ulica, setUlica] = useState("");
  const [brojKuce, setBrojKuce] = useState("");
  const [drzava, setDrzava] = useState("");
  const [broj, setBroj] = useState("");
  const [info, setInfo] = useState("");
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    const korisnik = { ime, prezime, korisnickoIme, email, password, potvrdaLozinke, grad, drzava, ulica, brojKuce, broj, info };
  
    fetch("http://localhost:8080/registrovaniKorisnik/signup", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(korisnik),
    })
      .then((response) => {
        return response.text().then((text) => {
          console.log("Odgovor sa backend-a:", text); // Debugging
          if (!response.ok) {
            throw new Error(text);
          }
          return JSON.parse(text);
        });
      })
      .then(() => {
        alert("Registracija uspešna! Preusmeravamo vas na prijavu...");
        navigate("/prijava");
      })
      .catch((error) => {
        try {
          const errorData = JSON.parse(error.message);
          setErrors(errorData);
        } catch {
          setErrors({ global: error.message });
        }
      });
  };
  
  return (
    <div className="registrovanje-container">
      <form className="prijavljivanje-form" onSubmit={handleSubmit}>
        <h1 className="registrovanje-title">Kreiraj nalog</h1>
          <div className={`error-container ${errors.global ? "show" : ""}`}>
            {errors.global && <p className="error-message">{errors.global}</p>}
          </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="Ime"
            className={`registrovanje-input ${errors.ime ? "error-border" : ""}`}
            value={ime}
            onChange={(e) => setIme(e.target.value)}
          />
          {errors.ime && <span className="error-text">{errors.ime}</span>}
        </div>
        
        <div className="input-group">
          <input
            type="text"
            placeholder="Prezime"
            className={`registrovanje-input ${errors.prezime ? "error-border" : ""}`}
            value={prezime}
            onChange={(e) => setPrezime(e.target.value)}
          />
          {errors.prezime && <span className="error-text">{errors.prezime}</span>}
        </div>
        
        <div className="input-group">
          <input
            type="text"
            placeholder="Korisnicko ime"
            className={`registrovanje-input ${errors.korisnickoIme ? "error-border" : ""}`}
            value={korisnickoIme}
            onChange={(e) => setKorisnickoIme(e.target.value)}
          />
          {errors.korisnickoIme && <span className="error-text">{errors.korisnickoIme}</span>}
        </div>
        
        <div className="input-group">
          <input
            type="email"
            placeholder="E-mail"
            className={`registrovanje-input ${errors.email ? "error-border" : ""}`}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          {errors.email && <span className="error-text">{errors.email}</span>}
        </div>
        
        <div className="input-group">
          <input
            type="password"
            placeholder="Lozinka"
            className={`registrovanje-input ${errors.password ? "error-border" : ""}`}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          {errors.password && <span className="error-text">{errors.password}</span>}
        </div>

        <div className="input-group">
          <input
            type="password"
            placeholder="Potvrda lozinke"
            className={`registrovanje-input ${errors.potvrdaLozinke ? "error-border" : ""}`}
            value={potvrdaLozinke}
            onChange={(e) => setPotvrdaLozinke(e.target.value)}
          />
          {errors.potvrdaLozinke && <span className="error-text">{errors.potvrdaLozinke}</span>}
        </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="Ulica"
            className={`registrovanje-input ${errors.ulica ? "error-border" : ""}`}
            value={ulica}
            onChange={(e) => setUlica(e.target.value)}
          />
          {errors.ulica && <span className="error-text">{errors.ulica}</span>}
        </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="Broj kuce/stana:"
            className={`registrovanje-input ${errors.brojKuce ? "error-border" : ""}`}
            value={brojKuce}
            onChange={(e) => setBrojKuce(e.target.value)}
          />
          {errors.brojKuce && <span className="error-text">{errors.brojKuce}</span>}
        </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="Grad"
            className={`registrovanje-input ${errors.grad ? "error-border" : ""}`}
            value={grad}
            onChange={(e) => setGrad(e.target.value)}
          />
          {errors.grad && <span className="error-text">{errors.grad}</span>}
        </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="Drzava"
            className={`registrovanje-input ${errors.drzava ? "error-border" : ""}`}
            value={drzava}
            onChange={(e) => setDrzava(e.target.value)}
          />
          {errors.drzava && <span className="error-text">{errors.drzava}</span>}
        </div>

        <div className="input-group">
          {errors.broj && <p className="error-message">{errors.broj}</p>}
          <input type="text" placeholder="Broj telefona" className="registrovanje-input" value={broj} onChange={(e) => setBroj(e.target.value)} />
        </div>

        <div className="input-group">
          {errors.info && <p className="error-message">{errors.info}</p>}
          <input type="text" placeholder="Dodatne informacije" className="registrovanje-input" value={info} onChange={(e) => setInfo(e.target.value)} />
        </div>
        
        <button type="submit" className="prijavljivanje-button">Registruj se</button>
          <Typography component="div" style={{ display: "flex", alignItems: "center", marginTop: "5px" }}>
            <span>Već imaš nalog?</span>
              <Link to="/prijava" className="prijavljivanje-link" style={{ marginLeft: "5px" }}>
                Prijavi se
              </Link>
          </Typography>
      </form>
    </div>
  );
}
