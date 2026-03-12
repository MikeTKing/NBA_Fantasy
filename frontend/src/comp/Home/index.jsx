import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import AnimatedLetters from '../AnimatedLetters';
import './index.css';

const Home = () => {
    const [letterClass, setLetterClass] = useState('text-animate')
    const nameArray = "Welcome to".split("");
    const jobArray = "NBA Fantasy!".split("");

    useEffect(() => {
        const timerId = setTimeout(() => {
            setLetterClass('text-animate-hover');
        }, 4000);

        return () => {
            clearTimeout(timerId);
        };
    }, []);

    return(
        <>
            <div className="container home-page">
                <div className="text-zone">
                    <div className="hero-card">
                        <div className="hero-kicker">
                            <strong>2025-26</strong> Player stats explorer
                        </div>
                        <h1>
                            <AnimatedLetters letterClass={letterClass} strArray={nameArray} idx={12} />
                            <br />
                            <AnimatedLetters letterClass={letterClass} strArray={jobArray} idx={15} />
                        </h1>
                        <h2>Browse players by team, position, or search by name.</h2>
                        <Link to="/teams" className="flat-button">GET STARTED</Link>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Home
