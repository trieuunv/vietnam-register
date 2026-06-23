import InfoCard from "@/components/InfoCard"
import InfoRow from "@/components/InfoRow"
import InfoItem from "@/components/InfoItem"
import styles from "./VehicleInfoCard.module.scss"
import DetailLink from "../DetailLink"

interface VehicleInfoCardProps {
    id: number,
    registrationNumber: string
    brand: string
    model: string
    color: string
    status: string
}

const VehicleInfoCard = ({ id, registrationNumber, brand, model, color, status }: VehicleInfoCardProps) => {
    return (
        <InfoCard title="Thông tin xe" rightElement={<DetailLink url={`/staff/vehicles/${id}`} />}>
            <InfoRow>
                <InfoItem label="Biển số xe">
                    <span className={styles.highlight}>{registrationNumber}</span>
                </InfoItem>
                <InfoItem label="Hãng xe">{brand}</InfoItem>
            </InfoRow>

            <InfoRow>
                <InfoItem label="Trạng thái">{status}</InfoItem>
            </InfoRow>

            <InfoRow>
                <InfoItem label="Mẫu xe">{model}</InfoItem>
                <InfoItem label="Màu sắc">{color}</InfoItem>
            </InfoRow>
        </InfoCard>
    )
}

export default VehicleInfoCard;
