import React, { useState } from "react";
import "./GameField.css";
import Keyboard from "../KeyBoard/Keyboard";

function GameField() {
    const [inputValues, setInputValues] = useState(Array(30).fill("")); // Масив для зберігання значень полів введення

    const handleKeyboardClick = (key) => {
        const newInputValues = [...inputValues]; // Копіюємо поточний масив значень
        const emptyIndex = newInputValues.indexOf(""); // Шукаємо перше порожнє поле введення

        if (emptyIndex !== -1) {
            newInputValues[emptyIndex] = key; // Встановлюємо значення клавіші в порожнє поле введення
            setInputValues(newInputValues); // Оновлюємо стан значень полів вводу
        }
    };

    const handleKeyPress = (index, event) => {
        if (event.key === "Enter") {
            const word = inputValues.slice(currentRow * 5, (currentRow + 1) * 5).join(""); // Об'єднуємо всі символи введені у поточному рядку
            console.log("Виведене слово:", word);
            // Переносимо фокус на перше поле введення наступного рядка
            const nextIndex = index + 5;
            if (nextIndex < inputValues.length) {
                document.getElementById(`input-${nextIndex}`).focus();
            }
            // Збільшуємо номер поточного рядка
            setCurrentRow(currentRow + 1);
            // Забороняємо подальше додавання символів
            event.preventDefault();
        } else if (event.key === "Backspace") {
            const currentValue = inputValues[index];
            if (currentValue) {
                const newInputValues = [...inputValues];
                newInputValues[index] = "";
                setInputValues(newInputValues);
            } else if (index > 0) {
                document.getElementById(`input-${index - 1}`).focus();
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
                        className="gamefield-tries-row-el"
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
            <div className="gamefield-keyboard">
                <Keyboard onClick={handleKeyboardClick} />
                <div className="gamefield-keyboard-btns">

                    <button className="gamefield-keyboard-btn-backspace">⭠</button>
                    <button className="gamefield-keyboard-btn-submit">ОК</button>
                </div>

            </div>
        </div>
    );
}

export default GameField;

