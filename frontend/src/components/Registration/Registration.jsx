import React, {useState} from 'react';
import {Link} from 'react-router-dom';
import './Registration.css';

function Registration() {
    const [login, setLogin] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordRepeat, setPasswordRepeat] = useState('');
    const [errors, setErrors] = useState({});
    const [successMessage, setSuccessMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const validateForm = () => {
        let newErrors = {};

        if (!login) newErrors.login = "Введіть логін";
        else if (login.length < 3) newErrors.login = "Логін повинен бути не менше 3 символів";
        else if (login.length > 45) newErrors.login = "Логін повинен бути не більше 45 символів";

        if (!email) newErrors.email = "Введіть email адресу";
        else if (!validateEmail(email)) newErrors.email = "Email має відповідати формату example@example.com";
        else if (email.length > 255) newErrors.email = "Email повинен бути не більше 255 символів";

        if (!password) newErrors.password = "Введіть пароль";
        else if (password.length < 8) newErrors.password = "Довжина пароля повинна бути не менше 8 символів";
        else if (password.length > 30) newErrors.password = "Пароль повинен бути не більше 30 символів";

        if (!passwordRepeat) newErrors.passwordRepeat = "Повторіть пароль";

        if (password !== passwordRepeat) newErrors.passwordRepeat = "Паролі не співпадають";

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        setIsLoading(true);

        const userDTO = new URLSearchParams();
        userDTO.append("login", login);
        userDTO.append("email", email);
        userDTO.append("password", password);

        fetch('https://wordle-4fel.onrender.com/auth/register', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: userDTO.toString(),
        })
            .then((response) => {
                setIsLoading(false);
                if (response.ok) {
                    return response.json().then((data) => {
                        setSuccessMessage(
                            'Реєстрація пройшла успішно. Будь ласка, перевірте свою електронну пошту для підтвердження акаунту.'
                        );
                        setLogin('');
                        setEmail('');
                        setPassword('');
                        setPasswordRepeat('');
                        setErrors({});
                    });
                } else if (response.status === 400) {
                    return response.json().then((errorData) => {
                        setErrors({general: errorData.message});
                    });
                } else {
                    return response.json().then(() => {
                        setErrors({general: 'Виникла помилка. Будь ласка, спробуйте пізніше.'});
                    });
                }
            })
            .catch((error) => {
                setIsLoading(false);
                console.error('There was a problem with the fetch operation:', error);
            });
    };

    return (
        <div className="registration-page">
            <div className="registration">
                <div className="registration-form">
                    <form className="registration-form--form" onSubmit={handleSubmit}>
                        <h1 className="registration-header">РЕЄСТРАЦІЯ</h1>
                        {successMessage && <p className="success-message">{successMessage}</p>}
                        <input
                            className="registration-form-input"
                            type="text"
                            name="login"
                            placeholder="Логін"
                            value={login}
                            onChange={(event) => setLogin(event.target.value)}
                        />
                        {errors.login && <p className="error">{errors.login}</p>}
                        <input
                            className="registration-form-input"
                            type="text"
                            name="email"
                            placeholder="Email"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                        />
                        {errors.email && <p className="error">{errors.email}</p>}
                        <input
                            className="registration-form-input"
                            type="password"
                            name="password"
                            placeholder="Пароль"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                        />
                        {errors.password && <p className="error">{errors.password}</p>}
                        <input
                            className="registration-form-input"
                            type="password"
                            name="passwordRepeat"
                            placeholder="Повторіть пароль"
                            value={passwordRepeat}
                            onChange={(event) => setPasswordRepeat(event.target.value)}
                        />
                        {errors.passwordRepeat && <p className="error">{errors.passwordRepeat}</p>}
                        {errors.general && <p className="error">{errors.general}</p>}
                        <button className="registration-form-btn" type="submit" disabled={isLoading}>
                            {isLoading ? 'ЗАВАНТАЖЕННЯ...' : 'ЗАРЕЄСТРУВАТИСЬ'}
                        </button>
                        <p className="registration-form-p">
                            Вже маєте акаунт?{' '}
                            <Link to="/" className="registration-form-btn-tologin">
                                Увійти
                            </Link>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Registration;
