import "./DictateWord.css";
import React, {useRef, useState, useEffect} from "react";
import {useNavigate} from "react-router";
import api from "../../api";

function DictateWord() {
    const navigate = useNavigate();
    const [letters, setLetters] = useState(["", "", "", "", ""]);
    const [message, setMessage] = useState("");
    const inputRefs = useRef([]);
    const [isSubmitDisabled, setIsSubmitDisabled] = useState(true);


    useEffect(() => {
        inputRefs.current = inputRefs.current.slice(0, letters.length);
        validateWord();
    }, [letters]);

    const handleChange = (index, event) => {
        const newLetters = [...letters];
        const inputChar = event.target.value.toUpperCase();
        const ukrainianAlphabet = /^[А-ЩЬЮЯЄІЇҐ]$/; // Regex for Ukrainian alphabet

        if (ukrainianAlphabet.test(inputChar) || inputChar === "") {
            newLetters[index] = inputChar;
            setLetters(newLetters);
            if (index < inputRefs.current.length - 1 && inputChar) {
                inputRefs.current[index + 1].focus();
            }
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

    const validateWord = () => {
        const isValidWord = letters.join("").length === 5 && letters.every(letter => /^[А-ЩЬЮЯЄІЇҐ]$/.test(letter));
        setIsSubmitDisabled(!isValidWord);
    };

    const handleSubmit = async () => {
        const word = letters.join("");
        console.log("Payload being sent:", word);
        api.post('/game/connect', {
            word: word
        }).then(response => {
            setMessage("Game connected successfully!");
            console.log("Game connected:", response.data);
            navigate('/waitingPage', {state: {gameData: response.data}});
        }).catch(error => {
            setMessage(`Failed to connect game: ${error.message}`);
            console.error("Failed to connect game:", error);
        });


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
                <button
                    className="dictate-word-form-btn"
                    onClick={handleSubmit}
                    disabled={isSubmitDisabled}
                >
                    ЗАГАДАТИ СЛОВО
                </button>
            </div>
            {message && <p className="message">{message}</p>}
        </div>
    );
}

export default DictateWord;
