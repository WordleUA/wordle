import React from "react";
import "./Keyboard.css";

function Keyboard({ onClick, keyboardColors }) {
    const handleClick = (key) => {
        onClick(key);
    };

    const ukrainianAlphabet = "ЙЦУКЕНГШЩЗХЇФІВАПРОЛДЖЄЯЧСМИТЬБЮ";

    return (
        <div className="gamefield-keyboard">
            {ukrainianAlphabet.split("").map((letter, index) => (
                <button
                    className={`gamefield-keyboard-el ${keyboardColors[letter] || ""}`}
                    key={index}
                    onClick={() => handleClick(letter)}
                >
                    {letter}
                </button>
            ))}
        </div>
    );
}
export default Keyboard;
