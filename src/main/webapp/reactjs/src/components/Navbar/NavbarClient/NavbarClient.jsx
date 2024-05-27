
import "./NavbarClient.css"

import {NavLink} from "react-router-dom";
import {useNavigate} from "react-router";

function NavbarClient () {
    const navigate = useNavigate();


    return (
        <nav className="nav">
            <div className="container">
                <div className="nav-row">
                    <NavLink to="/howToPlay">
                        <h3 className="logo-navbar">WORLDE UA</h3>
                    </NavLink>

                    <ul className="nav-list">
                        <NavLink to="/clientCabinet" className="nav-list__item">Особистий кабінет</NavLink>
                        <a className="nav-list__item">Статистика</a>
                        <NavLink to="/dictateWord" className="nav-list__item" style={{ color: '#519341' }}>Почати гру</NavLink>
                        <NavLink to="/howToPlay" className="nav-list__item">Як грати?</NavLink>
                        <a className="nav-list__item">Вихід</a>
                    </ul>
                </div>
            </div>
        </nav>

    );

}
export default NavbarClient;



