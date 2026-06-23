// import { formatDate } from "../../utils"

import InfoCard from "@/components/InfoCard"
import InfoItem from "@/components/InfoItem"
import InfoRow from "@/components/InfoRow"
import StatusTag from "@/components/StatusTag"
import CodeCard from "@/components/CodeCard"

interface AppointmentInfoCardProps {
    id: number
    date: string
    status: string
    confirmationCode: string
}

const AppointmentInfoCard = ({ id, date, status, confirmationCode }: AppointmentInfoCardProps) => {
    return (
        <InfoCard title="Thông tin lịch hẹn" rightElement={<StatusTag status={status} />}>
            <InfoRow>
                <InfoItem label="Mã lịch hẹn">#{id}</InfoItem>
                <InfoItem label="Ngày hẹn">{date}</InfoItem>
            </InfoRow>
            <InfoRow>
                <InfoItem label="Mã xác nhận" fullWidth>
                    <CodeCard code={confirmationCode} />
                </InfoItem>
            </InfoRow>
        </InfoCard>
    )
}

export default AppointmentInfoCard;
