import { useEffect, useState } from 'react';
import { Form, Input, Modal } from 'antd';

interface EditNotesModalProps {
    isOpen: boolean
    onClose: () => void
    initialData?: string,
    onSubmit: (notes: string) => Promise<void> | void;
    loading?: boolean;
}

const EditNotesModal = ({ isOpen, onClose, initialData, onSubmit, loading = false }: EditNotesModalProps) => {
    const [form] = Form.useForm();
    const [confirmLoading, setConfirmLoading] = useState(false);

    useEffect(() => {
        if (isOpen) {
            if (initialData) {
                form.setFieldsValue({
                    notes: initialData || '',
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
            await onSubmit(values.notes);
            onClose();
        } catch (error) {
            console.error('Validation failed:', error);
        } finally {
            setConfirmLoading(false);
        }
    };

    return (
        <Modal
            title="Chỉnh sửa ghi chú"
            open={isOpen}
            onOk={handleOk}
            confirmLoading={confirmLoading || loading}
            onCancel={onClose}
            maskClosable={false}
            okText='Cập nhật'
            cancelText='Hủy'
        >
            <Form form={form} layout="vertical">
                <Form.Item name="notes" label="Ghi chú">
                    <Input.TextArea rows={4} placeholder="Nhập ghi chú (nếu có)" />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default EditNotesModal;