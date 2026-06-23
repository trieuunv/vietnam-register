import styles from "./ActionCard.module.scss"

interface ActionCardProps {
    status: string
    onAction: (action: string) => void
    loading: string | null
}

export default function ActionCard({ status, onAction, loading }: ActionCardProps) {
    const handleAction = async (action: string) => {
        onAction(action)
    }

    // Hiển thị các nút hành động dựa trên trạng thái hiện tại
    const renderActionButtons = () => {
        switch (status.toLowerCase()) {
            case "pending":
                return (
                    <>
                        <button
                            className={`${styles.button} ${styles.primary}`}
                            onClick={() => handleAction("accept")}
                            disabled={loading !== null}
                        >
                            {loading === "accept" ? "Đang xử lý..." : "Chấp nhận"}
                        </button>
                        <button
                            className={`${styles.button} ${styles.danger}`}
                            onClick={() => handleAction("reject")}
                            disabled={loading !== null}
                        >
                            {loading === "reject" ? "Đang xử lý..." : "Từ chối"}
                        </button>
                    </>
                )
            case "confirmed":
                return (
                    <button
                        className={`${styles.button} ${styles.primary}`}
                        onClick={() => handleAction("start")}
                        disabled={loading !== null}
                    >
                        {loading === "start" ? "Đang xử lý..." : "Bắt đầu kiểm định"}
                    </button>
                )
            case "in_progress":
                return (
                    <>
                        <button
                            className={`${styles.button} ${styles.success}`}
                            onClick={() => handleAction("complete")}
                            disabled={loading !== null}
                        >
                            {loading === "complete" ? "Đang xử lý..." : "Hoàn thành kiểm định"}
                        </button>
                        <button
                            className={`${styles.button} ${styles.warning}`}
                            onClick={() => handleAction("issue")}
                            disabled={loading !== null}
                        >
                            {loading === "issue" ? "Đang xử lý..." : "Báo cáo vấn đề"}
                        </button>
                    </>
                )
            default:
                return null
        }
    }

    if (["cancelled", "rejected"].includes(status.toLowerCase())) {
        return null
    }

    return (
        <div className={styles.container}>
            <div></div>
            <div className={styles.actionSection}>{renderActionButtons()}</div>
        </div>
    )
}
