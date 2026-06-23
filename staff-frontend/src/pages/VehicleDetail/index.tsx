import { useCallback, useEffect, useState } from 'react';

import styles from './VehicleDetail.module.scss';
import PageHeader from '@/components/PageHeader';
import { useNavigate, useParams } from 'react-router-dom';
import { VehicleService } from '@/services';
import { IInspection, IInspectionStatistics, IVehicleDetail } from '@/types';
import VehicleInfoCard from './VehicleInfoCard';
import PageContentSkeleton from '@/components/PageContentSkeleton';
import OwnerInfoCard from '@/components/OwnerInfoCard';
import InspectionStatisticsCard from './InspectionStatisticsCard';
import InspectionHistoryCard from './InspectionHistoryCard';

const VehicleDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [vehicle, setVehicle] = useState<IVehicleDetail | null>(null);
    const [statistics, setStatistics] = useState<IInspectionStatistics | null>(null);

    const calculateStatistics = useCallback((inspections: IInspection[]) => {
        const total = inspections.length;

        const passed = inspections.filter((i) => i.result === 'passed').length;
        const conditional = inspections.filter((i) => i.result === 'conditional').length;
        const failed = inspections.filter((i) => i.result === 'failed').length;

        const sortedInspections = [...inspections].sort(
            (a, b) => new Date(b.inspectionDate).getTime() - new Date(a.inspectionDate).getTime()
        );

        const last = sortedInspections[0]?.inspectionDate || null;
        const next = sortedInspections[0]?.nextInspectionDate || null;

        return {
            totalInspections: total,
            passedCount: passed,
            conditionalCount: conditional,
            failedCount: failed,
            lastInspectionDate: last,
            nextInspectionDate: next,
        };
    }, []);

    useEffect(() => {
        const getVehicle = async () => {
            if (!id) return;

            setIsLoading(true);
            try {
                const res = await VehicleService.fetchVehicle(id);
                setVehicle(res.data.data);

                if (res.data.data.inspections) {
                    const statistics = calculateStatistics(res.data.data.inspections);
                    setStatistics(statistics);
                }
                console.log(res.data.data)
            } catch (error) {
                console.log(error);
            } finally {
                setIsLoading(false);
            }
        }

        getVehicle();
    }, [id, calculateStatistics]);

    const handleViewInspection = (id: number) => {
        navigate(`/staff/inspections/${id}`);
    }

    return (
        <div className={styles.container}>
            <PageHeader title='Chi tiết phương tiện' />

            <div className={styles.content}>
                {isLoading ? (
                    <PageContentSkeleton />
                ) : !vehicle ? (
                    <div>Không tìm thấy phương tiện</div>
                ) : (
                    <>
                        <VehicleInfoCard
                            registrationNumber={vehicle?.registrationNumber || 'N/A'}
                            brand={vehicle?.brand || 'N/A'}
                            model={vehicle?.model || 'N/A'}
                            status={vehicle.status}
                            manufactureYear={vehicle?.manufactureYear || 'N/A'}
                            chassisNumber={vehicle?.chassisNumber || 'N/A'}
                            engineNumber={vehicle?.engineNumber || 'N/A'}
                            vehicleTypeName={vehicle?.vehicleTypeName || 'N/A'}
                        />

                        {vehicle.owner && (
                            <OwnerInfoCard
                                fullName={vehicle.owner.fullName || 'N/A'}
                                phone={vehicle.owner.phone || 'N/A'}
                                email={vehicle.owner.email || 'N/A'}
                                citizenNumber={vehicle.owner.citizenNumber || 'N/A'}
                            />
                        )}

                        {statistics && (
                            <InspectionStatisticsCard
                                totalInspections={statistics.totalInspections}
                                passedCount={statistics.passedCount}
                                conditionalCount={statistics.conditionalCount}
                                failedCount={statistics.failedCount}
                                lastInspectionDate={statistics.lastInspectionDate}
                                nextInspectionDate={statistics.nextInspectionDate}
                            />
                        )}

                        <InspectionHistoryCard
                            inspections={vehicle.inspections}
                            onViewInspection={handleViewInspection}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default VehicleDetail;