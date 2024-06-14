
import "./NavbarClient.css"

import {NavLink} from "react-router-dom";
import {useNavigate} from "react-router";

function NavbarClient () {
    const navigate = useNavigate();
    const logout = () => {
        localStorage.clear();

        navigate('/');
        window.location.reload();
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


                    <ul className="nav-list">
                        <NavLink to="/clientCabinet" className="nav-list__item">Особистий кабінет</NavLink>
                        <NavLink to='/generalStatistic' className="nav-list__item">Рейтинг гравців</NavLink>
                        <NavLink to="/dictateWord" className="nav-list__item" style={{ color: '#519341' }}>Почати гру</NavLink>
                        <NavLink to="/howToPlay" className="nav-list__item">Як грати?</NavLink>
                        <a onClick={logout} className="nav-list__item">Вихід</a>
                    </ul>
                </div>
            </div>
        </nav>

    );

}
export default NavbarClient;



