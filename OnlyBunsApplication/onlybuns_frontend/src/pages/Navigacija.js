import { useNavigate } from 'react-router-dom';
import { Button, Toolbar, Typography } from '@mui/material';

const Navigacija = ({ setKorisnik }) => {
  const navigate = useNavigate();

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

  return (
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
  );
};

export default Navigacija;
