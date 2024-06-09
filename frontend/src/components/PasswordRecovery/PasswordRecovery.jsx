import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import "./PasswordRecovery.css";

function PasswordRecovery() {
    const [email, setEmail] = useState('');
    const [errors, setErrors] = useState({});
    const validateForm = () => {
        let newErrors = {};

        if (!email) newErrors.email = "Email не може бути порожнім";
        else if (email.length > 255) newErrors.email = "Email не може бути більше 255 символів";
        else if (!validateEmail(email)) newErrors.email = "Email має відповідати формату example@example.com";

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const validateEmail = email => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    return (
        <div className="login-page">
            <div className="login">
                <div className="login-form">
                    <form className="passwordrecovery-form--form" >
                        <h1 className="login-header">ВІДНОВЛЕННЯ ПАРОЛЮ</h1>
                        <input
                            className="login-form-input"
                            type="text"
                            name="email"
                            placeholder="Email"
                            value={email}
                            onChange={event => setEmail(event.target.value)}
                        />
                        {errors.email && <p className="error">{errors.email}</p>}

                        <button className="login-form-btn" type="submit">ВІДНОВИТИ ПАРОЛЬ</button>

                    </form>
                </div>
            </div>
        </div>
    );
}

export default PasswordRecovery;
