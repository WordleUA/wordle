import './index.css';
import GameField from "./components/GameField/GameField";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
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
import PasswordReset from "./components/PasswordReset/PasswordReset";
import ProtectedRoute from "./ProtectedRoute";

function App() {
    return (
        <div>
            <Router>
                <SocketProvider>
                    <div>
                        <NavbarParser/>
                        <Routes>
                            <Route path="*" element={<Navigate to="/"/>}/>

                            <Route path="/" element={<Login/>}/>
                            <Route path="/registration" element={<Registration/>}/>
                            <Route path="/howToPlay" element={<HowToPlay/>}/>
                            <Route path="/generalStatistic" element={<GeneralStatistic/>}/>
                            <Route path="/passwordRecovery" element={<PasswordRecovery/>}/>
                            <Route path="/passwordReset/:code" element={<PasswordReset/>}/>
                            <Route path="/confirmRegistration/:code" element={<ConfirmRegistration/>}/>

                            <Route path="/dictateWord" element={<ProtectedRoute allowedRoles={['ADMIN', 'PLAYER']}
                                                                                element={<DictateWord/>}/>}/>
                            <Route path="/gameField" element={<ProtectedRoute allowedRoles={['ADMIN', 'PLAYER']}
                                                                              element={<GameField/>}/>}/>
                            <Route path="/waitingPage" element={<ProtectedRoute allowedRoles={['ADMIN', 'PLAYER']}
                                                                                element={<WaitingPage/>}/>}/>
                            <Route path="/clientCabinet" element={<ProtectedRoute allowedRoles={['ADMIN', 'PLAYER']}
                                                                                  element={<ClientCabinet/>}/>}/>

                            <Route path="/administration"
                                   element={<ProtectedRoute allowedRoles={['ADMIN']} element={<Administration/>}/>}/>
                        </Routes>
                    </div>
                </SocketProvider>
            </Router>
        </div>
    );
}

export default App;