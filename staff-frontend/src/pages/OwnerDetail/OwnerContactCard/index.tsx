import InfoCard from "@/components/InfoCard"
import styles from "./OwnerContactCard.module.scss"
import InfoRow from "@/components/InfoRow"
import InfoItem from "@/components/InfoItem"

interface OwnerContactCardProps {
    phone: string
    email: string
    address: string
}

export default function OwnerContactCard({ phone, email, address }: OwnerContactCardProps) {
    return (
        <InfoCard title="Thông tin liên hệ">
            <InfoRow>
                <InfoItem label="Số điện thoại">
                    <a href={`tel:${phone}`} className={styles.contactLink}>
                        {phone}
                    </a>
                </InfoItem>
                <InfoItem label="Email">
                    <a href={`mailto:${email}`} className={styles.contactLink}>
                        {email}
                    </a>
                </InfoItem>
            </InfoRow>
            <InfoRow>
                <InfoItem label="Địa chỉ" fullWidth>
                    {address}
                </InfoItem>
            </InfoRow>
            <div className={styles.mapContainer}>
                <div className={styles.mapPlaceholder}>
                    <div className={styles.mapIcon}>📍</div>
                    <div className={styles.mapText}>Bản đồ</div>
                </div>
            </div>
        </InfoCard>
    )
}
