import React, { useEffect } from 'react';

import styles from './Inspection.module.scss';
import PageHeader from '@/components/PageHeader';
import { Table, Tag } from 'antd';
import { Minus } from 'lucide-react';
import { useAppDispatch, RootState } from '@/store/store';
import InspectionActionThunk from './slice/actions';
import { useSelector } from 'react-redux';

import { IInspectionListItem } from '@/types';

import type { TableColumnsType, TableProps } from 'antd';
import { useNavigate } from 'react-router-dom';
import { formatDate } from '@/utils/format';
import Button from '@/components/Button';

const renderStatus = (value: string) => {
    if (!value) {
        return <span><Minus /></span>;
    }

    let color = '';
    let label = '';

    switch (value) {
        case 'completed':
            color = 'success';
            label = 'Thành công';
            break;
        case 'in_progress':
            color = 'processing';
            label = 'Đang tiến hành';
            break;
        case 'error':
            color = 'error';
            label = 'Lỗi';
            break;
        default:
            color = 'default';
            label = '-';
            break;
    }

    return (
        <Tag color={color}>
            {label}
        </Tag>
    );
}

const onChange: TableProps<IInspectionListItem>['onChange'] = (pagination, filters, sorter, extra) => {
    console.log('params', pagination, filters, sorter, extra);
};

const Inspection = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const inspections = useSelector((state: RootState) => state.inspection.list);

    useEffect(() => {
        const getInspections = async () => {
            dispatch(InspectionActionThunk.fetchInspections());
        }

        getInspections();
    }, [dispatch]);

    const columns: TableColumnsType<IInspectionListItem> = [
        {
            title: 'Biển số',
            dataIndex: 'registrationNumber',
            sorter: {
                multiple: 3,
            },
        },
        {
            title: 'Người đăng kiểm',
            dataIndex: 'inspectorName',
            sorter: {
                multiple: 3,
            },
        },
        {
            title: 'Ngày đăng kiểm',
            dataIndex: 'inspectionDate',
            sorter: {
                multiple: 2,
            },
            render: (value: string) => {
                return formatDate(value);
            }
        },
        {
            title: 'Kết quả',
            dataIndex: 'result',
            sorter: {
                multiple: 1,
            },
            render: (value: string) => {
                if (!value) {
                    return <span><Minus /></span>
                }

                const color = value === 'passed' ? 'green' : 'red';
                const label = value === 'passed' ? 'Đạt' : 'Không đạt';
                return <Tag color={color}>{label}</Tag>
            }
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            sorter: {
                multiple: 1,
            },
            render: renderStatus
        },
        {
            title: 'Hành động',
            render: (record: IInspectionListItem) => (
                <Button onClick={() => handleEdit(record)} variant='secondary'>
                    Chi tiết
                </Button>
            )
        }
    ];

    const handleEdit = (record: IInspectionListItem) => (
        navigate(`${record.id}`)
    );

    return (
        <div className={styles.inspection}>
            <PageHeader title='Kiểm định'>
                <>
                    <Button>Kiểm định mới</Button>
                </>
            </PageHeader>

            <div className={styles.content}>
                <Table<IInspectionListItem>
                    bordered
                    columns={columns}
                    dataSource={inspections}
                    onChange={onChange}
                />
            </div>
        </div>
    );
};

export default Inspection;