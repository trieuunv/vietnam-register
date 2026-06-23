import { useEffect, useState } from 'react';
import styles from './AppointmentDetail.module.scss';
import { useNavigate, useParams } from 'react-router-dom';
import { AppointmentService } from '@/services';
import PageHeader from '@/components/PageHeader';
import AppointmentInfoCard from './components/AppointmentInfoCard';
import VehicleInfoCard from '@/components/VehicleInfoCard';
import NotesCard from '@/components/NotesCard';
import ActionCard from './components/ActionCard';
import OwnerInfoCard from '@/components/OwnerInfoCard';
import { IAppointmentDetail } from '@/types';
import PageContentSkeleton from '@/components/PageContentSkeleton';
import { useAppNotification } from '@/hooks/useAppNotification';
import InspectionInfoCard from './components/InspectionInfoCard';
import { formatDate } from '@/utils/format';

const AppointmentDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [appointment, setAppointment] = useState<IAppointmentDetail | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [actionLoading, setActionLoading] = useState<string | null>(null);
    const { showSuccess, showError, contextHolder } = useAppNotification();

    const handleAction = async (type: string) => {
        if (!id) return;

        setActionLoading(type);

        try {
            if (type === "accept" || type === "confirm") {
                const res = await AppointmentService.confirm(id);
                setAppointment(res.data.data);
                showSuccess({
                    message: 'Thành công',
                    description: 'Dữ liệu đã được lưu thành công!',
                });
            } else if (type === "reject") {
                const res = await AppointmentService.reject(id);
                setAppointment(res.data.data);
            } else if (type === "start") {
                const res = await AppointmentService.start(id);
                const newAppointment = res.data.data;
                setAppointment(newAppointment);
                navigate(`/staff/inspections/${newAppointment.inspection?.id}`);
            }
        } catch (error) {
            console.error(`Failed to ${type} appointment:`, error);
            showError({
                message: 'Lỗi',
                description: 'Không thể lưu dữ liệu. Vui lòng thử lại!',
            })
        } finally {
            setActionLoading(null);
        }
    }

    useEffect(() => {
        const getAppointment = async () => {
            if (!id) return;

            setIsLoading(true);
            try {
                const res = await AppointmentService.fetchAppointment(id);
                setAppointment(res.data.data);
            } catch (error) {
                console.error('Failed to fetch appointment:', error);
            } finally {
                setIsLoading(false);
            }
        }

        getAppointment();
    }, [id]);

    const handleEditClick = () => {

    }

    return (
        <div className={styles.container}>
            <PageHeader title='Chi tiết lịch trình' />

            <div className={styles.content}>
                {isLoading ? (
                    <PageContentSkeleton />
                ) : !appointment ? (
                    <div>Không tìm thấy lịch hẹn</div>
                ) : (
                    <>
                        <AppointmentInfoCard
                            id={appointment.id}
                            date={formatDate(appointment.appointmentDate)}
                            status={appointment.status}
                            confirmationCode={appointment.confirmationCode}
                        />

                        <VehicleInfoCard
                            registrationNumber={appointment.vehicle?.registrationNumber || 'N/A'}
                            brand={appointment.vehicle?.brand || 'N/A'}
                            model={appointment.vehicle?.model || 'N/A'}
                            color={appointment.vehicle?.color || 'N/A'}
                            status={appointment.vehicle?.status || 'N/A'}
                        />

                        <OwnerInfoCard
                            id={appointment.owner.id}
                            fullName={appointment.owner?.fullName || 'N/A'}
                            citizenNumber={appointment.owner?.citizenNumber || 'N/A'}
                            phone={appointment.owner?.phone || 'N/A'}
                            email={appointment.owner?.email || 'N/A'}
                        />

                        {appointment.inspection && (
                            <InspectionInfoCard
                                id={appointment.inspection.id}
                                inspectionDate={formatDate(appointment.inspection.inspectionDate)}
                                nextInspectionDate={appointment.inspection.nextInspectionDate ? formatDate(appointment.inspection.nextInspectionDate) : "N/A"}
                                certificateNumber={appointment.inspection.certificateNumber ? appointment.inspection.certificateNumber : "N/A"}
                                result={appointment.inspection.result}
                                status={appointment.inspection.status}
                            />
                        )}

                        <NotesCard
                            onEdit={handleEditClick}
                            notes={appointment.notes || 'Không có ghi chú'}
                        />

                        {appointment.status !== 'completed' && (
                            <ActionCard
                                status={appointment.status}
                                onAction={handleAction}
                                loading={actionLoading}
                            />
                        )}
                    </>
                )}
            </div>
            {contextHolder}
        </div>
    );
};

export default AppointmentDetail;