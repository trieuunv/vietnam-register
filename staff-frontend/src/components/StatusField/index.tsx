import styles from './StatusField.module.scss';
import StatusTag from '../StatusTag';

interface StatusFieldProps {
    status: string,
}

const StatusField = ({ status }: StatusFieldProps) => {
    return (
        <div className={styles.container}>
            <span className={styles.statusLabel}>Trạng thái:</span>
            <StatusTag status={status} />
        </div>
    );
};

export default StatusField;