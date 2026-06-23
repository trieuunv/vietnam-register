import { useState } from 'react';

import styles from './StaffLayout.module.scss';
import Sidebar from './Sidebar';
import MobileHeader from './MobileHeader';
import { Outlet } from 'react-router-dom';

const StaffLayout = () => {
    const [sidebarOpen, setSidebarOpen] = useState(false)

    const toggleSidebar = () => {
        setSidebarOpen(!sidebarOpen)
    }

    return (
        <div className={styles.staffLayout}>
            <Sidebar isOpen={sidebarOpen} onClose={toggleSidebar} />
            <div className={styles.mainContent}>
                <MobileHeader onMenuClick={toggleSidebar} />
                <main className={styles.main}>
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default StaffLayout;