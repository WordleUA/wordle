import React, { useState, useEffect } from "react";
import "./ClientCabinet.css";
import { useAuth } from "../Auth/AuthContext";

function ClientCabinet() {
    const [userInfo, setUserInfo] = useState({});
    const [userGames, setUserGames] = useState([]);
    const [wins, setWins] = useState(0);
    const [losses, setLosses] = useState(0);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newLogin, setNewLogin] = useState('');
    const { authFetch } = useAuth();
    const [error, setError] = useState("");

    useEffect(() => {
        authFetch(`https://wordle-4fel.onrender.com/user/cabinet`)
            .then(data => {
                if (!data.error) {
                    setUserInfo(data.user);
                    setUserGames(data.user_games);
                    setWins(data.wins);
                    setLosses(data.losses);
                } else {
                    console.log('Error fetching user data');
                }
            }).catch(error => {
            console.error('Error fetching user data:', error);
        })
    }, [authFetch]);

    const formatDateString = dateString => {
        const dateObject = new Date(dateString);

        const year = dateObject.getFullYear();
        const month = ("0" + (dateObject.getMonth() + 1)).slice(-2);
        const day = ("0" + dateObject.getDate()).slice(-2);
        const hours = ("0" + dateObject.getHours()).slice(-2);
        const minutes = ("0" + dateObject.getMinutes()).slice(-2);
        const seconds = ("0" + dateObject.getSeconds()).slice(-2);

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };

    const validateLogin = (login) => {
        if (!login) {
            return "Логін не може бути порожнім.";
        } else if (login.length < 5) {
            return "Логін не може бути менше 5 символів.";
        } else if (login.length > 45) {
            return "Логін не може бути більше 45 символів.";
        } else if (/\s|[^a-zA-Z0-9]/.test(login)) {
            return "Логін не може містити пробіли або спеціальні знаки.";
        }
        return "";
    };

    const handleSaveChanges = () => {
        const validationError = validateLogin(newLogin);
        if (validationError) {
            setError(validationError);
            return;
        }

        if (newLogin === userInfo.login) {
            setError("Новий логін не може бути таким же, як поточний.");
            return;
        }

        authFetch(`https://wordle-4fel.onrender.com/user/update`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login: newLogin })
        })
            .then(data => {
                if (!data.error) {
                    setUserInfo(prevState => ({ ...prevState, login: newLogin }));
                    setIsModalOpen(false);
                    setNewLogin(''); // Clear input after saving
                    setError("");
                } else {
                    console.log('Error updating login');
                }
            }).catch(error => {
            console.error('Error updating login:', error);
        });
    };

    const handleModalClose = () => {
        setIsModalOpen(false);
        setNewLogin(''); // Clear input when modal is closed
        setError(''); // Clear error when modal is closed
    };

    return (
        <div className="cabinet">
            <h1 className="cabinet-header">ОСОБИСТИЙ КАБІНЕТ</h1>
            <div className="cabinet-form">
                <div className="user-info">
                    <span>Логін: {userInfo.login}</span>
                    <span>Email: {userInfo.email}</span>
                    <span>Кількість монет: {userInfo.coins_total}</span>
                    <button className="edit-button" onClick={() => setIsModalOpen(true)}>Редагувати</button>
                </div>
                <div className="games-headers">
                    <h1 className="games-header">МОЇ ІГРИ</h1>
                    <div className="user-stats">
                        <p>Виграші/Програші</p>
                        <span className='user-stats-wins'>{wins}</span>
                        <span>/</span>
                        <span className='user-stats-losses'>{losses}</span>
                    </div>
                </div>

                <table className="games-table">
                    <thead>
                    <tr>
                        <th>Дата</th>
                        <th>Слово від опонента</th>
                        <th>Результат гри</th>
                        <th>Кількість монет</th>
                    </tr>
                    </thead>
                    <tbody>
                    {userGames.map((game, index) => (
                        <tr key={index}>
                            <td>{formatDateString(game.date)}</td>
                            <td>{game.word}</td>
                            <td>{game.player_status}</td>
                            <td>{game.coins}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            {isModalOpen && (
                <div className="modal">
                    <div className="modal-content-cabinet">
                        <button className="close-button" onClick={handleModalClose}>&times;</button>
                        <h2>Редагування логіну</h2>
                        <input
                            type="text"
                            value={newLogin}
                            onChange={(e) => setNewLogin(e.target.value)}
                            placeholder={userInfo.login}
                        />
                        {error && <p className="error-message">{error}</p>}
                        <button className="save-button" onClick={handleSaveChanges}>Зберегти зміни</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default ClientCabinet;
