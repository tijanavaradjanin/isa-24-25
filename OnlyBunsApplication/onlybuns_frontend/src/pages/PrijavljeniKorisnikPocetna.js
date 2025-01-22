import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Box, Typography } from '@mui/material';

const PrijavljeniKorisnikPocetna = () => {
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);

  useEffect(() => {
    // Učitavamo token iz localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      // Ako token nije pronađen, preusmeravamo na početnu stranicu
      navigate('/');
      return;
    }

    try {
      // Dekodiramo token da bismo dobili korisničke podatke
      const decodedToken = JSON.parse(atob(token.split('.')[1])); // Parsiramo payload iz JWT-a
      console.log('Decoded Token:', decodedToken);

      // Proveravamo da li postoji "sub" ključ (korisničko ime)
      const korisnickoIme = decodedToken.sub;
      if (korisnickoIme) {
        setKorisnik({ korisnickoIme, ...decodedToken });         
        localStorage.setItem('korisnickoIme', korisnickoIme);        //Sacuvaj korisnicko ime u memoriju aplikacije
      } else {
        throw new Error('Korisničko ime nije pronađeno u tokenu');
      }
    } catch (error) {
      console.error('Greška prilikom dekodiranja tokena:', error);
      localStorage.removeItem('token'); // Ako postoji greška, uklanjamo token
      navigate('/'); // Vraćamo korisnika na početnu
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('token'); // Brisanje tokena iz localStorage
    localStorage.removeItem('korisnickoIme');  //Brisanje korisnickog imena iz localStorage
    setKorisnik(null); // Očisti korisničke podatke iz stanja
    navigate('/'); // Preusmeravanje na početnu stranicu
  };

  const seeMyPosts = () => {
    navigate('/mojeObjave');
  };

  const seeMyProfile = () => {
    navigate('/mojProfil');
  };

  const makeAPost = () => {
    navigate('/kreiranjeObjave');
  };

  return (
    <div>
      {korisnik ? (
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h4">
            Dobrodošli, {korisnik.korisnickoIme}!
          </Typography>
          <Box sx={{ display: 'flex', gap: '8px', marginLeft: 'auto' }}>
            <Button
              variant="contained"
              color="warning"
              onClick={seeMyProfile}
              sx={{ height: '40px' }}
            >
              Moj profil
            </Button>
            <Button
              variant="contained"
              color="warning"
              onClick={seeMyPosts}
              sx={{ height: '40px' }}
            >
              Moje objave
            </Button>
            <Button
              variant="contained"
              color="warning"
              onClick={makeAPost}
              sx={{ height: '40px' }}
            >
              +Objava
            </Button>
            <Button
              variant="contained"
              color="warning"
              onClick={handleLogout}
              sx={{ height: '40px' }}
            >
              Odjavi se
            </Button>
          </Box>
        </Box>
      ) : (
        <p>Nije pronađen prijavljeni korisnik.</p>
      )}
    </div>
  );
};

export default PrijavljeniKorisnikPocetna;
