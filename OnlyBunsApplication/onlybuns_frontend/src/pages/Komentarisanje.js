import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../helpers/KorisnickoImeIzTokena";
import { IconButton, Modal, Box, Typography, TextField, Button } from "@mui/material";
import { ChatBubbleOutline } from "@mui/icons-material";

const Komentarisanje = ({ objavaId, brojKomentara, onUnauthorized }) => {
  const [open, setOpen] = useState(false);
  const [openKomentari, setOpenKomentari] = useState(false);
  const [komentar, setKomentar] = useState("");
  const [komentari, setKomentari] = useState(brojKomentara);
  const [listaKomentara, setListaKomentara] = useState([]);
  const navigate = useNavigate();
  const inputRef = useRef(null);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleOpenKomentari = async () => {
    try {
      const token = getToken();
      const url = `http://localhost:8080/objava/komentari?objavaId=${objavaId}`;

      const response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!response.ok) {
        throw new Error("Greška sa učitavanjem komentara.");
      }

      const data = await response.json();
      setListaKomentara(data);
      setOpenKomentari(true);
    } catch (error) {
      alert(error.message);
    }
  };

  useEffect(() => {
    if (open && inputRef.current) {
      inputRef.current.focus();
    }
  }, [open]);

  const handleCloseKomentari = () => setOpenKomentari(false);

  const handleKomentarisi = async () => {
    const token = getToken();
    if (!token) {
      onUnauthorized?.()
      return;
    }

    const url = `http://localhost:8080/objava/komentarisi?objavaId=${objavaId}&sadrzaj=${encodeURIComponent(komentar)}`;

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Došlo je do greške: ${errorMessage}`);
      }

      alert("Uspešno ste komentarisali!");
      setKomentar("");
      setKomentari((prev) => prev + 1);
      handleClose();
    } catch (error) {
      alert(error.message);
    }
  };

  return (
    <>
      {/* Ikonica i broj komentara u istoj liniji */}
      <Box sx={{ display: "flex", alignItems: "center", gap: 0 }}>
        <IconButton onClick={handleOpen}>
          <ChatBubbleOutline />
        </IconButton>
        <Typography
          variant="body2"
          sx={{ textDecoration: "underline", cursor: "pointer" }}
          onClick={handleOpenKomentari}
        >
          {komentari} komentara
        </Typography>
      </Box>

      {/* Modal za unos komentara */}
      <Modal open={open} onClose={handleClose}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "white",
            boxShadow: 24,
            p: 3,
            borderRadius: 2,
            display: "flex",
            flexDirection: "column",
            gap: 2
          }}
            tabIndex={-1}  // da Box može primiti fokus
            ref={el => {
            if (openKomentari && el) el.focus();
          }}
        >
          <Typography variant="h6">Ostavite komentar</Typography>
          <TextField
            multiline
            rows={3}
            fullWidth
            variant="outlined"
            placeholder="Unesite komentar..."
            value={komentar}
            onChange={(e) => setKomentar(e.target.value)}
            inputRef={inputRef}
          />
          <Button variant="contained" color="primary" onClick={handleKomentarisi}>
            Komentariši
          </Button>
        </Box>
      </Modal>

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
    </>
  );
};

export default Komentarisanje;
