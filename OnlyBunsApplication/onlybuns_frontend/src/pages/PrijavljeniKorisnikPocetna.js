import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Typography, Box } from "@mui/material";
import Objava from './Objava';
import Navigacija from './Navigacija';
import { korisnickoImeIzTokena, getToken, parseJwt } from "../helpers/KorisnickoImeIzTokena"; 

const PrijavljeniKorisnikPocetna = () => {
  const navigate = useNavigate();
  const [, setKorisnik] = useState(null);
  const [objave, setObjave] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = getToken();
    if (!token) {
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
        setObjave(data);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });

    try {
      const korisnickoIme = korisnickoImeIzTokena();
      const payload = parseJwt();

      if (!korisnickoIme || !payload) throw new Error("Token nije validan");

      setKorisnik({ korisnickoIme, ...payload });
      localStorage.setItem("korisnickoIme", korisnickoIme);
    } catch (err) {
      
      localStorage.removeItem("token");
      navigate('/');
  }
  }, [navigate]);

  return (
    <Box
      sx={{
        background: "linear-gradient(to right,rgb(69, 185, 194), #e3f2fd)",
        minHeight: "100vh",
        py: 2
      }}
    >
      <Navigacija setKorisnik={setKorisnik} />

      {/* Lista objava */}
      <Box sx={{ maxWidth: 700, margin: "auto", mt: 4 }}>
        {loading ? (
          <Typography variant="h6" textAlign="center">Učitavanje objava...</Typography>
        ) : error ? (
          <Typography variant="h6" color="error" textAlign="center">{error}</Typography>
        ) : objave.length === 0 ? (
          <Typography variant="h6" textAlign="center">Nema objava za prikaz.</Typography>
        ) : (
          objave.map((objava) => (
            <Objava
              key={objava.id}
              objava={objava}
            />
          ))
        )}
      </Box>
    </Box>
  );
};

export default PrijavljeniKorisnikPocetna;