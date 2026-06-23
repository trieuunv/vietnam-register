import InfoRow from "@/components/InfoRow"
import styles from "./VehicleInfoCard.module.scss"
import InfoItem from "@/components/InfoItem"
import InfoCard from "@/components/InfoCard"
import StatusField from "@/components/StatusField"

interface VehicleInfoCardProps {
    registrationNumber: string
    brand: string
    model: string
    manufactureYear: number | string
    chassisNumber: string
    engineNumber: string
    vehicleTypeName: string
    status?: string
}

export default function VehicleInfoCard({
    registrationNumber,
    brand,
    model,
    manufactureYear,
    chassisNumber,
    engineNumber,
    vehicleTypeName,
    status,
}: VehicleInfoCardProps) {
    return (
        <InfoCard
            title="Thông tin phương tiện"
            rightElement={
                status ? (
                    <StatusField status={status} />
                ) : undefined
            }
        >
            <InfoRow>
                <InfoItem label="Biển số xe">
                    <span className={styles.highlight}>{registrationNumber}</span>
                </InfoItem>
                <InfoItem label="Loại xe">
                    {vehicleTypeName}
                </InfoItem>
            </InfoRow>
            <InfoRow>
                <InfoItem label="Hãng xe">
                    {brand}
                </InfoItem>
                <InfoItem label="Mẫu xe">
                    {model}
                </InfoItem>
            </InfoRow>
            <InfoRow>
                <InfoItem label="Năm sản xuất">
                    {manufactureYear}
                </InfoItem>
            </InfoRow>
            <InfoRow>
                <InfoItem label="Số khung">
                    {chassisNumber}
                </InfoItem>
                <InfoItem label="Số máy">
                    {engineNumber}
                </InfoItem>
            </InfoRow>
        </InfoCard>
    )
}
