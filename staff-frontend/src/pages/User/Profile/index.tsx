import { useCallback, useEffect } from "react";

import { useAppDispatch } from "@/store/store";

import UserAuthThunk from '../slice/actions';

const Profile = () => {
    const dispatch = useAppDispatch();

    const fetchUserData = useCallback(() => {
        dispatch(UserAuthThunk.getMe());
    }, [dispatch]);

    useEffect(() => {
        fetchUserData();
    }, [fetchUserData]);

    return (
        <div>
            
        </div>
    );
};

export default Profile;