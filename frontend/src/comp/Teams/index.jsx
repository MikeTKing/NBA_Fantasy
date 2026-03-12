import React, { useEffect, useMemo, useState } from "react";
import { Link } from 'react-router-dom';
import "./index.css";
import AnimatedLetters from "../AnimatedLetters";
import teams from "../../data/teams.json";

const SPECIAL_LOGO_BASE_BY_ID = {
    LAC: 'Los_Angeles_Clippers',     // name is "LA Clippers"
    MIN: 'Minnesota_Timerwolves',   // file has a typo
    POR: 'Portland_Trailblazers',   // file omits underscore
};

const slugBase = (name) => {
    if (!name) return '';
    return String(name)
        .trim()
        .replace(/&/g, 'and')
        .replace(/[.'’]/g, '')
        .replace(/\s+/g, '_');
};

const unique = (arr) => Array.from(new Set(arr.filter(Boolean)));

const logoCandidates = (team) => {
    const bases = [];
    if (SPECIAL_LOGO_BASE_BY_ID[team.id]) bases.push(SPECIAL_LOGO_BASE_BY_ID[team.id]);
    bases.push(slugBase(team.name));

    // Helpful extra guesses for edge cases.
    if (team.name?.startsWith('LA ')) bases.push(slugBase(team.name.replace(/^LA\s+/, 'Los Angeles ')));
    if (team.name?.includes('Trail Blazers')) bases.push(slugBase(team.name.replace('Trail Blazers', 'Trailblazers')));

    return unique(bases).flatMap((b) => [`/teams/${b}.png`, `/teams/${b}.jpg`]);
};

const TeamLogo = ({ team }) => {
    const candidates = useMemo(() => logoCandidates(team), [team.id, team.name]);
    const [idx, setIdx] = useState(0);

    if (!candidates.length || idx >= candidates.length) {
        const initials = (team.abbrev || team.id || 'TEAM').toUpperCase();
        return <div className="team-logo-fallback" aria-hidden="true">{initials}</div>;
    }

    return (
        <img
            className="team-logo"
            src={candidates[idx]}
            alt={`${team.name} logo`}
            loading="lazy"
            onError={() => setIdx((i) => i + 1)}
        />
    );
};

const Teams = () => {
    const [letterClass, setLetterClass] = useState('text-animate');
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredTeams, setFilteredTeams] = useState([]);

    useEffect(() => {
        const timer = setTimeout(() => {
            setLetterClass("text-animate-hover");
        }, 3000);

        return () => {
            clearTimeout(timer);
        }
    }, []);

    const filtered = useMemo(() => {
        const q = searchQuery.trim().toLowerCase();
        if (!q) return teams;
        return teams.filter(t => t.name.toLowerCase().includes(q) || t.abbrev.toLowerCase().includes(q));
    }, [searchQuery]);

    useEffect(() => {
        setFilteredTeams(filtered);
    }, [filtered]);

    const handleSearchChange = event => {
        setSearchQuery(event.target.value);
    };

    const renderTeam = (teamsToRender) => {
        return (
            <div className="images-container">
                {teamsToRender.map((team) => (
                    <div
                        key={team.id}
                        className="image-box"
                        style={{
                            borderColor: team.colors?.[0] || '#ddd',
                        }}
                    >
                        <div className="content">
                            <div className="team-row">
                                <div
                                    className="team-logo-wrap"
                                    style={{
                                        background: `linear-gradient(135deg, ${team.colors?.[0] || 'rgba(255,255,255,0.18)'}, ${team.colors?.[1] || 'rgba(255,255,255,0.06)'})`,
                                    }}
                                >
                                    <TeamLogo team={team} />
                                </div>
                                <div className="team-meta">
                                    <p className="title">{team.name}</p>
                                    <p className="subtitle">{team.conference} {team.division}</p>
                                </div>
                            </div>
                            <div className="team-actions">
                                <Link className="btn" to={`/data?team=${encodeURIComponent(team.name)}`}>
                                    View
                                </Link>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        )
    }
    return (
        <>
            <div className="container teams-page">
                <h1 className = "page-title">
                    <AnimatedLetters letterClass = {letterClass} strArray={"Teams".split("")} idx={15}/>
                </h1>
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Search teams (e.g. Celtics, BOS)"
                        value={searchQuery}
                        onChange={handleSearchChange}
                    />
                </div>
                <div>{renderTeam(filteredTeams)}</div>
            </div>
        </>
    );
}

export default Teams
