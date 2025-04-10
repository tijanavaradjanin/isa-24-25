import React, { useEffect, useState } from "react";
import { Toolbar, Button, Modal, Card, CardHeader, CardMedia, CardContent, CardActions, Avatar, IconButton, Typography, Box, Dialog } from "@mui/material";
import { ThumbUp, ChatBubbleOutline, Room as LocationIcon } from "@mui/icons-material";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import { useNavigate } from "react-router-dom";
import "leaflet/dist/leaflet.css";
import L from 'leaflet';
import '../css/PocetnaStranica.css'; 

// Komponenta za modal prijavljivanja
const PrijavaModal = ({ open, handleClose, navigate }) => {
  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <Box sx={{ p: 3, textAlign: "center" }}>
        <Typography variant="h6" gutterBottom>Morate se prijaviti</Typography>
        <Typography variant="body2" color="textSecondary">
          Da biste lajkovali ili komentarisali objave, morate biti prijavljeni.
        </Typography>
        <Box sx={{ mt: 2 }}>
          <Button variant="contained" color="primary" fullWidth onClick={() => navigate("/prijava")}>
            Prijavite se
          </Button>
          <Button variant="outlined" color="primary" fullWidth sx={{ mt: 1 }} onClick={handleClose}>
            Zatvori
          </Button>
        </Box>
      </Box>
    </Dialog>
  );
};

const PocetnaStranica = () => {
  const [objave, setObjave] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [openMap, setOpenMap] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [listaKomentara, setListaKomentara] = useState([]);
  const [openKomentari, setOpenKomentari] = useState(false);
  const [objavaId, setObjavaId]=useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/objava/sveobjave")
      .then((response) => response.json())
      .then((data) => {
      setObjave(data);
      console.log("Dobijene objave: ", data);
    })
      .catch((error) => console.error("Greška pri dohvatanju objava:", error));
  }, []);

  const handleLikeClick = () => {
    setOpenModal(true);
  };

  const handleCommentClick = () => {
    setOpenModal(true);
  };

  const handleShowMap = (lat, lng) => {
    setSelectedLocation({ lat, lng });
    setOpenMap(true);
  };
  
  const handleCloseMap = () => {
    setOpenMap(false);
  };

  const goToUserProfile = (korisnickoIme) => {
    navigate(`/korisnikProfil/${korisnickoIme}`);
  };

  const handleOpenKomentari = async (objavaId) => {
    try {
      const url = `http://localhost:8080/objava/komentari?objavaId=${objavaId}`;
      const response = await fetch(url);

      if (!response.ok) throw new Error("Greška sa učitavanjem komentara.");

      const data = await response.json();
      setListaKomentara(data);
      setOpenKomentari(true);
    } catch (error) {
      alert(error.message);
    }
  };

  const handleCloseKomentari = () => setOpenKomentari(false);

  return (
  <div >
    <Box sx={{
    backgroundColor: 'rgba(157, 193, 222, 0.7);',
    backgroundRepeat: "repeat",
    backgroundSize: "contain",
    height: "max-content",
    }}>
      {/* Navigacija */}
      <Toolbar sx={{ justifyContent: "flex-end" }}>
        <Button color="info" onClick={() => navigate("/prijava")}>Prijavite se</Button>
        <Typography>|</Typography>
        <Button color="primary" onClick={() => navigate("/registracija")}>Registrujte se</Button>
      </Toolbar>

      {/* Lista objava */}
      <Box sx={{ width: "90%", maxWidth: "700px", margin: "auto", mt: 4 }}>
        {objave.length === 0 ? (
          <Typography variant="h6" textAlign="center">
            Nema objava za prikaz.
          </Typography>
        ) : (
          objave.map((objava) => (
            <Card key={objava.id} sx={{ mb: 3, boxShadow: 3 }}>
              <CardHeader
                avatar={<Avatar>{objava.korisnickoIme[0]}</Avatar>}
                title={
                  <Typography
                  variant="body1"
                  color="text.primary"
                  font-family= "Helvetica"
                  sx={{ cursor: "pointer" }}
                  onClick={() => goToUserProfile(objava.korisnickoIme)}
                >
                  {objava.korisnickoIme}
                  </Typography>
                }
                subheader={
                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Typography variant="body2" color="text.secondary" font-family= "Helvetica">
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

              {/* Dugmad za lajk, komentar i mapu */}
              <CardActions disableSpacing>
                <IconButton onClick={handleLikeClick}>
                  <ThumbUp />
                </IconButton>
                <Typography variant="body2">{objava.lajkovi.length} lajkova</Typography>

                <IconButton onClick={handleCommentClick}>
                  <ChatBubbleOutline />
                </IconButton>
                   <Typography
                      variant="body2"
                      sx={{ textDecoration: "underline", cursor: "pointer" }}
                      onClick={() => {
                        setObjavaId(objava.id); // Postavljamo ID objave pre otvaranja komentara
                        handleOpenKomentari(objava.id); // Prosleđujemo ID objave
                      }}
                    >
                      {objava.komentari.length} komentara
                   </Typography>
              </CardActions>
            </Card>
          ))
        )}
      </Box>
      
      {/* Modal za prijavu */}
      <PrijavaModal open={openModal} handleClose={() => setOpenModal(false)} navigate={navigate} />

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

      {/* Modal za prikaz komentara */}
            <Modal open={openKomentari} onClose={handleCloseKomentari}>
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
                <Typography variant="h6">Komentari</Typography>
                {listaKomentara.length > 0 ? (
                  listaKomentara.map((komentar) => (
                    <Box key={komentar.id} sx={{ borderBottom: "1px solid #ddd", pb: 1, mb: 1 }}>
                      <Typography variant="body1">
                        <Typography
                          component="span"
                          sx={{ fontWeight: "bold", cursor: "pointer", color: "blue" }}
                          onClick={() => navigate(`/korisnikProfil/${komentar.korisnickoIme}`)}
                        >
                          {komentar.korisnickoIme}
                        </Typography>
                        : {komentar.sadrzaj}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {new Date(
                          komentar.vremeKomentarisanja[0],
                          komentar.vremeKomentarisanja[1] - 1,
                          komentar.vremeKomentarisanja[2],
                          komentar.vremeKomentarisanja[3],
                          komentar.vremeKomentarisanja[4],
                          komentar.vremeKomentarisanja[5]
                        ).toLocaleString()}
                      </Typography>
                    </Box>
                  ))
                ) : (
                  <Typography>Nema komentara.</Typography>
                )}
                <Button variant="outlined" onClick={handleCloseKomentari}>
                  Zatvori
                </Button>
              </Box>
            </Modal>
       </Box>
    </div>
  );
};

export default PocetnaStranica;
