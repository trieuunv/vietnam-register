import { useState } from "react"
import styles from "./CodeCard.module.scss"

interface ConfirmationCodeProps {
    code: string
}

const CodeCard = ({ code }: ConfirmationCodeProps) => {
    const [copied, setCopied] = useState(false)

    const copyToClipboard = () => {
        navigator.clipboard.writeText(code)
        setCopied(true)
        setTimeout(() => setCopied(false), 2000)
    }

    return (
        <div className={styles.container}>
            <code>{code}</code>
            <button className={styles.copyButton} onClick={copyToClipboard}>
                <span>{copied ? "✓" : "📋"}</span>
                <span className={styles.srOnly}>Copy code</span>
            </button>
        </div>
    )
}

export default CodeCard;
