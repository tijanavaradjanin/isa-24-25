import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Prijava from "./nav/Prijava";
import Navigacija from "./pages/Navigacija";
import Registracija from "./nav/Registracija";
import RegistracijaAdmin from "./nav/RegistracijaAdmin";
import PrijavljeniKorisnikPregled from "./pages/PrijavljeniKorisnikPocetna";
import AdminSistemLozinka from "./pages/AdminSistemLozinka";
import AdminSistemPocetna from "./pages/AdminSistemPocetna";
import KreiranjeObjave from "./pages/KreiranjeObjave";
import MojeObjave from "./pages/MojeObjave";
import MojProfil from "./pages/MojProfil";
import RegistracijaAdminSistem from "./nav/RegistracijaAdminSistem";
import logo from './pics/bunnycircle.webp'
import Header from './Header'; 


function App() {
  return (
    <div className="App">
      <Header logoSrc={logo} pageTitle="OnlyBuns" /> {/* Header uvek prisutan */}
      <Router>
        <Routes>
          <Route path="/" element={<Navigacija />} />
          <Route path="/prijava" element={<Prijava />} />
          <Route path="/registracija" element={<Registracija />} />
          <Route path="/registrovanjeAdmina" element={<RegistracijaAdmin />} />
          <Route path="/prijavljeniKorisnikPocetna" element={<PrijavljeniKorisnikPregled/>} />
          <Route path="/adminSistemLozinka" element={<AdminSistemLozinka/>} />
          <Route path="/adminSistemPocetna" element={<AdminSistemPocetna/>} />
          <Route path="/registracijaAdminSistem" element={<RegistracijaAdminSistem/>} />
          <Route path="/kreiranjeObjave" element={<KreiranjeObjave/>} />
          <Route path="/mojeObjave" element={<MojeObjave/>} />
          <Route path="/mojProfil" element={<MojProfil/>} />
          

        </Routes>
      </Router>
    </div>
  );
}

export default App;
