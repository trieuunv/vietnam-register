import StatusTag from "@/components/StatusTag"
import styles from "./InspectionSummaryCard.module.scss"
import InfoCard from "@/components/InfoCard"
import { useState } from "react"
import { Button } from "antd"

interface InspectionSummaryCardProps {
    result: string
    passedCount: number
    failedCount: number
    warningCount: number
    totalCount: number
    onUpdateResult: (result: "passed" | "failed" | "conditional") => void
    readOnly?: boolean
    status?: string
}

export default function InspectionSummaryCard({
    result,
    passedCount,
    failedCount,
    warningCount,
    totalCount,
    onUpdateResult,
    readOnly = false,
    status,
}: InspectionSummaryCardProps) {
    const [loading, setLoading] = useState<string | null>(null)

    const passPercentage = Math.round((passedCount / totalCount) * 100) || 0
    const failPercentage = Math.round((failedCount / totalCount) * 100) || 0
    const warningPercentage = Math.round((warningCount / totalCount) * 100) || 0

    const handleUpdateResult = async (newResult: "passed" | "failed" | "conditional") => {
        if (readOnly || loading) return

        setLoading(newResult)
        try {
            await onUpdateResult(newResult)
        } finally {
            setLoading(null)
        }
    }

    const getResultBadge = () => {
        switch (result) {
            case "passed":
                return <div className={`${styles.resultBadge} ${styles.passedBadge}`}>Đạt</div>
            case "failed":
                return <div className={`${styles.resultBadge} ${styles.failedBadge}`}>Không đạt</div>
            case "conditional":
                return <div className={`${styles.resultBadge} ${styles.conditionalBadge}`}>Đạt có điều kiện</div>
            default:
                return null
        }
    }

    const getStatusElement = () => {
        if (!status) return getResultBadge()

        return (
            <div className={styles.statusContainer}>
                <div className={styles.resultBadgeContainer}>{getResultBadge()}</div>
                <div className={styles.statusBadgeContainer}>
                    <span className={styles.statusLabel}>Trạng thái:</span>
                    <StatusTag status={status} />
                </div>
            </div>
        )
    }

    return (
        <InfoCard title="Kết luận đăng kiểm" rightElement={getStatusElement()}>
            <div className={styles.summaryContent}>
                <div className={styles.progressSection}>
                    <div className={styles.progressBar}>
                        <div
                            className={styles.progressPassed}
                            style={{ width: `${passPercentage}%` }}
                            title={`Đạt: ${passedCount}/${totalCount} (${passPercentage}%)`}
                        ></div>
                        <div
                            className={styles.progressWarning}
                            style={{ width: `${warningPercentage}%` }}
                            title={`Cảnh báo: ${warningCount}/${totalCount} (${warningPercentage}%)`}
                        ></div>
                        <div
                            className={styles.progressFailed}
                            style={{ width: `${failPercentage}%` }}
                            title={`Không đạt: ${failedCount}/${totalCount} (${failPercentage}%)`}
                        ></div>
                    </div>
                    <div className={styles.progressStats}>
                        <div className={styles.statItem}>
                            <span className={styles.passedDot}></span>
                            <span>
                                Đạt: {passedCount}/{totalCount} ({passPercentage}%)
                            </span>
                        </div>
                        <div className={styles.statItem}>
                            <span className={styles.warningDot}></span>
                            <span>
                                Cảnh báo: {warningCount}/{totalCount} ({warningPercentage}%)
                            </span>
                        </div>
                        <div className={styles.statItem}>
                            <span className={styles.failedDot}></span>
                            <span>
                                Không đạt: {failedCount}/{totalCount} ({failPercentage}%)
                            </span>
                        </div>
                    </div>
                </div>

                {!readOnly && (
                    <div className={styles.actionSection}>
                        <div className={styles.actionTitle}>Kết luận:</div>
                        <div className={styles.actionButtons}>
                            <Button
                                className={`${(result === "passed" || loading === "passed") ? styles.activePass : ""}`}
                                onClick={() => handleUpdateResult("passed")}
                                disabled={loading != "passed" && loading !== null}
                                loading={loading === "passed"}
                            >
                                Đạt
                            </Button>
                            <Button
                                className={`${result === "conditional" || loading === "conditional" ? styles.activeWarning : ""}`}
                                onClick={() => handleUpdateResult("conditional")}
                                disabled={loading != "conditional" && loading !== null}
                                loading={loading === "conditional"}
                            >
                                Đạt có điều kiện
                            </Button>
                            <Button
                                className={`${result === "failed" || loading === "failed" ? styles.activeFail : ""}`}
                                onClick={() => handleUpdateResult("failed")}
                                disabled={loading != "failed" && loading !== null}
                                loading={loading === "failed"}
                            >
                                Không đạt
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </InfoCard>
    )
}
