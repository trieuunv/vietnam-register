import { useState } from "react"
import InfoCard from "@/components/InfoCard"
import styles from "./CriteriaResultsCard.module.scss"
import { ICriteriaResult } from "@/types/Criteria"
import { Button } from "antd"
import ResultTag from "@/components/ResultTag"
import { Pen, Play } from 'lucide-react';
import EditCriteriaModal from "../Modal/EditCriteriaModal"

interface CriteriaResultsCardProps {
    criteriaResults: ICriteriaResult[]
    onUpdateResult: (resultId: number, result: string, notes: string | null) => void
    readOnly?: boolean
}

const formatNotes = (notes: string | null) => {
    if (!notes) return <span className={styles.emptyNotes}>Không có ghi chú</span>
    return <div className={styles.notesContent}>{notes}</div>
}

const CriteriaResultsCard = ({
    criteriaResults,
    onUpdateResult,
    readOnly = false,
}: CriteriaResultsCardProps) => {
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedResult, setSelectedResult] = useState<ICriteriaResult | null>(null);
    const [updateLoading, setUpdateLoading] = useState(false);

    const handleEditClick = (result: ICriteriaResult) => {
        setSelectedResult(result);
        setModalVisible(true);
    };

    const handleModalSubmit = async (data: { result: string; notes?: string }) => {
        if (!selectedResult) return;

        try {
            setUpdateLoading(true);
            await onUpdateResult(
                selectedResult.id,
                data.result,
                data.notes || null
            );
            setModalVisible(false);
        } catch (error) {
            console.error('Update failed:', error);
        } finally {
            setUpdateLoading(false);
        }
    };

    return (
        <InfoCard title="Kết quả kiểm định">
            <div className={styles.resultsList}>
                {criteriaResults.map(criteriaResult => (
                    <div key={criteriaResult.id} className={styles.criteriaGroup}>
                        <div className={styles.criteriaHeader}>
                            <h3 className={styles.criteriaName}>{criteriaResult.criteriaName}</h3>
                        </div>

                        <div className={styles.resultItems}>
                            <div className={styles.resultItem}>
                                <div className={styles.resultHeader}>
                                    <div className={styles.resultId}>#{criteriaResult.id}</div>
                                    <div className={styles.resultActions}>
                                        {!readOnly && (
                                            <ResultTag result={criteriaResult.result} />
                                        )}
                                        {readOnly && (
                                            <div
                                                className={`${styles.resultBadge} ${criteriaResult.result === "passed" ? styles.passedBadge : styles.failedBadge
                                                    }`}
                                            >
                                                {criteriaResult.result === "passed" ? "Đạt" : "Không đạt"}
                                            </div>
                                        )}
                                        {criteriaResult.result === "uninspected" ? (
                                            <Button
                                                type="default"
                                                onClick={() => handleEditClick(criteriaResult)}
                                                icon={<Play size={16} />}
                                            >
                                                Bắt đầu kiểm định
                                            </Button>
                                        ) : (
                                            <Button
                                                type="default"
                                                onClick={() => handleEditClick(criteriaResult)}
                                                icon={<Pen size={14} />}
                                            >
                                                Chỉnh sửa
                                            </Button>
                                        )}
                                    </div>
                                </div>

                                {criteriaResult.notes && (
                                    <div className={styles.notesPreview}>
                                        <span className={styles.notesLabel}>📝 Ghi chú</span>
                                        {formatNotes(criteriaResult.notes)}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <EditCriteriaModal
                isOpen={modalVisible}
                onClose={() => setModalVisible(false)}
                initialData={selectedResult}
                onSubmit={handleModalSubmit}
                loading={updateLoading}
            />
        </InfoCard>
    )
}

export default CriteriaResultsCard;
