import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import "./Login.css";

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});

    const navigate = useNavigate();

    const validateEmail = email => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleSubmit = event => {
        event.preventDefault();

        let errors = {};

        if (!validateEmail(email)) {
            errors.email = "Email введено неправильно";
        }

        if (!password) {
            errors.password = "Пароль не може бути порожнім";
        }

        if (Object.keys(errors).length > 0) {
            setErrors(errors);
        } else {
            const userDTO = new URLSearchParams();
            userDTO.append("email", email);
            userDTO.append("password", password);

            fetch("https://wordle-4fel.onrender.com/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: userDTO.toString()
            }).then(response => {
                console.log('Response:', response);
                if (response.ok) {
                    return response.json().then(data => {
                        console.log('Response Data:', data);
                        if (data) {
                            // Збереження токенів та ролі в local storage
                            localStorage.setItem('accessToken', data.access_token);
                            localStorage.setItem('refreshToken', data.refresh_token);
                            localStorage.setItem('role', data.role);

                            // Виведення збережених даних у консоль
                            console.log('Access Token:', localStorage.getItem('accessToken'));
                            console.log('Refresh Token:', localStorage.getItem('refreshToken'));
                            console.log('Role:', localStorage.getItem('role'));

                            navigate('/clientCabinet');
                        } else {
                            console.error("Received undefined data.");
                        }
                    });
                } else {
                    return response.text().then(text => {
                        console.error("Network response was not ok.", text);
                        throw new Error("Network response was not ok.");
                    });
                }
            }).catch(error => {
                console.error("There was a problem with the fetch operation:", error);
            });
        }
    };

    return (
        <div className="login-page">
            <div className="login">
                <div className="login-form">
                    <form className="login-form--form" onSubmit={handleSubmit}>
                        <h1 className="login-header">ВХІД</h1>
                        <input className="login-form-input"
                               type="text"
                               name="email"
                               placeholder="Email"
                               value={email}
                               onChange={event => setEmail(event.target.value)}
                        />
                        {errors.email && <p className="error">{errors.email}</p>}
                        <input className="login-form-input"
                               type="password"
                               name="password"
                               placeholder="Пароль"
                               value={password}
                               onChange={event => setPassword(event.target.value)}
                        />
                        {errors.password && <p className="error">{errors.password}</p>}
                        <button className="login-form-btn" type="submit">УВІЙТИ</button>
                        <p className="login-form-p">Не маєте акаунта? <Link to="/registration" className="login-form-btn-toregistration">Зареєcтруватись</Link></p>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;
