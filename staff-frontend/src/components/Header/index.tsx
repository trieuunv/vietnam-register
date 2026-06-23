import styles from "./Header.module.scss";

import Navbar from "../Navbar";

const Header = () => {
    return (
        <header className={styles.header}>
            <div className={styles.container}>
                <Navbar />
            </div>
        </header>
    );
};

export default Header;