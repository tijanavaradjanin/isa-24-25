import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Avatar } from '@mui/material';
import '../css/AzuriranjeProfila.css';
import '../css/Prijavljivanje.css';
import { Box, Button, Checkbox, Typography, Toolbar, Table, TableBody, TableCell, TableContainer, TableRow, Paper, CircularProgress, Alert } from "@mui/material";

const AzuriranjeProfila = () => {
    const navigate = useNavigate();
    const [korisnik, setKorisnik] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showPromenaLozinke, setShowPromenaLozinke] = useState(false);
    const [ime, setIme] = useState("");
    const [prezime, setPrezime] = useState("");
    const [korisnickoIme, setKorisnickoIme] = useState("");
    const [password, setPassword] = useState("");
    const [novaLozinka, setNovaLozinka]=useState("");
    const [potvrdaNoveLozinke, setPotvrdaNoveLozinke] = useState("");
    const [grad, setGrad] = useState("");
    const [drzava, setDrzava] = useState("");
    const [broj, setBroj] = useState("");
    const [info, setInfo] = useState("");
    const [errors, setErrors] = useState({});

    const handleSubmit = (event) => {
        event.preventDefault();
        const updatedUser = { ime, prezime, korisnickoIme, grad, drzava, broj, info };

        if (showPromenaLozinke) {
          // Proveravamo da li su sva tri polja popunjena
          if (!password.trim() || !novaLozinka.trim() || !potvrdaNoveLozinke.trim()) {
              alert("Morate popuniti sva polja za promenu lozinke!"); 
              return; // Zaustavi slanje podataka ako nisu sva polja popunjena
          }
      
          updatedUser.password = password;
          updatedUser.novaLozinka = novaLozinka;
          updatedUser.potvrdaNoveLozinke = potvrdaNoveLozinke;
      }
      
      // Sada šaljemo updatedUser na backend, ali samo ako je validan      
        const token = localStorage.getItem('token');
        console.log('Token:', token);

        if (!token) {
        // Ako token nije pronađen, preusmeravamo na početnu stranicu
        navigate('/');
        return;
        }

        fetch("http://localhost:8080/registrovaniKorisnik/update", {
            method: "PUT",
            headers: { "Content-Type": "application/json",
                      Authorization: `Bearer ${token}`
            },
            body: JSON.stringify(updatedUser),
          })
          .then((response) => {
            return response.text().then((text) => {
              console.log("Odgovor sa backend-a:", text); // Debugging
              if (text.includes("Ponovo se prijavite.")){
                alert("Uspesno azuriranje, preusmeravamo vas na log in...");
                navigate("/prijava");
                localStorage.removeItem("token");
              }
              if (!response.ok) {
                throw new Error(text);
              }
            });
          })
          .catch((error) => {
            try {
              const errorData = JSON.parse(error.message);
              setErrors(errorData);
            } catch {
              setErrors({ global: error.message });
            }
          });
      };

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

    // Fetch funkcija za preuzimanje detalja korisnika
    fetch(`http://localhost:8080/registrovaniKorisnik/profil`, {
        method: 'GET',
        headers: {
        Authorization: `Bearer ${token}`
        },
    })
        .then((response) => {
        if (!response.ok) {
            throw new Error('Greška prilikom preuzimanja podataka korisnika.');
        }
        return response.json();
        })
        .then((data) => {
        setKorisnik(data);
        console.log(korisnik);
        })
        .catch((error) => {
        console.error('Greška:', error.message);
        setError('Došlo je do greške prilikom preuzimanja podataka.');
        })
        .finally(() => {
        setLoading(false);
        });
    }, [navigate]);

    useEffect(() => {
        if (korisnik) {
            setIme(korisnik.ime || "");
            setPrezime(korisnik.prezime || "");
            setKorisnickoIme(korisnik.korisnickoIme || "");
            setGrad(korisnik.grad || "");
            setDrzava(korisnik.drzava || "");
            setBroj(korisnik.broj || "");
            setInfo(korisnik.info || "");
        }
    }, [korisnik]);
    
    const handleLogout = () => {
        localStorage.removeItem('token');
        setKorisnik(null);
        navigate('/');
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

    const handleShowPromenaLozinke = (event) => {
        setShowPromenaLozinke(event.target.checked);
    }

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
        <Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
            <Box sx={{ display: 'flex', gap: '8px', marginLeft: 'auto' }}>
            <Toolbar sx={{ justifyContent: "flex-end" }}>
                 <Button color="primary" onClick={seeMyProfile}>Moj profil</Button>
               <Typography>|</Typography>
                 <Button color="primary" onClick={seeMyPosts}>Moje objave</Button>
                <Typography>|</Typography>
                 <Button color="primary" onClick={showMap}>Prikaz mape</Button>
                <Typography>|</Typography>
                 <Button color="primary" onClick={makeAPost}>+Objava</Button>
                <Typography>|</Typography>
                 <Button color="primary" onClick={handleLogout}>Odjavi se</Button>
              </Toolbar>
            </Box>
          </Box>
    
          <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '60vh', marginBottom: '20px' }}>
            {/* Podaci korisnika */}
            <TableContainer component={Paper} sx={{ maxWidth: 600}}>
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
                <Avatar 
                  src='userrabbit.png' 
                  alt="Profilna slika" 
                  sx={{ width: 100, height: 100, mb: 1, borderRadius: 0, objectFit: "cover", marginTop: 4 }} 
                />
                <Typography variant="h6" sx={{ fontWeight: 'bold', textAlign: 'center' }}>
                    {korisnik.korisnickoIme}
                  </Typography>
                  

              </Box>
              <Table>
                <TableBody>
                  <TableRow>
                    <TableCell>Ime</TableCell>
                    <TableCell>
                        <div>
                    <input
                        type="text"
                        placeholder={korisnik.ime}
                        className={`azuriranje-input ${errors.ime ? "error-border" : ""}`}
                        value={ime}
                        onChange={(e) => setIme(e.target.value)}
                    />
                    {errors.ime && <span className="error-text">{errors.ime}</span>}
                    </div>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Prezime</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="text"
                        placeholder={korisnik.prezime}
                        className={`azuriranje-input ${errors.prezime ? "error-border" : ""}`}
                        value={prezime}
                        onChange={(e) => setPrezime(e.target.value)}
                    />
                      {errors.prezime && <span className="error-text">{errors.prezime}</span>}
                    </div>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Korisničko ime</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="text"
                        placeholder={korisnik.korisnickoIme}
                        className={`azuriranje-input ${errors.korisnickoIme ? "error-border" : ""}`}
                        value={korisnickoIme}
                        onChange={(e) => setKorisnickoIme(e.target.value)}
                    />
                      {errors.korisnickoIme && <span className="error-text">{errors.korisnickoIme}</span>}
                      </div>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Grad</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="text"
                        placeholder={korisnik.grad}
                        className={`azuriranje-input ${errors.grad ? "error-border" : ""}`}
                        value={grad}
                        onChange={(e) => setGrad(e.target.value)}
                    />
                      {errors.grad && <span className="error-text">{errors.grad}</span>}
                    </div>        
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Drzava</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="text"
                        placeholder={korisnik.drzava}
                        className={`azuriranje-input ${errors.drzava ? "error-border" : ""}`}
                        value={drzava}
                        onChange={(e) => setDrzava(e.target.value)}
                    />
                    {errors.drzava && <span className="error-text">{errors.drzava}</span>}
                    </div>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Broj</TableCell>
                    <TableCell>
                    <input
                        type="text"
                        placeholder={korisnik.broj}
                        className={`azuriranje-input ${errors.broj ? "error-border" : ""}`}
                        value={broj}
                        onChange={(e) => setBroj(e.target.value)}
                    />
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Info</TableCell>
                    <TableCell>
                    <input
                        type="text"
                        placeholder={korisnik.info}
                        className={`azuriranje-input ${errors.info ? "error-border" : ""}`}
                        value={info}
                        onChange={(e) => setInfo(e.target.value)}
                    />
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Da li zelite da menjate i lozinku?</TableCell>
                    <TableCell>
                      <Checkbox 
                        checked={showPromenaLozinke} 
                        onChange={handleShowPromenaLozinke} 
                      />
                    </TableCell>
                  </TableRow>
                  {/* ovaj deo ispod treba da se pojavi samo ako je checkbox cekiran*/}
                  {showPromenaLozinke && (
                    <>
                  <TableRow>
                    <TableCell>Trenutna lozinka</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="password"
                        className={`azuriranje-input ${errors.password ? "error-border" : ""}`}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {errors.password && <span className="error-text">{errors.password}</span>}
                    </div>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Nova lozinka</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="password"
                        className={`azuriranje-input ${errors.novaLozinka ? "error-border" : ""}`}
                        value={novaLozinka}
                        onChange={(e) => setNovaLozinka(e.target.value)}
                    />
                    {errors.novaLozinka && <span className="error-text">{errors.novaLozinka}</span>}
                    </div>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Potvrda nove lozinke</TableCell>
                    <TableCell>
                      <div>
                    <input
                        type="password"
                        className={`azuriranje-input ${errors.potvrdaNoveLozinke ? "error-border" : ""}`}
                        value={potvrdaNoveLozinke}
                        onChange={(e) => setPotvrdaNoveLozinke(e.target.value)}
                    />
                    {errors.potvrdaNoveLozinke && <span className="error-text">{errors.potvrdaNoveLozinke}</span>}
                    </div>
                    </TableCell>
                  </TableRow>
                </> )}
                </TableBody>
              </Table>
            </TableContainer>
            <button type="submit" className="azuriranje-button" onClick={handleSubmit}> Potvrdi izmene </button>
            {/* Prikaz globalne greške ispod korisničkog imena i avatara */}
            {errors.global && (
              <div className="error-container show">
                <p className="error-message">{errors.global}</p>
              </div>
            )}
          </Box>
          </Box>
    );

};   

export default AzuriranjeProfila;
