package com.example

import com.example.model.CalculationUtils
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CalculationUnitTest {

    @Test
    fun testPercentageOf() {
        val result = CalculationUtils.calculatePercentageOf(20.0, 500.0)
        assertEquals(100.0, result.value, 0.001)
        assertEquals("100", result.formatted)
    }

    @Test
    fun testWhatPercentage() {
        val result = CalculationUtils.calculateWhatPercentage(100.0, 500.0)
        assertTrue(result.isSuccess)
        assertEquals(20.0, result.getOrThrow().value, 0.001)

        val zeroResult = CalculationUtils.calculateWhatPercentage(50.0, 0.0)
        assertTrue(zeroResult.isFailure)
    }

    @Test
    fun testPercentageChange() {
        val incResult = CalculationUtils.calculatePercentageChange(500.0, 600.0)
        assertTrue(incResult.isSuccess)
        assertEquals(20.0, incResult.getOrThrow().value, 0.001)

        val decResult = CalculationUtils.calculatePercentageChange(600.0, 480.0)
        assertTrue(decResult.isSuccess)
        assertEquals(-20.0, decResult.getOrThrow().value, 0.001)

        val zeroOldResult = CalculationUtils.calculatePercentageChange(0.0, 100.0)
        assertTrue(zeroOldResult.isFailure)
    }

    @Test
    fun testDiscountCalculation() {
        val result = CalculationUtils.calculateDiscount(5000.0, 20.0)
        assertTrue(result.isSuccess)
        val res = result.getOrThrow()
        assertEquals(1000.0, res.discountAmount, 0.001)
        assertEquals(4000.0, res.finalPrice, 0.001)
    }

    @Test
    fun testProfitAndLossCalculation() {
        val profitRes = CalculationUtils.calculateProfitOrLoss(800.0, 1000.0)
        assertTrue(profitRes.isSuccess)
        val p = profitRes.getOrThrow()
        assertTrue(p.isProfit)
        assertFalse(p.isBreakEven)
        assertEquals(200.0, p.difference, 0.001)
        assertEquals(25.0, p.profitOrLossPercentage, 0.001)
        assertEquals(20.0, p.grossMarginPercentage, 0.001)

        val lossRes = CalculationUtils.calculateProfitOrLoss(1000.0, 800.0)
        assertTrue(lossRes.isSuccess)
        val l = lossRes.getOrThrow()
        assertFalse(l.isProfit)
        assertEquals(-200.0, l.difference, 0.001)
        assertEquals(-20.0, l.profitOrLossPercentage, 0.001)

        val targetRes = CalculationUtils.calculateTargetSellingPrice(500.0, 20.0)
        assertTrue(targetRes.isSuccess)
        val t = targetRes.getOrThrow()
        assertEquals(100.0, t.profitAmount, 0.001)
        assertEquals(600.0, t.requiredSellingPrice, 0.001)
    }

    @Test
    fun testAgeCalculation() {
        val birthDate = LocalDate.of(2000, 5, 15)
        val targetDate = LocalDate.of(2025, 7, 20)

        val ageRes = CalculationUtils.calculateAge(birthDate, targetDate)
        assertTrue(ageRes.isSuccess)
        val a = ageRes.getOrThrow()
        assertEquals(25, a.years)
        assertEquals(2, a.months)
        assertEquals(5, a.days)

        val invalidRes = CalculationUtils.calculateAge(LocalDate.of(2025, 1, 1), LocalDate.of(2020, 1, 1))
        assertTrue(invalidRes.isFailure)
    }

    @Test
    fun testUnitConversions() {
        val cm = CalculationUtils.convertLength(1.0, CalculationUtils.LengthUnit.METER, CalculationUtils.LengthUnit.CENTIMETER)
        assertEquals(100.0, cm, 0.001)

        val km = CalculationUtils.convertLength(1.0, CalculationUtils.LengthUnit.MILE, CalculationUtils.LengthUnit.KILOMETER)
        assertEquals(1.609344, km, 0.001)

        val g = CalculationUtils.convertWeight(1.0, CalculationUtils.WeightUnit.KILOGRAM, CalculationUtils.WeightUnit.GRAM)
        assertEquals(1000.0, g, 0.001)

        val f = CalculationUtils.convertTemperature(0.0, CalculationUtils.TemperatureUnit.CELSIUS, CalculationUtils.TemperatureUnit.FAHRENHEIT)
        assertEquals(32.0, f, 0.001)

        val c = CalculationUtils.convertTemperature(212.0, CalculationUtils.TemperatureUnit.FAHRENHEIT, CalculationUtils.TemperatureUnit.CELSIUS)
        assertEquals(100.0, c, 0.001)
    }
}
