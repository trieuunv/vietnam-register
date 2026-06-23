import React from 'react';
import { Link } from 'react-router-dom';

import styles from './ProductSection.module.scss';
import ProductList from '@/components/ProductList';
import { IProduct } from '@/types/common/Product.type';

interface ProductSectionProps {
    title: string,
    products: IProduct[]
}

const ProductSection: React.FC<ProductSectionProps> = (props) => {
    const { title, products } = props;

    return (
        <div className={styles.container}>
            <div className={styles.wrapper}>
                <div className={styles.title}>
                    <h3>{title}</h3>
                    <Link to={'/'}>
                        Xem tất cả
                    </Link>   
                </div>

                <ProductList products={products} />   
            </div>
        </div>
    );
};

export default ProductSection;