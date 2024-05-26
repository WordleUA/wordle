import React, { useState, useEffect } from "react";
import "./GameField.css";
import Keyboard from "../KeyBoard/Keyboard";
import Modal from "../Modal/Modal"; // Import the Modal component

const TARGET_WORD = "ШКОЛА"; // Загадане слово

function GameField() {
    const [inputValues, setInputValues] = useState(Array(30).fill("")); // Масив для зберігання значень полів введення
    const [currentRow, setCurrentRow] = useState(0); // Додаємо стан для поточного рядка
    const [rowColors, setRowColors] = useState(Array(30).fill("")); // Масив для зберігання кольорів літер
    const [gameStatus, setGameStatus] = useState(""); // Стан для відображення повідомлення про виграш або програш
    const [showModal, setShowModal] = useState(false); // State to control the modal visibility
    const [timeLeft, setTimeLeft] = useState(600); // Timer state in seconds (10 minutes)
    const [timeTaken, setTimeTaken] = useState(0); // State to store the time taken

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
            setTimeTaken(600 - timeLeft); // Calculate the time taken
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
        newInputValues[index] = event.target.value;
        setInputValues(newInputValues);
    };

    const validateCurrentRow = () => {
        const startIndex = currentRow * 5;
        const word = inputValues.slice(startIndex, startIndex + 5).join("");

        validateWord(word);
    };

    const validateWord = (word) => {
        const newRowColors = [...rowColors];
        const startIndex = currentRow * 5;

        if (word === TARGET_WORD) {
            for (let i = 0; i < 5; i++) {
                newRowColors[startIndex + i] = "green";
            }
            setRowColors(newRowColors);
            setGameStatus("Ви виграли!");
            setShowModal(true); // Show the modal on win
        } else {
            const targetWordArray = TARGET_WORD.split("");
            const wordArray = word.split("");
            const correctLetterCounts = {};

            // First pass: Mark correct letters (green)
            wordArray.forEach((char, index) => {
                if (char === TARGET_WORD[index]) {
                    newRowColors[startIndex + index] = "green";
                    correctLetterCounts[char] = (correctLetterCounts[char] || 0) + 1;
                }
            });

            // Second pass: Mark present letters (yellow) and absent letters (darkgrey)
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
            setCurrentRow(currentRow + 1);
            if (currentRow === 5) {
                setGameStatus("Ви програли! Слово було: " + TARGET_WORD);
                setShowModal(true); // Show the modal on loss
            }
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
                        className={`gamefield-tries-row-el ${rowColors[index]}`}
                        value={inputValues[index]}
                        onChange={(e) => handleInputChange(index, e)}
                        onKeyDown={(e) => handleKeyPress(index, e)}
                        maxLength="1" // Обмеження на 1 символ
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
                    <button className="gamefield-keyboard-btn-backspace" onClick={() => handleBackspace(inputValues.lastIndexOf(""))}>⭠</button>
                    <button className="gamefield-keyboard-btn-submit" onClick={validateCurrentRow}>ОК</button>
                </div>
            </div>
        </div>
    );
}

export default GameField;
