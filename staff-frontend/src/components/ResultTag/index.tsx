import styles from "./ResultTag.module.scss";

interface ResultTagProps {
  result: string;
}

export default function ResultTag({ result }: ResultTagProps) {
  const getResultStyle = (result: string) => {
    switch (result) {
      case "passed":
        return { bg: "#f6ffed", text: "#52c41a", border: "#b7eb8f", label: "Đạt", icon: "✔" };
      case "failed":
        return { bg: "#fff1f0", text: "#f5222d", border: "#ffa39e", label: "Không đạt", icon: "✖" };
      case "warning":
        return { bg: "#fffbe6", text: "#faad14", border: "#ffe58f", label: "Cảnh báo", icon: "⚠" };
      case "uninspected":
        return { bg: "#e6f4ff", text: "#1890ff", border: "#91d5ff", label: "Chưa đăng kiểm", icon: "⏳" };
      default:
        return { bg: "#f0f0f0", text: "#595959", border: "#d9d9d9", label: "Không rõ", icon: "?" };
    }
  };

  const style = getResultStyle(result);

  return (
    <div
      className={styles.container}
      style={{
        backgroundColor: style.bg,
        color: style.text,
        borderColor: style.border,
      }}
    >
      <span>{style.icon}</span>
      <span>{style.label}</span>
    </div>
  );
}
