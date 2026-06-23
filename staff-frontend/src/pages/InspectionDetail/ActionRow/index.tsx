import { useState } from "react"
import styles from "./ActionRow.module.scss"
import { Button } from "antd"

interface ActionRowProps {
    status: string
    onAction: (action: string) => void
    canComplete?: boolean
}

export default function ActionRow({ status, onAction, canComplete = false }: ActionRowProps) {
    const [loading, setLoading] = useState<string | null>(null)

    const handleAction = async (action: string) => {
        setLoading(action)
        try {
            await onAction(action)
        } finally {
            setLoading(null)
        }
    }

    const renderActionButtons = () => {
        switch (status.toLowerCase()) {
            case "pending":
                return (
                    <>
                        <Button
                            type="primary"
                            onClick={() => handleAction("start")}
                            disabled={(loading != "start" && loading !== null) || !canComplete}
                            loading={loading == "start"}
                        >
                            Bắt đầu kiểm định
                        </Button>
                    </>
                )
            case "in_progress":
                return (
                    <>
                        <Button
                            type="primary"
                            onClick={() => handleAction("complete")}
                            disabled={(loading != "complete" && loading !== null) || !canComplete}
                            loading={loading == "complete"}
                        >
                            Hoàn thành đăng kiểm
                        </Button>
                        <Button
                            type="default"
                            onClick={() => handleAction("save")}
                            disabled={loading != "save" && loading !== null}
                            loading={loading == "save"}
                        >
                            Lưu nháp
                        </Button>
                        <Button
                            type="primary" danger
                            onClick={() => handleAction("cancel")}
                            disabled={loading != "cancel" && loading !== null}
                            loading={loading == "cancel"}
                        >
                            Hủy đăng kiểm
                        </Button>
                    </>
                )
            case "completed":
                return (
                    <>
                        <Button
                            className={`${styles.default}`}
                            onClick={() => handleAction("print")}
                            disabled={loading !== null}
                        >
                            {loading === "print" ? "Đang xử lý..." : "In kết quả"}
                        </Button>
                        <Button
                            className={`${styles.default}`}
                            onClick={() => handleAction("export")}
                            disabled={loading !== null}
                        >
                            {loading === "export" ? "Đang xử lý..." : "Xuất PDF"}
                        </Button>
                    </>
                )
            case "cancelled":
                return (
                    <>
                        <Button
                            disabled={loading != "undo_cancel" && loading !== null}
                            loading={loading == "undo_cancel"}
                            onClick={() => handleAction("undo_cancel")}
                            type="primary"
                        >Bỏ hủy</Button>
                    </>
                )
            default:
                return null
        }
    }

    return <div className={styles.container}>{renderActionButtons()}</div>
}
