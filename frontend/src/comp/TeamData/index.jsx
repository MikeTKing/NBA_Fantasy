// src/components/TeamData/index.jsx (or TeamData.js)
import React, { useState, useEffect } from 'react';
import "./index.css";
import AnimatedLetters from "../AnimatedLetters";

const TeamData = () => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [playerData, setPlayerData] = useState([]);
    const [playersToShow, setPlayersToShow] = useState(10);
    const [letterClass] = useState('text-animate');
    const [exportHref, setExportHref] = useState('/api/players/export');

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const teamValue     = params.get('team');
        const positionValue = params.get('position');
        const nameValue     = params.get('name');

        let url = '/api/players'; // proxied by Vite in dev
        let exportUrl = '/api/players/export';

        if (teamValue) {
            url += `?team=${encodeURIComponent(teamValue)}`;
            exportUrl += `?team=${encodeURIComponent(teamValue)}`;
        } else if (positionValue) {
            url += `?position=${encodeURIComponent(positionValue)}`;
            exportUrl += `?position=${encodeURIComponent(positionValue)}`;
        } else if (nameValue) {
            url += `?name=${encodeURIComponent(nameValue)}`;
            exportUrl += `?name=${encodeURIComponent(nameValue)}`;
        }

        setExportHref(exportUrl);

        fetch(url, { headers: { 'Accept': 'application/json' } })
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
            .catch(err => {
                setError(err?.message || 'Failed to load player data');
                setLoading(false);
            });
    }, []);

    const downloadCsv = async () => {
        const res = await fetch(exportHref, { headers: { 'Accept': 'text/csv' } });
        if (!res.ok) {
            const text = await res.text().catch(() => '');
            throw new Error(`CSV export failed (${res.status}): ${text || res.statusText}`);
        }

        const blob = await res.blob();
        const cd = res.headers.get('content-disposition') || '';
        const match = cd.match(/filename="([^"]+)"/i);
        const filename = match?.[1] || 'players.csv';

        const a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(a.href);
    };

    if (loading) {
        return <p className="loading">Loading player data...</p>;
    }

    if (error) {
        return <p className="error">Error: {error}</p>;
    }

    return (
        <div className={`fade-in ${loading ? 'loading' : ''}`}>
            <div className="table-container">
                <h1 className="page-title">
                    <AnimatedLetters
                        letterClass={letterClass}
                        strArray={"Player Data".split("")}
                        idx={12}
                    />
                </h1>

                <div className="actions">
                    <button
                        type="button"
                        className="download-button"
                        onClick={() => downloadCsv().catch(e => setError(e?.message || 'CSV export failed'))}
                    >
                        Download CSV
                    </button>
                </div>

                {playerData.length === 0 ? (
                    <p>No players found matching your criteria.</p>
                ) : (
                    <>
                        <table>
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Team</th>
                                <th>Position</th>
                                <th>Age</th>
                                <th>PTS</th>
                                <th>REB</th>
                                <th>AST</th>
                                <th>STL</th>
                                <th>BLK</th>
                                <th>TO</th>
                                <th>PF</th>
                            </tr>
                            </thead>
                            <tbody>
                            {playerData.slice(0, playersToShow).map(player => (
                                <tr key={player.name}>
                                    <td>{player.name}</td>
                                    <td>{player.team}</td>
                                    <td>{player.position}</td>
                                    <td>{player.age ?? '-'}</td>
                                    <td>{player.pts ?? '-'}</td>
                                    <td>{player.reb ?? '-'}</td>
                                    <td>{player.ast ?? '-'}</td>
                                    <td>{player.stl ?? '-'}</td>
                                    <td>{player.blk ?? '-'}</td>
                                    <td>{player.to ?? '-'}</td>
                                    <td>{player.pf ?? '-'}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>

                        {playersToShow < playerData.length && (
                            <button
                                onClick={() => setPlayersToShow(playersToShow + 10)}
                                className="show-more-button"
                                style={{ margin: '20px auto', display: 'block' }}
                            >
                                Show More
                            </button>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};

export default TeamData;
