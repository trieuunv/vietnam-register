import React, { ReactNode } from 'react';

import styles from './PageContent.module.scss';

interface IPageContentProps {
    children: ReactNode
}

const PageContent = ({ children }: IPageContentProps) => {
    return (
        <div className={styles.container}>
            {children}
        </div>
    );
};

export default PageContent;