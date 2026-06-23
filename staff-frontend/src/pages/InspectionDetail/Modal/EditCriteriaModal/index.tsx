import { useEffect, useState } from 'react';
import { Form, Input, Modal, Select, Typography } from 'antd';
import { ICriteriaResult } from '@/types/Criteria';

const { Text } = Typography;

interface EditCriteriaModalProps {
    isOpen: boolean
    onClose: () => void
    initialData?: ICriteriaResult | null
    onSubmit: (data: ICriteriaResult) => Promise<void> | void;
    loading?: boolean;
}

const EditCriteriaModal = ({ isOpen, onClose, initialData, onSubmit, loading = false }: EditCriteriaModalProps) => {
    const [form] = Form.useForm();
    const [confirmLoading, setConfirmLoading] = useState(false);

    useEffect(() => {
        if (isOpen) {
            if (initialData) {
                form.setFieldsValue({
                    result: initialData.result,
                    notes: initialData.notes || '',
                });
            } else {
                form.resetFields(); // chỉ gọi khi Modal mở
            }
        }
    }, [isOpen, initialData, form]);

    const handleOk = async () => {
        try {
            setConfirmLoading(true);
            const values = await form.validateFields();
            await onSubmit(values);
            onClose();
        } catch (error) {
            console.error('Validation failed:', error);
        } finally {
            setConfirmLoading(false);
        }
    };

    return (
        <Modal
            title="Chỉnh sửa tiêu chí kiểm định"
            open={isOpen}
            onOk={handleOk}
            confirmLoading={confirmLoading || loading}
            onCancel={onClose}
            maskClosable={false}
            okText='Cập nhật'
            cancelText='Hủy'
        >
            <Form form={form} layout="vertical">
                <Form.Item>
                    <Text>{initialData?.criteriaName || '---'}</Text>
                </Form.Item>

                <Form.Item
                    name="result"
                    label="Kết quả"
                    rules={[{ required: true, message: 'Vui lòng chọn kết quả' }]}
                >
                    <Select placeholder="Chọn kết quả">
                        <Select.Option value="passed">Đạt</Select.Option>
                        <Select.Option value="failed">Không đạt</Select.Option>
                    </Select>
                </Form.Item>

                <Form.Item name="notes" label="Ghi chú">
                    <Input.TextArea rows={4} placeholder="Nhập ghi chú (nếu có)" />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default EditCriteriaModal;