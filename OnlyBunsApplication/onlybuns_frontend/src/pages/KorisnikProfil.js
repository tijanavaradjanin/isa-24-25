import { useParams } from "react-router-dom";
import React, { useEffect, useState } from 'react';
import { cirilicaULatinicu } from '../helpers/PismoKonverter';
import { Box, Typography, Avatar, Table, Button, TableBody, TableCell, CircularProgress, Alert, TableContainer, TableHead, TableRow, Paper} from "@mui/material";

const KorisnikProfil = () => {
  const { korisnickoIme } = useParams(); // Uzima korisničko ime iz URL-a
  const [userProfile, setUserProfile] = useState(null);
  const [korisnik, setKorisnik] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [prati, setPrati] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) return;

    // Provera da li korisnik već prati prikazanog korisnika
    fetch(`http://localhost:8080/pracenje/provera?zapraceni=${korisnickoIme}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => setPrati(data))
      .catch((error) => console.error("Greška prilikom provere praćenja:", error));
  }, [korisnickoIme]);

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

  const handlePracenje = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Morate biti prijavljeni da biste zapratili korisnika.");
      return;
    }

    const url = prati
      ? `http://localhost:8080/pracenje/delete?zapraceni=${encodeURIComponent(korisnickoIme)}`
      : `http://localhost:8080/pracenje/add?zapraceni=${encodeURIComponent(korisnickoIme)}`;
    
    try {
      const response = await fetch(url, {
        method: prati ? "DELETE" : "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Došlo je do greške: ${errorMessage}`);
      }

      setPrati(!prati);
      alert(prati ? "Uspešno ste otpratili korisnika!" : "Uspešno ste zapratili korisnika!");
    } catch (error) {
      alert(error.message);
    }
  };

  return (
    <Box
    sx={{
      background: "linear-gradient(to right,rgb(69, 185, 194), #e3f2fd)",
      minHeight: "80vh",
      py: 2
      }}>
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh', marginTop: '70px' }}>
        <TableContainer component={Paper} sx={{ maxWidth: 600 }}>
            <Button sx={{ marginLeft: '55vh', marginTop: '1vh', fontWeight: 'bold' }} 
              onClick={handlePracenje}
            >
              {prati ? "Otprati" : "Zaprati"}
            </Button>
          <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
            <Avatar 
                src='/userrabbit.png' 
                alt="Profilna slika korisnika" 
                sx={{ width: 100, height: 100, mb: 1, borderRadius: 0, objectFit: "cover", marginTop: 0 }} 
              />
            <Typography variant="h6" sx={{ fontWeight: 'bold', textAlign: 'center' }}>
                {korisnik.korisnickoIme}
              </Typography>
          </Box>
          <Table>
            <TableHead>
            </TableHead>
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
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default KorisnikProfil;
