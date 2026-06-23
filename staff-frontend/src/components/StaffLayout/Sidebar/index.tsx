import React from 'react';
import { Link, useLocation } from "react-router-dom"
import styles from './Sidebar.module.scss';
import {
    LayoutDashboard,
    ClipboardList,
    FileText,
    Users,
    Settings,
    LogOut,
    X,
    CarFront,
    Calendar
  } from "lucide-react"

interface SidebarProps {
    isOpen: boolean
    onClose: () => void
}

const navItems = [
    { name: "Bảng điều khiển", href: "/staff/dashboard", icon: LayoutDashboard },
    { name: "Đăng kiểm", href: "/staff/inspections", icon: ClipboardList },
    { name: "Phương tiện", href: "/staff/vehicles", icon: CarFront },
    { name: "Lịch trình", href: "/staff/appointments", icon: Calendar },
    { name: "Báo cáo", href: "/staff/reports", icon: FileText },
    { name: "Quản lý nhân viên", href: "/staff/users", icon: Users },
    { name: "Cài đặt", href: "/staff/settings", icon: Settings },
  ]

const Sidebar: React.FC<SidebarProps> = ({ isOpen, onClose }) => {
    const location = useLocation()

    return (
        <div className={`${styles.sidebar} ${isOpen ? styles.sidebarOpen : ""}`}>
            <div className={styles.sidebarHeader}>
                <h2 className={styles.sidebarTitle}>Đăng Kiểm Xe Máy</h2>
                <button onClick={onClose} className={styles.closeButton}>
                    <X className={styles.icon} />
                </button>
            </div>

            <nav className={styles.sidebarNav}>
                <ul>
                    {navItems.map((item, index) => (
                        <li key={index}>
                            <Link
                                to={item.href}
                                className={`${styles.navItem} ${
                                location.pathname === item.href ? styles.navItemActive : ""
                                }`}
                            >
                                <item.icon className={styles.icon} />
                                <span>{item.name}</span>
                            </Link>
                        </li>
                    ))}
                </ul>
            </nav>

            <div className={styles.sidebarFooter}>
                <button className={styles.logoutButton}>
                    <LogOut className={styles.icon} />
                    Đăng xuất
                </button>
            </div>
        </div>
    );
};

export default Sidebar;