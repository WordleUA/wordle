import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

// Створення AuthContext
const AuthContext = createContext();

// AuthProvider компонент
export const AuthProvider = ({ children }) => {
    const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken') || '');
    const [refreshToken, setRefreshToken] = useState(localStorage.getItem('refreshToken') || '');
    const [role, setRole] = useState(localStorage.getItem('role') || '');
    const navigate = useNavigate();

    useEffect(() => {
        localStorage.setItem('accessToken', accessToken);
    }, [accessToken]);

    useEffect(() => {
        localStorage.setItem('refreshToken', refreshToken);
    }, [refreshToken]);

    useEffect(() => {
        localStorage.setItem('role', role);
    }, [role]);

    const clearAuthData = useCallback(() => {
        console.log("Clearing auth data");
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('role');
        navigate('/');
        window.location.reload();
    }, [navigate]);

    const refresh = useCallback(async () => {
        try {
            const response = await fetch('https://wordle-4fel.onrender.com/auth/refresh', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${refreshToken}`
                }
            });
            const data = await response.json();

            if (!response.ok && data.message === "Token has expired") {
                console.log("Refresh token has expired");
                clearAuthData();
            } else {
                console.log("Tokens refreshed");
                setAccessToken(data.access_token);
                setRefreshToken(data.refresh_token);
                setRole(data.role);
                return data;
            }
        } catch (error) {
            console.error('Error refreshing access token:', error);
            clearAuthData();
            throw error;
        }
    }, [refreshToken, clearAuthData, navigate]);

    const authFetch = useCallback(async (url, options = {}) => {
        const { headers = {}, ...otherOptions } = options;
        headers['Authorization'] = `${accessToken}`;

        try {
            let response = await fetch(url, { headers, ...otherOptions });
            let data = await response.json();

            if (!response.ok && data.message === "Token has expired") {
                console.log('Access token has expired, refreshing...');
                const refreshedData = await refresh();

                if (refreshedData) {
                    headers['Authorization'] = `${refreshedData.access_token}`;
                    response = await fetch(url, { headers, ...otherOptions });
                    data = await response.json();
                }
            }
            return data;
        } catch (error) {
            console.error('Error making authenticated request:', error);
            throw error;
        }
    }, [accessToken, refresh]);

    return (
        <AuthContext.Provider value={{authFetch}}>
            {children}
        </AuthContext.Provider>
    );
};

// Custom hook to use AuthContext
export const useAuth = () => useContext(AuthContext);
