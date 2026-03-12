import React, { useEffect, useMemo, useState } from "react";
import { Link } from 'react-router-dom';
import "./index.css";
import AnimatedLetters from "../AnimatedLetters";
import positions from "../../data/positions.json";

const Positions = () => {
    const [letterClass, setLetterClass] = useState('text-animate');
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredPositions, setFilteredPositions] = useState([]);

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
        if (!q) return positions;
        return positions.filter(p =>
            p.name.toLowerCase().includes(q) || p.abbrev.toLowerCase().includes(q)
        );
    }, [searchQuery]);

    useEffect(() => {
        setFilteredPositions(filtered);
    }, [filtered]);

    const handleSearchChange = event => {
        setSearchQuery(event.target.value);
    };

    const renderPosition = (positionsToRender) => {
        return (
            <div className="images-container">
                {positionsToRender.map((position) => (
                    <div key={position.id} className="image-box">
                        <div className="content">
                            <p className="title">{position.name} ({position.abbrev})</p>
                            <p className="subtitle">{position.description}</p>
                            <Link className="btn" to={`/data?position=${encodeURIComponent(position.abbrev)}`}>
                                View
                            </Link>
                        </div>
                    </div>
                ))}
            </div>
        )
    };

    return (
        <>
            <div className="container position-page">
                <h1 className="page-title">
                    <br/>
                    <AnimatedLetters letterClass={letterClass} strArray={"Positions".split("")} idx={15}/>
                </h1>
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Search positions (e.g. PG, Center)"
                        value={searchQuery}
                        onChange={handleSearchChange}
                    />
                </div>
                <div>{renderPosition(filteredPositions)}</div>
            </div>
        </>
    );
}

export default Positions;
