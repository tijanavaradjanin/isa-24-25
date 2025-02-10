import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import './App.css';

export default function Header(props) {
    const navigate = useNavigate();

    const homePage = () => {
        navigate('/prijavljeniKorisnikPocetna'); // Navigacija na početnu stranicu korisnika
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
