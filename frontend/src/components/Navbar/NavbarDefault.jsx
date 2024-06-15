import "./NavbarClient/NavbarClient.css";
import { useState } from "react";
import { NavLink } from "react-router-dom";

function NavbarDefault() {
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    return (
        <nav className="nav">
            <div className="container">
                <div className="nav-row">
                    <NavLink to="/howToPlay">
                        <h3 className="logo-navbar">WORLDE UA</h3>
                    </NavLink>
                    <div className={`nav-burger ${isMenuOpen ? "open" : ""}`} onClick={toggleMenu}>
                        <div className="burger-line"></div>
                        <div className="burger-line"></div>
                        <div className="burger-line"></div>
                    </div>
                </div>
                <ul className={`nav-list ${isMenuOpen ? "open" : ""}`}>
                    <NavLink to="/" className="nav-list__item" onClick={toggleMenu}>Вхід</NavLink>
                    <NavLink to="/registration" className="nav-list__item" onClick={toggleMenu}>Реєстрація</NavLink>
                    <NavLink to="/howToPlay" className="nav-list__item" onClick={toggleMenu}>Як грати?</NavLink>
                </ul>
            </div>
        </nav>
    );
}

export default NavbarDefault;
