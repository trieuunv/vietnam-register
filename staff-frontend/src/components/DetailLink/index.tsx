import { Link } from 'react-router-dom';

import styles from './DetailLink.module.scss';
import { ArrowRight } from 'lucide-react';

interface IDetailLinkProps {
    url: string
}

const DetailLink = ({ url }: IDetailLinkProps) => {
    return (
        <Link to={url} className={styles.link}>Chi tiết <ArrowRight size={14} /></Link>
    );
};

export default DetailLink;