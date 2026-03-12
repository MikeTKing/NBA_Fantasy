import React, { useState, useEffect } from 'react';
import "./index.css";

const DataHandling = () => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [playerData, setPlayerData] = useState([]);

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const teamValue = params.get('team');

        if (teamValue) {
            fetch(`/api/players?team=${encodeURIComponent(teamValue)}`, { headers: { 'Accept': 'application/json' } })
                .then(async (res) => {
                    if (!res.ok) {
                        const text = await res.text().catch(() => '');
                        throw new Error(`Request failed (${res.status}): ${text || res.statusText}`);
                    }
                    return res.json();
                })
                .then(data => {
                    setPlayerData(Array.isArray(data) ? data : []);
                    setLoading(false);
                })
                .catch(error => {
                    setError(error);
                    setLoading(false);
                });
        } else {
            setLoading(false);
        }
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>Error: {error.message}</p>;
    }


    return (
        <div className = "table-container">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Position</th>
                    <th>Age</th>
                    <th>Matches Played</th>
                    <th>Starts</th>
                    <th>Minutes Played</th>
                    <th>Goals</th>
                    <th>Assists</th>
                    <th>Penalties Kicked</th>
                    <th>Yellow Cards</th>
                    <th>Red Cards</th>
                    <th>Expected Goals (xG)</th>
                    <th>Expected Assists (xAG)</th>
                    <th>Team</th>
                </tr>
                </thead>
                <tbody>
                {playerData.map(player => (
                    <tr key={player.name}>
                        <td>{player.name}</td>
                        <td>{player.position}</td>
                        <td>{player.age}</td>
                        <td>{player.team}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );

};

export default DataHandling;
