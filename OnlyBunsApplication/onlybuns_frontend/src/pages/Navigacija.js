import React from "react";
import { Link } from "react-router-dom";

function Navigacija() {
  return (
    <div>
      <Link to="/"> Pocetna </Link><br/>
      <Link to="/prijava"> Prijavi se kao Korisnik </Link><br/>
      <Link to="/adminSistemLogin"> Prijavi se kao Admin sistema </Link>

    </div>
  );
}

export default Navigacija;
