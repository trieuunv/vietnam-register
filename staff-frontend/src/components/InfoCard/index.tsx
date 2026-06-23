import { ReactNode } from 'react';

import styles from './InfoCard.module.scss';

interface IInfoCardProps {
    title: string,
    children: ReactNode,
    rightElement?: ReactNode
}

const InfoCard = ({ title, children, rightElement }: IInfoCardProps) => {
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2>{title}</h2>
                {rightElement && rightElement}
            </div>
            <div className={styles.content}>
                {children}
            </div>
        </div>
    );
};

export default InfoCard;