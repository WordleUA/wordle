import './index.css';
import NavbarClient from "./components/Navbar/NavbarClient/NavbarClient.jsx"
import GameField from "./components/GameField/GameField";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import WebSocket from "./components/WebSocket/WebSocket";
import DictateWord from "./components/DictateWord/DictateWord";
import WaitingPage from "./components/WaitingPage/WaitingPage.jsx";
import HowToPlay from "./components/HowToPlay/HowToPlay";
import ClientCabinet from "./components/ClientCabinet/ClientCabinet";
import Registration from "./components/Registration/Registration";
import Login from "./components/Login/Login";

function App() {
    return (
        <div>
            <Router>
                <div>
                    <NavbarClient />
                    <Routes>
                        <Route path="/dictateWord" element={<DictateWord />} />
                        <Route path="/webSocket" element={<WebSocket />} />
                        <Route path="/gameField" element={<GameField />} />
                        <Route path="/waitingPage" element={<WaitingPage />} />
                        <Route path="/howToPlay" element={<HowToPlay />} />
                        <Route path="/clientCabinet" element={<ClientCabinet />} />
                        <Route path="/registration" element={<Registration />} />
                        <Route path="/" element={<Login />} />
                    </Routes>
                </div>
            </Router>
        </div>

    );
}

export default App;