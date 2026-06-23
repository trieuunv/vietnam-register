import type { ReactNode } from "react"
import styles from "./InfoRow.module.scss"

interface IInfoRowProps {
    children: ReactNode
}

const InfoRow = ({ children }: IInfoRowProps) => {
    return <div className={styles.container}>{children}</div>
}

export default InfoRow;