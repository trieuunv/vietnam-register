import styles from './OwnerDetail.module.scss';
import PageHeader from '@/components/PageHeader';
import PageContent from '@/components/PageContent';
import OwnerProfileCard from './OwnerProfileCard';
import OwnerContactCard from './OwnerContactCard';
import OwnerVehiclesCard from './OwnerVehiclesCard';
import OwnerDocumentsCard from './OwnerDocumentsCard';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { IOwnerDetail } from '@/types/Owner';
import { OwnerService } from '@/services';
import PageContentSkeleton from '@/components/PageContentSkeleton';

const OwnerDetail = () => {
    const { id } = useParams();
    const [isLoading, setIsLoading] = useState(false);

    const [owner, setOwner] = useState<IOwnerDetail | null>();

    useEffect(() => {
        const getOwner = async () => {
            if (!id) return;

            setIsLoading(true);
            try {
                const res = await OwnerService.getOwner(id);

                setOwner(res.data.data);
                console.log(res.data.data);
            } catch (error) {
                console.log(error);
            } finally {
                setIsLoading(false);
            }
        }

        getOwner();
    }, [id]);

    const documents = [
        {
            id: 1,
            type: "CCCD",
            number: "079201012345",
            issueDate: "2021-01-15T00:00:00.000000Z",
            expiryDate: "2031-01-15T00:00:00.000000Z",
            issuedBy: "Cục Cảnh sát quản lý hành chính về trật tự xã hội",
            status: "active",
        },
        {
            id: 2,
            type: "GPLX",
            number: "790123456789",
            issueDate: "2018-05-20T00:00:00.000000Z",
            expiryDate: "2028-05-20T00:00:00.000000Z",
            issuedBy: "Sở GTVT TP. Hồ Chí Minh",
            status: "active",
        },
    ]

    return (
        <div className={styles.container}>
            <PageHeader title='Thông tin chủ sở hữu' />

            <PageContent>
                {isLoading ? (
                    <PageContentSkeleton />
                ) : !owner ? (
                    <div>Không tìm thấy thông tin chủ sỡ hữu...</div>
                ) : (
                    <>
                        <OwnerProfileCard
                            fullName={owner.fullName}
                            citizenId={owner.citizenNumber || 'N/A'}
                            dateOfBirth={owner.dayOfBirth || 'N/A'}
                            gender={owner.gender || 'N/A'}
                            registrationDate={owner.createdAt || 'N/A'}
                        />

                        <OwnerContactCard
                            phone={owner.phone || 'N/A'}
                            email={owner.email || 'N/A'}
                            address='Kí Túc Xá Trường Đại Học Công Nghệ Thông Tin Và Truyền Thông Việt Hàn, Số 470, Đường Trần Đại Nghĩa
                                    Phường Hòa Quý, Quận Ngũ Hành Sơn, Đà Nẵng'
                        />

                        <OwnerVehiclesCard
                            onAddVehicle={() => { }}
                            onViewVehicle={() => { }}
                            vehicles={owner.vehicles}
                        />

                        <OwnerDocumentsCard
                            documents={documents}
                        />
                    </>
                )}
            </PageContent>
        </div>
    );
};

export default OwnerDetail;