import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom'; // Importujemo useNavigate za navigaciju
import { Button, Box, Toolbar } from '@mui/material';

const AdminSistemPocetna = () => {
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
        const decodedUser = JSON.parse(atob(token.split('.')[1])); // Ovo je primer dekodiranja JWT tokena
        setKorisnik(decodedUser); 
      }
    }
  }, [location.state, navigate]);

  const adminRegister = () => {
    navigate('/'); // Preusmeravanje na početnu stranicu
  };

  const changePassword = () => {
    navigate('/'); // Preusmeravanje na početnu stranicu
  };

  const handleLogout = () => {
    localStorage.removeItem('token'); // Brisanje tokena iz localStorage
    setKorisnik(null); // Očisti korisničke podatke iz stanja
    navigate('/'); // Preusmeravanje na početnu stranicu
  };

  return (
        <Box>
          {/* Navigacija */}
          <Toolbar sx={{ justifyContent: "flex-end" }}>
            <Button color="primary" onClick={handleLogout}>Odjavi se</Button>
          </Toolbar>
        </Box>
  )
};

export default AdminSistemPocetna;
