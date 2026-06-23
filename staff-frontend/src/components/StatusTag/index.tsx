import styles from "./StatusTag.module.scss";

interface StatusTagProps {
  status: string
}

export default function StatusTag({ status }: StatusTagProps) {
  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case "completed":
        return { bg: "#e6f7ff", text: "#1890ff", border: "#91d5ff" }
      case "pending":
        return { bg: "#fff7e6", text: "#fa8c16", border: "#ffd591" }
      case "cancelled":
        return { bg: "#fff1f0", text: "#f5222d", border: "#ffa39e" }
      default:
        return { bg: "#f6ffed", text: "#52c41a", border: "#b7eb8f" }
    }
  }

  const statusColor = getStatusColor(status)

  const getStatusText = (status: string) => {
    switch (status.toLowerCase()) {
      case "completed":
        return "Hoàn thành"
      case "pending":
        return "Đang chờ"
      case "cancelled":
        return "Đã hủy"
      default:
        return status
    }
  }

  const getStatusIcon = (status: string) => {
    switch (status.toLowerCase()) {
      case "completed":
        return "✓"
      case "pending":
        return "⏱"
      case "cancelled":
        return "✕"
      default:
        return "⚙"
    }
  }

  return (
    <div
      className={styles.container}
      style={{
        backgroundColor: statusColor.bg,
        color: statusColor.text,
        borderColor: statusColor.border,
      }}
    >
      <span>{getStatusIcon(status)}</span>
      <span>{getStatusText(status)}</span>
    </div>
  )
}
