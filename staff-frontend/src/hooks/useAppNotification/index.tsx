import { notification } from 'antd';
import { ReactNode } from 'react';

type NotificationType = 'success' | 'info' | 'warning' | 'error';

interface NotificationConfig {
    message: ReactNode;
    description?: ReactNode;
    duration?: number;
    placement?: 'top' | 'topLeft' | 'topRight' | 'bottom' | 'bottomLeft' | 'bottomRight';
}

const useAppNotification = () => {
    const [api, contextHolder] = notification.useNotification();

    const showNotification = (type: NotificationType, config: NotificationConfig) => {
        api[type]({
            ...config,
            duration: config.duration || (type === 'error' ? 5 : 3),
            placement: config.placement || 'topRight',
        });
    };

    const showSuccess = (config: Omit<NotificationConfig, 'type'>) =>
        showNotification('success', config);

    const showError = (config: Omit<NotificationConfig, 'type'>) =>
        showNotification('error', config);

    const showInfo = (config: Omit<NotificationConfig, 'type'>) =>
        showNotification('info', config);

    const showWarning = (config: Omit<NotificationConfig, 'type'>) =>
        showNotification('warning', config);

    return {
        contextHolder,
        showNotification,
        showSuccess,
        showError,
        showInfo,
        showWarning,
    };
};

const NotificationProvider = ({ children }: { children: ReactNode }) => {
    const { contextHolder } = useAppNotification();

    return (
        <>
            {contextHolder}
            {children}
        </>
    );
};

export {
    useAppNotification,
    NotificationProvider,
    type NotificationType,
    type NotificationConfig,
};