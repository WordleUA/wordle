import React, {useState, useEffect} from "react";
import "./ClientCabinet.css";
import {useAuth} from "../Auth/AuthContext";

function ClientCabinet() {
    const [userInfo, setUserInfo] = useState({});
    const [userGames, setUserGames] = useState([]);
    const [wins, setWins] = useState(0);
    const [losses, setLosses] = useState(0);
    const {authFetch} = useAuth();


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

    const handleLoginChange = () => {

    };

    return (
        <div className="cabinet">
            <h1 className="cabinet-header">ОСОБИСТИЙ КАБІНЕТ</h1>
            <div className="cabinet-form">
                <div className="user-info">
                    <span>Логін: {userInfo.login}</span>
                    <span>Email: {userInfo.email}</span>
                    <span>Кількість монет: {userInfo.coins_total}</span>
                    <button className="edit-button" onClick={handleLoginChange}>Редагувати</button>
                </div>
                <div className="user-stats">
                    <p>Виграші/Програші</p>
                    <span className='user-stats-wins'>{wins}</span>
                    <span>/</span>
                    <span className='user-stats-losses'>{losses}</span>
                </div>
                <h1 className="games-header">МОЇ ІГРИ</h1>
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
        </div>
    );
}

export default ClientCabinet;
