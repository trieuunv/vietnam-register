import { Button, Col, DatePicker, Form, Input, Modal, Row, Select } from 'antd';
import React, { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { IAppointmentListFilter } from '@/types';

interface IFilterListModalProps {
    isModalOpen: boolean,
    onClose: () => void,
    onSubmit: (data: IAppointmentListFilter) => void;
    initialValues?: IAppointmentListFilter;
}

const FilterListModal: React.FC<IFilterListModalProps> = ({ 
    isModalOpen,
    onClose,
    onSubmit,
    initialValues
}) => {
    const { control, handleSubmit, reset, setValue, getValues } = useForm<IAppointmentListFilter>({
        defaultValues: initialValues || {
            status: undefined,
            dateFrom: undefined,
            dateTo: undefined,
            ownerName: '',  
            vehicleRegNumber: '',
        }
    });

    const handleFormSubmit = (data: IAppointmentListFilter) => {
        console.log(getValues('vehicleRegNumber'));
        console.log(data);
        onSubmit(data);
      };

    const handleOk = () => {
        onClose();
    };
    
    const handleCancel = () => {
        onClose()
    };

    const handleReset = () => {
        setValue('status', undefined);
        setValue('dateFrom', undefined);
        setValue('dateTo', undefined);
        setValue('ownerName', '');
        setValue('vehicleRegNumber', '');
    }

    useEffect(() => {
        if (initialValues) {
            reset(initialValues);
        }

        console.log(123);
        console.log(initialValues);
    }, [initialValues, reset]);

    return (
        <Modal
            title="Lọc" 
            open={isModalOpen} 
            onOk={handleOk} 
            onCancel={handleCancel}
            width={600}
            footer={[
                <Button key='reset' onClick={handleReset}>
                    Làm mới
                </Button>,

                <Button key="cancel" onClick={onClose}>
                    Hủy
                </Button>,

                <Button key="submit" type="primary" onClick={handleSubmit(handleFormSubmit)}>
                    Áp dụng
                </Button>,
            ]}
        >
            <Form
                layout='vertical'
                onFinish={handleSubmit(handleFormSubmit)}
            >
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item label="Bắt đầu">
                            <Controller
                                name='dateFrom'
                                control={control}
                                render={({ field }) => (
                                    <DatePicker 
                                        {...field}
                                        placeholder='Chọn ngày bắt đầu'
                                        style={{ width: "100%" }}
                                    />
                                )}
                            />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Kết thúc">
                            <Controller 
                                name='dateTo'
                                control={control}
                                render={({ field }) => (
                                    <DatePicker 
                                        {...field}
                                        placeholder='Chọn ngày kết thúc'
                                        style={{ width: "100%" }}
                                    />
                                )}
                            />
                        </Form.Item>
                    </Col>
                </Row>

                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item label="Biển số">
                            <Controller 
                                name='vehicleRegNumber'
                                control={control}
                                render={({ field }) => (
                                    <Input
                                        {...field}
                                        placeholder='Nhập biển số'
                                    />
                                )}
                            />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Trạng thái">
                            <Controller 
                                name='status'
                                control={control}
                                render={({ field }) => (
                                    <Select
                                        {...field}
                                        placeholder='Chọn trạng thái'
                                        allowClear
                                        options={[
                                            { value: 'completed', label: 'Thành công' },
                                            { value: 'cancelled', label: 'Đã hủy' },
                                            { value: 'confirmed', label: 'Đã Xác nhận' },
                                            { value: 'pending', label: 'Đang chờ' },
                                        ]}
                                    />
                                )}
                            />
                        </Form.Item>
                    </Col>
                </Row>

                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item label='Chủ sở hữu'>
                            <Controller 
                                name='ownerName'
                                control={control}
                                render={({ field }) => (
                                    <Input 
                                        {...field}
                                        placeholder='Nhập tên chủ sở hữu'
                                    />
                                )}
                            />
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
        </Modal>
    );
};

export default FilterListModal;