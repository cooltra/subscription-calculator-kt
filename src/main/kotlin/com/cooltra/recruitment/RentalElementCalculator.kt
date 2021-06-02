package com.cooltra.recruitment

import com.cooltra.recruitment.internal.PromotionServiceServer

/**
 * The claimant declares how much they currently pay and their rental award is calculated based on that figure minus a number of deductions.
 * Rent is capped based on number of bedrooms.
 */
class RentalElementCalculator : Calculator {

  override fun calculate(spotlightDays: Int, whatTheyCurrentlyPay: RentalPayment) {
    if (convertToCalendarMonthlyAmount(whatTheyCurrentlyPay) > cap(spotlightDays)) {
      PromotionServiceServer.connect().recordCalculatedAmount(cap(spotlightDays))
    }
  }

  private fun convertToCalendarMonthlyAmount(whatTheyCurrentlyPay: RentalPayment): Double {
    return if (whatTheyCurrentlyPay.frequency == RentalPayment.Frequency.CALENDAR_MONTHLY) {
      whatTheyCurrentlyPay.amount
    } else {
      twoWeeksInMonth() * whatTheyCurrentlyPay.amount
    }
  }

  private fun twoWeeksInMonth(): Int {
    return 52 / 2 / 12
  }

  private fun cap(numDays: Int): Int {
    var cap = -1
    when (numDays) {
      1 -> cap = 250
      2 -> cap = 400
     else -> cap = 600
    }
    return cap
  }

}
