import React from 'react';

import styles from './ProductList.module.scss';

import Product from '../Product';
import { IProduct } from '@/types/common/Product.type';

const ProductList: React.FC<{ products: IProduct[] }> = (props) => { 
    const { products } = props;

    return (
        <div className={styles.container}>
            <div className={styles.wrapper}>
                {products.map((product, index) => (
                    <Product 
                        key={index}
                        name={product.name} 
                        imgUrl={product.imgUrl} 
                        price={product.price} 
                    />
                ))}
            </div>
        </div>
    );
};

export default ProductList;