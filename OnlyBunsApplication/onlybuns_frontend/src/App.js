import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Pocetna from "./pages/Pocetna";
import Prijava from "./pages/Prijava";
import Registracija from "./pages/Registracija";
import RegistracijaAdmin from "./pages/RegistracijaAdmin";
import PrijavljeniKorisnikPregled from "./pages/PrijavljeniKorisnikPregled";
import AdminSistemLogin from "./pages/AdminSistemLogin";
import AdminSistemPocetna from "./pages/AdminSistemPocetna";
import AdminSistemView from "./pages/AdminSistemView";
import RegistracijaAdminSistem from "./pages/RegistracijaAdminSistem";


function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Pocetna />} />
          <Route path="/prijava" element={<Prijava />} />
          <Route path="/registracija" element={<Registracija />} />
          <Route path="/registrovanjeAdmina" element={<RegistracijaAdmin />} />
          <Route path="/prijavljeniKorisnikPregled" element={<PrijavljeniKorisnikPregled/>} />
          <Route path="/adminSistemLogin" element={<AdminSistemLogin/>} />
          <Route path="/adminSistemPocetna" element={<AdminSistemPocetna/>} />
          <Route path="/adminSistemView" element={<AdminSistemView/>} />
          <Route path="/registracijaAdminSistem" element={<RegistracijaAdminSistem/>} />

        </Routes>
      </Router>
    </div>
  );
}

export default App;
