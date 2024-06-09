import './index.css';
import GameField from "./components/GameField/GameField";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import {SocketProvider} from "./components/WebSocket/SocketContext";
import DictateWord from "./components/DictateWord/DictateWord";
import WaitingPage from "./components/WaitingPage/WaitingPage.jsx";
import HowToPlay from "./components/HowToPlay/HowToPlay";
import ClientCabinet from "./components/ClientCabinet/ClientCabinet";
import Registration from "./components/Registration/Registration";
import Login from "./components/Login/Login";
import Administration from "./components/Administration/Administration";
import GeneralStatistic from "./components/GeneralStatistic/GeneralStatistic";
import NavbarParser from "./components/Navbar/NavbarParser";
import PasswordRecovery from "./components/PasswordRecovery/PasswordRecovery";
import ConfirmRegistration from "./components/Confirmation/ConfirmRegistration";

function App() {
    return (
        <div>
            <SocketProvider>
            <Router>
                <div>
                    <NavbarParser/>
                    <Routes>
                        <Route path="/dictateWord" element={<DictateWord />} />

                        <Route path="/gameField" element={<GameField />} />
                        <Route path="/waitingPage" element={<WaitingPage />} />
                        <Route path="/howToPlay" element={<HowToPlay />} />
                        <Route path="/clientCabinet" element={<ClientCabinet />} />
                        <Route path="/registration" element={<Registration />} />
                        <Route path="/" element={<Login/>} />
                        <Route path="/administration" element={<Administration />} />
                        <Route path="/generalStatistic" element={<GeneralStatistic />} />
                        <Route path="/passwordRecovery" element={<PasswordRecovery />} />
                        <Route path="/confirmRegistration/:code" element={<ConfirmRegistration />} />

                    </Routes>
                </div>
            </Router>
            </SocketProvider>
        </div>

    );
}

export default App;