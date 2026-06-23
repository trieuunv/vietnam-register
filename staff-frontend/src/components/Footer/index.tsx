import { Link } from 'react-router-dom';

import styles from "./Footer.module.scss";

const Footer = () => {
    return (
        <footer className={styles.footer}>
            <div className={styles.container}>
                <div className={styles.wrapper}>
                    <div className={styles.footerColumn}>
                        <div className={styles.logo}>
                            <Link to={'/'} >
                                <img src="./imgs/Spotify_Full_Logo_RGB_Black.png" alt="" />
                            </Link> 
                        </div>
                    </div>

                    <div className={styles.footerColumn}>
                        <ul>
                            <span className={styles.footerTitle}>CÔNG TY</span>
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Giới thiệu
                                </Link>
                            </li>
                            
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Việc làm
                                </Link>
                            </li>
                        </ul>
                    </div>

                    <div className={styles.footerColumn}>
                        <ul>
                            <span className={styles.footerTitle}>CỘNG ĐỒNG</span>
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Dành cho các nghệ sĩ
                                </Link>
                            </li>
                            
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Nhà phát triển
                                </Link>
                            </li>

                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Quảng cáo
                                </Link>
                            </li>

                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Nhà đầu tư
                                </Link>
                            </li>
                            
                        </ul>
                    </div>

                    <div className={styles.footerColumn}>
                        <ul>
                            <span className={styles.footerTitle}>LIÊN KẾT HỮU ÍCH</span>
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Hỗ trợ
                                </Link>
                            </li>
                            
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Trình phát Web
                                </Link>
                            </li>

                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Ứng dụng Di động Miễn phí
                                </Link>
                            </li>
                        </ul>
                    </div>

                    <div className={styles.footerColumn}>
                        <ul>
                            <span className={styles.footerTitle}>CÁC GÓI SPOTIFY</span>
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Premium Individual
                                </Link>
                            </li>
                            
                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Premium Student
                                </Link>
                            </li>

                            <li className={styles.footerMenu}>
                                <Link 
                                    to={'/'}  
                                >
                                    Spotify Free
                                </Link>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <div className={styles.copyright}>
                <p>© 2025 Spotify AB</p>
            </div>
        </footer>
    );
};

export default Footer;