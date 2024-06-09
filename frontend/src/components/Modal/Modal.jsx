import React from 'react';
import './Modal.css';

function Modal({ playerStatus, messageLose, timeTaken, coins, onClose }) {

    const getMessage = (status) => {
        switch (status) {
            case "WIN":
                return "Ви виграли!";
            case "LOSE":
                return "Ви програли!";
            case "DRAW":
                return "Час вийшов!";
            default:
                return "";
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>{getMessage(playerStatus)}</h2>
                {playerStatus === "LOSE" && <p>{messageLose}</p>}
                {timeTaken && <p>Час виконання: {timeTaken}</p>}
                <p>{playerStatus === "WIN" ? `Нараховано монет: ${coins}` : playerStatus === "LOSE" ? `Списано монет: 1` : 'Нараховано монет: 0'}</p>
                <button onClick={onClose}>OK</button>
            </div>
        </div>
    );
}

export default Modal;
