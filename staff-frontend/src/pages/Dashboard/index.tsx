import React from 'react';

import styles from './Dashboard.module.scss';
import PageHeader from '@/components/PageHeader';

const Dashboard = () => {
    return (
        <div className={styles.dashboardContainer}>
            <PageHeader title='Bảng điều khiển' />

        </div>
    );
};

export default Dashboard;   