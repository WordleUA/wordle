import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken') || '');
    const [refreshToken, setRefreshToken] = useState(localStorage.getItem('refreshToken') || '');
    const [role, setRole] = useState(localStorage.getItem('role') || '');
    const navigate = useNavigate();

    useEffect(() => {
        if (accessToken) localStorage.setItem('accessToken', accessToken);
    }, [accessToken]);

    useEffect(() => {
        if (refreshToken) localStorage.setItem('refreshToken', refreshToken);
    }, [refreshToken]);

    useEffect(() => {
        if (role) localStorage.setItem('role', role);
    }, [role]);

    const clearAuthData = useCallback(() => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('role');
        setAccessToken('');
        setRefreshToken('');
        setRole('');
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

            if (!response.ok) {
                throw new Error('Failed to refresh token');
            }

            const data = await response.json();
            setAccessToken(data.access_token);
            setRefreshToken(data.refresh_token);
            setRole(data.role);

            return data;
        } catch (error) {
            clearAuthData();
            throw error;
        }
    }, [refreshToken, clearAuthData]);

    const authFetch = useCallback(async (url, options = {}) => {
        const { headers = {}, ...otherOptions } = options;
        headers['Authorization'] = `${accessToken}`;

        try {
            let response = await fetch(url, { headers, ...otherOptions });
            if (response.status === 401) {
                const refreshedData = await refresh();
                headers['Authorization'] = `${refreshedData.access_token}`;
                response = await fetch(url, { headers, ...otherOptions });
            }

            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.message || 'Error making authenticated request');
            }

            return data;
        } catch (error) {
            console.error('Error making authenticated request:', error);
            throw error;
        }
    }, [accessToken, refresh]);

    return (
        <AuthContext.Provider value={{ authFetch, role }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
