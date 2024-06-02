import React from 'react';
import './Modal.css';
import {useSocket} from "../WebSocket/SocketContext";

function Modal({ message, messageLose, timeTaken, coins, onClose }) {


    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>{message}</h2>
                {message === "Ви програли!" && <p>{messageLose}</p>}
                {timeTaken && <p>Час виконання: {timeTaken}</p>}
                <p>{message === "Ви виграли!" ? `Нараховано монет: ${coins}` : message === "Ви програли!" ? `Списано монет: 1` : 'Нараховано монет: 0'}</p>
                <button onClick={onClose}>OK</button>
            </div>
        </div>
    );
}

export default Modal;
