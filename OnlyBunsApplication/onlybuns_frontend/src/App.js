import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import ZastitaPristupa from './helpers/ZastitaPristupa';
import Prijavljivanje from "./pages/Prijavljivanje";
import PocetnaStranica from "./pages/PocetnaStranica";
import Registrovanje from "./pages/Registrovanje";
import PrijavljeniKorisnikPregled from "./pages/PrijavljeniKorisnikPocetna";
import AdminSistemPocetna from "./pages/AdminSistemPocetna";
import KreiranjeObjave from "./pages/KreiranjeObjave";
import MojeObjave from "./pages/MojeObjave";
import MojProfil from "./pages/MojProfil";
import PrikazMape from "./pages/PrikazMape";
import KorisnikProfil from "./pages/KorisnikProfil";
import logo from './pics/bunnycircle.webp'
import Header from './Header'; 
import 'leaflet/dist/leaflet.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Header logoSrc={logo} pageTitle="OnlyBuns" />
        <Routes>
          {/* Stranice dostupne svima */}
          <Route path="/" element={<PocetnaStranica />} />
          <Route path="/prijava" element={<Prijavljivanje />} />
          <Route path="/registracija" element={<Registrovanje />} />

          {/* Admin stranica - samo za ADMIN ulogu */}
          <Route path="/adminSistemPocetna" element={
            <ZastitaPristupa dozvoljeneUloge={['ADMIN']}>
              <AdminSistemPocetna />
            </ZastitaPristupa>
          } />

          {/* Stranice za korisnike - samo za KORISNIK ulogu */}
          <Route path="/prijavljeniKorisnikPocetna" element={
            <ZastitaPristupa dozvoljeneUloge={['KORISNIK']}>
              <PrijavljeniKorisnikPregled />
            </ZastitaPristupa>
          } />
          <Route path="/kreiranjeObjave" element={
            <ZastitaPristupa dozvoljeneUloge={['KORISNIK']}>
              <KreiranjeObjave />
            </ZastitaPristupa>
          } />
          <Route path="/mojeObjave" element={
            <ZastitaPristupa dozvoljeneUloge={['KORISNIK']}>
              <MojeObjave />
            </ZastitaPristupa>
          } />
          <Route path="/mojProfil" element={
            <ZastitaPristupa dozvoljeneUloge={['KORISNIK']}>
              <MojProfil />
            </ZastitaPristupa>
          } />
          <Route path="/prikazMape" element={
            <ZastitaPristupa dozvoljeneUloge={['KORISNIK']}>
              <PrikazMape />
            </ZastitaPristupa>
          } />
          <Route path="/korisnikProfil/:korisnickoIme" element={
            <ZastitaPristupa dozvoljeneUloge={['KORISNIK']}>
              <KorisnikProfil />
            </ZastitaPristupa>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
