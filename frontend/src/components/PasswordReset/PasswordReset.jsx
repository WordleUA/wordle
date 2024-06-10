import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import "./PasswordReset.css";

function PasswordReset() {


    return (
        <div className="login-page">
            <div className="login">
                <div className="login-form">
                    <form className="reset-form--form">
                        <h1 className="login-header">Введіть новий пароль</h1>
                        <p className="login-form--form-p">Довжина пароля повинна бути не менше 8 та не більше 30 символів</p>
                        <input
                            className="login-form-input"
                            type="password"
                            name="password"
                            placeholder="Пароль"

                        />

                        <input
                            className="login-form-input"
                            type="password"
                            name="password"
                            placeholder="Повторіть пароль"

                        />

                        <Link to="/" className="reset-form-btn" type="submit">Підтвердити</Link>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default PasswordReset;
