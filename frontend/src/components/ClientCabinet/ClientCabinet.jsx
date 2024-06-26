import React, {useState, useEffect} from "react";
import "./ClientCabinet.css";
import {useNavigate} from "react-router-dom";
import api from "../../api";

function ClientCabinet() {
    const [userInfo, setUserInfo] = useState({});
    const [userGames, setUserGames] = useState([]);
    const [wins, setWins] = useState(0);
    const [losses, setLosses] = useState(0);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newLogin, setNewLogin] = useState('');
    const [error, setError] = useState("");
    const navigate = useNavigate()

    useEffect(() => {
        api.get('/user/cabinet').then(response => {
            setUserInfo(response.data.user);
            setUserGames(response.data.user_games);
            setWins(response.data.wins);
            setLosses(response.data.losses);
        }).catch(error => {
            console.error('Error fetching user data', error);
        })
    }, []);

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

        api.patch('/user/update', {
                login: newLogin
            }
        ).then(response => {
            setUserInfo(prevState => ({...prevState, login: newLogin}));
            setIsModalOpen(false);
            setNewLogin('');
            setError("");
        }).catch(error => {
            if (error.response && error.response.status === 409) {
                setError("Цей логін вже зайнятий.");
            } else {
                setError("Виникла помилка. Спробуйте пізніше.");
            }
        });

    };

    const handleModalClose = () => {
        setIsModalOpen(false);
        setNewLogin('');
        setError('');
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
