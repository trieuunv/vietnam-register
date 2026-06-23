import { useCallback, useState } from "react";
import styles from './Login.module.scss';

import { Link, useNavigate } from "react-router-dom";
import { useForm, Controller } from "react-hook-form";

import { ILoginRequest } from "@/types";
import { useAppDispatch } from "@/store/store";
import AuthActionThunk from '../slice/actions';

import { AlertCircle } from "lucide-react"
import { Input, Button } from 'antd';

const Login = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const { handleSubmit, control } = useForm<ILoginRequest>();

    const [error, setError] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);

    const onSubmit = useCallback(
        async (data: ILoginRequest) => {
            setLoading(true);
            try {
                await dispatch(AuthActionThunk.login(data)).unwrap();
                navigate('/staff/dashboard');
            } catch (err: any) {
                if (err?.status === 401) {
                    setError('Thông tin đăng nhập không chính xác.');
                } else {
                    setError('Đã có lỗi xảy ra, vui lòng thử lại.');
                }
            } finally {
                setLoading(false);
            }
        },
        [dispatch]
    );

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <div className={styles.header}>
                    <h1 className={styles.title}>Đăng Nhập</h1>
                    <p className={styles.description}>Đăng nhập vào hệ thống đăng kiểm xe máy</p>
                </div>

                <div className={styles.content}>
                    <form onSubmit={handleSubmit(onSubmit)}>
                        {error && (
                            <div className={styles.errorAlert}>
                                <AlertCircle className={styles.alertIcon} />
                                <p className={styles.errorMessage}>{error}</p>
                            </div>
                        )}

                        <div className={styles.formGroup}>
                            <div className={styles.inputGroup}>
                                <label 
                                    htmlFor="email"
                                    className={styles.inputLabel}
                                >
                                    Email
                                </label>
                                <Controller 
                                    name="email"
                                    control={control}
                                    render={({ field }) => (
                                        <Input 
                                            {...field}
                                            id="email"
                                            placeholder="Nhập email"
                                            size="large"
                                        />
                                    )}
                                />
                            </div>

                            <div className={styles.inputGroup}>
                                <label 
                                    htmlFor="password"
                                    className={styles.inputLabel}
                                >
                                    Mật khẩu
                                </label>
                                <Controller 
                                    name="password"
                                    control={control}
                                    render={({ field }) => (
                                        <Input.Password 
                                            {...field}
                                            id="password"
                                            placeholder="Nhập mật khẩu"
                                            size="large"
                                        />
                                    )}
                                />
                            </div>

                            <div className={styles.submit}>
                                <Button 
                                    type="primary" 
                                    htmlType="submit"
                                    size='large'
                                    block
                                    loading={loading}
                                >
                                    Đăng nhập
                                </Button>
                            </div>
                        </div>
                    </form>
                </div>

                <div className={styles.footer}>
                    <p className={styles.loginLink}>
                        <Link to={'/'}>
                            Quay lại trang chủ
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;