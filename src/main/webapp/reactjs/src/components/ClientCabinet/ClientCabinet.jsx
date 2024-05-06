import "./ClientCabinet.css"
import React from "react";

function ClientCabinet() {

    return (
        <div className="cabinet">
            <h1 className="cabinet-header">ОСОБИСТИЙ КАБІНЕТ</h1>
                <div className="cabinet-form">
                    <div className="cabinet-form--form">
                        <p className="cabinet-form-data">
                            ЛОГІН: <br/>sofikoshka
                        </p>

                        <p className="cabinet-form-data">
                            EMAIL: <br/>sofiia.kolokolcheva@nure.ua
                        </p>
                        <button
                            className="cabinet-form-btn"

                        >
                            РЕДАГУВАТИ ОСОБИСТІ ДАНІ
                        </button>
                        <p className="cabinet-form-data">
                            КІЛЬКІСТЬ ЗІГРАНИХ ІГОР: <br/>24
                        </p>
                        <p className="cabinet-form-data">
                            КІЛЬКІСТЬ ПЕРЕМОГ: <br/>10
                        </p>


                    </div>
                </div>



        </div>


    );
}

export default ClientCabinet;




