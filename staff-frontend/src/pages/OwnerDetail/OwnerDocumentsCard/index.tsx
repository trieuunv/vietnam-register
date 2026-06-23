import InfoCard from "@/components/InfoCard"
import styles from "./OwnerDocumentsCard.module.scss"
import { formatDate } from "@/utils/format"

interface Document {
    id: number
    type: string
    number: string
    issueDate: string
    expiryDate: string
    issuedBy: string
    status: string
}

interface OwnerDocumentsCardProps {
    documents: Document[]
}

export default function OwnerDocumentsCard({ documents }: OwnerDocumentsCardProps) {
    const getDocumentTypeText = (type: string) => {
        switch (type) {
            case "CCCD":
                return "Căn cước công dân"
            case "CMND":
                return "Chứng minh nhân dân"
            case "GPLX":
                return "Giấy phép lái xe"
            case "HC":
                return "Hộ chiếu"
            default:
                return type
        }
    }

    const getDocumentIcon = (type: string) => {
        switch (type) {
            case "CCCD":
            case "CMND":
                return "🪪"
            case "GPLX":
                return "🚗"
            case "HC":
                return "📘"
            default:
                return "📄"
        }
    }

    const getStatusClass = (status: string) => {
        switch (status.toLowerCase()) {
            case "active":
                return styles.statusActive
            case "expired":
                return styles.statusExpired
            case "pending":
                return styles.statusPending
            default:
                return ""
        }
    }

    const getStatusText = (status: string) => {
        switch (status.toLowerCase()) {
            case "active":
                return "Còn hiệu lực"
            case "expired":
                return "Hết hạn"
            case "pending":
                return "Đang xử lý"
            default:
                return status
        }
    }

    const getDaysRemaining = (expiryDate: string) => {
        const today = new Date()
        const expiry = new Date(expiryDate)
        const diffTime = expiry.getTime() - today.getTime()
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

        return diffDays
    }

    return (
        <InfoCard title="Giấy tờ">
            <div className={styles.documentsList}>
                {documents.map((document) => {
                    const daysRemaining = getDaysRemaining(document.expiryDate)
                    const isExpired = daysRemaining < 0

                    return (
                        <div key={document.id} className={styles.documentCard}>
                            <div className={styles.documentIcon}>{getDocumentIcon(document.type)}</div>
                            <div className={styles.documentInfo}>
                                <div className={styles.documentHeader}>
                                    <h3 className={styles.documentType}>{getDocumentTypeText(document.type)}</h3>
                                    <span className={`${styles.documentStatus} ${getStatusClass(document.status)}`}>
                                        {getStatusText(document.status)}
                                    </span>
                                </div>
                                <div className={styles.documentNumber}>
                                    <span className={styles.documentLabel}>Số:</span> {document.number}
                                </div>
                                <div className={styles.documentDates}>
                                    <div className={styles.documentDate}>
                                        <span className={styles.documentLabel}>Ngày cấp:</span> {formatDate(document.issueDate)}
                                    </div>
                                    <div className={styles.documentDate}>
                                        <span className={styles.documentLabel}>Ngày hết hạn:</span>{" "}
                                        <span className={`${styles.expiryValue} ${isExpired ? styles.expired : ""}`}>
                                            {formatDate(document.expiryDate)}
                                        </span>
                                        <span className={`${styles.daysRemaining} ${isExpired ? styles.expired : ""}`}>
                                            {isExpired ? `(Đã hết hạn ${Math.abs(daysRemaining)} ngày)` : `(Còn ${daysRemaining} ngày)`}
                                        </span>
                                    </div>
                                </div>
                                <div className={styles.documentIssuer}>
                                    <span className={styles.documentLabel}>Nơi cấp:</span> {document.issuedBy}
                                </div>
                            </div>
                        </div>
                    )
                })}
            </div>
        </InfoCard>
    )
}
