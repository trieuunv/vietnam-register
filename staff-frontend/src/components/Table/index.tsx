import type React from "react"
import { useState, useEffect } from "react"
import { ChevronUp, ChevronDown, ChevronLeft, ChevronRight, Loader2 } from "lucide-react"
import styles from "./Table.module.css"

export type TableColumn<T> = {
  key: string
  title: React.ReactNode
  dataIndex: keyof T
  render?: (value: any, record: T, index: number) => React.ReactNode
  width?: number | string
  sorter?: boolean | ((a: T, b: T) => number)
  align?: "left" | "center" | "right"
}

export type TableProps<T> = {
  columns: TableColumn<T>[]
  dataSource: T[]
  rowKey?: keyof T | ((record: T) => string)
  loading?: boolean
  striped?: boolean
  size?: "small" | "middle" | "large"
  pagination?:
  | boolean
  | {
    pageSize?: number
    current?: number
    total?: number
    onChange?: (page: number, pageSize: number) => void
  }
  onChange?: (pagination: any, filters: any, sorter: any) => void
  className?: string
}

export function TableNew<T extends Record<string, any>>({
  columns,
  dataSource,
  rowKey = "id",
  loading = false,
  striped = false,
  size = "middle",
  pagination = false,
  onChange,
  className = "",
}: TableProps<T>) {
  const [sortedInfo, setSortedInfo] = useState<{
    columnKey: string | null
    order: "ascend" | "descend" | null
  }>({
    columnKey: null,
    order: null,
  })

  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, setPageSize] = useState(3) // Changed from 10 to 3 to make pagination visible with sample data

  // Handle pagination props
  useEffect(() => {
    if (typeof pagination === "object") {
      if (pagination.current) setCurrentPage(pagination.current)
      if (pagination.pageSize) setPageSize(pagination.pageSize)
    }
  }, [pagination])

  const handleSort = (column: TableColumn<T>) => {
    if (!column.sorter) return

    let newOrder: "ascend" | "descend" | null = "ascend"

    if (sortedInfo.columnKey === column.key) {
      if (sortedInfo.order === "ascend") {
        newOrder = "descend"
      } else if (sortedInfo.order === "descend") {
        newOrder = null
      }
    }

    const newSortedInfo = {
      columnKey: newOrder ? column.key : null,
      order: newOrder,
    }

    setSortedInfo(newSortedInfo)

    if (onChange) {
      onChange({ current: currentPage, pageSize }, {}, { columnKey: column.key, order: newOrder })
    }
  }

  const handlePageChange = (page: number) => {
    setCurrentPage(page)

    if (onChange) {
      onChange({ current: page, pageSize }, {}, { columnKey: sortedInfo.columnKey, order: sortedInfo.order })
    }

    if (typeof pagination === "object" && pagination.onChange) {
      pagination.onChange(page, pageSize)
    }
  }

  // Sort data if needed
  const displayData = [...dataSource]
  if (sortedInfo.columnKey && sortedInfo.order) {
    const column = columns.find((col) => col.key === sortedInfo.columnKey)
    if (column && column.sorter) {
      if (typeof column.sorter === "function") {
        displayData.sort((a, b) => {
          const result = (column.sorter as Function)(a, b)
          return sortedInfo.order === "ascend" ? result : -result
        })
      } else {
        displayData.sort((a, b) => {
          const aValue = a[column.dataIndex]
          const bValue = b[column.dataIndex]
          if (aValue < bValue) return sortedInfo.order === "ascend" ? -1 : 1
          if (aValue > bValue) return sortedInfo.order === "ascend" ? 1 : -1
          return 0
        })
      }
    }
  }

  // Handle pagination
  const showPagination = pagination !== false && displayData.length > 0
  const total = typeof pagination === "object" && pagination.total ? pagination.total : displayData.length
  const totalPages = Math.ceil(total / pageSize)

  // Create a copy of the data before slicing for pagination
  let paginatedData = [...displayData]
  if (showPagination) {
    const startIndex = (currentPage - 1) * pageSize
    paginatedData = displayData.slice(startIndex, startIndex + pageSize)
  }

  const getRowKey = (record: T, index: number): string => {
    if (typeof rowKey === "function") {
      return rowKey(record)
    }
    return String(record[rowKey] ?? index)
  }

  const tableClasses = [
    styles.table,
    styles[size],
    striped ? styles.striped : "",
    loading ? styles.loading : "",
    className,
  ]
    .filter(Boolean)
    .join(" ")

  return (
    <div>
      <div className={styles.tableContainer}>
        <table className={tableClasses}>
          <thead className={styles.tableHeader}>
            <tr>
              {columns.map((column) => (
                <th
                  key={column.key}
                  className={`${column.sorter ? styles.sortable : ""} ${column.align ? styles[`align${column.align.charAt(0).toUpperCase() + column.align.slice(1)}`] : ""
                    }`}
                  style={{ width: column.width }}
                  onClick={() => column.sorter && handleSort(column)}
                >
                  <div className={styles.headerContent}>
                    <span>{column.title}</span>
                    {column.sorter && (
                      <span className={styles.sorterIcons}>
                        <ChevronUp
                          size={14}
                          className={`${styles.sorterIcon} ${sortedInfo.columnKey === column.key && sortedInfo.order === "ascend" ? styles.active : ""
                            }`}
                        />
                        <ChevronDown
                          size={14}
                          className={`${styles.sorterIcon} ${sortedInfo.columnKey === column.key && sortedInfo.order === "descend" ? styles.active : ""
                            }`}
                        />
                      </span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody className={styles.tableBody}>
            {paginatedData.length > 0 ? (
              paginatedData.map((record, index) => (
                <tr key={getRowKey(record, index)}>
                  {columns.map((column) => (
                    <td
                      key={`${getRowKey(record, index)}-${column.key}`}
                      className={
                        column.align
                          ? styles[`align${column.align.charAt(0).toUpperCase() + column.align.slice(1)}`]
                          : ""
                      }
                    >
                      {column.render
                        ? column.render(record[column.dataIndex], record, index)
                        : record[column.dataIndex]}
                    </td>
                  ))}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={columns.length} className={styles.emptyCell}>
                  {loading ? "Loading..." : "No data"}
                </td>
              </tr>
            )}
          </tbody>
        </table>
        {loading && (
          <div className={styles.loadingOverlay}>
            <Loader2 className={styles.spinner} size={24} />
          </div>
        )}
      </div>

      {showPagination && totalPages > 1 && (
        <div className={styles.pagination}>
          <button
            className={`${styles.paginationButton} ${styles.paginationArrow} ${currentPage === 1 ? styles.disabled : ""}`}
            onClick={() => currentPage > 1 && handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
            aria-label="Previous page"
          >
            <ChevronLeft size={16} />
          </button>
          <div className={styles.paginationPages}>
            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
              let pageNumber: number
              if (totalPages <= 5) {
                pageNumber = i + 1
              } else if (currentPage <= 3) {
                pageNumber = i + 1
              } else if (currentPage >= totalPages - 2) {
                pageNumber = totalPages - 4 + i
              } else {
                pageNumber = currentPage - 2 + i
              }

              return (
                <button
                  key={pageNumber}
                  className={`${styles.paginationPage} ${currentPage === pageNumber ? styles.active : ""}`}
                  onClick={() => handlePageChange(pageNumber)}
                >
                  {pageNumber}
                </button>
              )
            })}
          </div>
          <button
            className={`${styles.paginationButton} ${styles.paginationArrow} ${currentPage === totalPages ? styles.disabled : ""}`}
            onClick={() => currentPage < totalPages && handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
            aria-label="Next page"
          >
            <ChevronRight size={16} />
          </button>
        </div>
      )}
    </div>
  )
}
