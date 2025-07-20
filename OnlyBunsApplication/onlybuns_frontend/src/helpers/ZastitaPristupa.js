import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { parseJwt } from './KorisnickoImeIzTokena'; 

const ZastitaPristupa = ({ dozvoljeneUloge, children }) => {
  const [poruka, setPoruka] = useState('');
  const location = useLocation();

  useEffect(() => {
    const payload = parseJwt();
    if (!payload) {
      setPoruka('Niste prijavljeni.');
      return;
    }

    const uloga = payload.uloga?.naziv;
    if (!dozvoljeneUloge.includes(uloga)) {
      setPoruka('Nemate pristup ovoj stranici.');
    }
  }, [location.pathname, dozvoljeneUloge]);

  if (poruka) {
    return (
      <div style={{
        color: 'red',
        textAlign: 'center',
        fontWeight: 'bold',
        marginTop: '2rem',
        fontSize: '1.2rem'
      }}>
        {poruka}
      </div>
    );
  }

  return children;
};

export default ZastitaPristupa;
