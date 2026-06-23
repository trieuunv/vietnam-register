export function formatDate(dateString: string): string {
    const date = new Date(dateString)
    const day = String(date.getDate()).padStart(2, '0')
    const month = String(date.getMonth() + 1).padStart(2, '0') // Tháng bắt đầu từ 0
    const year = date.getFullYear()

    return `${day}/${month}/${year}`
}

export function formatNumber(value: number): string {
    return value.toLocaleString('vi-VN')
}

export function formatNumberWithDecimal(value: number): string {
    return value.toLocaleString('vi-VN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    })
}

export const formatCurrencyVND = (amount: number): string => {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0
    }).format(amount);
};
