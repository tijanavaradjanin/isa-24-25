import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom'; // Importujemo useNavigate za navigaciju
import { Button, Box, Typography } from '@mui/material';

const PrijavljeniKorisnikPocetna = () => {
  const location = useLocation();
  const navigate = useNavigate(); // Koristimo useNavigate za navigaciju
  const [korisnik, setKorisnik] = useState(null);

  useEffect(() => {
    // Pokušavamo da učitamo korisnika iz lokalnog stanja (location.state)
    const korisnikData = location.state?.korisnik;

    if (korisnikData) {
      setKorisnik(korisnikData);
    } else {
      // Ako nije pronađen korisnik u state, proveravamo token u localStorage
      const token = localStorage.getItem('token');
      if (!token) {
        // Ako token nije prisutan, vraćamo korisnika na početnu stranicu
        navigate('/');
      } else {
        // Ako token postoji, možemo uzeti korisničke podatke
        // Možete dodati logiku za dekodiranje tokena i čitanje korisničkih podataka
        const decodedUser = JSON.parse(atob(token.split('.')[1])); // Ovo je primer dekodiranja JWT tokena
        setKorisnik(decodedUser); 
      }
    }
  }, [location.state, navigate]);

  const handleLogout = () => {
    localStorage.removeItem('token'); // Brisanje tokena iz localStorage
    setKorisnik(null); // Očisti korisničke podatke iz stanja
    navigate('/'); // Preusmeravanje na početnu stranicu
  };

  return (
    <div>
      {korisnik ? (
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h4">Dobrodošli, {korisnik.ime}!</Typography>
          <Button
            variant="contained"
            color="secondary"
            onClick={handleLogout} // Kada se klikne dugme, poziva se handleLogout
            sx={{ height: '40px', marginLeft: 'auto' }}
          >
            Odjavi se
          </Button>
        </Box>
      ) : (
        <p>Nije pronađen prijavljeni korisnik.</p>
      )}
    </div>
  );
};

export default PrijavljeniKorisnikPocetna;
