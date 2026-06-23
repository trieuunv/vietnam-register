import { useState } from "react"
import styles from "./OwnerVehiclesCard.module.scss"
import InfoCard from "@/components/InfoCard"
import { formatDate } from "@/utils/format"
import { Plus } from 'lucide-react';
import { IVehicle } from "@/types";

interface OwnerVehiclesCardProps {
    vehicles: IVehicle[]
    onViewVehicle: (id: number) => void
    onAddVehicle: () => void
}

export default function OwnerVehiclesCard({ vehicles, onViewVehicle, onAddVehicle }: OwnerVehiclesCardProps) {
    const [searchTerm, setSearchTerm] = useState("")

    const filteredVehicles = vehicles.filter((vehicle) => {
        const searchTermLower = searchTerm.toLowerCase();
        const regNumber = vehicle.registrationNumber ? vehicle.registrationNumber.toLowerCase() : '';
        const brand = vehicle.brand ? vehicle.brand.toLowerCase() : '';
        const model = vehicle.model ? vehicle.model.toLowerCase() : '';

        return (
            regNumber.includes(searchTermLower) ||
            brand.includes(searchTermLower) ||
            model.includes(searchTermLower)
        );
    });

    const getStatusClass = (status?: string) => {
        if (!status) return ""

        switch (status.toLowerCase()) {
            case "active":
                return styles.statusActive
            case "pending":
                return styles.statusPending
            case "expired":
                return styles.statusExpired
            default:
                return ""
        }
    }

    const getStatusText = (status?: string) => {
        if (!status) return "N/A"

        switch (status.toLowerCase()) {
            case "active":
                return "Hoạt động"
            case "pending":
                return "Đang chờ"
            case "expired":
                return "Hết hạn"
            default:
                return status
        }
    }

    const getDaysRemaining = (nextInspectionDate?: string | null) => {
        if (!nextInspectionDate) return null

        const today = new Date()
        const nextDate = new Date(nextInspectionDate)
        const diffTime = nextDate.getTime() - today.getTime()
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

        return diffDays
    }

    return (
        <InfoCard
            title="Phương tiện sở hữu"
            rightElement={
                <button className={styles.addButton} onClick={onAddVehicle}>
                    <Plus size={14} />
                    <span>Thêm phương tiện</span>
                </button>
            }
        >
            <div className={styles.searchContainer}>
                <input
                    type="text"
                    placeholder="Tìm kiếm phương tiện..."
                    className={styles.searchInput}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            {filteredVehicles.length === 0 ? (
                <div className={styles.emptyState}>
                    <div className={styles.emptyIcon}>🔍</div>
                    <div className={styles.emptyText}>
                        {searchTerm ? "Không tìm thấy phương tiện phù hợp" : "Chưa có phương tiện nào"}
                    </div>
                </div>
            ) : (
                <div className={styles.vehiclesList}>
                    {filteredVehicles.map((vehicle) => {
                        const daysRemaining = getDaysRemaining(vehicle.nextInspectionDate)
                        const isExpired = daysRemaining !== null && daysRemaining < 0

                        return (
                            <div key={vehicle.id} className={styles.vehicleCard} onClick={() => onViewVehicle(vehicle.id)}>
                                <div className={styles.vehicleIcon}>
                                    <span>🚗</span>
                                </div>
                                <div className={styles.vehicleInfo}>
                                    <div className={styles.vehicleHeader}>
                                        <h3 className={styles.vehicleNumber}>{vehicle.registrationNumber}</h3>
                                        <span className={`${styles.vehicleStatus} ${getStatusClass(vehicle.status)}`}>
                                            {getStatusText(vehicle.status)}
                                        </span>
                                    </div>
                                    <div className={styles.vehicleDetails}>
                                        <div className={styles.vehicleModel}>
                                            {vehicle.brand} {vehicle.model} ({vehicle.manufactureYear})
                                        </div>
                                        <div className={styles.vehicleType}>{vehicle.vehicleTypeName}</div>
                                    </div>
                                    <div className={styles.inspectionInfo}>
                                        {vehicle.nextInspectionDate ? (
                                            <div className={`${styles.expiryDate} ${isExpired ? styles.expired : ""}`}>
                                                <span className={styles.expiryLabel}>{isExpired ? "Đã hết hạn: " : "Hết hạn đăng kiểm: "}</span>
                                                <span className={styles.expiryValue}>{formatDate(vehicle.nextInspectionDate)}</span>
                                                {daysRemaining !== null && (
                                                    <span className={`${styles.daysRemaining} ${isExpired ? styles.expired : ""}`}>
                                                        {isExpired ? `(${Math.abs(daysRemaining)} ngày trước)` : `(còn ${daysRemaining} ngày)`}
                                                    </span>
                                                )}
                                            </div>
                                        ) : (
                                            <div className={styles.noInspection}>Chưa có thông tin đăng kiểm</div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        )
                    })}
                </div>
            )}
        </InfoCard>
    )
}
