import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

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
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('role');
        setAccessToken('');
        setRefreshToken('');
        setRole('');
    }, []);

    const refreshAccessToken = useCallback(async () => {
        try {
            const response = await fetch('https://wordle-4fel.onrender.com/auth/refresh', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${refreshToken}`
                }
            });

            if (!response.ok) {
                const errorData = await response.json();
                if (errorData.message === 'Token has expired') {
                    clearAuthData();
                    navigate('/login');
                } else {
                    throw new Error(errorData.message);
                }
            }

            const data = await response.json();
            setAccessToken(data.accessToken);
            setRefreshToken(data.refreshToken);
            setRole(data.role);
            return data.accessToken;
        } catch (error) {
            console.error('Error refreshing access token:', error);
            clearAuthData();
            navigate('/login');
            throw error;
        }
    }, [refreshToken, clearAuthData, navigate]);

    const authFetch = useCallback(async (url, options = {}) => {
        const { headers = {}, ...otherOptions } = options;
        headers['Authorization'] = `${accessToken}`;

        try {
            const response = await fetch(url, { headers, ...otherOptions });

            if (response.status === 401) {
                const newAccessToken = await refreshAccessToken();
                headers['Authorization'] = `${newAccessToken}`;
                const retryResponse = await fetch(url, { headers, ...otherOptions });
                return retryResponse;
            }

            return response;
        } catch (error) {
            console.error('Error making authenticated request:', error);
            throw error;
        }
    }, [accessToken, refreshAccessToken]);

    return (
        <AuthContext.Provider value={{ accessToken, refreshToken, role, setAccessToken, setRefreshToken, setRole, authFetch }}>
            {children}
        </AuthContext.Provider>
    );
};


export const useAuth = () => useContext(AuthContext);