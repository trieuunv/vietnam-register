import { useEffect, useState } from 'react';
import styles from './InspectionDetail.module.scss';

import { useParams } from 'react-router-dom';
import { useAppNotification } from '@/hooks/useAppNotification';

import PageHeader from '@/components/PageHeader';
import { CriteriaResultService, InspectionService } from '@/services';
import InspectionInfoCard from './InspectionInfoCard';
import VehicleInfoCard from '@/components/VehicleInfoCard';
import OwnerInfoCard from '@/components/OwnerInfoCard';
import CriteriaResultsCard from './CriteriaResultsCard';
import PageContentSkeleton from '@/components/PageContentSkeleton';
import InspectionSummaryCard from './InspectionSummaryCard';
import ActionRow from './ActionRow';

import { ICriteriaResult } from '@/types/Criteria';
import { IInspectionDetail } from '@/types';
import { formatCurrencyVND, formatDate } from '@/utils/format';
import NotesCard from '@/components/NotesCard';
import EditNotesModal from './Modal/EditNotesModal';

const InspectionDetail = () => {
    const { id } = useParams();
    const [isLoading, setIsLoading] = useState(false);
    const [inspection, setInspection] = useState<IInspectionDetail | null>(null);
    const [isEditNotesModalOpen, setIsEditNotesModalOpen] = useState(false);
    const { showError, showSuccess, contextHolder } = useAppNotification();

    useEffect(() => {
        const getInspection = async () => {
            if (!id) {
                return;
            }

            setIsLoading(true);
            try {
                const res = await InspectionService.fetchInspection(id);
                setInspection(res.data.data);
            } catch (error) {
                console.log(error);
            } finally {
                setIsLoading(false);
            }
        }

        getInspection();
    }, [id]);

    const handleUpdateResult = async (resultId: number, newResult: string, notes: string | null) => {
        if (!inspection || !id) return;

        try {
            await CriteriaResultService.update(resultId, { result: newResult, notes });

            const updatedResults: ICriteriaResult[] = inspection.criteriaResults.map((item) => {
                if (item.id === resultId) {
                    return {
                        ...item,
                        result: newResult as "passed" | "failed" | "warning",
                        notes,
                        checkedAt: new Date().toISOString(),
                    } as ICriteriaResult;
                }
                return item;
            });


            setInspection((prev) => {
                if (!prev) return null;

                return {
                    ...prev,
                    criteriaResults: updatedResults,
                };
            });
        } catch (error) {
            console.error('Update result failed:', error);
            showError({
                message: 'Lỗi cập nhật',
                description: 'Không thể cập nhật kết quả. Vui lòng thử lại.'
            });

            setInspection(prev => prev && { ...prev });
        }
    }

    const getEstimatedResult = () => {
        if (!inspection) return '';
        const results = inspection.criteriaResults;
        if (results.some((r) => r.result === 'uninspected')) {
            return '';
        }
        if (results.some((r) => r.result === 'failed')) {
            return 'failed';
        }
        if (results.some((r) => r.result === 'warning')) {
            return 'conditional';
        }
        return 'passed';
    };

    const handleAction = async (action: string) => {
        if (!inspection || !id) return;

        try {
            if (action == 'complete') {
                const uninspected = inspection.criteriaResults.some((r) => r.result === 'uninspected');
                if (uninspected) {
                    showError({
                        message: 'Không thể hoàn thành',
                        description: 'Vui lòng hoàn thành đánh giá tất cả các tiêu chí trước khi hoàn thành.',
                    });
                    return;
                }

                const res = await InspectionService.submit({}, id);
                setInspection(res.data.data);

                showSuccess({
                    message: 'Thành công',
                    description: 'Kết quả đăng kiểm đã được tính toán và lưu thành công!',
                });
            } else if (action == 'save') {
                showSuccess({
                    message: 'Thành công',
                    description: 'Thông tin đăng kiểm đã được lưu nháp.',
                });
            } else if (action == 'cancel') {
                const res = await InspectionService.cancel(id);
                setInspection(res.data.data);

                showSuccess({
                    message: 'Thành công',
                    description: 'Đã hủy thành công.',
                });
            } else if (action == 'undo_cancel') {
                const res = await InspectionService.update(id, { status: 'in_progress' });
                setInspection(res.data.data);

                showSuccess({
                    message: 'Thành công',
                    description: 'Đã bỏ hủy thành công.',
                });
            } else if (action == 'start') {
                const res = await InspectionService.update(id, { status: 'in_progress' });
                setInspection(res.data.data);

                showSuccess({
                    message: 'Thành công',
                    description: 'Đã bắt đầu kiểm định thành công.',
                });
            }
        } catch (error) {
            console.log(error);
            showError({
                message: 'Lỗi',
                description: 'Không thể lưu dữ liệu. Vui lòng thử lại!',
            })
        }
    }

    // Note
    const handleEditClick = () => {
        setIsEditNotesModalOpen(true);
    }

    const handleUpdateNotes = async (notes: string) => {
        if (!inspection || !id) return;

        const res = await InspectionService.update(id, { notes });
        setInspection(res.data.data);
    }

    return (
        <div className={styles.container}>
            <PageHeader title='Chi tiết kiểm định' />

            <div className={styles.content}>
                {isLoading ? (
                    <PageContentSkeleton />
                ) : !inspection ? (
                    <div>Không tìm thấy thông tin đăng kiểm</div>
                ) : (
                    <>
                        <InspectionInfoCard
                            inspectionDate={formatDate(inspection.inspectionDate)}
                            nextInspectionDate={inspection.nextInspectionDate ? formatDate(inspection.nextInspectionDate) : 'N/A'}
                            result={inspection.result || 'N/A'}
                            fee={inspection.fee ? formatCurrencyVND(inspection.fee) : 'N/A'}
                            certificateNumber={inspection.certificateNumber || 'N/A'}
                            status={inspection.status}
                        />

                        <OwnerInfoCard
                            id={inspection.owner?.id}
                            fullName={inspection.owner?.fullName || 'N/A'}
                            citizenNumber={inspection.owner?.citizenNumber || 'N/A'}
                            phone={inspection.owner?.phone || 'N/A'}
                            email={inspection.owner?.email || 'N/A'}
                        />

                        <VehicleInfoCard
                            id={inspection.vehicle.id}
                            registrationNumber={inspection.vehicle?.registrationNumber || 'N/A'}
                            brand={inspection.vehicle?.brand || 'N/A'}
                            model={inspection.vehicle?.model || 'N/A'}
                            color={inspection.vehicle?.color || 'N/A'}
                            status={inspection.status || 'N/A'}
                        />

                        <CriteriaResultsCard
                            criteriaResults={inspection.criteriaResults}
                            onUpdateResult={handleUpdateResult}
                        />

                        <InspectionSummaryCard
                            result={inspection.status === 'completed' ? inspection.result : getEstimatedResult()}
                            passedCount={inspection.criteriaResults.filter((r) => r.result === "passed").length}
                            failedCount={inspection.criteriaResults.filter((r) => r.result === "failed").length}
                            warningCount={inspection.criteriaResults.filter((r) => r.result === "warning").length}
                            totalCount={inspection.criteriaResults.length}
                            onUpdateResult={() => {}}
                            readOnly={true}
                        />

                        <NotesCard
                            notes={inspection.notes}
                            onEdit={handleEditClick}
                        />

                        <ActionRow
                            status={inspection.status}
                            onAction={handleAction}
                            canComplete={true}
                        />
                    </>
                )}
            </div>
            {contextHolder}
            <EditNotesModal
                isOpen={isEditNotesModalOpen}
                onClose={() => setIsEditNotesModalOpen(false)}
                initialData={inspection?.notes}
                onSubmit={handleUpdateNotes}
            />
        </div>
    );
};

export default InspectionDetail;