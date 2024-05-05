import React from 'react';

import "./Registration.css"

function Registration() {
    return (
        <div className="registration-page">
            <h3 className="logo">WORLDE UA</h3>
            <div className="registration">

                <div className="registration-form">

                    <form className="registration-form--form">
                        <h1 className="registration-header">РEЄСТРАЦІЯ</h1>
                        <input className="registration-form-input"
                               type="text"
                               name="login"
                               placeholder="Логін"

                        />
                        <input className="registration-form-input"
                               type="email"
                               name="email"
                               placeholder="Email"
                        />

                        <input className="registration-form-input"
                               type="password"
                               name="password"
                               placeholder="Пароль"

                        />
                        <input className="registration-form-input"
                               type="password"
                               name="password"
                               placeholder="Повторіть пароль"
                        />

                        <button className="registration-form-btn" type="submit">ЗАРЕЄСТРУВАТИСЬ
                        </button>
                        <p className="registration-form-p">Вже маєте акаунт? <button className="registration-form-btn-tologin">Увійти</button></p>
                    </form>
                </div>
            </div>
        </div>

    );

}

export default Registration;