import InfoCard from "@/components/InfoCard"
import styles from "./OwnerProfileCard.module.scss"
import { formatDate } from "@/utils/format"
import InfoRow from "@/components/InfoRow"
import InfoItem from "@/components/InfoItem"

interface OwnerProfileCardProps {
    fullName: string
    citizenId: string
    dateOfBirth: string
    gender: string
    registrationDate: string
}

export default function OwnerProfileCard({
    fullName,
    citizenId,
    dateOfBirth,
    gender,
    registrationDate,
}: OwnerProfileCardProps) {
    const getGenderText = (gender: string) => {
        switch (gender) {
            case "Male":
                return "Nam"
            case "Female":
                return "Nữ"
            case "Other":
                return "Khác"
            default:
                return gender
        }
    }

    return (
        <InfoCard title="Thông tin cá nhân">
            <div className={styles.profileHeader}>
                <div className={styles.avatarContainer}>
                    <div className={styles.avatar}>
                        {fullName
                            .split(" ")
                            .map((name) => name[0])
                            .slice(-2)
                            .join("")
                            .toUpperCase()}
                    </div>
                </div>
                <div className={styles.nameContainer}>
                    <h2 className={styles.fullName}>{fullName}</h2>
                    <div className={styles.registrationInfo}>Đăng ký từ: {formatDate(registrationDate)}</div>
                </div>
            </div>

            <div className={styles.profileDetails}>
                <InfoRow>
                    <InfoItem label="CCCD/CMND">
                        {citizenId}
                    </InfoItem>
                    <InfoItem label="Ngày sinh">
                        {formatDate(dateOfBirth)}
                    </InfoItem>
                </InfoRow>
                <InfoRow>
                    <InfoItem label="Giới tính">
                        {getGenderText(gender)}
                    </InfoItem>
                </InfoRow>
            </div>
        </InfoCard>
    )
}
