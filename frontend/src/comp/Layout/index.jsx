import './index.css';
import Sidebar from '../Sidebar'
import { Outlet } from 'react-router-dom'

const Layout = () => {
    return(
        <div className="AppShell">
            <Sidebar />
            <div className="page">
                <Outlet />
            </div>
        </div>
    )
}

export default Layout
