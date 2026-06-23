import InfoCard from "@/components/InfoCard"
import styles from "./InspectionStatisticsCard.module.scss"
import { formatDate } from "@/utils/format"

interface InspectionStatisticsCardProps {
    totalInspections: number
    passedCount: number
    conditionalCount: number
    failedCount: number
    lastInspectionDate: string | null
    nextInspectionDate: string | null
}

export default function InspectionStatisticsCard({
    totalInspections,
    passedCount,
    conditionalCount,
    failedCount,
    lastInspectionDate,
    nextInspectionDate,
}: InspectionStatisticsCardProps) {
    const passPercentage = Math.round((passedCount / totalInspections) * 100) || 0
    const conditionalPercentage = Math.round((conditionalCount / totalInspections) * 100) || 0
    const failPercentage = Math.round((failedCount / totalInspections) * 100) || 0

    // Tính số ngày còn lại đến kỳ đăng kiểm tiếp theo
    const getDaysRemaining = () => {
        if (!nextInspectionDate) return null

        const today = new Date()
        const nextDate = new Date(nextInspectionDate)
        const diffTime = nextDate.getTime() - today.getTime()
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

        return diffDays
    }

    const daysRemaining = getDaysRemaining()

    // Xác định trạng thái của kỳ đăng kiểm tiếp theo
    const getNextInspectionStatus = () => {
        if (!daysRemaining) return null

        if (daysRemaining < 0) {
            return { text: "Đã quá hạn", className: styles.expired }
        } else if (daysRemaining <= 30) {
            return { text: "Sắp đến hạn", className: styles.warning }
        } else {
            return { text: "Còn hạn", className: styles.valid }
        }
    }

    const nextInspectionStatus = getNextInspectionStatus()

    return (
        <InfoCard title="Thống kê đăng kiểm">
            <div className={styles.statisticsContent}>
                <div className={styles.summarySection}>
                    <div className={styles.summaryItem}>
                        <div className={styles.summaryValue}>{totalInspections}</div>
                        <div className={styles.summaryLabel}>Tổng số lần đăng kiểm</div>
                    </div>

                    <div className={styles.summaryItem}>
                        <div className={styles.summaryValue}>{lastInspectionDate ? formatDate(lastInspectionDate) : "Chưa có"}</div>
                        <div className={styles.summaryLabel}>Lần đăng kiểm gần nhất</div>
                    </div>

                    <div className={styles.summaryItem}>
                        <div className={styles.summaryValue}>
                            {nextInspectionDate ? (
                                <span className={nextInspectionStatus?.className}>{formatDate(nextInspectionDate)}</span>
                            ) : (
                                "Chưa có"
                            )}
                        </div>
                        <div className={styles.summaryLabel}>Ngày hết hạn đăng kiểm</div>
                    </div>

                    {nextInspectionStatus && (
                        <div className={styles.summaryItem}>
                            <div className={`${styles.summaryValue} ${nextInspectionStatus.className}`}>
                                {daysRemaining && daysRemaining < 0
                                    ? `${Math.abs(daysRemaining)} ngày`
                                    : daysRemaining
                                        ? `${daysRemaining} ngày`
                                        : ""}
                            </div>
                            <div className={styles.summaryLabel}>{daysRemaining && daysRemaining < 0 ? "Quá hạn" : "Còn lại"}</div>
                        </div>
                    )}
                </div>

                <div className={styles.progressSection}>
                    <div className={styles.progressTitle}>Kết quả đăng kiểm</div>
                    <div className={styles.progressBar}>
                        <div
                            className={styles.progressPassed}
                            style={{ width: `${passPercentage}%` }}
                            title={`Đạt: ${passedCount}/${totalInspections} (${passPercentage}%)`}
                        ></div>
                        <div
                            className={styles.progressConditional}
                            style={{ width: `${conditionalPercentage}%` }}
                            title={`Đạt có điều kiện: ${conditionalCount}/${totalInspections} (${conditionalPercentage}%)`}
                        ></div>
                        <div
                            className={styles.progressFailed}
                            style={{ width: `${failPercentage}%` }}
                            title={`Không đạt: ${failedCount}/${totalInspections} (${failPercentage}%)`}
                        ></div>
                    </div>
                    <div className={styles.progressStats}>
                        <div className={styles.statItem}>
                            <span className={styles.passedDot}></span>
                            <span>
                                Đạt: {passedCount}/{totalInspections} ({passPercentage}%)
                            </span>
                        </div>
                        <div className={styles.statItem}>
                            <span className={styles.conditionalDot}></span>
                            <span>
                                Đạt có điều kiện: {conditionalCount}/{totalInspections} ({conditionalPercentage}%)
                            </span>
                        </div>
                        <div className={styles.statItem}>
                            <span className={styles.failedDot}></span>
                            <span>
                                Không đạt: {failedCount}/{totalInspections} ({failPercentage}%)
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </InfoCard>
    )
}
