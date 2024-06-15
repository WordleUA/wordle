import "./NavbarClient.css";
import { useState } from "react";
import { NavLink } from "react-router-dom";
import { useNavigate } from "react-router";

function NavbarClient() {
    const navigate = useNavigate();
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const logout = () => {
        localStorage.clear();
        navigate('/');
        window.location.reload();
    };

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    return (
        <nav className="nav">
            <div className="container">
                <div className="nav-row">
                    <div className="nav-row-name">
                        <NavLink to="/howToPlay">
                            <h3 className="logo-navbar">WORLDE UA</h3>
                        </NavLink>
                    </div>
                    <div className={`nav-burger ${isMenuOpen ? "open" : ""}`} onClick={toggleMenu}>
                        <div className="burger-line"></div>
                        <div className="burger-line"></div>
                        <div className="burger-line"></div>
                    </div>
                </div>
                <ul className={`nav-list ${isMenuOpen ? "open" : ""}`}>
                    <NavLink to="/clientCabinet" className="nav-list__item" onClick={toggleMenu}>Особистий кабінет</NavLink>
                    <NavLink to="/generalStatistic" className="nav-list__item" onClick={toggleMenu}>Рейтинг гравців</NavLink>
                    <NavLink to="/dictateWord" className="nav-list__item" style={{ color: '#519341' }} onClick={toggleMenu}>Почати гру</NavLink>
                    <NavLink to="/howToPlay" className="nav-list__item" onClick={toggleMenu}>Як грати?</NavLink>
                    <a onClick={() => { logout(); toggleMenu(); }} className="nav-list__item">Вихід</a>
                </ul>
            </div>
        </nav>
    );
}

export default NavbarClient;
