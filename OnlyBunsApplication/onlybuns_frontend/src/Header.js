import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import './App.css';
import { parseJwt } from './helpers/KorisnickoImeIzTokena';

export default function Header(props) {
    const navigate = useNavigate();

    const homePage = () => {
        const payload = parseJwt();
        if (!payload) {
            navigate('/');
            return;
        }

        const uloga = payload.uloga?.naziv || null;
        if (uloga==='ADMIN') {
            navigate('/adminSistemPocetna');
        } else {
            navigate('/prijavljeniKorisnikPocetna');
        }
        };

    return (
        <header className="App-header">
            <img 
                src={props.logoSrc} 
                className="App-logo" 
                alt="logo" 
                onClick={homePage} 
                style={{ cursor: 'pointer' }} // Dodaj da bude očigledno da je klikabilno
            />
            <h1 className="App-title">{props.pageTitle}</h1>
        </header>
    );
};
