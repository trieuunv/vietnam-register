import { Link } from "react-router-dom";
import styles from "./Navbar.module.scss";

import { Search, User, Notification } from '@/icons';

const Navbar = () => {
    return (
        <nav className={styles.nav}>
            <Link
                to={'/'}
                className={styles.logo}
            >
                <img src="./imgs/Spotify_Full_Logo_RGB_Black.png" alt="" />
            </Link>

            <ul className={styles.leftNavigation}>
                <li className={styles.tabNavigation}>
                    <Link to={'/'} >Showcase</Link>
                </li>

                <li className={styles.tabNavigation}>
                    <Link to={'/'} >Showcase</Link>
                </li>

                <li className={styles.tabNavigation}>
                    <Link to={'/'} >Showcase</Link>
                </li>
            </ul>

            <div className={styles.rightNavigation}>
                <div className={styles.navElement}>
                    <Link to={'/'} >
                        <User />
                    </Link>
                </div>

                <div className={styles.navElement}>
                    <Link to={'/'} >
                        <Notification />
                    </Link>
                </div>

                <div className={styles.navElement}>
                    <button>
                        <Search />
                    </button>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;