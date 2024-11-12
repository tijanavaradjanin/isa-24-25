import * as React from 'react';
import{ useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';



const defaultTheme = createTheme();

export default function Registrovanje() {
  const[ime,setIme]=useState('')
  const[prezime,setPrezime]=useState('')
  const[grad,setGrad]=useState('')
  const[drzava,setDrzava]=useState('')
  const[broj,setBroj]=useState('')
  const [brojError, setBrojError] = useState(false);
  const[info,setInfo]=useState('')
  const[email,setEmail]=useState('')
  const [emailError, setEmailError] = useState(false);
  const[password,setPassword]=useState('')
  const [repeatPassword, setRepeatPassword] = useState(''); // Dodato stanje za ponovljenu lozinku
  const [passwordMismatch, setPasswordMismatch] = useState(false); // Dodato stanje za praćenje neuspešnog podudaranja
  const [errorMessage, setErrorMessage] = useState('');

  const validateBroj = (inputBroj) => {
    const brojRegex = /^\d{10}$/; // Regex za desetocifren broj
    return brojRegex.test(inputBroj);
  };
  const handleBrojChange = (e) => {
    const newBroj = e.target.value;
    setBroj(newBroj);
  
    // Provera formata broja telefona
    setBrojError(!validateBroj(newBroj));
  };

  // Funkcija za proveru formata e-mail adrese
  const validateEmail = (inputEmail) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Regex za proveru formata e-mail adrese
    return emailRegex.test(inputEmail);
  };
  // Event handler za promenu vrednosti e-mail adrese
  const handleEmailChange = (e) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
  
    // Provera formata e-mail adrese
    setEmailError(!validateEmail(newEmail));
  };  

  const checkEmailExists = async () => {
    try {
      const response = await fetch("http://localhost:8080/registrovaniKorisnik/emails");

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      return data.includes(email);
    } catch (error) {
      console.error("Error checking email:", error);
      return false;
    }
  };

  
  const handleSubmit = async (event) => {
    event.preventDefault();
    // Provera podudaranja lozinki
    if (password !== repeatPassword) {
      setPasswordMismatch(true);
      return;
    }

    if (!validateBroj(broj)) {
      setErrorMessage('Unesite ispravan broj telefona.');
      return;
    }

    if (!ime || !prezime || !grad || !drzava || !broj || !info || !email || !password || !repeatPassword) {
      setErrorMessage('Podaci nisu dobro popunjeni.');
      return;
    }

    const emailExists = await checkEmailExists();
    if (emailExists) {
      setErrorMessage('Email adresa već postoji.');
      return;
    }
    const korisnik = {ime, prezime, grad, drzava, broj, email, password, info}
    console.log(korisnik);
    
    setErrorMessage('');


    fetch("http://localhost:8080/registrovaniKorisnik/add",{
      method:"POST",
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify(korisnik)

     }).then(()=>{
        console.log("Novi korisnik dodat")
     })
};

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Registruj se
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="ime"
              label="Ime"
              name="ime"
              autoComplete="ime"
              autoFocus
              value={ime}
              onChange={(e)=>setIme(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="prezime"
              label="Prezime"
              name="prezime"
              autoComplete="prezime"
              value={prezime}
              onChange={(e)=>setPrezime(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              value={email}
              onChange={handleEmailChange}
              error={emailError}
              helperText={emailError ? 'Unesite validnu e-mail adresu' : ''}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Lozinka"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
            onChange={(e)=>setPassword(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="repeatPassword"
              label="Ponovi Lozinku"
             type="password"
             id="repeatPassword"
             autoComplete="current-password"
             value={repeatPassword}
             onChange={(e) => {
              setRepeatPassword(e.target.value);
              setPasswordMismatch(false); // Resetujte stanje neuspešnog podudaranja kada korisnik menja ponovljenu lozinku
              }}
            />
            {passwordMismatch && (
              <Typography color="error" variant="body2" gutterBottom>
                Lozinke se ne podudaraju.
              </Typography>
            )}
            <TextField
              margin="normal"
              required
              fullWidth
              id="grad"
              label="Grad"
              name="grad"
              autoComplete="grad"
              value={grad}
              onChange={(e)=>setGrad(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="drzava"
              label="Drzava"
              name="drzava"
              autoComplete="drzava"
              value={drzava}
              onChange={(e)=>setDrzava(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="broj"
              label="Broj Telefona"
              name="broj"
              autoComplete="broj"
              value={broj}
              onChange={handleBrojChange}
              error={brojError}
              helperText={brojError ? 'Unesite desetocifren broj' : ''}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="info"
              label="Dodatne infomracije"
              name="info"
              autoComplete="info"
              value={info}
              onChange={(e)=>setInfo(e.target.value)}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Registruj se
            </Button>
            {errorMessage && (
              <Typography color="error" variant="body2" gutterBottom>
                {errorMessage}
              </Typography>
            )}
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}