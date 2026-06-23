"use client"

import type React from "react"

import { useState } from "react"
import { Loader2 } from "lucide-react"
import styles from "./button.module.css"

export type ButtonProps = {
  children: React.ReactNode
  type?: "button" | "submit" | "reset"
  variant?: "primary" | "secondary" | "danger" | "success" | "warning"
  size?: "small" | "middle" | "large"
  disabled?: boolean
  loading?: boolean
  block?: boolean
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void
  className?: string
}

export default function Button({
  children,
  type = "button",
  variant = "primary",
  size = "middle",
  disabled = false,
  loading = false,
  block = false,
  onClick,
  className = "",
}: ButtonProps) {
  const [isHovered, setIsHovered] = useState(false)
  const [isActive, setIsActive] = useState(false)

  const buttonClasses = [
    styles.button,
    styles[variant],
    styles[size],
    block ? styles.block : "",
    disabled || loading ? styles.disabled : "",
    isHovered && !disabled && !loading ? styles.hovered : "",
    isActive && !disabled && !loading ? styles.active : "",
    className,
  ]
    .filter(Boolean)
    .join(" ")

  return (
    <button
      type={type}
      className={buttonClasses}
      disabled={disabled || loading}
      onClick={onClick}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => {
        setIsHovered(false)
        setIsActive(false)
      }}
      onMouseDown={() => setIsActive(true)}
      onMouseUp={() => setIsActive(false)}
    >
      {loading && (
        <span className={styles.loadingIcon}>
          <Loader2 size={16} className={styles.spinner} />
        </span>
      )}
      <span className={loading ? styles.loadingText : ""}>{children}</span>
    </button>
  )
}
