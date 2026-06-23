import { useState } from "react"
import styles from "./InspectionHistoryCard.module.scss"
import InfoCard from "@/components/InfoCard"
import { formatDate } from "@/utils/format"
import { IInspection } from "@/types"
import Button from "@/components/Button"

interface InspectionHistoryCardProps {
    inspections: IInspection[]
    onViewInspection: (id: number) => void
}

export default function InspectionHistoryCard({ inspections, onViewInspection }: InspectionHistoryCardProps) {
    const [expandedId, setExpandedId] = useState<number | null>(null)

    const toggleExpand = (id: number) => {
        setExpandedId(expandedId === id ? null : id)
    }

    const getResultBadge = (result: string) => {
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

    const getDotClass = (result: string) => {
        switch (result.toLowerCase()) {
            case 'passed':
                return styles.timelineDot + ' ' + styles['timelineDot--pass'];
            case 'failed':
                return styles.timelineDot + ' ' + styles['timelineDot--fail'];
            case 'conditional':
                return styles.timelineDot + ' ' + styles['timelineDot--conditional'];
            default:
                return styles.timelineDot;
        }
    };

    // Sắp xếp lịch sử đăng kiểm theo thời gian mới nhất
    const sortedInspections = [...inspections].sort(
        (a, b) => new Date(b.inspectionDate).getTime() - new Date(a.inspectionDate).getTime(),
    )

    return (
        <InfoCard title="Lịch sử đăng kiểm">
            <div className={styles.timeline}>
                {sortedInspections.map((inspection, index) => (
                    <div
                        key={inspection.id}
                        className={`${styles.timelineItem} ${expandedId === inspection.id ? styles.expanded : ""}`}
                    >
                        <div className={styles.timelineConnector}>
                            <div className={getDotClass(inspection.result)}></div>
                            {index < sortedInspections.length - 1 && <div className={styles.timelineLine}></div>}
                        </div>

                        <div className={styles.timelineContent}>
                            <div className={styles.timelineHeader} onClick={() => toggleExpand(inspection.id)}>
                                <div className={styles.timelineDate}>
                                    <div className={styles.inspectionDate}>{formatDate(inspection.inspectionDate)}</div>
                                    <div className={styles.expiryDate}>Hết hạn: {formatDate(inspection.inspectionDate)}</div>
                                </div>
                                <div className={styles.timelineActions}>
                                    {getResultBadge(inspection.result)}
                                    <button
                                        className={styles.expandButton}
                                        onClick={(e) => {
                                            e.stopPropagation()
                                            toggleExpand(inspection.id)
                                        }}
                                    >
                                        {expandedId === inspection.id ? "Thu gọn" : "Xem thêm"}
                                    </button>
                                </div>
                            </div>

                            {expandedId === inspection.id && (
                                <div className={styles.timelineDetails}>
                                    <div className={styles.detailRow}>
                                        <div className={styles.detailItem}>
                                            <div className={styles.detailLabel}>Số giấy chứng nhận</div>
                                            <div className={styles.detailValue}>
                                                {inspection.certificateNumber || <span className={styles.emptyValue}>Không có</span>}
                                            </div>
                                        </div>
                                        <div className={styles.detailItem}>
                                            <div className={styles.detailLabel}>Trung tâm đăng kiểm</div>
                                            <div className={styles.detailValue}>{inspection.centerName}</div>
                                        </div>
                                    </div>

                                    {inspection.notes ? (
                                        <div className={styles.notesSection}>
                                            <div className={styles.notesHeader}>
                                                <div className={styles.notesLabel}>📝 Ghi chú đăng kiểm</div>
                                            </div>
                                            <div className={styles.notesContent}>
                                                {inspection.notes.split("\n").map((line, index) => (
                                                    <p key={index} className={styles.notesLine}>
                                                        {line || "\u00A0"}
                                                    </p>
                                                ))}
                                            </div>
                                        </div>
                                    ) : (
                                        <div className={styles.emptyNotesSection}>
                                            <div className={styles.emptyNotesIcon}>📋</div>
                                            <div className={styles.emptyNotesText}>Không có ghi chú cho lần đăng kiểm này</div>
                                        </div>
                                    )}

                                    <div className={styles.detailActions}>
                                        <Button onClick={() => onViewInspection(inspection.id)}>
                                            Xem chi tiết đăng kiểm
                                        </Button>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </InfoCard>
    )
}
