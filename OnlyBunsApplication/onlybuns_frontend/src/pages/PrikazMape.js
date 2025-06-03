import React, { useEffect, useState, useRef } from 'react';
import { Box, Typography, Modal, Card, CardContent, CardActions, CardMedia, CardHeader, Avatar } from "@mui/material";
import { useNavigate } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Popup} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import Lajkovanje from "./Lajkovanje";
import Komentarisanje from "./Komentarisanje";
import { cirilicaULatinicu } from '../helpers/PismoKonverter.js';
import L from 'leaflet';

const PrikazMape = () => {
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);
  const [location, setLocation] = useState(null);
  const [post, setPost] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [locationMessage, setLocationMessage] = useState(null);
  const [nearbyPosts, setNearbyPosts] = useState([]); // Dodato: Držimo obližnje objave
  const mapRef = useRef(null);

  const getCoordinates = async (fullAddress) => {
    const response = await fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(fullAddress)}&format=json`);
    const data = await response.json();
    return data?.[0] ? [parseFloat(data[0].lat), parseFloat(data[0].lon)] : null;
  };

  const handleOpenPost = (post) => {
   setPost(post);
   setModalOpen(true);
  };

  const handleCloseModal = () => {
   setModalOpen(false);
   setPost(null);
  };

  const goToUserProfile = (korisnickoIme) => {
    navigate(`/korisnikProfil/${korisnickoIme}`);
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const korisnickoIme = localStorage.getItem('korisnickoIme');

    if (!token || !korisnickoIme) {
      navigate('/');
      return;
    }

    fetch(`http://localhost:8080/registrovaniKorisnik/profil`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(response => response.ok ? response.json() : Promise.reject('Neuspešno dobijanje podataka'))
      .then(data => {
        setKorisnik(data);
        console.log("Korisnik podaci:", data);

        if (data.adresa) {
          getCoordinates(data.adresa).then(coords => {
            if (coords) {
              setLocation(coords);
              setLocationMessage(null);
              if (mapRef.current) {
                mapRef.current.setView(coords, 12);
              }
              fetchNearbyPosts(token); // Pozivamo funkciju da dohvatimo obližnje objave
            } else {
              setLocationMessage("Vaša lokacija je nepostojeća, molimo Vas ažurirajte je.");
            }
          });
        } else {
          setLocationMessage("Vaša lokacija je nepostojeća, molimo Vas ažurirajte je.");
        }
      })
      .catch(error => {
        console.error('Greška:', error);
        setLocationMessage("Vaša lokacija je nepostojeća, molimo Vas ažurirajte je.");
      });
  }, []);

  const fetchNearbyPosts = async (token) => {
    try {
      const response = await fetch(`http://localhost:8080/objava/obliznjeObjave`, {
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.ok) {
        const data = await response.json();
        setNearbyPosts(data);
        console.log(data);
      } else {
        console.error("Greška pri dohvatanju obližnjih objava");
      }
    } catch (error) {
      console.error("Greška:", error);
    }
  };

  const userIcon = new L.Icon({
    iconUrl: require('leaflet/dist/images/kucica.png'),
    iconSize: [40, 42],
    iconAnchor: [12, 41],
    popupAnchor: [8, -34]
  });

  const nearbyPostIcon = new L.Icon({
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    iconSize: [25, 41],
    iconAnchor: [12, 38],
    popupAnchor: [1, -34]
  });
  

  return (
    <Box position="relative">
      {location ? (
        <MapContainer
          center={location}
          zoom={12}
          style={{ height: "calc(100vh - 100px)", width: "100%" }}
          ref={mapRef}
        >
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          
        <Marker position={location} icon={userIcon}>
          <Popup>
            <Box>
              <Typography variant="body2">
                {cirilicaULatinicu(korisnik?.adresa) || "Nepoznata lokacija"}
              </Typography>
            </Box>
          </Popup>
        </Marker>

          {/* Obližnje objave */}
          {nearbyPosts.map((post, index) => (
            <Marker key={index} position={[post.latituda, post.longituda]} icon={nearbyPostIcon}>
              <Popup>
                <Box sx={{ p: 0 }} >
                  <Typography
                  variant="body2">
                    {cirilicaULatinicu(post.lokacija)}
                  </Typography>
                  <Typography 
                    variant="body2" 
                    sx={{ cursor: 'pointer', color: 'primary.main' }} 
                    onClick={() => handleOpenPost(post)}
                  >
                    <strong>Prikaži objavu</strong>
                  </Typography>
                </Box>
              </Popup>
            </Marker>
          ))}

          {/* Modal za selektovanu objavu */}
          {post && ( <>
          {console.log("POST ZA MODAL:", post)}
          <Modal open={modalOpen} onClose={handleCloseModal}>
            <Card 
              key={post.id}
              sx={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              width: 600,
              boxShadow: 6,
              outline: 'none',
              borderRadius: '12px'
              }}
            >
                <CardHeader
                  avatar={<Avatar sx={{bgcolor: "#1976d2"}}>
                    {post.korisnickoIme[0].toUpperCase()}
                  </Avatar>}
                  title={
                    <Typography
                    variant="body1"
                    color="text.primary"
                    sx={{ cursor: "pointer" }}
                    onClick={() => goToUserProfile(post.korisnickoIme)}
                  >
                    {post.korisnickoIme}
                    </Typography>
                 }
                 subheader={
                    <Box sx={{ display: "flex", alignItems: "center" }}>
                      <Typography variant="body2" color="text.secondary">
                        {new Date(
                          post.vremeKreiranja[0],
                          post.vremeKreiranja[1] - 1,
                          post.vremeKreiranja[2],
                          post.vremeKreiranja[3],
                          post.vremeKreiranja[4],
                          post.vremeKreiranja[5]
                        ).toLocaleString()}
                      </Typography>
                    </Box>
                  }
                />
  
                {/* Slika objave */}
                {post.slika && (
                  <CardMedia
                    component="img"
                    height="300"
                    image={`http://localhost:8080/slika/${post.slika}`}
                    alt="Slika objave"
                    sx={{ objectFit: "cover" }}
                  />
                )}
  
                {/* Opis objave */}
                <CardContent>
                  <Typography variant="body1">{post.opis}</Typography>
                  <Box sx={{ display: "flex", alignItems: "flex-start", mt: 0 }}></Box>
                </CardContent>
  
                {/* Dugmad za lajk i komentar */}
                <CardActions disableSpacing sx={{ gap: 2, paddingX: 1 }}>
                <Lajkovanje objavaId={post.id} brojLajkova={post.lajkovi.length} />
                <Komentarisanje objavaId={post.id} brojKomentara={post.komentari.length} />
                </CardActions>
              </Card>
          </Modal>
          </>
          )}
        </MapContainer>
    ) : (
        <Typography textAlign="center" p={3}>Učitavanje mape...</Typography>
      )}

      {locationMessage && (
        <Box 
          position="absolute"
          top={0}
          left={0}
          width="100%"
          height="100%"
          bgcolor="rgba(0,0,0,0.5)"
          display="flex"
          alignItems="center"
          justifyContent="center"
          zIndex={1000}
        >
          <Typography 
            bgcolor="white" 
            color="black" 
            p={2} 
            borderRadius={2} 
            fontSize={18}
            textAlign="center"
            boxShadow={3}
          >
            {locationMessage}
          </Typography>
        </Box>
      )}
    </Box>
  );
};

export default PrikazMape;
