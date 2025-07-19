import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Avatar } from '@mui/material';
import { cirilicaULatinicu } from '../helpers/PismoKonverter.js';
import Navigacija from './Navigacija'; 
import { korisnickoImeIzTokena, getToken } from "../helpers/KorisnickoImeIzTokena";
import { Box, Button, Typography, Table, Modal, TableBody, TableCell, TableContainer, TableRow, Paper, CircularProgress, Alert } from "@mui/material";

const MojProfil = () => {
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const [openLista, setOpenLista] = useState(false);
  const [listaZapracenih, setListaZapracenih] = useState([]);

  useEffect(() => {
    const token = getToken();
    const korisnickoIme = korisnickoImeIzTokena();

    if (!token || !korisnickoIme) {
      navigate('/');
      return;
    }
  
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
    const token = getToken();
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
    <Box
      sx={{
      background: "linear-gradient(to right,rgb(69, 185, 194), #e3f2fd)",
      minHeight: "100vh",
      py: 2
      }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
        <Box sx={{ display: 'flex', gap: '8px', marginLeft: 'auto' }}>
         <Navigacija setKorisnik={setKorisnik} />
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
          </Box>
          <Table>
            <TableBody>
              <TableRow>
                <TableCell>Ime:</TableCell>
                <TableCell>{korisnik.ime}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Prezime:</TableCell>
                <TableCell>{korisnik.prezime}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Korisničko ime:</TableCell>
                <TableCell>{korisnik.korisnickoIme}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Email:</TableCell>
                <TableCell>{korisnik.email}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell
                sx={{
                  whiteSpace: "normal", // omogućava više redova
                  wordBreak: "break-word", // prelama duge reči ako treba
                }}>Adresa:</TableCell>
                <TableCell>{cirilicaULatinicu(korisnik.adresa)}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Broj:</TableCell>
                <TableCell>{korisnik.broj}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Info:</TableCell>
                <TableCell>{korisnik.info}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>Lozinka:</TableCell>
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
                    <Box key={zapracen.korisnickoIme} sx={{ borderBottom: "1px solid #ddd", pb: 1, mb: 1 }}>
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
