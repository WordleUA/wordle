import "./NavbarClient.css";
import React from "react";


function NavbarClient() {

    return (
        <nav className="nav">
            <div className="container">
                <div className="nav-row">

                        <h3 className="logo-navbar">WORLDE UA</h3>

                    <ul className="nav-list">
                        <a  className="nav-list__item">Особистий кабінет</a>
                        <a className="nav-list__item">Налаштування</a>
                        <a className="nav-list__item" style={{ color: '#519341' }}>Створити гру</a>
                        <a className="nav-list__item">Як грати?</a>

                        <a className="nav-list__item" >Вихід</a>
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default NavbarClient;
