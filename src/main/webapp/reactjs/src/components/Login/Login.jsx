import React from 'react';
import { Link } from 'react-router-dom'; // Додайте імпорт Link з react-router-dom

import "./Login.css";

function Login() {
    return (
        <div className="login-page">

            <div className="login">

                <div className="login-form">

                    <form className="login-form--form">
                        <h1 className="login-header">ВХІД</h1>
                        <input className="login-form-input"
                               type="text"
                               name="email"
                               placeholder="Email"

                        />

                        <input className="login-form-input"
                               type="password"
                               name="password"
                               placeholder="Пароль"

                        />

                        <button className="login-form-btn" type="submit">УВІЙТИ
                        </button>

                        <p className="login-form-p">Не маєте акаунта? <Link to="/registration" className="login-form-btn-toregistration">Зареєcтруватись</Link></p>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;
