import React, { useEffect } from 'react';
import './WaitingPage.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { useSocket } from '../WebSocket/SocketContext';

function WaitingPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const { subscribeToSocket, gameData, setGameData, message, gameStarted } = useSocket();

    useEffect(() => {
        if (location.state?.gameData) {
            const initialGameData = location.state.gameData;
            setGameData(initialGameData);
            if (initialGameData.game_status === 'IN_PROGRESS') {
                subscribeToSocket(initialGameData.game_id);
                navigateToGame(initialGameData);
            } else {
                subscribeToSocket(initialGameData.game_id);
            }
        }
    }, [location.state]);

    useEffect(() => {
        if (gameStarted && gameData) {
            navigateToGame(gameData);
        }
    }, [gameStarted, gameData]);

    const navigateToGame = (gameData) => {
        navigate('/gameField', { state: { gameData } });
    };

    return (
        <div className="waiting-page">
            {!gameStarted ? (
                <>
                    <h1 className="waiting-page-header">ОЧІКУЄМО СУПЕРНИКА...</h1>
                </>
            ) : (
                <></>
            )}
        </div>
    );
}

export default WaitingPage;