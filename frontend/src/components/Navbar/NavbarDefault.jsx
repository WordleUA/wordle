
import "./NavbarClient/NavbarClient.css"

import {NavLink} from "react-router-dom";
import {useNavigate} from "react-router";

function NavbarDefault () {
    const navigate = useNavigate();


    return (
        <nav className="nav">
            <div className="container">
                <div className="nav-row">
                    <NavLink to="/howToPlay">
                        <h3 className="logo-navbar">WORLDE UA</h3>
                    </NavLink>

                    <ul className="nav-list">
                        <NavLink to="/" className="nav-list__item">Вхід</NavLink>
                        <NavLink to='/registration' className="nav-list__item">Реєстрація</NavLink>
                        <NavLink to="/howToPlay" className="nav-list__item">Як грати?</NavLink>

                    </ul>
                </div>
            </div>
        </nav>

    );

}
export default NavbarDefault;



