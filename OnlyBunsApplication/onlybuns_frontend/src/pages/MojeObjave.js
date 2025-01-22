import React, { useEffect, useState } from "react";
import { Box, Typography, CircularProgress, Card, CardContent, CardMedia, Grid, Alert } from "@mui/material";
import { useLocation, useNavigate } from 'react-router-dom';
import '../css/MojeObjave.css';

export default function MojeObjave() {
  const [objave, setObjave] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

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

    fetch(`http://localhost:8080/objava/mojeObjave`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Greška prilikom preuzimanja objava.");
        }
        return response.json();
      })
      .then((data) => {
        console.log(data);
        setObjave(data.sort((a, b) => new Date(b.vremeKreiranja) - new Date(a.vremeKreiranja)));
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [navigate]);

  const formatDate = (dateArray) => {
    // Kreiraj Date objekat iz niza
    const dateObj = new Date(...dateArray);
  
    // Proveri da li je datum validan
    if (dateObj.getTime()) {
      return dateObj.toLocaleString(); // Formatiraj i vrati datum
    } else {
      return 'Nevalidan datum';
    }
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
    <Box sx={{ padding: 4 }}>
      <Typography variant="h4" gutterBottom textAlign="center">
        Moje objave
      </Typography>
      {objave.length === 0 ? (
        <Typography textAlign="center" color="textSecondary">
          Nemate kreiranih objava.
        </Typography>
      ) : (
        <Grid container spacing={4}>
          {objave.map((objava) => (
            <Grid item xs={12} sm={6} md={4} key={objava.id}>
              <Card sx={{ maxWidth: 345, margin: "auto" }}>
                {objava.slika && (
                  <CardMedia
                    component="img"
                    height="200"
                    image={`http://localhost:8080/slika/${objava.slika}`}
                    alt="Slika objave"
                    sx={{ objectFit: "cover" }}
                  />
                )}
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    {objava.opis}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Latituda: {objava.latituda}, Longituda: {objava.longituda}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Kreirano: {formatDate(objava.vremeKreiranja)}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Broj lajkova: {objava.brojLajkova || 0}, Broj komentara: {objava.brojKomentara || 0}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
}
