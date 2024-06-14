import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { ukUA } from '@mui/x-data-grid/locales';
import TextField from '@mui/material/TextField';
import './GeneralStatistic.css';

const columns = [
    {
        field: 'ranking',
        headerName: 'Рейтинг',
        flex: 1,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center',
        align: 'center',
        sortable: false,
        filterable: false,
    },
    {
        field: 'login',
        headerName: 'Користувач',
        flex: 2,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center',
        align: 'center',
        sortable: false,
        filterable: false,
        renderCell: (params) => {

            const isTop3 = params.row.ranking <= 3;

            let medalType;
            switch (params.row.ranking) {
                case 1:
                    medalType = '🥇'; // Золота медаль
                    break;
                case 2:
                    medalType = '🥈'; // Срібна медаль
                    break;
                case 3:
                    medalType = '🥉'; // Бронзова медаль
                    break;
                default:
                    medalType = ''; // Жодна медаль для решти
            }

            return (
                <div>
                    {isTop3 && <span>{medalType}</span>}
                    {params.value}
                </div>
            );
        },
    },
    {
        field: 'coins_total',
        headerName: 'Кількість монет',
        flex: 2,
        headerClassName: 'super-app-theme--header',
        headerAlign: 'center',
        align: 'center'
    },
];

function GeneralStatistic() {
    const [rows, setRows] = useState([]);
    const [filteredRows, setFilteredRows] = useState([]);
    const [search, setSearch] = useState('');

    useEffect(() => {
        fetch('https://wordle-4fel.onrender.com/user/generalStatistic')
            .then((response) => response.json())
            .then((data) => {
                // Генеруємо рейтинг для кожного рядка
                const rankedData = data.map((item, index) => ({ ...item, ranking: index + 1 }));
                setRows(rankedData);
                setFilteredRows(rankedData); // Встановлюємо початковий стан для фільтрованих рядків
            })
            .catch((error) => console.error('Error fetching user data:', error));
    }, []);

    useEffect(() => {
        // Фільтрація рядків за логіном
        setFilteredRows(
            rows.filter(row => row.login.toLowerCase().includes(search.toLowerCase()))
        );
    }, [search, rows]);

    return (
        <div>
            <h1 className='administration-header' style={{ textAlign: 'center' }}>РЕЙТИНГ ГРАВЦІВ</h1>
            <div style={{ height: 'auto', width: '20%', paddingLeft: '20%' }}>
                <TextField
                    label="Пошук за логіном"
                    variant="outlined"
                    fullWidth
                    style={{ marginBottom: '1rem' }}
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
            </div>
            {filteredRows.length === 0 ? (
                <p style={{ textAlign: 'center' }}>Немає результатів</p>
            ) : (
                <div style={{ height: 'auto', width: '60%', paddingLeft: '20%' }}>
                    <DataGrid
                        localeText={ukUA.components.MuiDataGrid.defaultProps.localeText}
                        rows={filteredRows}
                        columns={columns}
                        getRowId={(row) => row.user_id}
                        initialState={{ pagination: { paginationModel: { page: 0, pageSize: 5 } } }}
                        pageSizeOptions={[5, 10]}
                        disableSelectionOnClick
                        className="data-grid"
                    />
                </div>
            )}
        </div>
    );
}

export default GeneralStatistic;
