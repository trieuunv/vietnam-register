import React from 'react';

import styles from './CustomTag.module.scss';
import { Tag } from 'antd';

interface CustomTagProps {
    content?: string,
}

const CustomTag: React.FC<CustomTagProps> = ({ content }) => {
    return (
        <Tag className={styles.customTag}>
            {content}
        </Tag>
    );
};

export default CustomTag;