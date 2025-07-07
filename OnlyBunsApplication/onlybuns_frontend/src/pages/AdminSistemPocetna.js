import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Box, Toolbar, Typography, Tooltip } from '@mui/material';
import Objava from './Objava';
import { korisnickoImeIzTokena, getToken, parseJwt } from "../helpers/KorisnickoImeIzTokena";

const AdminSistemPocetna = () => {
  const navigate = useNavigate();
  const [, setKorisnik] = useState(null);
  const [objave, setObjave] = useState([]);
  const [, setError] = useState(null);
  const [, setLoading] = useState(false);
  const [selektovaneObjave, setSelektovaneObjave] = useState([]);
  const [selektovanjeAktivno, setSelektovanjeAktivno] = useState(false);

  useEffect(() => {
    const token = getToken();
    if (!token) {
      navigate('/');
      return;
    }

    fetch(`http://localhost:8080/objava/sveobjave`, {
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
      .then((data) => setObjave(data))
      .catch((error) => setError(error.message))
      .finally(() => setLoading(false));

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

  const handleLogout = () => {
    localStorage.removeItem('token');
    setKorisnik(null);
    navigate('/');
  };

  const toggleSelektovanaObjava = (id) => {
    setSelektovaneObjave((prev) =>
      prev.includes(id) ? prev.filter((oid) => oid !== id) : [...prev, id]
    );
  };

  const handleAdvertising = () => {
    const token = getToken();

    fetch("http://localhost:8080/adminsistem/posalji-reklamama", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(selektovaneObjave),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Greška pri slanju objava agencijama");
        return res.text();
      })
      .then((poruka) => {
        alert(poruka);
        setSelektovaneObjave([]);
        setSelektovanjeAktivno(false);
      })
      .catch((err) => {
        alert("Došlo je do greške: " + err.message);
      });
  };

  return (
    <Box
      sx={{
        background: "linear-gradient(to right, rgb(69, 185, 194), #e3f2fd)",
        minHeight: "100vh",
        py: 2,
      }}
    >
      {/* Navigacija */}
      <Toolbar sx={{ justifyContent: "flex-end", gap: 0.5 }}>
        {!selektovanjeAktivno ? (
          <Button onClick={() => setSelektovanjeAktivno(true)}>
            Označi objave za reklame
          </Button>
        ) : (
          <>
            <Button
              variant="outlined"
              onClick={() => {
                setSelektovaneObjave([]);
                setSelektovanjeAktivno(false);
              }}
              sx={{ fontWeight: "normal", textTransform: "uppercase" }}
            >
              Otkaži selekciju
            </Button>

            <Button
              variant="outlined"
              onClick={() => {
                if (selektovaneObjave.length === objave.length) {
                  setSelektovaneObjave([]);
                } else {
                  setSelektovaneObjave(objave.map((o) => o.id));
                }
              }}
              sx={{ fontWeight: "normal", textTransform: "uppercase" }}
            >
              {selektovaneObjave.length === objave.length
                ? `PONIŠTI SVE (${selektovaneObjave.length}/${objave.length})`
                : `OZNAČI SVE (${selektovaneObjave.length}/${objave.length})`}
            </Button>

            <Button
              variant="contained"
              color="info"
              onClick={handleAdvertising}
              disabled={selektovaneObjave.length === 0}
              sx={{ fontWeight: "normal", textTransform: "uppercase" }}
            >
              Pošalji selektovane objave agencijama
            </Button>
          </>
        )}
        <Typography>|</Typography>
        <Tooltip title="Uskoro dostupno" arrow>
          <span>
            <Button color="primary" disabled>
              Trendovi
            </Button>
          </span>
        </Tooltip>
        <Typography>|</Typography>
        <Tooltip title="Uskoro dostupno" arrow>
          <span>
            <Button color="primary" disabled>
              Analitika
            </Button>
          </span>
        </Tooltip>
        <Typography>|</Typography>
        <Tooltip title="Uskoro dostupno" arrow>
          <span>
            <Button color="primary" disabled>
              Korisnici
            </Button>
          </span>
        </Tooltip>
        <Typography>|</Typography>
        <Button color="info" onClick={handleLogout}>
          Odjavi se
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
              adminPogled={true}
              selektovana={selektovaneObjave.includes(objava.id)}
              onToggle={() => toggleSelektovanaObjava(objava.id)}
              selektovanjeAktivno={selektovanjeAktivno}
            />
          ))
        )}
      </Box>
    </Box>
  );
};

export default AdminSistemPocetna;
