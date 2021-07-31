package com.erdees.toyswap.view.fragments.addItemFragment

import android.content.Context
import com.erdees.toyswap.R
import io.reactivex.rxjava3.subjects.PublishSubject

class AddItemFragmentNullSafetyHandler {

    val isPictureProvidedRX: PublishSubject<Boolean> = PublishSubject.create()
    val isCategoryProvidedRX: PublishSubject<Boolean> = PublishSubject.create()
    val isNameProvidedRX: PublishSubject<Boolean> = PublishSubject.create()
    val isDescProvidedRX: PublishSubject<Boolean> = PublishSubject.create()
    val isPriceProvidedRX: PublishSubject<Boolean> = PublishSubject.create()
    val isShipmentAndOptionRX: PublishSubject<Boolean> = PublishSubject.create()
    val isDeliveryPriceProvidedRX: PublishSubject<Boolean> = PublishSubject.create()
    val isPersonalPickupAnOptionRX: PublishSubject<Boolean> = PublishSubject.create()
    val isItemConditionPickedRX: PublishSubject<Boolean> = PublishSubject.create()

    var isPictureProvided = false
    var isCategoryProvided = false
    var isNameProvided = false
    var isDescProvided = false
    var isPriceProvided = false
    var isShipmentAnOption = false
    var isDeliveryPriceProvided = false
    var isPersonalPickupAnOption = false
    var isItemConditionPicked = false

    private fun isAnyPickupOptionChecked(): Boolean {
        return (isShipmentAnOption || isPersonalPickupAnOption)
    }

    private fun isShipmentDataProvidedIfNecessary(): Boolean {
        return if (!isShipmentAnOption) true
        else (isShipmentAnOption && isDeliveryPriceProvided)
    }

    fun isAllDataProvided(): Boolean {
        return (isPictureProvided &&
                isCategoryProvided &&
                isNameProvided &&
                isPriceProvided &&
                isDescProvided &&
                isItemConditionPicked &&
                isShipmentDataProvidedIfNecessary() &&
                isAnyPickupOptionChecked()
                )
    }

    private fun findErrorMessage(): String {
        return when {
            !isNameProvided -> "Item name must be provided."
            !isDescProvided -> "Item description must be provided."
            !isPriceProvided -> "Item price must be provided."
            !isPictureProvided -> "Item must have at least 1 picture."
            !isCategoryProvided -> "Item must have category."
            !isItemConditionPicked -> "Item condition must be provided."
            !isShipmentDataProvidedIfNecessary() -> "You must provide delivery price."
            !isAnyPickupOptionChecked() -> "At least one pickup option must be chosen."
            else -> ""
        }
    }

    fun getErrorMessage(context: Context): String {
        val list = listOf(
            isNameProvided,
            isDescProvided,
            isPriceProvided,
            isPictureProvided,
            isCategoryProvided,
            isItemConditionPicked,
            isShipmentDataProvidedIfNecessary(),
            isAnyPickupOptionChecked()
        ).filter { !it }
        return if (list.size > 1) context.getString(R.string.provideAllData)
        else findErrorMessage()
    }

}