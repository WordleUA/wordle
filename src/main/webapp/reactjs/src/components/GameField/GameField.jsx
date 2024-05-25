import React, { useState } from "react";
import "./GameField.css";
import Keyboard from "../KeyBoard/Keyboard";

const TARGET_WORD = "ШКОЛА"; // Загадане слово

function GameField() {
    const [inputValues, setInputValues] = useState(Array(30).fill(""));
    const [currentRow, setCurrentRow] = useState(0);
    const [rowColors, setRowColors] = useState(Array(30).fill(""));
    const [gameStatus, setGameStatus] = useState("");

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
        } else {
            const targetWordArray = TARGET_WORD.split("");
            const wordArray = word.split("");
            const correctLetterCounts = {};


            wordArray.forEach((char, index) => {
                if (char === TARGET_WORD[index]) {
                    newRowColors[startIndex + index] = "green";
                    correctLetterCounts[char] = (correctLetterCounts[char] || 0) + 1;
                }
            });


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

    return (
        <div className="gamefield">
            <div className="gamefield-tries">{renderInputRows()}</div>
            {gameStatus && <div className="gamefield-status">{gameStatus}</div>}
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
