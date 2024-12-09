import React from "react";
import { Link, useLocation } from "react-router-dom";
import "../css/Navigacija.css"; // Ispravan uvoz CSS-a

function Navigacija() {
  const location = useLocation(); // Praćenje trenutne rute

  // Prikazuj linkove samo na početnoj strani
  if (location.pathname !== "/") {
    return null; // Ne prikazuje ništa ako niste na početnoj stranici
  }

  return (
    <div className="navigacija-container">
      <Link to="/prijava" className="navigacija-link">Prijavite se</Link>
      <Link to="/" className="navigacija-link">Nastavite kao gost</Link>
    </div>
  );
}

export default Navigacija;
