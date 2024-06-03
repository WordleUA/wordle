import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { FormControl, MenuItem, Select } from '@mui/material';
import { ukUA } from '@mui/x-data-grid/locales';
import './Administration.css';

const columns = [
    {
        field: 'login',
        headerName: 'Логін',
        width: 150,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'email',
        headerName: 'Пошта',
        width: 200,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'role',
        headerName: 'Роль',
        width: 150,
        renderCell: (params) => <RoleDropdown {...params} />, // Render RoleDropdown component
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'coins_total',
        headerName: 'Кількість монет',
        type: 'number',
        width: 200,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'game_lose_count',
        headerName: 'Кількість програних ігор',
        type: 'number',
        width: 250,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'game_win_count',
        headerName: 'Кількість виграних ігор',
        type: 'number',
        width: 231,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'block',
        headerName: 'Блокування',
        width: 190,

        headerClassName: 'super-app-theme--header',
        headerAlign: 'center',
        sortable: false,
        filterable: false,
        description: 'Кнопка блокування користувача'
    },
];

function Administration() {
    const [rows, setRows] = useState([]);

    useEffect(() => {
        // Fetch your user data here
        const userData = [
            { id: 1, login: 'user1', email: 'user1@example.com', role: 'PLAYER', coins_total: 100, game_lose_count: 5, game_win_count: 10 },
            { id: 2, login: 'user2', email: 'user2@example.com', role: 'PLAYER', coins_total: 50, game_lose_count: 3, game_win_count: 8 },
            { id: 3, login: 'user3', email: 'user3@example.com', role: 'ADMIN', coins_total: 500, game_lose_count: 2, game_win_count: 15 },
            { id: 4, login: 'user4', email: 'user4@example.com', role: 'PLAYER', coins_total: 200, game_lose_count: 7, game_win_count: 12 },
            { id: 5, login: 'user5', email: 'user5@example.com', role: 'PLAYER', coins_total: 150, game_lose_count: 4, game_win_count: 9 },
            { id: 6, login: 'user6', email: 'user6@example.com', role: 'PLAYER', coins_total: 300, game_lose_count: 6, game_win_count: 11 }
        ];
        setRows(userData);
    }, []);

    return (
        <div style={{ height: 400, width: '90%', paddingLeft: '5%' }}>
            <h1 className='administration-header' style={{ textAlign: 'center' }}>СПИСОК КОРИСТУВАЧІВ</h1>
            <DataGrid
                localeText={ukUA.components.MuiDataGrid.defaultProps.localeText}
                rows={rows}
                columns={columns}
                getRowId={(row) => row.id}
                pageSizeOptions={[5, 10]}
                disableSelectionOnClick
            />
        </div>
    );
}

export default Administration;

function RoleDropdown({ id, value, row }) {
    const [role, setRole] = useState(value);

    const handleChange = (event) => {
        const newRole = event.target.value;
        setRole(newRole);
    };

    return (
        <FormControl variant="standard" fullWidth>
            <Select value={role} onChange={handleChange} displayEmpty>
                <MenuItem value="PLAYER">PLAYER</MenuItem>
                <MenuItem value="ADMIN">ADMIN</MenuItem>
            </Select>
        </FormControl>
    );
}
