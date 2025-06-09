import { useEffect, useState } from "react";
import { Box, CircularProgress, Typography, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";
import Objava from "./Objava";
import Navigacija from './Navigacija'; 
import { korisnickoImeIzTokena, getToken } from "../helpers/KorisnickoImeIzTokena";

export default function MojeObjave() {
  const [objave, setObjave] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [, setKorisnik] = useState(korisnickoImeIzTokena());
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    const korisnickoIme = korisnickoImeIzTokena();

    if (!token || !korisnickoIme) {
      navigate("/");
      return;
    }

    fetch(`http://localhost:8080/objava/mojeObjave`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Greška prilikom preuzimanja objava.");
        return res.json();
      })
      .then(setObjave)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [navigate]);

  return (
    <Box
      sx={{
        background: "linear-gradient(to right, rgb(69, 185, 194), #e3f2fd)",
        minHeight: "100vh",
        py: 2,
      }}
    >

      <Navigacija setKorisnik={setKorisnik} />
    
      <Box sx={{ width: "90%", maxWidth: "700px", margin: "auto", mt: 4 }}>
        {loading ? (
          <Box display="flex" justifyContent="center" alignItems="center" height="50vh">
            <CircularProgress />
          </Box>
        ) : error ? (
          <Alert severity="error">{error}</Alert>
        ) : objave.length === 0 ? (
          <Typography variant="h6" textAlign="center">
            Nema objava za prikaz.
          </Typography>
        ) : (
          objave.map((objava) => <Objava key={objava.id} objava={objava} />)
        )}
      </Box>
    </Box>
  );
}
