import React, { useEffect, useState, useRef } from "react";
import { Toolbar, Button, Typography, Box, Dialog } from "@mui/material";
import { useNavigate } from "react-router-dom";
import "leaflet/dist/leaflet.css";
import '../css/PocetnaStranica.css'; 
import Objava from './Objava';

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
  const [openModal, setOpenModal] = useState(null);
  const navigate = useNavigate();
  //const backgroundRef = useRef(null);

  useEffect(() => {
   /* if (openModal) {
      backgroundRef.current?.setAttribute("inert", "true");
    } else {
      backgroundRef.current?.removeAttribute("inert");
    }*/

    fetch("http://localhost:8080/objava/sveobjave")
      .then((response) => response.json())
      .then((data) => {
      setObjave(data);
    })
      .catch((error) => console.error("Greška pri dohvatanju objava:", error));
  }, [openModal]);

  const handleOnUnauthorized = () => {
    setOpenModal(true);
  };

  return (
    <div>
      <Box
        //ref={backgroundRef}
        sx={{
          background: "linear-gradient(to right, rgb(69, 185, 194), #e3f2fd)",
          minHeight: "100vh", // Celo telo stranice
          py: 2,
        }}
      >
        {/* Navigacija */}
        <Toolbar sx={{ justifyContent: "flex-end" }}>
          <Button color="info" onClick={() => navigate("/prijava")}>
            Prijavite se
          </Button>
          <Typography sx={{ mx: 1 }}>|</Typography>
          <Button color="primary" onClick={() => navigate("/registracija")}>
            Registrujte se
          </Button>
        </Toolbar>

        {/* Lista objava */}
        <Box sx={{ width: "90%", maxWidth: "700px", margin: "auto", mt: 4 }}>
          {objave.length === 0 ? (
            <Typography variant="h6" textAlign="center">
              Nema objava za prikaz.
            </Typography>
          ) : (
            objave.map((objava) => (
              <Objava
                key={objava.id}
                objava={objava}
                onUnauthorized={handleOnUnauthorized}
              />
            ))
          )}
        </Box>
      </Box>

    {/* Modal za prijavu */}
    <PrijavaModal
      open={openModal}
      handleClose={() => setOpenModal(false)}
      navigate={navigate}
    />
  </div>
);

};

export default PocetnaStranica;
