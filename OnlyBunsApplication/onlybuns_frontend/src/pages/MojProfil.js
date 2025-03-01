import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Avatar } from '@mui/material';
import { Box, Button, Typography, Toolbar, Table, Modal, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Alert } from "@mui/material";

const MojProfil = () => {
  //const location = useLocation();
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const [openLista, setOpenLista] = useState(false);
  const [zapracen, setZapracen] = useState("");
  const [listaZapracenih, setListaZapracenih] = useState([]);

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

  const handleOpenListaZapracenih = async () => {
    const token = localStorage.getItem('token');
    fetch(`http://localhost:8080/pracenje/listaZapracenih`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Greška prilikom preuzimanja korisnika.");
        }
        return response.json();
      })
      .then((data) => {
        setOpenLista(true);
        setListaZapracenih(data);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleCloseLista= () => setOpenLista(false);
  
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

  const handleUpdateProfile = () => {
    navigate('/azuriranjeProfila');
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
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
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

      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '60vh', marginBottom: '20px' }}>
        {/* Podaci korisnika */}
        <TableContainer component={Paper} sx={{ maxWidth: 600}}>
           <Button sx={{ marginLeft: '54vh', marginTop: '1vh', fontWeight: 'bold', display: 'right' }} 
                onClick={handleOpenListaZapracenih}>
                koga pratite
            </Button>
          <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
            <Avatar 
              src='userrabbit.png' 
              alt="Profilna slika" 
              sx={{ width: 100, height: 100, mb: 1, borderRadius: 0, objectFit: "cover", marginTop: 0 }} 
            />
            <Typography variant="h6" sx={{ fontWeight: 'bold', textAlign: 'center' }}>
                {korisnik.korisnickoIme}
              </Typography>
              <Button onClick={handleUpdateProfile}> izmeni profil </Button>
          </Box>
          <Table>
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

      {/* Modal za prikaz zapracenih korisnika */}
            <Modal open={openLista}>
              <Box
                sx={{
                  position: "absolute",
                  top: "50%",
                  left: "50%",
                  transform: "translate(-50%, -50%)",
                  width: 400,
                  maxHeight: 500,
                  overflowY: "auto",
                  bgcolor: "white",
                  boxShadow: 24,
                  p: 3,
                  borderRadius: 2,
                  display: "flex",
                  flexDirection: "column",
                  gap: 2,
                }}
              >
                <Typography variant="h6">Zapraceni korisnici</Typography>
                {listaZapracenih.length > 0 ? (
                  listaZapracenih.map((zapracen) => (
                    <Box key={zapracen.id} sx={{ borderBottom: "1px solid #ddd", pb: 1, mb: 1 }}>
                      <Typography variant="body1">
                        <Typography
                          component="span"
                          sx={{ fontWeight: "bold", cursor: "pointer", color: "blue" }}
                          onClick={() => navigate(`/korisnikProfil/${zapracen.korisnickoIme}`)}
                        >
                          {zapracen.korisnickoIme}
                          </Typography>
                          </Typography>
                    </Box>
                  ))
                ) : (
                  <Typography>Nema zapracenih korisnika.</Typography>
                )}
                <Button variant="outlined" onClick={handleCloseLista}>
                  Zatvori
                </Button>
              </Box>
            </Modal>
    </Box>
  );
};

export default MojProfil;
