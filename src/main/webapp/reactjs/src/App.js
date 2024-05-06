import './index.css';
import Registration from "./components/Registration/Registration.jsx"
import Login from "./components/Login/Login.jsx"
import ClientCabinet from "./components/ClientCabinet/ClientCabinet.jsx"
import NavbarClient from "./components/Navbar/NavbarClient/NavbarClient.jsx"


function App() {
  return (
      <div>
        <NavbarClient/>
        <Login/>
      </div>
  );
}

export default App;
