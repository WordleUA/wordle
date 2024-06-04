import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import "./Login.css";

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const validateForm = () => {
        let newErrors = {};

        if (!email) newErrors.email = "Email не може бути порожнім";
        else if (email.length > 255) newErrors.email = "Email не може бути більше 255 символів";
        else if (!validateEmail(email)) newErrors.email = "Email має відповідати формату example@example.com";

        if (!password) newErrors.password = "Пароль не може бути порожнім";
        else if (password.length > 30) newErrors.password = "Пароль має бути до 30 символів";

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const validateEmail = email => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleSubmit = event => {
        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        const userDTO = new URLSearchParams();
        userDTO.append("email", email);
        userDTO.append("password", password);

        fetch("https://wordle-4fel.onrender.com/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: userDTO.toString()
        }).then(response => {
            if (response.ok) {
                return response.json().then(data => {
                    localStorage.setItem('accessToken', data.access_token);
                    localStorage.setItem('refreshToken', data.refresh_token);
                    localStorage.setItem('role', data.role);
                    navigate('/clientCabinet');
                });
            } else if (response.status === 401) {
                return response.json().then(errorData => {
                    setErrors({general: errorData.message});
                });
            } else {
                return response.json().then(() => {
                    setErrors({general: "Виникла помилка. Будь ласка, спробуйте пізніше."});
                    throw new Error("Network response was not ok.");
                });
            }
        }).catch(error => {
            console.error("There was a problem with the fetch operation:", error);
        });
    };

    return (
        <div className="login-page">
            <div className="login">
                <div className="login-form">
                    <form className="login-form--form" onSubmit={handleSubmit}>
                        <h1 className="login-header">ВХІД</h1>
                        <input
                            className="login-form-input"
                            type="text"
                            name="email"
                            placeholder="Email"
                            value={email}
                            onChange={event => setEmail(event.target.value)}
                        />
                        {errors.email && <p className="error">{errors.email}</p>}
                        <input
                            className="login-form-input"
                            type="password"
                            name="password"
                            placeholder="Пароль"
                            value={password}
                            onChange={event => setPassword(event.target.value)}
                        />
                        {errors.password && <p className="error">{errors.password}</p>}
                        {errors.general && <p className="error">{errors.general}</p>}
                        <button className="login-form-btn" type="submit">УВІЙТИ</button>
                        <p className="login-form-p">
                            Не маєте акаунта? <Link to="/registration"
                                                    className="login-form-btn-toregistration">Зареєcтруватись</Link>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;
