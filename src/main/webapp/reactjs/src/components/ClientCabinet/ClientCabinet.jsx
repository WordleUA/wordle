import React from "react";
import "./ClientCabinet.css";

function ClientCabinet() {
    const games = [
        { date: "03.10", word: "ШКОЛА", result: "Виграв", coins: 4 },
        { date: "03.10", word: "КОЗЕЛ", result: "Програв", coins: -1 },
        { date: "03.10", word: "БАРАН", result: "Програв", coins: -1 },
    ];

    return (
        <div className="cabinet">
            <h1 className="cabinet-header">ОСОБИСТИЙ КАБІНЕТ</h1>
            <div className="cabinet-form">
                <div className="user-info">
                    <span>Логін: Sofiko</span>
                    <span>Email: sofiakolokolcheva@gmail.com</span>
                    <span>Кількість монет: 256</span>
                    <button className="edit-button">Редагувати</button>
                </div>
                <h1 className="games-header">МОЇ ІГРИ</h1>
                <table className="games-table">
                    <thead>
                    <tr>
                        <th>Дата</th>
                        <th>Слово</th>
                        <th>Результат гри</th>
                        <th>Кількість монет</th>
                    </tr>
                    </thead>
                    <tbody>
                    {games.map((game, index) => (
                        <tr key={index}>
                            <td>{game.date}</td>
                            <td>{game.word}</td>
                            <td>{game.result}</td>
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
