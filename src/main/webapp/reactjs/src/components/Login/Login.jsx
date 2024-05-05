import React from 'react';

import "./Login.css"

function Login() {
    return (
        <div className="login-page">
            <h3 className="logo">WORLDE UA</h3>
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
                        <p className="login-form-p">Не маєте акаунта? <button className="login-form-btn-toregistration">Зареєтруватись</button></p>
                    </form>
                </div>
            </div>
        </div>

    );

}

export default Login;