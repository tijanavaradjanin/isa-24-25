import { useParams } from "react-router-dom";
import React, { useEffect, useState } from 'react';
import { Box, Typography, Table, TableBody, TableCell, CircularProgress, Alert, TableContainer, TableHead, TableRow, Paper} from "@mui/material";

const KorisnikProfil = () => {
  const { korisnickoIme } = useParams(); // Uzima korisničko ime iz URL-a
  const [userProfile, setUserProfile] = useState(null);
  const [korisnik, setKorisnik] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Poziv backend-a da preuzme podatke o korisniku
    fetch(`http://localhost:8080/registrovaniKorisnik/korisnikProfil/${korisnickoIme}`, {
        method: 'GET',
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error('Greška prilikom preuzimanja podataka korisnika.');
          }
          return response.json();
        })
        .then((data) => {
          setKorisnik(data);
          console.log(data);
        })
        .catch((error) => {
          console.error('Greška:', error.message);
          setError('Došlo je do greške prilikom preuzimanja podataka.');
        })
        .finally(() => {
          setLoading(false);
        });
    }, [korisnickoIme]);

  if (!KorisnikProfil) {
    return <div>Učitavanje...</div>; // Prikazuj poruku dok se učitava profil
  }

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
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <TableContainer component={Paper} sx={{ maxWidth: 600 }}>
          <Typography variant="h4" gutterBottom textAlign="center" sx={{ padding: '16px' }}>
            Profil korisnika
          </Typography>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell colSpan={2} sx={{ fontWeight: 'bold', fontSize: '16px' }}>Podaci korisnika {korisnik.korisnickoIme}</TableCell>
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
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default KorisnikProfil;
