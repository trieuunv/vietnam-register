import InfoCard from "@/components/InfoCard"
import styles from "./NotesCard.module.scss"
import { Button } from "antd"
import { Pen } from 'lucide-react';

interface NotesCardProps {
    notes?: string,
    onEdit: () => void
}

const NotesCard = ({ notes, onEdit }: NotesCardProps) => {
    return (
        <InfoCard title="Ghi chú" rightElement={<Button icon={<Pen size={14} />} onClick={onEdit}>Chỉnh sửa</Button>}>
            <div className={styles.content}>{notes ? notes : 'Không có ghi chú...'}</div>
        </InfoCard>
    )
}

export default NotesCard;
