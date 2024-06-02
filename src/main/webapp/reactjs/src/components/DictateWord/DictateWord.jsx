import "./DictateWord.css";
import React, { useRef, useState, useEffect } from "react";
import { useNavigate } from "react-router";

function DictateWord() {
    const navigate = useNavigate();

    const [letters, setLetters] = useState(["", "", "", "", ""]);
    const [message, setMessage] = useState("");
    const inputRefs = useRef([]);

    const user_id = 34;


    useEffect(() => {
        inputRefs.current = inputRefs.current.slice(0, letters.length);
    }, [letters.length]);

    const handleChange = (index, event) => {
        const newLetters = [...letters];
        newLetters[index] = event.target.value.toUpperCase();
        setLetters(newLetters);
        if (index < inputRefs.current.length - 1 && event.target.value) {
            inputRefs.current[index + 1].focus();
        }
    };


    const handleBackspace = (index, event) => {
        if (event.key === "Backspace" && letters[index] === "") {
            event.preventDefault();
            const newLetters = [...letters];
            newLetters[index - 1] = "";
            setLetters(newLetters);
            if (index > 0) {
                inputRefs.current[index - 1].focus();
            }
        } else if (event.key === "Backspace" && letters[index] !== "") {
            event.preventDefault();
            const newLetters = [...letters];
            newLetters[index] = "";
            setLetters(newLetters);
        }
    };

    const word = letters.join("");

    const handleSubmit = async () => {
        const gameStartDTO = {
            user_id: user_id,
            word: word
        };

        try {
            const response = await fetch('https://wordle-4fel.onrender.com/game/connect', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(gameStartDTO)
            });

            if (response.ok) {
                const gameData = await response.json();
                setMessage("Game connected successfully!");
                console.log("Game connected:", gameData);
                navigate('/waitingPage', { state: { gameData } });
            } else {
                const gameDTO = await response.json();
                setMessage("Failed to connect game");
                console.error("Failed to connect game");
                console.log("Game connected:", gameDTO);
            }
        } catch (error) {
            setMessage("Error: " + error.message);
            console.error("Error:", error);
        }
    };

    return (
        <div className="dictate-word">
            <h3 className="dictate-word-header">ЗАГАДАЙТЕ СЛОВО СУПЕРНИКУ</h3>
            <div className="dictate-word-form">
                <div className="input-letters-container">
                    {letters.map((letter, index) => (
                        <input
                            key={index}
                            type="text"
                            maxLength="1"
                            value={letter}
                            onChange={(e) => handleChange(index, e)}
                            onKeyDown={(e) => handleBackspace(index, e)}
                            ref={(input) => inputRefs.current[index] = input}
                            className="input-letter"
                        />
                    ))}
                </div>
                <button className="dictate-word-form-btn" onClick={handleSubmit}>ЗАГАДАТИ СЛОВО</button>
            </div>
            {message && <p className="message">{message}</p>}

        </div>
    );
}

export default DictateWord;
