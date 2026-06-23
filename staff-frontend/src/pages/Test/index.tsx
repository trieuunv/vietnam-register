import { UserService } from '@/services';
import React, { useEffect } from 'react';

const Test = () => {
    useEffect(() => {
        const getAccessToken = async () => {
            try {
                const user = await UserService.getMe();
                console.log(user);
            } catch (error) {
                console.log(error);
            }
        }

        getAccessToken();
    }, []);

    return (
        <div>
            Test Page  
        </div>
    );
};

export default Test;