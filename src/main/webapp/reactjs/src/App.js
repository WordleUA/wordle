import './index.css';
import Registration from "./components/Registration/Registration.jsx"
import Login from "./components/Login/Login.jsx"
import ClientCabinet from "./components/ClientCabinet/ClientCabinet.jsx"
import NavbarClient from "./components/Navbar/NavbarClient/NavbarClient.jsx"
import HowToPlay from "./components/HowToPlay/HowToPlay";
import DictateWord from "./components/DictateWord/DictateWord";

function App() {
  return (
      <div>
        <NavbarClient/>
        <DictateWord/>
      </div>
  );
}

export default App;
