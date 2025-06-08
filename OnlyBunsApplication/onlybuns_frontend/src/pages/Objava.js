import { useNavigate } from "react-router-dom";
import React, { useState } from "react";
import { Button, Card, CardHeader, CardMedia, CardContent, CardActions, Avatar, Typography, Box, Modal } from "@mui/material";
import { Room as LocationIcon} from "@mui/icons-material";
import { MapContainer, TileLayer, Marker } from "react-leaflet";
import { cirilicaULatinicu } from '../helpers/PismoKonverter';
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import Lajkovanje from "./Lajkovanje";
import Komentarisanje from "./Komentarisanje";

// Ikonica za marker da bi se prikazivala korektno
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png",
});

const Objava = ({ objava,  onUnauthorized }) => {
  const [openMap, setOpenMap] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [adresaZaPrikaz, setAdresaZaPrikaz] = useState("");
  const navigate = useNavigate();

  const handleShowMap = (lat, lng, lokacija) => {
    setSelectedLocation({ lat, lng });
    setAdresaZaPrikaz(lokacija || "Nepoznata lokacija");
    setOpenMap(true);
  };

  const handleCloseMap = () => {
    setOpenMap(false);
  };

  const goToUserProfile = (korisnickoIme) => {
    navigate(`/korisnikProfil/${korisnickoIme}`);
  };

  return (
    <>
      <Card sx={{ mb: 4, width: "700px", borderRadius: 4, backgroundColor: "rgba(255, 255, 255, 1)", boxShadow: 3 }}>
        <CardHeader
          avatar={<Avatar sx={{ bgcolor: "#1976d2" }}>{objava.korisnickoIme[0].toUpperCase()}</Avatar>}
          title={
            <Typography
              variant="body1"
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
              <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }}>
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

        {objava.slika && (
          <CardMedia
            component="img"
            height="300"
            image={`http://localhost:8080/slika/${objava.slika}`}
            alt="Slika objave"
            sx={{ objectFit: "cover" }}
          />
        )}

        <CardContent>
          <Typography variant="body1">{objava.opis}</Typography>
        </CardContent>

        <CardActions disableSpacing sx={{ gap: 2, paddingX: 1 }}>
              <Lajkovanje objavaId={objava.id} brojLajkova={objava.lajkovi.length} onUnauthorized={onUnauthorized}/>
              <Komentarisanje objavaId={objava.id} brojKomentara={objava.komentari.length} onUnauthorized={onUnauthorized}/>
        </CardActions>
      </Card>

      {/* Modal za mapu */}
      <Modal open={openMap} onClose={handleCloseMap}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 700,
            height: 500,
            bgcolor: "background.paper",
            border: "2px solid #ccc",
            boxShadow: 24,
            p: 2,
            borderRadius: 2,
          }}
        >
          <Typography variant="h6" gutterBottom>
            Lokacija: {cirilicaULatinicu(adresaZaPrikaz)}
          </Typography>
          {selectedLocation && (
            <MapContainer
              center={[selectedLocation.lat, selectedLocation.lng]}
              zoom={13}
              style={{ height: "400px", width: "100%"}}
            >
              <TileLayer
                attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />
              <Marker position={[selectedLocation.lat, selectedLocation.lng]} />
            </MapContainer>
          )}
        </Box>
      </Modal>
    </>
  );
};

export default Objava;
