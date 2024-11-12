import React, { useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import {
  MDBContainer,
  MDBNavbar,
  MDBNavbarNav,
  MDBNavbarLink,
  MDBCollapse
} from 'mdb-react-ui-kit';


const AdminSistemPocetna = () => {
  const location = useLocation();
  const { korisnik } = location.state || {};
  const [openNavSecond] = useState(false);

  return (
    <MDBContainer fluid>
      <div>
        {korisnik ? (
          <>
            <h1>
              Dobrodošli, AdminSistem {korisnik.ime} {korisnik.prezime}!
            </h1>
          </>
        ) : (
          <p>Nije pronađen prijavljeni korisnik.</p>
        )}
      </div>
      <MDBNavbar expand='lg' light bgColor='light'>
      <MDBContainer fluid>
        <MDBCollapse navbar open={openNavSecond}>
          <MDBNavbarNav>
            <Link to="/addAdminSistemView" style={{ textDecoration: 'none' }}> {/* Add this Link */}
              <MDBNavbarLink>Administratori sistema</MDBNavbarLink>
            </Link>
          </MDBNavbarNav>
        </MDBCollapse>
      </MDBContainer>
    </MDBNavbar>
    </MDBContainer>
    
  );
};

export default AdminSistemPocetna;
