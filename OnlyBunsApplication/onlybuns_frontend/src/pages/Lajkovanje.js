import React, { useEffect, useState } from "react";
import { IconButton, Modal, Box, Typography, Button } from "@mui/material";
import { ThumbUp } from "@mui/icons-material";
import { getToken } from "../helpers/KorisnickoImeIzTokena";

const Lajkovanje = ({ objavaId, brojLajkova, onUnauthorized }) => {
  const [lajkovano, setLajkovano] = useState(false);
  const [lajkovi, setLajkovi] = useState(brojLajkova);
  const [openLajkovi, setOpenLajkovi] = useState(false);
  const [listaLajkova, setListaLajkova] = useState([]);

  useEffect(() => {
    const token = getToken();
    if (!token) return;

    fetch(`http://localhost:8080/objava/jeLajkovao?objavaId=${objavaId}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(res => res.ok ? res.json() : Promise.reject("Greška prilikom provere lajkova"))
      .then(data => setLajkovano(data))
      .catch(console.error);
  }, [objavaId]);

  const handleLike = async () => {
    const token = getToken();
    if (!token) {
      onUnauthorized?.();
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/objava/lajkuj?objavaId=${objavaId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 401) {
        onUnauthorized?.();
        return;
      }

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      setLajkovano(true);
      setLajkovi(prev => prev + 1);
    } catch (error) {
      alert(error.message);
    }
  };

  const handleDislike = async () => {
    const token = getToken();
    if (!token) {
      onUnauthorized?.();
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/objava/dislajkuj?objavaId=${objavaId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      setLajkovano(false);
      setLajkovi(prev => prev - 1);
    } catch (error) {
      alert(error.message);
    }
  };

  const toggleLike = () => {
    if (lajkovano) {
      handleDislike();
    } else {
      handleLike();
    }
  };

  const handleOpenLajkovi = async () => {
    const token = getToken();
    if (!token) {
      alert("Morate biti prijavljeni da biste videli ko je lajkovao.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/objava/lajkovi?objavaId=${objavaId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Ne možete videti lajkove jer ne pratite korisnika.");
      }

      const data = await response.json();
      setListaLajkova(data);
      setOpenLajkovi(true);
    } catch (error) {
      alert(error.message);
    }
  };

  const handleCloseLajkovi = () => setOpenLajkovi(false);

  return (
    <>
      <Box sx={{ display: "flex", alignItems: "center", gap: 0 }}>
        <IconButton onClick={toggleLike}>
          <ThumbUp color={lajkovano ? "primary" : "inherit"} />
        </IconButton>
        <Typography
          variant="body2"
          sx={{ textDecoration: "underline", cursor: "pointer" }}
          onClick={handleOpenLajkovi}
        >
          {lajkovi} lajkova
        </Typography>
      </Box>

      {/* Modal za lajkove */}
      <Modal open={openLajkovi} onClose={handleCloseLajkovi}>
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
          <Typography variant="h6">Lajkovali</Typography>
          {listaLajkova.length > 0 ? (
            listaLajkova.map((lajk) => (
              <Box key={lajk.id} sx={{ borderBottom: "1px solid #ddd", pb: 1, mb: 1 }}>
                <Typography variant="body1">
                  <strong>{lajk.korisnickoIme}</strong>
                </Typography>
                <Typography variant="caption" color="gray">
                  {new Date(
                    lajk.vremeLajkovanja[0],
                    lajk.vremeLajkovanja[1] - 1,
                    lajk.vremeLajkovanja[2],
                    lajk.vremeLajkovanja[3],
                    lajk.vremeLajkovanja[4],
                    lajk.vremeLajkovanja[5]
                  ).toLocaleString()}
                </Typography>
              </Box>
            ))
          ) : (
            <Typography>Nema lajkova.</Typography>
          )}
          <Button variant="outlined" onClick={handleCloseLajkovi}>
            Zatvori
          </Button>
        </Box>
      </Modal>
    </>
  );
};

export default Lajkovanje;
