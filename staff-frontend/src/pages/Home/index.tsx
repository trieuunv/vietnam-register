import styles from './Home.module.scss';
import Banner from './components/Banner';
import ProductSection from './components/ProductSection';

const Home = () => {
    const slides = [
        { url: 'http://127.0.0.1:8000/storage/images/1.png', target: '' },
        { url: 'http://127.0.0.1:8000/storage/images/2.png', target: '' },
        { url: 'http://127.0.0.1:8000/storage/images/3.png', target: '' },
    ];

    const products = [
        { name: "Túi Chống Sốc Bảo Vệ Laptop 2 ngăn.", imgUrl: "http://127.0.0.1:8000/storage/images/jus.jpg", price: "200.000" },
        { name: "Túi Chống Sốc Bảo Vệ Laptop 2 ngăn.", imgUrl: "http://127.0.0.1:8000/storage/images/jus.jpg", price: "200.000" },
        { name: "Túi Chống Sốc Bảo Vệ Laptop 2 ngăn.", imgUrl: "http://127.0.0.1:8000/storage/images/jus.jpg", price: "200.000" },
        { name: "Túi Chống Sốc Bảo Vệ Laptop 2 ngăn.", imgUrl: "http://127.0.0.1:8000/storage/images/jus.jpg", price: "200.000" },
        { name: "Túi Chống Sốc Bảo Vệ Laptop 2 ngăn.", imgUrl: "http://127.0.0.1:8000/storage/images/jus.jpg", price: "200.000" },
        { name: "Túi Chống Sốc Bảo Vệ Laptop 2 ngăn.", imgUrl: "http://127.0.0.1:8000/storage/images/jus.jpg", price: "200.000" },
    ]

    return (
        <div className={styles.container}>
            <Banner slides={slides} />
            <ProductSection 
                title='Sản Phẩm' 
                products={products} 
            />
        </div>
    );
};

export default Home;