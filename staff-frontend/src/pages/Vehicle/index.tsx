import React, { useEffect, useState } from 'react';

import styles from './Vehicle.module.scss';
import PageHeader from '@/components/PageHeader';
import { IVehicleListItem } from '@/types/Vehicle';
import { Minus } from 'lucide-react';

import { VehicleService } from '@/services';
import { Button, Table, TablePaginationConfig, TableProps, Tag } from 'antd';
import EditVehicleModal from './Modal/EditVehicleModal';
import { useNavigate } from 'react-router-dom';

const renderStatus = (status: string) => {
    if (!status) {
        return <Minus />
    }

    let color = '';
    let label = '';

    switch (status) {
        case 'active':
            color = 'success';
            label = 'Đang hoạt động';
            break;
        case 'inactive':
            color = 'orange';
            label = 'Không hoạt động';
            break;
        default:
            return <Minus />;
    }

    return (
        <Tag color={color}>
            {label}
        </Tag>
    );
}

const renderRegistrationStatus = (registrationStatus: string) => {
    if (!registrationStatus) {
        return <Minus />
    }

    let color = '';
    let label = '';

    switch (registrationStatus) {
        case 'registered':
            color = 'success';
            label = 'Đã đăng kí';
            break;
        case 'pending_registration':
            color = 'processing';
            label = 'Đang chờ';
            break;
        case 'unregistered':
            color = 'warning';
            label = 'Chưa đăng kí';
            break;
        case 'suspended':
            color = 'error';
            label = 'Bị cấm';
            break;
        default:
            return <Minus />;
    }

    return (
        <Tag color={color}>
            {label}
        </Tag>
    );
}

const Vehicle = () => {
    const navigate = useNavigate();
    const [vehicles, setVehicles] = useState<IVehicleListItem[]>([]);
    const [pagination, setPagination] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [isModalVisible, setIsModalVisible] = useState(false);

    const getVehicles = async (page = 1, pageSize = 10) => {
        try {
            const res = await VehicleService.fetchVehicles({ page, per_page: pageSize });
            const { data } = res.data;

            setVehicles(data.data);
            setPagination({
                current: data.meta.currentPage,
                pageSize: data.meta.perPage,
                total: data.meta.total,
                showSizeChanger: true,
                pageSizeOptions: ['5', '10', '15', '20', '50'],
                showTotal: (total, range) =>
                    `${range[0]}-${range[1]} trong tổng số ${total} phương tiện`,
            });
        } catch (error) {
            console.log(error);
        }
    }

    const handleEdit = (id: number) => {
        navigate(`/staff/vehicles/${id}`);
    }

    useEffect(() => {
        getVehicles(pagination.current, pagination.pageSize);
    }, []);

    const handleTableChange: TableProps<IVehicleListItem>['onChange'] = (pagination) => {
        getVehicles(pagination.current, pagination.pageSize);
    };

    const columns = [
        {
            title: 'Biển số',
            dataIndex: 'registrationNumber',
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            render: renderStatus
        },
        {
            title: 'Trạng thái đăng ký',
            dataIndex: 'registrationStatus',
            render: renderRegistrationStatus
        },
        {
            title: 'Chủ sở hữu',
            dataIndex: 'ownerName',
        },
        {
            title: 'Loại xe',
            dataIndex: 'typeName',
        },
        {
            title: 'Ngày đăng kiểm gần nhất',
            dataIndex: 'inspectedAt',
            render: (value: string | null) => value ? new Date(value).toLocaleDateString('vi-VN') : '-',
        },
        {
            title: 'Hành động',
            render: (record: IVehicleListItem) => (
                <Button onClick={() => handleEdit(record.id)}>
                    Sửa
                </Button>
            )
        }
    ];

    return (
        <div className={styles.container}>
            <PageHeader title='Phương tiện'>

            </PageHeader>

            <div>
                <Table<IVehicleListItem>
                    rowKey="id"
                    columns={columns}
                    dataSource={vehicles}
                    onChange={handleTableChange}
                    pagination={pagination}

                />
            </div>

            <EditVehicleModal isModalOpen={isModalVisible} onClose={() => setIsModalVisible(false)} />
        </div>
    );
};

export default Vehicle;