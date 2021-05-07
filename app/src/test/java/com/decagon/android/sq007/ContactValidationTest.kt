package com.decagon.android.sq007

import com.decagon.android.sq007.util.ContactValidator.validatePhoneNum
import org.junit.Assert.*
import org.junit.Test

class ContactValidationTest {

    @Test
    fun phoneNumber_startsWithZeroEight_returnsTrue() {
        //  Given
        val phone = "08104690512"
        //  When
        val result = validatePhoneNum(phone)
        //  Then
        assertTrue(result)
    }

    @Test
    fun phoneNumber_startsWithZeroNine_returnsTrue() {
        //  Arrange
        val phone = "09087229018"
        //  Act
        val result = validatePhoneNum(phone)
        //  Assert
        assertTrue(result)
    }

    @Test
    fun phoneNumber_lessThanElevenDigits_returnsFalse() {
        // Given
        val phone = "0810234345"
        // When
        val result = validatePhoneNum(phone)
        // Then
        assertFalse(result)
    }

    @Test
    fun phoneNumber_startsWithZeroAndMoreThanEleven_returnsFalse() {
        // Given
        val phone = "081046905122"
        // When
        val result = validatePhoneNum(phone)
        // Then
        assertFalse(result)
    }

    @Test
    fun phoneNumber_startsWithTwoAndHasMoreThanThirteen_returnsFalse() {
        // Given
        val phone = "23481046905123"
        // When
        val result = validatePhoneNum(phone)
        // Then
        assertFalse(result)
    }
}
