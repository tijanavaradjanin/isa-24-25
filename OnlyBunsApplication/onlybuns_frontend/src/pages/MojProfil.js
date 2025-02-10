import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Box, Button, Typography, Toolbar, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Alert } from "@mui/material";

const MojProfil = () => {
  //const location = useLocation();
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const korisnickoIme = localStorage.getItem('korisnickoIme');
    console.log('Korisnicko ime:', korisnickoIme);
    console.log('Token:', token);


    if (!token || !korisnickoIme) {
      // Ako token nije pronađen, preusmeravamo na početnu stranicu
      navigate('/');
      return;
    }
  
    // Fetch funkcija za preuzimanje detalja korisnika
    fetch(`http://localhost:8080/registrovaniKorisnik/profil`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Greška prilikom preuzimanja podataka korisnika.');
        }
        return response.json();
      })
      .then((data) => {
        setKorisnik(data);
        console.log(korisnik);
      })
      .catch((error) => {
        console.error('Greška:', error.message);
        setError('Došlo je do greške prilikom preuzimanja podataka.');
      })
      .finally(() => {
        setLoading(false);
      });
  }, [navigate]);
  
  
  const handleLogout = () => {
    localStorage.removeItem('token');
    setKorisnik(null);
    navigate('/');
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

  const showMap = () => {
    navigate('/prikazMape');
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prevState) => !prevState);
  };  

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box mt={4} mx={2}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <Box sx={{ display: 'flex', gap: '8px', marginLeft: 'auto' }}>
        <Toolbar sx={{ justifyContent: "flex-end" }}>
             <Button color="primary" onClick={seeMyProfile}>Moj profil</Button>
           <Typography>|</Typography>
             <Button color="primary" onClick={seeMyPosts}>Moje objave</Button>
            <Typography>|</Typography>
             <Button color="primary" onClick={showMap}>Prikaz mape</Button>
            <Typography>|</Typography>
             <Button color="primary" onClick={makeAPost}>+Objava</Button>
            <Typography>|</Typography>
             <Button color="primary" onClick={handleLogout}>Odjavi se</Button>
          </Toolbar>
        </Box>
      </Box>

      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <TableContainer component={Paper} sx={{ maxWidth: 600 }}>
          <Typography variant="h4" gutterBottom textAlign="center" sx={{ padding: '16px' }}>
            Moj profil
          </Typography>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell colSpan={2} sx={{ fontWeight: 'bold', fontSize: '16px' }}>Podaci korisnika</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <TableCell>Ime</TableCell>
                <TableCell>{korisnik.ime}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Prezime</TableCell>
                <TableCell>{korisnik.prezime}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Korisničko ime</TableCell>
                <TableCell>{korisnik.korisnickoIme}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Email</TableCell>
                <TableCell>{korisnik.email}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Grad</TableCell>
                <TableCell>{korisnik.grad}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Drzava</TableCell>
                <TableCell>{korisnik.drzava}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Broj</TableCell>
                <TableCell>{korisnik.broj}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Info</TableCell>
                <TableCell>{korisnik.info}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Lozinka</TableCell>
                <TableCell>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                     {showPassword ? korisnik.password : '********'}
                    <Button onClick={togglePasswordVisibility} sx={{ minWidth: 'auto', ml: 1 }}>
                            {showPassword ? <VisibilityOff /> : <Visibility />}
                    </Button>
                 </Box>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default MojProfil;
