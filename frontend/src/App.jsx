import React, { useEffect } from 'react';
import './App.css';
import { Route, Routes } from 'react-router-dom';
import Layout from './comp/Layout';
import Home from './comp/Home';
import Teams from './comp/Teams';
import TeamData from './comp/TeamData';
import Position from "./comp/Position";
import Search from "./comp/Search";

function App() {
    useEffect(() => {
        document.title = 'Nba Fantasy';
    }, []);

    return (
        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<Home />} />
                <Route path="teams" element={<Teams />} />
                <Route path="data" element={<TeamData />} />
                <Route path="position" element={<Position />} />
                <Route path="search" element={<Search />} />
            </Route>
        </Routes>
    );
}

export default App;