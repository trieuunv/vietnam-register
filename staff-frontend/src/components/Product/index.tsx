import React from 'react';
import { Link } from 'react-router-dom';

import styles from './Product.module.scss'

import { IProduct } from '@/types/common/Product.type';

const Product: React.FC<IProduct> = (product) => {
    return (
        <div className={styles.container} >
            <Link to={'/'}>
                <div className={styles.wrapper}>
                    <div className={styles.thumbnail}>
                        <img src={product.imgUrl} alt="" />
                    </div>

                    <div className={styles.description}>
                        <h4 className={styles.name}>{product.name}</h4>
                        <span className={styles.price}>{product.price}</span>
                    </div>
                </div>
            </Link>
        </div>
    );
};

export default Product;