"use client"

import type React from "react"
import { useState, forwardRef } from "react"
import styles from "./input.module.css"

export type InputProps = {
  placeholder?: string
  type?: string
  size?: "small" | "middle" | "large"
  disabled?: boolean
  error?: boolean
  prefix?: React.ReactNode
  suffix?: React.ReactNode
  value?: string
  defaultValue?: string
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void
  onFocus?: (e: React.FocusEvent<HTMLInputElement>) => void
  onBlur?: (e: React.FocusEvent<HTMLInputElement>) => void
  className?: string
  block?: boolean
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  (
    {
      placeholder,
      type = "text",
      size = "middle",
      disabled = false,
      error = false,
      prefix,
      suffix,
      value,
      defaultValue,
      onChange,
      onFocus,
      onBlur,
      className = "",
      block = false,
      ...rest
    },
    ref,
  ) => {
    const [isFocused, setIsFocused] = useState(false)

    const handleFocus = (e: React.FocusEvent<HTMLInputElement>) => {
      setIsFocused(true)
      if (onFocus) onFocus(e)
    }

    const handleBlur = (e: React.FocusEvent<HTMLInputElement>) => {
      setIsFocused(false)
      if (onBlur) onBlur(e)
    }

    const inputClasses = [
      styles.inputWrapper,
      styles[size],
      error ? styles.error : "",
      disabled ? styles.disabled : "",
      isFocused ? styles.focused : "",
      block ? styles.block : "",
      className,
    ]
      .filter(Boolean)
      .join(" ")

    return (
      <div className={inputClasses}>
        {prefix && <div className={styles.prefix}>{prefix}</div>}
        <input
          ref={ref}
          type={type}
          className={styles.input}
          placeholder={placeholder}
          disabled={disabled}
          value={value}
          defaultValue={defaultValue}
          onChange={onChange}
          onFocus={handleFocus}
          onBlur={handleBlur}
          {...rest}
        />
        {suffix && <div className={styles.suffix}>{suffix}</div>}
      </div>
    )
  },
)

Input.displayName = "Input"

export default Input
