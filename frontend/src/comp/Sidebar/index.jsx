import './index.css'
import { NavLink } from 'react-router-dom'
import { useState } from 'react'

const Sidebar = () => {
    const [showNav, setShowNav] = useState(false)
    return(
        <header className="nav-bar">
            <div className="brand">
                <div className="brand-title">NBA Fantasy</div>
                <button
                    className="nav-toggle"
                    type="button"
                    onClick={() => setShowNav(s => !s)}
                    aria-label={showNav ? 'Close navigation' : 'Open navigation'}
                >
                    {showNav ? 'Close' : 'Menu'}
                </button>
            </div>

            <nav className={showNav ? 'nav mobile-show' : 'nav'}>
                <NavLink to="/" end className={({ isActive }) => isActive ? 'active' : undefined} onClick={() => setShowNav(false)}>
                    Home
                </NavLink>
                <NavLink to="/teams" className={({ isActive }) => isActive ? 'active' : undefined} onClick={() => setShowNav(false)}>
                    Teams
                </NavLink>
                <NavLink to="/position" className={({ isActive }) => isActive ? 'active' : undefined} onClick={() => setShowNav(false)}>
                    Positions
                </NavLink>
                <NavLink to="/search" className={({ isActive }) => isActive ? 'active' : undefined} onClick={() => setShowNav(false)}>
                    Search
                </NavLink>
            </nav>
        </header>
    )
}

export default Sidebar
