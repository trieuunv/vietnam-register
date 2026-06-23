import CodeCard from '@/components/CodeCard';
import InfoCard from '@/components/InfoCard';
import InfoItem from '@/components/InfoItem';
import InfoRow from '@/components/InfoRow';
import DetailLink from '@/components/DetailLink';

interface InspectionInfoCardProps {
    id: number
    inspectionDate: string
    nextInspectionDate: string
    certificateNumber: string
    result: string
    status: string
}

const InspectionInfoCard = ({ id, inspectionDate, nextInspectionDate, certificateNumber, result, status }: InspectionInfoCardProps) => {
    return (
        <InfoCard title='Thông tin đăng kiểm' rightElement={<DetailLink url={`/staff/inspections/${id}`} />}>
            <InfoRow>
                <InfoItem label='Ngày kiểm định'>{inspectionDate}</InfoItem>
                <InfoItem label='Ngày kiểm định tiếp theo'>{nextInspectionDate}</InfoItem>
            </InfoRow>

            <InfoRow>
                <InfoItem label='Trạng thái'>{status}</InfoItem>
                <InfoItem label='Kết quả'>{result}</InfoItem>
            </InfoRow>

            <InfoRow>
                <InfoItem label='Số chứng chỉ' fullWidth>
                    <CodeCard code={certificateNumber} />
                </InfoItem>
            </InfoRow>
        </InfoCard>
    );
};

export default InspectionInfoCard;