import React, { useEffect} from 'react';
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Box, Button, Alert, Typography } from "@mui/material";
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import ErrorBoundary from './ErrorBoundary'; // Import the ErrorBoundary component

delete L.Icon.Default.prototype._getIconUrl;

const MapaObjava = () => {
  const navigate = useNavigate();
  //const [korisnik, setKorisnik] = useState(null);
  const [error, setError] = useState(null);
  const [location, setLocation] = useState([44.8176, 20.4569]); // Defaultna lokacija (Beograd)

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

   /* fetch(`http://localhost:8080/registrovaniKorisnik/profil`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Neuspešno dobijanje podataka o korisniku.');
        }
        return response.json();
      })
      .then((data) => {
        setKorisnik(data);
        console.log(data);
        // Ovde pretvaramo adresu u geografske koordinate
      // if (korisnik.grad === "Beograd" && korisnik.drzava === "Srbija") {
      //   setLocation([44.8176, 20.4569]); // Beograd
       //} else {
        // setLocation([44.0165, 21.0059]); // Centar Srbije
       // }
      })
      .catch((error) => {
        setError(error.message);
      });*/
  }, [navigate]);
/*
  const handleLogout = () => {
    localStorage.removeItem('token');
   // setKorisnik(null);
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
*/
  L.Icon.Default.mergeOptions({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
  });

  if (error) {
    return (
      <Box mt={4} mx={2}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

 // if (!korisnik) {
 //   return <p>Podaci o korisniku se učitavaju...</p>; // Prikazivanje poruke dok se podaci učitavaju
 // }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <Box sx={{ display: 'flex', gap: '8px', marginLeft: 'auto' }}>
        <Typography variant="h4">
          </Typography>
          <Button variant="contained" color="warning" sx={{ height: '40px' }}>
            moj profil
          </Button>
          <Button variant="contained" color="warning" sx={{ height: '40px' }}>
            moje objave
          </Button>
          <Button variant="contained" color="warning" sx={{ height: '40px' }}>
            +objava
          </Button>
          <Button variant="contained" color="warning" sx={{ height: '40px' }}>
            odjavi se
          </Button>
        </Box>
      </Box>

      {/* Mapa Srbije */}
      <MapContainer center={location} zoom={7} style={{ height: "500px", width: "100%" }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
      </MapContainer>

    </Box>  
  );
};

export default MapaObjava;
