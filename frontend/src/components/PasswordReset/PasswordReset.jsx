import React, {useState, useEffect} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import "./PasswordReset.css";

function PasswordReset() {
    const {code} = useParams();
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState({});
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [notFound, setNotFound] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (!code || code.length < 64) {
            setNotFound(true);
        }
    }, [code]);

    const validateForm = () => {
        let newErrors = {};

        if (!password) newErrors.password = "Введіть новий пароль";
        else if (password.length < 8) newErrors.password = "Довжина пароля повинна бути не менше 8 символів";
        else if (password.length > 30) newErrors.password = "Довжина пароля повинна бути не більше 30 символів";

        if (!confirmPassword) newErrors.confirmPassword = "Повторіть новий пароль";
        if (password !== confirmPassword) newErrors.confirmPassword = "Паролі не співпадають";

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        setLoading(true);

        fetch("https://wordle-4fel.onrender.com/user/reset-password", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({password_reset_code: code, password}),
        }).then(response => {
            setLoading(false);
            if (response.ok) {
                setMessage('Пароль успішно змінено');
                setTimeout(() => {
                    navigate('/');
                }, 3000);
            } else if (response.status === 400 || response.status === 401) {
                setErrors({general: 'Ваш акаунт заблоковано або посилання для зміни пароля не дійсне'});
            } else if (response.status === 404) {
                setErrors({general: 'Посилання для зміни пароля не дійсне'});
            } else {
                setErrors({general: 'Сталася помилка. Будь ласка, спробуйте пізніше'});
            }
        }).catch(error => {
            setLoading(false);
            console.error('Error:', error);
        });
    };

    if (notFound) {
        return (
            <div className="login-page">
                <div className="login">
                    <div className="login-form">
                        <h1 className="login-header">Сторінка не знайдена</h1>

                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="login-page">
            <div className="login">
                <div className="login-form">
                    <form className="reset-form--form" onSubmit={handleSubmit}>
                        <h1 className="login-header">Введіть новий пароль</h1>
                        {message && <p className="success-message">{message}</p>}
                        <input
                            className="login-form-input"
                            type="password"
                            name="password"
                            placeholder="Пароль"
                            value={password}
                            onChange={event => setPassword(event.target.value)}
                        />
                        {errors.password && <p className="error">{errors.password}</p>}

                        <input
                            className="login-form-input"
                            type="password"
                            name="confirmPassword"
                            placeholder="Повторіть пароль"
                            value={confirmPassword}
                            onChange={event => setConfirmPassword(event.target.value)}
                        />
                        {errors.confirmPassword && <p className="error">{errors.confirmPassword}</p>}
                        {errors.general && <p className="error">{errors.general}</p>}

                        <button className="reset-form-btn" type="submit" disabled={loading}>
                            {loading ? 'Завантаження...' : 'Підтвердити'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default PasswordReset;
