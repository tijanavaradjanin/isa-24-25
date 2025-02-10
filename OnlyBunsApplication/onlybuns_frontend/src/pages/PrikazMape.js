import React, { useEffect, useState, useRef } from 'react';
import { Box, Typography } from "@mui/material";
import { useNavigate } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

const PrikazMape = () => {
  const navigate = useNavigate();
  const [korisnik, setKorisnik] = useState(null);
  const [location, setLocation] = useState(null);
  const [locationMessage, setLocationMessage] = useState(null);
  const [nearbyPosts, setNearbyPosts] = useState([]); // Dodato: Držimo obližnje objave
  const mapRef = useRef(null);

  const getCoordinates = async (city, country) => {
    const response = await fetch(`https://nominatim.openstreetmap.org/search?city=${city}&country=${country}&format=json`);
    const data = await response.json();
    return data?.[0] ? [parseFloat(data[0].lat), parseFloat(data[0].lon)] : null;
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const korisnickoIme = localStorage.getItem('korisnickoIme');

    if (!token || !korisnickoIme) {
      navigate('/');
      return;
    }

    fetch(`http://localhost:8080/registrovaniKorisnik/profil`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(response => response.ok ? response.json() : Promise.reject('Neuspešno dobijanje podataka'))
      .then(data => {
        setKorisnik(data);

        if (data.grad && data.drzava) {
          getCoordinates(data.grad, data.drzava).then(coords => {
            if (coords) {
              setLocation(coords);
              setLocationMessage(null);
              if (mapRef.current) {
                mapRef.current.setView(coords, 12);
              }
              fetchNearbyPosts(token); // Pozivamo funkciju da dohvatimo obližnje objave
            } else {
              setLocationMessage("Vaša lokacija je nepostojeća, molimo Vas ažurirajte je.");
            }
          });
        } else {
          setLocationMessage("Vaša lokacija je nepostojeća, molimo Vas ažurirajte je.");
        }
      })
      .catch(error => {
        console.error('Greška:', error);
        setLocationMessage("Vaša lokacija je nepostojeća, molimo Vas ažurirajte je.");
      });
  }, []);

  const fetchNearbyPosts = async (token) => {
    try {
      const response = await fetch(`http://localhost:8080/objava/obliznjeObjave`, {
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.ok) {
        const data = await response.json();
        setNearbyPosts(data);
      } else {
        console.error("Greška pri dohvatanju obližnjih objava");
      }
    } catch (error) {
      console.error("Greška:", error);
    }
  };

  const userIcon = new L.Icon({
    iconUrl: require('leaflet/dist/images/kucica.png'),
    iconSize: [40, 42],
    iconAnchor: [12, 41],
    popupAnchor: [8, -34]
  });

  const nearbyPostIcon = new L.Icon({
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    iconSize: [25, 41],
    iconAnchor: [12, 38],
    popupAnchor: [1, -34]
  });
  

  return (
    <Box position="relative">
      {location ? (
        <MapContainer
          center={location}
          zoom={12}
          style={{ height: "calc(100vh - 100px)", width: "100%" }}
          ref={mapRef}
        >
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          
          {/* Korisnička lokacija */}
          <Marker position={location} icon={userIcon}>
            <Popup>
              {korisnik?.grad && korisnik?.drzava ? `${korisnik.grad}, ${korisnik.drzava}` : "Nepoznata lokacija"}
            </Popup>
          </Marker>

          {/* Obližnje objave */}
          {nearbyPosts.map((post, index) => (
            <Marker key={index} position={[post.latituda, post.longituda]} icon={nearbyPostIcon}>
              <Popup>
                <strong>{post.korisnickoIme}</strong>
                <br />
                {post.opis}
              </Popup>
            </Marker>
          ))}
          
        </MapContainer>
      ) : (
        <Typography textAlign="center" p={3}>Učitavanje mape...</Typography>
      )}

      {locationMessage && (
        <Box 
          position="absolute"
          top={0}
          left={0}
          width="100%"
          height="100%"
          bgcolor="rgba(0,0,0,0.5)"
          display="flex"
          alignItems="center"
          justifyContent="center"
          zIndex={1000}
        >
          <Typography 
            bgcolor="white" 
            color="black" 
            p={2} 
            borderRadius={2} 
            fontSize={18}
            textAlign="center"
            boxShadow={3}
          >
            {locationMessage}
          </Typography>
        </Box>
      )}
    </Box>
  );
};

export default PrikazMape;
