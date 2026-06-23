import React from 'react';

import styles from './Loading.module.scss';
import { Spin } from 'antd';

const LoadingScreen = () => {
    return (
        <div className={styles.container}>
            <Spin />
        </div>
    );
};

export default LoadingScreen;