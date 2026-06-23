import type { ReactNode } from "react"
import styles from "./InfoItem.module.scss"

interface IInfoItemProps {
    label: string
    fullWidth?: boolean
    children: ReactNode
}

const InfoItem = ({ label, fullWidth = false, children }: IInfoItemProps) => {
    return (
        <div className={`${styles.item} ${fullWidth ? styles.fullWidth : ""}`}>
            <span className={styles.label}>{label}</span>
            {typeof children === "string" ? <span className={styles.value}>{children}</span> : children}
        </div>
    )
}

export default InfoItem;
