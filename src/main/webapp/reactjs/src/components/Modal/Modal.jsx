import React from 'react';
import './Modal.css';

function Modal({ message, timeTaken, onClose}) {
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>{message}</h2>
                {timeTaken && <p>Час виконання: {timeTaken}</p>}
                <p>Нараховано монет: 3</p>
                <button onClick={onClose}>OK</button>
            </div>
        </div>
    );
}

export default Modal;
