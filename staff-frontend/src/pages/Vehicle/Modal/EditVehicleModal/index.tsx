import { Button, Modal } from 'antd';
import React from 'react';

interface IEditVehicleModalProps {
    isModalOpen: boolean,
    onClose: () => void
}

const EditVehicleModal: React.FC<IEditVehicleModalProps> = ({ isModalOpen, onClose }) => {    
    const handleOk = () => {
        onClose();
    };
    
    const handleCancel = () => {
        onClose()
    };

      return (
        <>
          <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
          </Modal>
        </>
      );
};

export default EditVehicleModal;