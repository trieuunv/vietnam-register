import React from 'react';

import styles from './LoadingScreen.module.scss';

const LoadingScreen = () => {
    return (
        <div className={styles.container}>
            <span className={styles.spinner}>
                
            </span>
            <p>Đang tải dữ liệu...</p>
        </div>
    );
};

export default LoadingScreen;