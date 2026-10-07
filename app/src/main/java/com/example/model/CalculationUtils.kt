package com.example.model

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

object CalculationUtils {

    private val standardFormat = DecimalFormat("#,##0.##").apply {
        isGroupingUsed = true
    }

    private val preciseFormat = DecimalFormat("#,##0.######").apply {
        isGroupingUsed = true
    }

    fun formatNumber(value: Double, maxDecimals: Int = 4): String {
        if (value.isNaN() || value.isInfinite()) return "Invalid"
        val sanitized = if (kotlin.math.abs(value) < 1e-9) 0.0 else value
        return if (maxDecimals <= 2) {
            standardFormat.format(sanitized)
        } else {
            preciseFormat.format(sanitized)
        }
    }

    fun formatCurrency(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Invalid"
        val sanitized = if (kotlin.math.abs(value) < 1e-9) 0.0 else value
        val df = DecimalFormat("#,##0.00")
        return df.format(sanitized)
    }

    // --- 1. PERCENTAGE CALCULATOR ---
    data class PercentageResult(
        val value: Double,
        val formatted: String,
        val explanation: String
    )

    fun calculatePercentageOf(percentage: Double, total: Double): PercentageResult {
        val result = (percentage * total) / 100.0
        return PercentageResult(
            value = result,
            formatted = formatNumber(result),
            explanation = "${formatNumber(percentage)}% of ${formatNumber(total)} = ${formatNumber(result)}"
        )
    }

    fun calculateWhatPercentage(part: Double, total: Double): Result<PercentageResult> {
        if (total == 0.0) {
            return Result.failure(IllegalArgumentException("Total cannot be zero"))
        }
        val result = (part / total) * 100.0
        return Result.success(
            PercentageResult(
                value = result,
                formatted = "${formatNumber(result)}%",
                explanation = "${formatNumber(part)} is ${formatNumber(result)}% of ${formatNumber(total)}"
            )
        )
    }

    fun calculatePercentageChange(oldVal: Double, newVal: Double): Result<PercentageResult> {
        if (oldVal == 0.0) {
            return Result.failure(IllegalArgumentException("Original value cannot be zero"))
        }
        val diff = newVal - oldVal
        val percentage = (diff / kotlin.math.abs(oldVal)) * 100.0
        val isIncrease = diff >= 0
        val label = if (isIncrease) "increase" else "decrease"
        return Result.success(
            PercentageResult(
                value = percentage,
                formatted = "${if (diff >= 0) "+" else ""}${formatNumber(percentage)}%",
                explanation = "${formatNumber(kotlin.math.abs(percentage))}% $label (${formatNumber(oldVal)} → ${formatNumber(newVal)})"
            )
        )
    }

    // --- 2. DISCOUNT CALCULATOR ---
    data class DiscountResult(
        val originalPrice: Double,
        val discountPercentage: Double,
        val discountAmount: Double,
        val finalPrice: Double,
        val formattedDiscountAmount: String,
        val formattedFinalPrice: String,
        val formattedSaved: String
    )

    fun calculateDiscount(originalPrice: Double, discountPercent: Double): Result<DiscountResult> {
        if (originalPrice < 0) return Result.failure(IllegalArgumentException("Price cannot be negative"))
        if (discountPercent < 0) return Result.failure(IllegalArgumentException("Discount cannot be negative"))
        val discountAmount = (originalPrice * discountPercent) / 100.0
        val finalPrice = (originalPrice - discountAmount).coerceAtLeast(0.0)

        return Result.success(
            DiscountResult(
                originalPrice = originalPrice,
                discountPercentage = discountPercent,
                discountAmount = discountAmount,
                finalPrice = finalPrice,
                formattedDiscountAmount = formatNumber(discountAmount),
                formattedFinalPrice = formatNumber(finalPrice),
                formattedSaved = formatNumber(discountAmount)
            )
        )
    }

    // --- 3. PROFIT CALCULATOR ---
    data class ProfitAnalysisResult(
        val costPrice: Double,
        val sellingPrice: Double,
        val difference: Double,
        val isProfit: Boolean,
        val isBreakEven: Boolean,
        val profitOrLossPercentage: Double,
        val grossMarginPercentage: Double,
        val markupPercentage: Double,
        val formattedDifference: String,
        val formattedPercentage: String,
        val formattedMargin: String,
        val formattedMarkup: String
    )

    fun calculateProfitOrLoss(costPrice: Double, sellingPrice: Double): Result<ProfitAnalysisResult> {
        if (costPrice < 0 || sellingPrice < 0) {
            return Result.failure(IllegalArgumentException("Cost and Selling prices must be non-negative"))
        }
        val diff = sellingPrice - costPrice
        val isProfit = diff > 0.000001
        val isBreakEven = kotlin.math.abs(diff) <= 0.000001

        val profitPercentOnCost = if (costPrice == 0.0) {
            if (sellingPrice == 0.0) 0.0 else Double.POSITIVE_INFINITY
        } else {
            (diff / costPrice) * 100.0
        }

        val markupPercent = profitPercentOnCost

        val marginPercent = if (sellingPrice == 0.0) {
            if (costPrice == 0.0) 0.0 else Double.NEGATIVE_INFINITY
        } else {
            (diff / sellingPrice) * 100.0
        }

        return Result.success(
            ProfitAnalysisResult(
                costPrice = costPrice,
                sellingPrice = sellingPrice,
                difference = diff,
                isProfit = isProfit,
                isBreakEven = isBreakEven,
                profitOrLossPercentage = profitPercentOnCost,
                grossMarginPercentage = marginPercent,
                markupPercentage = markupPercent,
                formattedDifference = formatNumber(kotlin.math.abs(diff)),
                formattedPercentage = "${formatNumber(kotlin.math.abs(profitPercentOnCost))}%",
                formattedMargin = "${formatNumber(marginPercent)}%",
                formattedMarkup = "${formatNumber(markupPercent)}%"
            )
        )
    }

    data class TargetSellingPriceResult(
        val costPrice: Double,
        val desiredProfitPercentage: Double,
        val profitAmount: Double,
        val requiredSellingPrice: Double,
        val formattedProfitAmount: String,
        val formattedSellingPrice: String
    )

    fun calculateTargetSellingPrice(costPrice: Double, desiredProfitPercent: Double): Result<TargetSellingPriceResult> {
        if (costPrice < 0) return Result.failure(IllegalArgumentException("Cost price cannot be negative"))
        val profitAmount = (costPrice * desiredProfitPercent) / 100.0
        val requiredSellingPrice = costPrice + profitAmount
        return Result.success(
            TargetSellingPriceResult(
                costPrice = costPrice,
                desiredProfitPercentage = desiredProfitPercent,
                profitAmount = profitAmount,
                requiredSellingPrice = requiredSellingPrice,
                formattedProfitAmount = formatNumber(profitAmount),
                formattedSellingPrice = formatNumber(requiredSellingPrice)
            )
        )
    }

    // --- 4. AGE CALCULATOR ---
    data class AgeResult(
        val years: Int,
        val months: Int,
        val days: Int,
        val totalMonths: Long,
        val totalDays: Long,
        val nextBirthdayDays: Long? = null
    )

    fun calculateAge(birthDate: LocalDate, targetDate: LocalDate): Result<AgeResult> {
        if (targetDate.isBefore(birthDate)) {
            return Result.failure(IllegalArgumentException("Target date cannot be earlier than birth date"))
        }

        val period = Period.between(birthDate, targetDate)
        val totalMonths = ChronoUnit.MONTHS.between(birthDate, targetDate)
        val totalDays = ChronoUnit.DAYS.between(birthDate, targetDate)

        val thisYearBirthday = birthDate.withYear(targetDate.year)
        val nextBirthday = if (thisYearBirthday.isBefore(targetDate) || thisYearBirthday.isEqual(targetDate)) {
            birthDate.withYear(targetDate.year + 1)
        } else {
            thisYearBirthday
        }
        val daysToNextBirthday = ChronoUnit.DAYS.between(targetDate, nextBirthday)

        return Result.success(
            AgeResult(
                years = period.years,
                months = period.months,
                days = period.days,
                totalMonths = totalMonths,
                totalDays = totalDays,
                nextBirthdayDays = daysToNextBirthday
            )
        )
    }

    // --- 5. UNIT CONVERTER ---
    enum class UnitCategory(val displayName: String) {
        LENGTH("Length"),
        WEIGHT("Weight"),
        TEMPERATURE("Temperature")
    }

    enum class LengthUnit(val displayName: String, val toMetersMultiplier: Double) {
        MILLIMETER("Millimeter (mm)", 0.001),
        CENTIMETER("Centimeter (cm)", 0.01),
        METER("Meter (m)", 1.0),
        KILOMETER("Kilometer (km)", 1000.0),
        INCH("Inch (in)", 0.0254),
        FOOT("Foot (ft)", 0.3048),
        YARD("Yard (yd)", 0.9144),
        MILE("Mile (mi)", 1609.344);
    }

    enum class WeightUnit(val displayName: String, val toGramsMultiplier: Double) {
        MILLIGRAM("Milligram (mg)", 0.001),
        GRAM("Gram (g)", 1.0),
        KILOGRAM("Kilogram (kg)", 1000.0),
        OUNCE("Ounce (oz)", 28.349523125),
        POUND("Pound (lb)", 453.59237);
    }

    enum class TemperatureUnit(val displayName: String) {
        CELSIUS("Celsius (°C)"),
        FAHRENHEIT("Fahrenheit (°F)");

        companion object {
            fun convert(value: Double, from: TemperatureUnit, to: TemperatureUnit): Double {
                if (from == to) return value
                return if (from == CELSIUS && to == FAHRENHEIT) {
                    (value * 9.0 / 5.0) + 32.0
                } else {
                    (value - 32.0) * 5.0 / 9.0
                }
            }
        }
    }

    fun convertLength(value: Double, from: LengthUnit, to: LengthUnit): Double {
        if (from == to) return value
        val inMeters = value * from.toMetersMultiplier
        return inMeters / to.toMetersMultiplier
    }

    fun convertWeight(value: Double, from: WeightUnit, to: WeightUnit): Double {
        if (from == to) return value
        val inGrams = value * from.toGramsMultiplier
        return inGrams / to.toGramsMultiplier
    }

    fun convertTemperature(value: Double, from: TemperatureUnit, to: TemperatureUnit): Double {
        return TemperatureUnit.convert(value, from, to)
    }
}


