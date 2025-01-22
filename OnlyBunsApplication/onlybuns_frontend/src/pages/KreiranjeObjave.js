import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/KreiranjeObjave.css';

export default function KreiranjeObjave() {
  const [opis, setOpis] = useState('');
  const [slika, setSlika] = useState(null);
  const [latituda, setLatituda] = useState('');
  const [longituda, setLongituda] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSlikaChange = (event) => {
    setSlika(event.target.files[0]);  // Spremaj fajl direktno
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Validacija unosa
    if (!opis || !latituda || !longituda || !slika) {
      setError('Sva polja moraju biti popunjena.');
      return;
    }

    // Validacija da latituda bude između -90 i 90
    const lat = parseFloat(latituda);
    if (isNaN(lat) || lat < -90 || lat > 90) {
      setError('Latituda mora biti broj između -90 i 90.');
      return;
    }

    // Validacija da longituda bude između -180 i 180
    const lon = parseFloat(longituda);
    if (isNaN(lon) || lon < -180 || lon > 180) {
      setError('Longituda mora biti broj između -180 i 180.');
      return;
    }

    setError('');
    setLoading(true);

    const token = localStorage.getItem('token');
    if (!token) {
      setError('Morate biti prijavljeni da biste kreirali objavu.');
      setLoading(false);
      return;
    }

    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const korisnickoIme = decodedToken.korisnickoIme; // Korisničko ime iz tokena

    const formData = new FormData();
    formData.append("opis", opis);
    formData.append("slika", slika); // Dodajemo fajl
    formData.append("latituda", latituda);
    formData.append("longituda", longituda);
    formData.append("korisnickoIme", korisnickoIme);

    fetch("http://localhost:8080/objava/add", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`
      },
      body: formData, // Postavljanje FormData u telo zahteva
    })
      .then((response) => {
        if (!response.ok) {
          return response.json().then((data) => {
            throw new Error(data.message || 'Greška sa serverom');
          });
        }
        return response.json();
      })
      .then((data) => {
        console.log('Objava uspešno kreirana:', data);
        navigate('/prijavljeniKorisnikPocetna');
      })
      .catch((error) => {
        console.error("Greška pri kreiranju objave:", error);
        setError(`Kreiranje objave nije uspelo. ${error.message}`);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <div className="kreiranje-objave-container">
      <form className="kreiranje-objave-form" onSubmit={handleSubmit}>
        <h1 className="kreiranje-objave-title">Kreiranje Objave</h1>
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
        <input
          type="text"
          placeholder="Latituda"
          className="kreiranje-objave-input"
          value={latituda}
          onChange={(e) => setLatituda(e.target.value)}
        />
        <input
          type="text"
          placeholder="Longituda"
          className="kreiranje-objave-input"
          value={longituda}
          onChange={(e) => setLongituda(e.target.value)}
        />
        <button 
          type="submit" 
          className="kreiranje-objave-button"
          disabled={loading} 
        >
          {loading ? 'Kreiranje...' : 'Kreiraj'}
        </button>
      </form>
    </div>
  );
}
