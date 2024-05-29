import React, { useState, useEffect, useRef } from "react";
import { useLocation } from 'react-router-dom';
import "./GameField.css";
import Keyboard from "../KeyBoard/Keyboard";
import Modal from "../Modal/Modal";
import { useNavigate } from "react-router";

function GameField() {
    const navigate = useNavigate();
    const location = useLocation();
    const gameData = location.state?.gameData;
    const TARGET_WORD = gameData.opponent_word;

    const [inputValues, setInputValues] = useState(Array(30).fill(""));
    const [currentRow, setCurrentRow] = useState(0);
    const [rowColors, setRowColors] = useState(Array(30).fill(""));
    const [gameStatus, setGameStatus] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [timeLeft, setTimeLeft] = useState(600);
    const [timeTaken, setTimeTaken] = useState(0);
    const [canSubmit, setCanSubmit] = useState(false);

    // Create refs for input fields
    const inputRefs = useRef([]);

    useEffect(() => {
        if (inputValues.slice(currentRow * 5, (currentRow + 1) * 5).join("").length === 5) {
            setCanSubmit(true);
        } else {
            setCanSubmit(false);
        }
    }, [inputValues, currentRow]);

    const validateCurrentRow = () => {
        const startIndex = currentRow * 5;
        const word = inputValues.slice(startIndex, startIndex + 5).join("");

        if (word.length === 5) {
            validateWord(word);
        } else {
        }
    };

    useEffect(() => {
        if (timeLeft > 0 && !showModal) {
            const timer = setInterval(() => {
                setTimeLeft(timeLeft - 1);
            }, 1000);
            return () => clearInterval(timer);
        } else if (timeLeft === 0) {
            setGameStatus("Час вийшов! Ви програли!");
            setShowModal(true);
        }
    }, [timeLeft, showModal]);

    useEffect(() => {
        if (gameStatus === "Ви виграли!") {
            setTimeTaken(600 - timeLeft);
        }
    }, [gameStatus]);

    const handleKeyboardClick = (key) => {
        const newInputValues = [...inputValues];
        const emptyIndex = newInputValues.indexOf("");

        if (emptyIndex !== -1 && emptyIndex < (currentRow + 1) * 5) {
            newInputValues[emptyIndex] = key;
            setInputValues(newInputValues);
        }
    };

    const handleKeyPress = (index, event) => {
        if (event.key === "Enter") {
            if (index % 5 === 4) {
                validateCurrentRow();
            }
            event.preventDefault();
        } else if (event.key === "Backspace") {
            handleBackspace(index);
            event.preventDefault();
        }
    };

    const handleBackspace = (index) => {
        if (index >= 0) {
            const newInputValues = [...inputValues];
            if (newInputValues[index] === "") {
                handleBackspace(index - 1);
            } else {
                newInputValues[index] = "";
                setInputValues(newInputValues);
                if (index > 0) {
                    document.getElementById(`input-${index - 1}`).focus();
                }
            }
        }
    };

    const handleInputChange = (index, event) => {
        const newInputValues = [...inputValues];
        newInputValues[index] = event.target.value.toUpperCase();
        setInputValues(newInputValues);

        // Move focus to next input if a letter is entered and not the last input in the row
        if (event.target.value && index < (currentRow + 1) * 5 - 1) {
            inputRefs.current[index + 1].focus();
        }
    };

    const validateWord = (word) => {
        const newRowColors = [...rowColors];
        const startIndex = currentRow * 5;
        const targetWordArray = TARGET_WORD.split("");
        const wordArray = word.split("");

        // Reset correctLetterCounts
        const correctLetterCounts = {};

        // First pass: Green letters
        wordArray.forEach((char, index) => {
            if (char === targetWordArray[index]) {
                newRowColors[startIndex + index] = "green";
                correctLetterCounts[char] = (correctLetterCounts[char] || 0) + 1;
            }
        });

        // Second pass: Yellow letters
        wordArray.forEach((char, index) => {
            if (newRowColors[startIndex + index] !== "green") {
                if (targetWordArray.includes(char) && (correctLetterCounts[char] || 0) < targetWordArray.filter(c => c === char).length) {
                    newRowColors[startIndex + index] = "yellow";
                    correctLetterCounts[char] = (correctLetterCounts[char] || 0) + 1;
                } else {
                    newRowColors[startIndex + index] = "darkgrey";
                }
            }
        });

        setRowColors(newRowColors);

        if (word === TARGET_WORD) {
            setGameStatus("Ви виграли!");
            setShowModal(true);
        } else if (currentRow === 5) {
            setGameStatus("Ви програли! Слово було: " + TARGET_WORD);
            setShowModal(true);
        } else {
            setCurrentRow(currentRow + 1);
        }
    };
    const renderInputRows = () => {
        const rows = [];
        for (let i = 0; i < 6; i++) {
            const inputRow = [];
            for (let j = 0; j < 5; j++) {
                const index = i * 5 + j;
                inputRow.push(
                    <input
                        key={index}
                        id={`input-${index}`}
                        ref={(el) => (inputRefs.current[index] = el)}
                        className={`gamefield-tries-row-el ${rowColors[index]}`}
                        value={inputValues[index]}
                        onChange={(e) => handleInputChange(index, e)}
                        onKeyDown={(e) => handleKeyPress(index, e)}
                        maxLength="1"
                    />
                );
            }
            rows.push(
                <div key={i} className="gamefield-tries-row">
                    {inputRow}
                </div>
            );
        }
        return rows;
    };

    const handleCloseModal = () => {
        setShowModal(false);
        navigate('/howToPlay');
    };

    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
    };

    return (
        <div className="gamefield">
            <div className="gamefield-timer">Час: {formatTime(timeLeft)}</div>
            <div className="gamefield-tries">{renderInputRows()}</div>
            {showModal && <Modal message={gameStatus} timeTaken={gameStatus === "Ви виграли!" ? formatTime(timeTaken) : null} onClose={handleCloseModal} />}
            <div className="gamefield-keyboard">
                <Keyboard onClick={handleKeyboardClick} />
                <div className="gamefield-keyboard-btns">
                    <button className="gamefield-keyboard-btn-backspace" onClick={() => handleBackspace(inputValues.lastIndexOf(""))}>←</button>
                    <button className="gamefield-keyboard-btn-submit" onClick={canSubmit ? validateCurrentRow : null}>OK</button>
                </div>
            </div>
        </div>
    );
}

export default GameField;
