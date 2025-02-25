import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ThumbUp, ChatBubbleOutline, Room as LocationIcon } from "@mui/icons-material";
import { MapContainer, TileLayer, Marker } from "react-leaflet";
import Lajkovanje from "./Lajkovanje";
import Komentarisanje from "./Komentarisanje";
import { Toolbar, Button, Card, CardHeader, CardMedia, CardContent, CardActions, Avatar, IconButton, Typography, Box, Modal } from "@mui/material";
import "leaflet/dist/leaflet.css";
import L from 'leaflet';

const PrijavljeniKorisnikPocetna = () => {
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);
  const [openMap, setOpenMap] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [objave, setObjave] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Učitavamo token iz localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      // Ako token nije pronađen, preusmeravamo na početnu stranicu
      navigate('/');
      return;
    }

    fetch(`http://localhost:8080/objava/feed`, {
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
        setObjave(data);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });

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

  const showMap = () => {
    navigate('/prikazMape');
  };

  const goToUserProfile = (korisnickoIme) => {
    navigate(`/korisnikProfil/${korisnickoIme}`);
  };

  const handleShowMap = (lat, lng) => {
    setSelectedLocation({ lat, lng });
    setOpenMap(true);
  };
  
  const handleCloseMap = () => {
    setOpenMap(false);
  };

  return (
      <Box>
        {/* Navigacija */}
        <Toolbar sx={{ justifyContent: "flex-end" }}>
          <Button color="primary" onClick={seeMyProfile}>Moj profil</Button>
          <Typography>|</Typography>
          <Button color="primary" onClick={seeMyPosts}>Moje objave</Button>
          <Typography>|</Typography>
          <Button color="primary" onClick={showMap}>Prikaz mape</Button>
          <Typography>|</Typography>
          <Button color="primary" onClick={makeAPost}>+objava</Button>
          <Typography>|</Typography>
          <Button color="primary" onClick={handleLogout}>Odjavi se</Button>
        </Toolbar>
  
        {/* Lista objava */}
        <Box sx={{ maxWidth: 600, margin: "auto", mt: 4 }}>
          {objave.length === 0 ? (
            <Typography variant="h6" textAlign="center">
              Nema objava za prikaz.
            </Typography>
          ) : (
            objave.map((objava) => (
              <Card key={objava.id} sx={{ mb: 3, boxShadow: 3 }}>
                <CardHeader
                  avatar={<Avatar></Avatar>}
                  title={
                    <Typography
                    variant="body1"
                    color="text.primary"
                    sx={{ cursor: "pointer" }}
                    onClick={() => goToUserProfile(objava.korisnickoIme)}
                  >
                    {objava.korisnickoIme}
                    </Typography>
                  }
                  subheader={
                    <Box sx={{ display: "flex", alignItems: "center" }}>
                      <Typography variant="body2" color="text.secondary">
                        {new Date(
                          objava.vremeKreiranja[0],
                          objava.vremeKreiranja[1] - 1,
                          objava.vremeKreiranja[2],
                          objava.vremeKreiranja[3],
                          objava.vremeKreiranja[4],
                          objava.vremeKreiranja[5]
                        ).toLocaleString()}
                      </Typography>
                      <Typography variant="body2" color="text.secondary"  sx={{ ml: 1 }}>
                        , 
                      </Typography>
                      <Button
                        variant="text"
                        startIcon={<LocationIcon />}
                        onClick={() => handleShowMap(objava.latituda, objava.longituda)}
                        sx={{ color: "grey", textTransform: "none", ml:0, marginRight: 0.5,
                          "& .MuiButton-startIcon": {
                            marginRight: 0.5, // Razmak izmedju ikonice i teksta
                          }
                        }}
                      >
                        Lokacija
                      </Button>
                    </Box>
                  }
                />
  
                {/* Slika objave */}
                {objava.slika && (
                  <CardMedia
                    component="img"
                    height="300"
                    image={`http://localhost:8080/slika/${objava.slika}`}
                    alt="Slika objave"
                    sx={{ objectFit: "cover" }}
                  />
                )}
  
                {/* Opis objave */}
                <CardContent>
                  <Typography variant="body1">{objava.opis}</Typography>
                  <Box sx={{ display: "flex", alignItems: "flex-start", mt: 0 }}></Box>
                </CardContent>
  
                {/* Dugmad za lajk i komentar */}
                <CardActions disableSpacing sx={{ gap: 2, paddingX: 1 }}>
                <Lajkovanje objavaId={objava.id} brojLajkova={objava.lajkovi.length} />
                <Komentarisanje objavaId={objava.id} brojKomentara={objava.komentari.length} />
                </CardActions>
              </Card>
            ))
          )}
        </Box>
  
        {openMap && (
          <Modal open={openMap} onClose={handleCloseMap}>
            <Box
              sx={{
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                width: 600,
                height: 400,
                bgcolor: "white",
                boxShadow: 24,
                p: 2,
                borderRadius: 2
              }}
            >
              <Typography variant="h6" mb={2}>Lokacija objave</Typography>
              <div style={{ height: "100%", width: "100%" }}>
                <MapContainer
                  center={[selectedLocation?.lat, selectedLocation?.lng]}
                  zoom={13}
                  style={{ height: "80%", width: "100%" }}
                >
                  <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                  />
                  <Marker
                    position={[selectedLocation?.lat, selectedLocation?.lng]}
                    icon={new L.Icon({
                      iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
                      iconSize: [25, 41],
                      iconAnchor: [12, 41],
                      popupAnchor: [1, -34],
                      shadowSize: [41, 41],
                    })}
                  >
                  </Marker>
                </MapContainer>
              </div>
            </Box>
          </Modal>
        )}
  
        
      </Box>
    );
  
  
};

export default PrijavljeniKorisnikPocetna;
