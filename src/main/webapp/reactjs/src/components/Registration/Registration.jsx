import React, { useState } from 'react';
import "./Registration.css";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router";

function Registration() {
    const [login, setLogin] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordRepeat, setPasswordRepeat] = useState('');
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

        if (password !== passwordRepeat) {
            errors.passwordRepeat = "Паролі не співпадають";
        }

        if (Object.keys(errors).length > 0) {
            setErrors(errors);
        } else {
            const userDTO = new URLSearchParams();
            userDTO.append("login", login);
            userDTO.append("email", email);
            userDTO.append("password", password);

            fetch("https://wordle-4fel.onrender.com/auth/register", {
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
        <div className="registration-page">
            <div className="registration">
                <div className="registration-form">
                    <form className="registration-form--form" onSubmit={handleSubmit}>
                        <h1 className="registration-header">РEЄСТРАЦІЯ</h1>
                        <input className="registration-form-input"
                               type="text"
                               name="login"
                               placeholder="Логін"
                               value={login}
                               onChange={event => setLogin(event.target.value)}
                        />
                        <input className="registration-form-input"
                               type="email"
                               name="email"
                               placeholder="Email"
                               value={email}
                               onChange={event => setEmail(event.target.value)}
                        />
                        {errors.email && <p className="error">{errors.email}</p>}
                        <input className="registration-form-input"
                               type="password"
                               name="password"
                               placeholder="Пароль"
                               value={password}
                               onChange={event => setPassword(event.target.value)}
                        />
                        <input className="registration-form-input"
                               type="password"
                               name="passwordRepeat"
                               placeholder="Повторіть пароль"
                               value={passwordRepeat}
                               onChange={event => setPasswordRepeat(event.target.value)}
                        />
                        {errors.passwordRepeat && <p className="error">{errors.passwordRepeat}</p>}
                        <button className="registration-form-btn" type="submit">ЗАРЕЄСТРУВАТИСЬ</button>
                        <p className="registration-form-p">Вже маєте акаунт? <Link to="/" className="registration-form-btn-tologin">Увійти</Link></p>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Registration;
