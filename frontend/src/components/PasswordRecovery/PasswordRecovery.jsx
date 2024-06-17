import React, {useState} from 'react';
import "./PasswordRecovery.css";

function PasswordRecovery() {
    const [email, setEmail] = useState('');
    const [errors, setErrors] = useState({});
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    const validateForm = () => {
        let newErrors = {};

        if (!email) newErrors.email = "Будь ласка, введіть ваш email";
        else if (email.length > 255) newErrors.email = "Email не може бути більше 255 символів";
        else if (!validateEmail(email)) newErrors.email = "Email має відповідати формату example@example.com";

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const validateEmail = email => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        setLoading(true);

        fetch('https://wordle-4fel.onrender.com/user/forgot-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: email.toString(),
        })
            .then(response => {
                    setLoading(false);
                    if (response.ok) {
                        setMessage('На вашу пошту було відправлено лист з інструкціями для відновлення паролю');
                    } else if (response.status === 400 || response.status === 401) {
                        return response.json().then(errorData => {
                            setErrors({general: errorData.message});
                        });
                    } else
                        setErrors({general: 'Сталася помилка. Будь ласка, спробуйте пізніше'});

                }
            )
            .catch(error => {
                setLoading(false);
                console.error('Error:', error);
            });
    };

    return (
        <div className="login-page">
            <div className="login">
                <div className="login-form">
                    <form className="passwordrecovery-form--form" onSubmit={handleSubmit}>
                        <h1 className="login-header">ВІДНОВЛЕННЯ ПАРОЛЮ</h1>
                        {message && <p className="success-message">{message}</p>}
                        <input
                            className="login-form-input"
                            type="text"
                            name="email"
                            placeholder="Email"
                            value={email}
                            onChange={event => setEmail(event.target.value)}
                        />
                        {errors.email && <p className="error">{errors.email}</p>}
                        {errors.general && <p className="error">{errors.general}</p>}
                        <button className="recovery-form-btn" type="submit"
                                disabled={loading}> {/* Disable button when loading */}
                            {loading ? 'ЗАВАНТАЖЕННЯ...' : 'ВІДНОВИТИ ПАРОЛЬ'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default PasswordRecovery;
