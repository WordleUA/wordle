
import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function WebSocket() {
    const [messages, setMessages] = useState([]);
    const [connected, setConnected] = useState(false);
    const gameId = '10'; // замініть на реальний ідентифікатор гри

    useEffect(() => {
        // Підключення до SockJS endpoint
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Connected to WebSocket');
                setConnected(true);

                // Підписка на канал гри
                stompClient.subscribe(`/topic/game/start/${gameId}`, (message) => {
                    const messageBody = message.body;
                    console.log('Received message:', messageBody);
                    setMessages((prevMessages) => [...prevMessages, messageBody]);
                });
            },
            onDisconnect: () => {
                console.log('Disconnected from WebSocket');
                setConnected(false);
            },
        });

        // Активація WebSocket з'єднання
        stompClient.activate();

        // Деактивація WebSocket з'єднання при розмонтаженні компонента
        return () => {
            stompClient.deactivate();
        };
    }, [gameId]);

    return (
        <div>
            <h1>WebSocket Game Start</h1>
            {connected ? <p>Connected to WebSocket</p> : <p>Connecting...</p>}
            <div>
                <h2>Messages:</h2>
                <ul>
                    {messages.map((msg, index) => (
                        <li key={index}>{msg}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default WebSocket;
