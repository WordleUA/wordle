import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Select, MenuItem, FormControl } from '@mui/material';
import { ukUA } from '@mui/x-data-grid/locales';
import './Administration.css';

const columns = (handleUpdateRow) => [
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
        renderCell: (params) => <RoleDropdown {...params} handleUpdateRow={handleUpdateRow} />,
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
        width: 200,
        renderCell: (params) => <BlockButton {...params} handleUpdateRow={handleUpdateRow} />,
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
        fetchUsers();
    }, []);

    const fetchUsers = () => {
        fetch('https://wordle-4fel.onrender.com/user/usersByAdmin')
            .then((response) => response.json())
            .then((data) => {
                const rowsWithIds = data.map((row, index) => ({ ...row, id: index }));
                setRows(rowsWithIds);
            })
            .catch((error) => console.error('Error fetching user data:', error));
    };

    const handleUpdateRow = (id, updates) => {
        setRows((prevRows) => prevRows.map((row) => (row.id === id ? { ...row, ...updates } : row)));
    };

    return (
        <div style={{ height: 400, width: '90%', paddingLeft: '5%' }}>
            <h1 className='administration-header' style={{ textAlign: 'center' }}>СПИСОК КОРИСТУВАЧІВ</h1>
            <DataGrid
                localeText={ukUA.components.MuiDataGrid.defaultProps.localeText}
                rows={rows}
                columns={columns(handleUpdateRow)}
                getRowId={(row) => row.id}
                initialState={{ pagination: { paginationModel: { page: 0, pageSize: 5 } } }}
                pageSizeOptions={[5, 10]}
                disableSelectionOnClick
            />
        </div>
    );
}

export default Administration;

function RoleDropdown({ id, value, row, handleUpdateRow }) {
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

function BlockButton({ id, row, handleUpdateRow }) {
    return (
        <div style={{ textAlign: 'center' }}>
            <button className='block-button'>Заблокувати</button>
        </div>
    );
}
