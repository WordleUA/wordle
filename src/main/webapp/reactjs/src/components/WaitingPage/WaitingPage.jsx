import React, { useState, useEffect } from 'react';
import './WaitingPage.css';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useLocation, useNavigate } from 'react-router-dom';

function WaitingPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const [gameData, setGameData] = useState(location.state?.gameData || null);
    const [message, setMessage] = useState('');
    const [gameStarted, setGameStarted] = useState(false);

    useEffect(() => {
        if (gameData) {
            setMessage(`Game Status: ${gameData.game_status}`);
            if (gameData.game_status === 'IN_PROGRESS') {
                setGameStarted(true);
                navigateToGame(gameData);
            } else {
                subscribeToSocket(gameData.game_id);
            }
        }
    }, [gameData]);

    const navigateToGame = (gameData) => {
        navigate('/gameField', { state: { gameData } });
    };

    const subscribeToSocket = (gameId) => {
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Connected to WebSocket');
                stompClient.subscribe(`/topic/game/${gameId}`, (message) => {
                    const messageBody = JSON.parse(message.body);
                    console.log('Message from socket:', messageBody);
                    setGameData(messageBody);
                    setMessage(`Game Status: ${messageBody.game_status}`);
                    if (messageBody.game_status === 'IN_PROGRESS') {
                        setGameStarted(true);
                        navigateToGame(messageBody);
                    }
                });
            },
            onDisconnect: () => {
                console.log('Disconnected from WebSocket');
            },
        });

        stompClient.activate();
    };

    return (
        <div className="waiting-page">
            {gameStarted ? (
                <></>
            ) : (
                <>
                    <h1>ОЧІКУЄМО СУПЕРНИКА...</h1>
                    {message && <p>{message}</p>}
                </>
            )}
        </div>
    );
}

export default WaitingPage;
