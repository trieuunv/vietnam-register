import React from 'react';

import styles from './MobileHeader.module.scss';
import { Menu } from 'lucide-react';

interface MobileHeaderProps {
    onMenuClick: () => void
}

const MobileHeader: React.FC<MobileHeaderProps> = ({ onMenuClick }) => {
    return (
        <div className={styles.mobileHeader}>
            <button onClick={onMenuClick} className={styles.menuButton}>
                <Menu className={styles.icon} />
            </button>
            <h2 className={styles.mobileTitle}>Đăng Kiểm Xe Máy</h2>
        </div>
    );
};

export default MobileHeader;