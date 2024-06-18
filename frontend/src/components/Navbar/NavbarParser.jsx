import NavbarAdmin from "./NavbarAdmin";
import NavbarClient from "./NavbarClient/NavbarClient";
import NavbarDefault from "./NavbarDefault";
import { useEffect, useState } from "react";

function NavbarParser() {
    const [role, setRole] = useState(() => {
        const storedRole = localStorage.getItem("role");
        console.log(storedRole);
        return storedRole ? parseRole(storedRole) : null;
    });

    function parseRole(roleString) {
        try {
            return JSON.parse(roleString);
        } catch (error) {
            return roleString;
        }
    }

    useEffect(() => {
        const handleStorageChange = () => {
            const storedRole = localStorage.getItem('role');
            setRole(storedRole ? parseRole(storedRole) : null);
        };
        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    return (
        <nav className="nav">
            {!role && <NavbarDefault />}
            {typeof role === 'string' && role === 'PLAYER' && <NavbarClient />}
            {typeof role === 'object' && role !== null && role === 'PLAYER' && <NavbarClient />}
            {typeof role === 'string' && role === 'ADMIN' && <NavbarAdmin />}
            {typeof role === 'object' && role !== null && role === 'ADMIN' && <NavbarAdmin />}
        </nav>
    );
}

export default NavbarParser;