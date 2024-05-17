import "./DictateWord.css";
import React, { useRef, useState, useEffect } from "react";

function DictateWord() {
    const [letters, setLetters] = useState(["", "", "", "", ""]);
    const inputRefs = useRef([]);

    useEffect(() => {
        inputRefs.current = inputRefs.current.slice(0, letters.length);
    }, [letters.length]);

    const handleChange = (index, event) => {
        const newLetters = [...letters];
        setLetters(newLetters);
        if (index < inputRefs.current.length - 1) {
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
                <button className="dictate-word-form-btn" onClick={() => console.log(word)}>ЗАГАДАТИ СЛОВО</button>
            </div>

        </div>
    );
}
export default DictateWord;