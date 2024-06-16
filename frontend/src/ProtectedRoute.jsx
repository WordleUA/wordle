import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './components/Auth/AuthContext';

const ProtectedRoute = ({ element, allowedRoles }) => {
    const { role } = useAuth();

    if (allowedRoles.includes(role)) {
        return element;
    } else {
        return <Navigate to="/howToPlay" />;
    }
};

export default ProtectedRoute;
