import './index.css';
import NavbarClient from "./components/Navbar/NavbarClient/NavbarClient.jsx"
import GameField from "./components/GameField/GameField";
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import WebSocket from "./components/WebSocket/WebSocket";
function App() {
  return (

      <div>
        <NavbarClient/>
        <GameField/>
          <Router>
              <Routes>
                  <Route path="/webSocket" element={<WebSocket/>}></Route>
              </Routes>
          </Router>
      </div>

  );
}

export default App;
