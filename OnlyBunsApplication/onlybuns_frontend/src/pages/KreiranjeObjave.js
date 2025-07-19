import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
import { Box } from '@mui/material';
import 'leaflet/dist/leaflet.css';
import '../css/KreiranjeObjave.css';
import L from 'leaflet';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import Navigacija from './Navigacija'; 
import { korisnickoImeIzTokena, getToken } from "../helpers/KorisnickoImeIzTokena";

const customIcon = new L.Icon({
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});

export default function KreiranjeObjave() {
  const [opis, setOpis] = useState('');
  const [grad, setGrad] = useState('');
  const [ulica, setUlica]=useState('');
  const [broj, setBroj]=useState('');
  const [drzava, setDrzava] = useState('');
  const [slika, setSlika] = useState(null);
  const [error, setError] = useState('');
  const [, setKorisnik] = useState(korisnickoImeIzTokena());
  const [loading, setLoading] = useState(false);
  const [location, setLocation] = useState(null); // Koordinate sa mape
  const [mapEnabled, setMapEnabled] = useState(true); // Da li je mapa aktivna
  const [preview, setPreview] = useState(null); // State za prikaz slike

  const navigate = useNavigate();

   const resetLocation = () => {
    setLocation(null);
    setGrad('');
    setDrzava('');
    setUlica('');
    setBroj('');
  }; 

  const handleSlikaChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSlika(file);
      setPreview(URL.createObjectURL(file)); // Generiši privremeni URL za prikaz slike
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if ((!opis || !slika) || (!grad && !drzava && !ulica && !broj && !location)) {
      setError('Morate popuniti opis i dodati sliku, uz ručni unos lokacije ili mapu.');
      return;
    }

    setError('');
    setLoading(true);

    const token = getToken();
    if (!token) {
      setError('Morate biti prijavljeni da biste kreirali objavu.');
      setLoading(false);
      return;
    }

    const korisnickoIme = korisnickoImeIzTokena();

    const formData = new FormData();
    formData.append("opis", opis);
    formData.append("slika", slika);
    formData.append("korisnickoIme", korisnickoIme);
    if (grad && drzava && ulica && broj) {
      formData.append("grad", grad);
      formData.append("drzava", drzava);
      formData.append("ulica", ulica);
      formData.append("broj", broj);
  } else if (location) {
      formData.append("latitude", location.lat);
      formData.append("longitude", location.lng);
  } else {
    setError("Morate uneti adresu ili odabrati lokaciju na mapi.");
    return;
   }

    fetch("http://localhost:8080/objava/add", {
      method: "POST",
      headers: { "Authorization": `Bearer ${token}` },
      body: formData,
    })
      .then((response) => response.ok ? response.json() : response.json().then(data => { throw new Error(data.message || 'Greška pri kreiranju objave.'); }))
      .then((data) => {
        navigate('/mojeObjave');
      })
      .catch((error) => {
        setError(error.message || "Kreiranje objave nije uspelo. Proverite validnost unosa.");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // Omogućava korisniku da klikne na mapu i postavi lokaciju
  function LocationMarker() {
    useMapEvents({
      click(e) {
        if (!mapEnabled) return; // Ako je mapa isključena, ne dozvoljava klikove
        setLocation(e.latlng);
        setGrad('');
        setDrzava('');
      },
    });

    return location ? <Marker position={location} icon={customIcon} /> : null;
  }

  return (
    <div style={{ position: "relative" }}>
      <div style={{ position: "absolute", top: 16, right: 0, zIndex: 10 }}>
        <Navigacija setKorisnik={setKorisnik} sx={{ fontSize: "1.6rem" }}/>
    </div>
      <Box sx={{
        background: "linear-gradient(to right,rgb(69, 185, 194), #e3f2fd)",
        width: "99vw",
        minHeight: "100vh",
        py: 4,
        display: "flex",
        justifyContent: "center",
        alignItems: "center"
      }}>
      <form className="kreiranje-objave-form" onSubmit={handleSubmit}>
        <h1 className="kreiranje-objave-title">Kreiranje objave</h1>
        {error && <p className="error-message">{error}</p>}
        
        <textarea
          placeholder="Opis"
          className="kreiranje-objave-input"
          value={opis}
          onChange={(e) => setOpis(e.target.value)}
        />
        <input
          type="file"
          className="kreiranje-objave-input"
          onChange={handleSlikaChange}
        />
        {/* Prikaz izabrane slike */}
        {preview && (
        <div className="image-preview-container" style={{ marginTop: '16px' }}>
          <img src={preview} alt="Preview" className="image-preview" />
        </div>
        )}

        {/* Ručni unos grada i države */}
        <input
          type="text"
          placeholder="Grad"
          className="kreiranje-objave-input"
          value={grad}
          onChange={(e) => {
            setGrad(e.target.value);
            setLocation(null);
            setMapEnabled(e.target.value === '' && drzava === '');
          }}
          disabled={location !== null} // Ako korisnik koristi mapu, blokiraj polje
        />
        <input
          type="text"
          placeholder="Drzava"
          className="kreiranje-objave-input"
          value={drzava}
          onChange={(e) => {
            setDrzava(e.target.value);
            setLocation(null);
            setMapEnabled(grad === '' && e.target.value === '');
          }}
          disabled={location !== null}
        />
        <input
          type="text"
          placeholder="Ulica"
          className="kreiranje-objave-input"
          value={ulica}
          onChange={(e) => {
            setUlica(e.target.value);
            setLocation(null);
            setMapEnabled(grad === '' && e.target.value === '');
          }}
          disabled={location !== null}
        />
        <input
          type="number"
          placeholder="Broj"
          className="kreiranje-objave-input"
          value={broj}
          onChange={(e) => {
            setBroj(e.target.value);
            setLocation(null);
            setMapEnabled(grad === '' && e.target.value === '');
          }}
          disabled={location !== null}
        />

        <p className="mapa-label">Ili odaberite lokaciju na mapi:</p>
        <MapContainer center={[44.7866, 20.4489]} zoom={12} className={`map-container ${!mapEnabled ? 'disabled' : ''}`}>
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
          <LocationMarker />
        </MapContainer>

         {/* Dugme za resetovanje lokacije, prikazuje se samo ako je korisnik odabrao mapu */}
         {location && (
          <button type="button" onClick={resetLocation} style={{ marginTop: '10px', backgroundColor: 'red', color: 'white' }}>
            Obriši lokaciju
          </button>
        )}

        <button type="submit" className="kreiranje-objave-button" disabled={loading}>
          {loading ? 'Kreiranje...' : 'Kreiraj'}
        </button>
      </form>
    </Box>
    </div>
  );
}
