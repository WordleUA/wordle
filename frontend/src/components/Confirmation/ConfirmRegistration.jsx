import React, { useState, useEffect } from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import './ConfirmRegistration.css';
import {Link} from 'react-router-dom';

function ConfirmRegistration() {
    const [confirmationStatus, setConfirmationStatus] = useState('');
    const [loading, setLoading] = useState(true);
    const { code } = useParams();

    useEffect(() => {
        fetch('https://wordle-4fel.onrender.com/auth/confirm-registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: code.toString(),
        })
            .then(response => {
                if (response.ok) {
                    setConfirmationStatus('success');
                } else {
                    setConfirmationStatus('fail');
                }
            })
            .catch(error => {
                console.error('Error confirming registration:', error);
                setConfirmationStatus('fail');
            })
            .finally(() => {
                setLoading(false);
            });
    }, [code]);

    return (
        <div className="confirmation-page">
            <div className="confirmation-container">
                {loading ? (
                    <div className="loading">
                        <p>Завантаження...</p>
                    </div>
                ) : confirmationStatus === 'success' ? (
                    <div className="confirmation-success">
                        <h1>Підтвердження успішне</h1>
                        <p>Ваш акаунт успішно підтверджено. Тепер ви можете увійти.</p>
                        <Link to="/login" className="login-button">Увійти</Link>
                    </div>
                ) : (
                    <div className="confirmation-fail">
                        <h1>Помилка підтвердження</h1>
                        <p>Виникла помилка підтвердження вашого акаунту. Ваша електронна адреса вже підтверджена або
                            посилання недійсне.</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default ConfirmRegistration;
