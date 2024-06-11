import React, {useEffect, useState} from 'react';
import {DataGrid} from '@mui/x-data-grid';
import {Select, MenuItem, FormControl} from '@mui/material';
import {ukUA} from '@mui/x-data-grid/locales';
import './Administration.css';
import {useAuth} from "../Auth/AuthContext";

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
        renderCell: (params) => <RoleDropdown {...params} handleUpdateRow={handleUpdateRow}/>,
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
        renderCell: (params) => <BlockButton {...params} handleUpdateRow={handleUpdateRow}/>,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center',
        sortable: false,
        filterable: false,
        description: 'Кнопка блокування користувача'
    },
];

function Administration() {
    const [rows, setRows] = useState([]);
    const {authFetch} = useAuth();

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = () => {
        authFetch('https://wordle-4fel.onrender.com/user/usersByAdmin')
            .then((data) => {
                if (!data.error) {
                    const rowsWithIds = data.map((row) => ({...row, id: row.user_id}));
                    setRows(rowsWithIds);
                } else {
                    console.error('Error fetching user data:', data.error);
                }
            }).catch(error => {
            console.error('Error fetching user data:', error);
        })
    };

    const handleUpdateRow = (id, updates) => {
        setRows((prevRows) => prevRows.map((row) => (row.id === id ? {...row, ...updates} : row)));
    };

    return (
        <div>
            <h1 className='administration-header'>СПИСОК КОРИСТУВАЧІВ</h1>
            <div style={{height: 400, width: '90%', paddingLeft: '5%'}}>
                <DataGrid
                    localeText={ukUA.components.MuiDataGrid.defaultProps.localeText}
                    rows={rows}
                    columns={columns(handleUpdateRow)}
                    getRowId={(row) => row.id}
                    initialState={{pagination: {paginationModel: {page: 0, pageSize: 5}}}}
                    pageSizeOptions={[5, 10]}
                    disableSelectionOnClick
                />
            </div>
        </div>
    );
}

export default Administration;

function RoleDropdown({id, value, row, handleUpdateRow}) {
    const [role, setRole] = useState(value);
    const {is_banned} = row;
    const {authFetch} = useAuth();

    const handleChange = (event) => {
        const newRole = event.target.value;
        setRole(newRole);
        authFetch('https://wordle-4fel.onrender.com/user/role', {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({role: newRole, id: id})
        }).then((data) => {
            if (!data.error) {
                handleUpdateRow(id, {role: newRole});
            } else {
                console.error('Error updating user role:', data.error);
            }
        }).catch(error => {
            console.error('Error updating user role:', error);
        })
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

function BlockButton({id, row, handleUpdateRow}) {
    const {is_banned, role} = row;
    const [isBanned, setIsBanned] = useState(is_banned);
    const {authFetch} = useAuth();

    const handleBlockToggle = () => {
        authFetch('https://wordle-4fel.onrender.com/user/block', {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({id: id})
        }).then((data) => {
            if (!data.error) {
                setIsBanned(!isBanned);
                handleUpdateRow(id, {is_banned: !isBanned});
            } else {
                console.error('Error updating user block status:', data.error);
            }
        }).catch(error => {
            console.error('Error updating user block status:', error);
        })
    };

    if (role === 'ADMIN') return null;

    return (
        <div style={{textAlign: 'center'}}>
            <button
                className={isBanned ? 'unblock-button' : 'block-button'}
                onClick={handleBlockToggle}
            >
                {isBanned ? 'Розблокувати' : 'Заблокувати'}
            </button>
        </div>
    );
}
