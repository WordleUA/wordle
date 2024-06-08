import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './ConfirmRegistration.css';

function ConfirmRegistration() {
    const [confirmationStatus, setConfirmationStatus] = useState('');
    const [loading, setLoading] = useState(true);
    const { code } = useParams();

    useEffect(() => {
        fetch('http://localhost:8080/auth/confirm-registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(code),
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
                        <button className="login-button">Увійти</button>
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
