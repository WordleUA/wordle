import React, { createContext, useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const SocketContext = createContext();

export const useSocket = () => useContext(SocketContext);

export const SocketProvider = ({ children }) => {
    const [gameData, setGameData] = useState(null);
    const [message, setMessage] = useState('');
    const [gameStarted, setGameStarted] = useState(false);
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        const socket = new SockJS('https://wordle-4fel.onrender.com/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Connected to WebSocket');
            },
            onDisconnect: () => {
                console.log('Disconnected from WebSocket');
            },
        });

        client.activate();
        setStompClient(client);

        return () => {
            client.deactivate();
        };
    }, []);

    const subscribeToSocket = (gameId) => {
        if (stompClient) {
            const subscription = stompClient.subscribe(`/topic/game/${gameId}`, (message) => {
                const messageBody = JSON.parse(message.body);
                console.log('Message from socket:', messageBody);
                setGameData(messageBody);
                setMessage(messageBody);
                if (messageBody.game_status === 'IN_PROGRESS') {
                    setGameStarted(true);
                }
            });

            return () => subscription.unsubscribe();

        }
    };

    return (
        <SocketContext.Provider value={{ subscribeToSocket, gameData, setGameData, message, gameStarted }}>
            {children}
        </SocketContext.Provider>
    );
};