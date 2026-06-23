import CodeCard from '@/components/CodeCard';
import InfoCard from '@/components/InfoCard';
import InfoItem from '@/components/InfoItem';
import InfoRow from '@/components/InfoRow';
import StatusField from '@/components/StatusField';

interface InspectionInfoCardProps {
    inspectionDate: string
    nextInspectionDate: string
    certificateNumber: string
    fee: string
    result: string
    status: string
}

const InspectionInfoCard = ({ inspectionDate, nextInspectionDate, certificateNumber, fee, result, status }: InspectionInfoCardProps) => {
    return (
        <InfoCard title='Thông tin đăng kiểm' rightElement={<StatusField status={status} />}>
            <InfoRow>
                <InfoItem label='Ngày kiểm định'>{inspectionDate}</InfoItem>
                <InfoItem label='Ngày kiểm định tiếp theo'>{nextInspectionDate}</InfoItem>
            </InfoRow>

            <InfoRow>
                <InfoItem label='Kết quả'>{result}</InfoItem>
                <InfoItem label='Phí đăng kiểm'>{fee}</InfoItem>
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