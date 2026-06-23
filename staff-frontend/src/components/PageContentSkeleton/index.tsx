import React from 'react';
import { Flex, Skeleton } from 'antd';

const PageContentSkeleton: React.FC = () => {
    return (
        <Flex gap={20} vertical>
            <Skeleton.Node
                active={true}
                style={{
                    width: '100%',
                    height: 200,
                }}
            >
                <div style={{ width: '100%', height: '100%' }} />
            </Skeleton.Node>

            <Skeleton.Node
                active={true}
                style={{
                    width: '100%',
                    height: 200
                }}
            >
                <div style={{ width: '100%', height: '100%' }} />
            </Skeleton.Node>

            <Skeleton.Node
                active={true}
                style={{
                    width: '100%',
                    height: 200
                }}
            >
                <div style={{ width: '100%', height: '100%' }} />
            </Skeleton.Node>

            <Skeleton.Node
                active={true}
                style={{
                    width: '100%',
                    height: 200
                }}
            >
                <div style={{ width: '100%', height: '100%' }} />
            </Skeleton.Node>
        </Flex>
    );
};

export default PageContentSkeleton;