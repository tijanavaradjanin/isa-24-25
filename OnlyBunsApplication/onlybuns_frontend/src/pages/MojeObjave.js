import React, { useEffect, useState } from "react";
import {  } from "@mui/material";
import { useNavigate } from 'react-router-dom';
import { ThumbUp, ChatBubbleOutline, Room as LocationIcon } from "@mui/icons-material";
import { MapContainer, TileLayer, Marker } from "react-leaflet";
import { Toolbar, Button, CardHeader, CardMedia, CardActions, Avatar, IconButton, Typography, CircularProgress, Card, CardContent, Alert, Box, Modal } from "@mui/material";
import "leaflet/dist/leaflet.css";
import '../css/MojeObjave.css';
import L from 'leaflet';

export default function MojeObjave() {
  const [objave, setObjave] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [adresaZaPrikaz, setAdresaZaPrikaz] = useState("");
  const navigate = useNavigate();
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [korisnik, setKorisnik] = useState(null);
  const [openMap, setOpenMap] = useState(false);

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
        setObjave(data);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });
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

  const handleShowMap = (lat, lng, lokacija) => {
    setSelectedLocation({ lat, lng });
    setAdresaZaPrikaz(lokacija);
    setOpenMap(true);
  };

  const handleCloseMap = () => {
    setOpenMap(false);
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
      minHeight: "80vh",
      py: 2
    }}
    >
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
      <Box sx={{ width: "90%", maxWidth: "700px", margin: "auto", mt: 4 }}>
        {objave.length === 0 ? (
          <Typography variant="h6" textAlign="center">
            Nema objava za prikaz.
          </Typography>
        ) : (
          objave.map((objava) => (
            <Card key={objava.id}
              sx={{ 
              mb: 4, 
              borderRadius: 4,  
              backdropFilter: "blur(8px)",
              backgroundColor: "rgba(255, 255, 255, 0.75)",
              border: "1px solid rgba(255, 255, 255, 0.2)",
              overflow: "hidden",
              boxShadow: 3 }}
            >
              <CardHeader
                avatar={
                  <Avatar sx={{bgcolor: "#1976d2"}}>
                    {objava.korisnickoIme[0].toUpperCase()}
                  </Avatar>
                }
                title={
                  <Typography
                    variant="body1"
                    color="text.primary"
                    sx={{ cursor: "pointer" }}
                    onClick={() => {
                        goToUserProfile(objava.korisnickoIme);
                    }}
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
                      onClick={() => handleShowMap(objava.latituda, objava.longituda, objava.lokacija)}
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

              {/* Dugmad za lajk, komentar i mapu */}
              <CardActions disableSpacing>
                <IconButton >
                  <ThumbUp />
                </IconButton>
                <Typography variant="body2">{objava.lajkovi.length} Lajkova</Typography>

                <IconButton >
                  <ChatBubbleOutline />
                </IconButton>
                <Typography variant="body2">{objava.komentari.length} Komentara</Typography>
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
              width: 800,
              height: 600,
              bgcolor: "white",
              boxShadow: 24,
              p: 2,
              borderRadius: 2
            }}
          >
            <Typography variant="h6" mb={2}>{adresaZaPrikaz}</Typography>
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
}
