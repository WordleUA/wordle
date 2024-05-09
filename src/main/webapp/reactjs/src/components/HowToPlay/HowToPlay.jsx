import "./HowToPlay.css";
import React from "react";

function HowToPlay() {
    return (
        <div className="howtoplay">
            <h3 className="howtoplay-header">ЯК ГРАТИ?</h3>
            <div className="howtoplay-form">
                <p className="howtoplay-form-text">
                    Вам треба відгадати задане суперником слово, що складається з 5
                    літер. На це дається 6 спроб. <br />Колір літер буде показувати,
                    наскільки ви близькі. Наприклад:
                </p>
                <div className="howtoplay-form-example">
                    <div className="howtoplay-form-example-gray">Ш</div>
                    <div className="howtoplay-form-example-yellow">К</div>
                    <div className="howtoplay-form-example-gray">О</div>
                    <div className="howtoplay-form-example-green">Л</div>
                    <div className="howtoplay-form-example-gray">А</div>
                </div>
                <div className="howtoplay-form-explanation">
                    <div className="howtoplay-form-explanation-row">
                        <div className="howtoplay-form-explanation-row-square" style={{background: "#b3b3b3"}}></div>
                        <p>Літера не знаходиться у слові</p>
                    </div>
                    <div className="howtoplay-form-explanation-row">
                        <div className="howtoplay-form-explanation-row-square" style={{background: "#e8e482"}}></div>
                        <p>Літера є у слові, але знаходиться не на своєму місці</p>
                    </div>
                    <div className="howtoplay-form-explanation-row">
                        <div className="howtoplay-form-explanation-row-square" style={{background: "#86c14c"}}></div>
                        <p>Літера є у слові і знаходиться на своєму місці</p>
                    </div>
            </div>

            </div>
        </div>
    );
}

export default HowToPlay;
