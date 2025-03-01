import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Prijavljivanje from "./pages/Prijavljivanje";
import PocetnaStranica from "./pages/PocetnaStranica";
import Registrovanje from "./pages/Registrovanje";
import RegistracijaAdmin from "./nav/RegistracijaAdmin";
import PrijavljeniKorisnikPregled from "./pages/PrijavljeniKorisnikPocetna";
import AdminSistemLozinka from "./pages/AdminSistemLozinka";
import AdminSistemPocetna from "./pages/AdminSistemPocetna";
import KreiranjeObjave from "./pages/KreiranjeObjave";
import MojeObjave from "./pages/MojeObjave";
import MojProfil from "./pages/MojProfil";
import MapaObjava from "./pages/MapaObjava";
import PrikazMape from "./pages/PrikazMape";
import KorisnikProfil from "./pages/KorisnikProfil";
import AzuriranjeProfila from "./pages/AzuriranjeProfila";
import RegistracijaAdminSistem from "./nav/RegistracijaAdminSistem";
import logo from './pics/bunnycircle.webp'
import Header from './Header'; 
import 'leaflet/dist/leaflet.css';

function App() {
  return (
    <Router>  {/* BrowserRouter obavija ceo sadržaj */}
      <div className="App">
        <Header logoSrc={logo} pageTitle="OnlyBuns" /> {/* Header je sada unutar Router-a */}
        <Routes>
          <Route path="/" element={<PocetnaStranica />} />
          <Route path="/prijava" element={<Prijavljivanje />} />
          <Route path="/registracija" element={<Registrovanje />} />
          <Route path="/registrovanjeAdmina" element={<RegistracijaAdmin />} />
          <Route path="/prijavljeniKorisnikPocetna" element={<PrijavljeniKorisnikPregled />} />
          <Route path="/adminSistemLozinka" element={<AdminSistemLozinka />} />
          <Route path="/adminSistemPocetna" element={<AdminSistemPocetna />} />
          <Route path="/registracijaAdminSistem" element={<RegistracijaAdminSistem />} />
          <Route path="/kreiranjeObjave" element={<KreiranjeObjave />} />
          <Route path="/mojeObjave" element={<MojeObjave />} />
          <Route path="/mojProfil" element={<MojProfil />} />
          <Route path="/mapaObjava" element={<MapaObjava />} />
          <Route path="/prikazMape" element={<PrikazMape />} />
          <Route path="/korisnikProfil/:korisnickoIme" element={<KorisnikProfil />} />
          <Route path="/azuriranjeProfila" element={<AzuriranjeProfila />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
