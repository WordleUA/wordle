import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Select, MenuItem, FormControl } from '@mui/material';
import { ukUA } from '@mui/x-data-grid/locales';
import './Administration.css';

const columns = (handleUpdateRow) => [
    {
        field: 'login',
        headerName: 'Логін',
        flex: 1,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'email',
        headerName: 'Пошта',
        flex: 2,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'role',
        headerName: 'Роль',
        flex: 0.5,
        renderCell: (params) => <RoleDropdown {...params} handleUpdateRow={handleUpdateRow} />,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'coins_total',
        headerName: 'Кількість монет',
        type: 'number',
        flex: 1,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'game_lose_count',
        headerName: 'Кількість програних ігор',
        type: 'number',
        flex: 1,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'game_win_count',
        headerName: 'Кількість виграних ігор',
        type: 'number',
        flex: 1,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center'
    },
    {
        field: 'block',
        headerName: 'Блокування',
        flex: 1,
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
                const rowsWithIds = data.map((row) => ({ ...row, id: row.user_id }));
                setRows(rowsWithIds);
            })
            .catch((error) => console.error('Error fetching user data:', error));
    };

    const handleUpdateRow = (id, updates) => {
        setRows((prevRows) => prevRows.map((row) => (row.id === id ? { ...row, ...updates } : row)));
    };

    return (
        <div>
            <h1 className='administration-header'>СПИСОК КОРИСТУВАЧІВ</h1>
            <div style={{ height: 400, width: '90%', paddingLeft: '5%' }}>
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
        </div>
    );
}

export default Administration;

function RoleDropdown({ id, value, row, handleUpdateRow }) {
    const [role, setRole] = useState(value);
    const { is_banned } = row;

    const handleChange = (event) => {
        const newRole = event.target.value;
        setRole(newRole);

        fetch(`https://wordle-4fel.onrender.com/user/role/${id}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ role: newRole }),
        })
            .then((response) => {
                if (!response.ok) throw new Error('Failed to update role');
                handleUpdateRow(id, { role: newRole });
            })
            .catch((error) => console.error('Error updating role:', error));
    };

    return (
        <FormControl variant="standard" fullWidth>
            <Select value={role} onChange={handleChange} displayEmpty disabled={is_banned}>
                <MenuItem value="PLAYER">PLAYER</MenuItem>
                <MenuItem value="ADMIN">ADMIN</MenuItem>
            </Select>
        </FormControl>
    );
}

function BlockButton({ id, row, handleUpdateRow }) {
    const { is_banned, role } = row;
    const [isBanned, setIsBanned] = useState(is_banned);

    const handleBlockToggle = () => {
        fetch(`https://wordle-4fel.onrender.com/user/block/${id}`, { method: 'PATCH' })
            .then((response) => {
                if (!response.ok) throw new Error('Failed to toggle block status');
                setIsBanned(!isBanned);
                handleUpdateRow(id, { is_banned: !isBanned });
            })
            .catch((error) => console.error('Error toggling block status:', error));
    };

    if (role === 'ADMIN') return null;

    return (
        <div style={{ textAlign: 'center' }}>
            <button
                className={isBanned ? 'unblock-button' : 'block-button'}
                onClick={handleBlockToggle}
            >
                {isBanned ? 'Розблокувати' : 'Заблокувати'}
            </button>
        </div>
    );
}
