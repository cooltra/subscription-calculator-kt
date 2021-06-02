package com.cooltra.recruitment

data class RentalPayment(val amount: Double, val frequency: Frequency) {

  enum class Frequency {
    BIWEEKLY, CALENDAR_MONTHLY
  }
}
