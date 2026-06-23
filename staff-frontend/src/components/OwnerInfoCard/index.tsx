import InfoCard from "@/components/InfoCard"
import InfoItem from "@/components/InfoItem"
import InfoRow from "@/components/InfoRow"
import DetailLink from "../DetailLink"

interface OwnerInfoCardProps {
    id: number
    fullName: string
    phone: string
    email?: string
    citizenNumber?: string
}

const OwnerInfoCard = ({ id, fullName, phone, email, citizenNumber }: OwnerInfoCardProps) => {
    return (
        <InfoCard title="Thông tin chủ xe" rightElement={<DetailLink url={`/staff/owners/${id}`} />}>
            <InfoRow>
                <InfoItem label="Họ và tên">{fullName}</InfoItem>
                <InfoItem label="CCCD/CMND">{citizenNumber}</InfoItem>
            </InfoRow>
            <InfoRow>
                <InfoItem label="Số điện thoại">{phone}</InfoItem>
                <InfoItem label="Email">{email}</InfoItem>
            </InfoRow>
        </InfoCard>
    )
}

export default OwnerInfoCard;
