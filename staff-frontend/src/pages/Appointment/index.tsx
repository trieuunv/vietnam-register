import { useEffect, useState } from 'react';

import styles from './Appointment.module.scss';
import { AppointmentService } from '@/services';

import FilterListModal from './Modal/FilterListModal';
import PageHeader from '@/components/PageHeader';

import { IAppointmentListItem, IAppointmentListFilter } from '@/types';
import { Button, Table, TableColumnsType } from 'antd';
import { Filter } from 'lucide-react';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { momentToString, stringToMoment } from '@/utils/dateUtils';
import { formatDate } from '@/utils/format';

const Appointment = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const [appointments, setAppointments] = useState<IAppointmentListItem[]>([]);
    const [filterValues, setFilterValues] = useState<IAppointmentListFilter>({});
    const [modals, setModals] = useState({
        filter: false,
    });

    console.log(filterValues);

    const getAppointments = async (filter?: Record<string, any>) => {
        const res = await AppointmentService.fetchAppointments(filter);
        setAppointments(res.data.data);
    }

    const onFilterSubmit = (data: IAppointmentListFilter) => {
        const params = new URLSearchParams();

        Object.entries(data).forEach(([key, value]) => {
            if (value) {
                params.set(key, value);
            }
        });

        navigate(`?${params.toString()}`);
    }

    const handleDetail = (record: IAppointmentListItem) => {
        navigate(`${record.id}`)
    }

    useEffect(() => {
        const searchParams = new URLSearchParams(location.search);
        const filter: IAppointmentListFilter = {
            status: searchParams.get("status") || undefined,
            ownerName: searchParams.get("ownerName") || undefined,
            vehicleRegNumber: searchParams.get("vehicleRegNumber") || undefined,
            dateFrom: stringToMoment(searchParams.get("dateFrom")),
            dateTo: stringToMoment(searchParams.get("dateTo")),
        };

        setFilterValues(filter);

        const apiFilter = {
            ...filter,
            dateFrom: momentToString(filter.dateFrom),
            dateTo: momentToString(filter.dateTo)
        };

        getAppointments(apiFilter);
    }, [location.search]);

    const columns: TableColumnsType<IAppointmentListItem> = [
        {
            title: 'ID',
            dataIndex: 'id'
        },
        {
            title: 'Ngày',
            dataIndex: 'appointmentDate',
            render: (value) => { return formatDate(value) }
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            sorter: {
                multiple: 3,
            },
        },

        {
            title: 'Chủ sở hữu',
            dataIndex: 'ownerName',
            sorter: {
                multiple: 3,
            },
        },
        {
            title: 'Biển số',
            dataIndex: 'vehicleRegNumber',
            sorter: {
                multiple: 3,
            },
        },
        {
            title: 'Hành động',
            render: (record: IAppointmentListItem) => (
                <Button onClick={() => handleDetail(record)}>
                    Chi tiết
                </Button>
            )
        }
    ];

    return (
        <div className={styles.container}>
            <PageHeader title='Lịch trình'>
                <div>
                    <Button
                        icon={<Filter size={16} />}
                        onClick={() => setModals(prev => ({ ...prev, filter: true }))}
                    >
                        Lọc
                    </Button>
                </div>
            </PageHeader>

            <div className={styles.content}>
                <div className={styles.filterWrapper}>

                </div>
                <div className={styles.tableWrapper}>
                    <Table<IAppointmentListItem>
                        bordered
                        size='large'
                        locale={{ emptyText: "Không có dữ liệu" }}
                        rowKey="id"
                        columns={columns}
                        dataSource={appointments}
                    // onChange={onChange} 
                    />
                </div>
            </div>

            <FilterListModal
                isModalOpen={modals.filter}
                onClose={() => setModals(prev => ({ ...prev, filter: false }))}
                onSubmit={onFilterSubmit}
                initialValues={filterValues}
            />
        </div>
    );
};

export default Appointment;