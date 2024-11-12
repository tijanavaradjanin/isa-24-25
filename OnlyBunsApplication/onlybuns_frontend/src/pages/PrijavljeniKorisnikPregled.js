import React from 'react';
import { useLocation } from 'react-router-dom';

const PrijavljeniKorisnikPregled = () => {
  const location = useLocation();
  const { korisnik } = location.state || {};

  return (
    <div>
      {korisnik ? (
        <>
          <h1>Dobrodošli, {korisnik.ime} {korisnik.prezime}!</h1>
          <p>Email: {korisnik.email}</p>
        </>
      ) : (
        <p>Nije pronađen prijavljeni korisnik.</p>
      )}
    </div>
  );
};

export default PrijavljeniKorisnikPregled;
