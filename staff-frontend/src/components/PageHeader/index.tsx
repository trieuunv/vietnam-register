import React, { ReactElement } from 'react';

import styles from './PageHeader.module.scss';
import { Button } from 'antd';

import { ArrowLeft } from 'lucide-react';

interface PageHeaderProps {
    title: string,
    children?: ReactElement,
    onBack?: () => void
}

const PageHeader: React.FC<PageHeaderProps> = ({ title, children, onBack }) => {
    return (
        <div className={styles.pageHeader}>
            <div className={styles.headerLeft}>
                {onBack && (
                    <Button color="default" variant="text" onClick={onBack}>
                        <ArrowLeft size={16} />
                    </Button>
                )}
                <div className={styles.pageTitle}>{title}</div>
            </div>
            {children}
        </div>
    );
};

export default PageHeader;