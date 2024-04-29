import React from 'react';

import "./Registration.css"

function Registration() {
    return (
        <div className="registration">

            <div className="registration-form">

                  <form className="registration-form--form">
                    <h1 className="registration-header">Реєстрація</h1>
                    <input className="registration-form-input"
                           type="text"
                           name="login"

                    />
                    <input className="registration-form-input"
                           type="email"
                           name="email"
                    />

                    <input className="registration-form-input"
                           type="password"
                           name="password"


                    />
                    <input className="registration-form-input"
                           type="password"
                           name="password"
                    />

                    <button className="registration-form-btn" type="submit">Зареєструватись
                    </button>
                    <p>Вже маєте акаунт? <button>Увійти</button></p>
                </form>
            </div>
        </div>
    );

}

export default Registration;