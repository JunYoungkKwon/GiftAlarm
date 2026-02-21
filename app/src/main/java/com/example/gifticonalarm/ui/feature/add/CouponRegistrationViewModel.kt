package com.example.gifticonalarm.ui.feature.add
import com.example.gifticonalarm.ui.feature.shared.text.CommonText

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.AddGifticonUseCase
import com.example.gifticonalarm.domain.usecase.BuildAmountGifticonMemoUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.ParseGifticonMemoUseCase
import com.example.gifticonalarm.domain.usecase.SaveGifticonImageUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDate
import com.example.gifticonalarm.ui.feature.add.bottomsheet.CouponRegistrationInfoSheetType
import com.example.gifticonalarm.ui.feature.shared.livedata.consumeEffect
import com.example.gifticonalarm.ui.feature.shared.livedata.emitEffect
import com.example.gifticonalarm.ui.feature.shared.livedata.nullLiveData
import com.example.gifticonalarm.ui.feature.shared.util.parseCouponIdOrNull
import com.example.gifticonalarm.ui.feature.shared.validation.ValidationMessages
import com.example.gifticonalarm.ui.feature.shared.validation.firstValidationError
import com.example.gifticonalarm.ui.feature.shared.validation.validateRequired
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Calendar
import javax.inject.Inject

/**
 * 쿠폰 등록 저장 로직을 담당하는 ViewModel.
 */
data class CouponRegistrationFormState(
    val couponId: String? = null,
    val isEditMode: Boolean = false,
    val barcode: String = "",
    val place: String = "",
    val couponName: String = "",
    val withoutBarcode: Boolean = false,
    val couponType: CouponType = CouponType.EXCHANGE,
    val amount: String = "",
    val memo: String = "",
    val thumbnailUri: String? = null
)

sealed interface CouponRegistrationEffect {
    data class ShowMessage(val message: String) : CouponRegistrationEffect
    data object RegistrationCompleted : CouponRegistrationEffect
}

private data class GifticonSavePayload(
    val couponId: String?,
    val existingGifticon: Gifticon?,
    val name: String,
    val brand: String,
    val barcode: String,
    val expiryDate: Date,
    val imageUri: String?,
    val memo: String?,
    val type: GifticonType
)

@HiltViewModel
class CouponRegistrationViewModel @Inject constructor(
    private val addGifticonUseCase: AddGifticonUseCase,
    private val updateGifticonUseCase: UpdateGifticonUseCase,
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val saveGifticonImageUseCase: SaveGifticonImageUseCase,
    private val parseGifticonMemoUseCase: ParseGifticonMemoUseCase,
    private val buildAmountGifticonMemoUseCase: BuildAmountGifticonMemoUseCase
) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _effect = MutableLiveData<CouponRegistrationEffect?>()
    val effect: LiveData<CouponRegistrationEffect?> = _effect

    private val _formState = MutableLiveData(CouponRegistrationFormState())
    val formState: LiveData<CouponRegistrationFormState> = _formState

    private val _isExpiryBottomSheetVisible = MutableLiveData(false)
    val isExpiryBottomSheetVisible: LiveData<Boolean> = _isExpiryBottomSheetVisible

    private val _selectedExpiryDate = MutableLiveData<ExpirationDate?>(null)
    val selectedExpiryDate: LiveData<ExpirationDate?> = _selectedExpiryDate

    private val _draftExpiryDate = MutableLiveData(ExpirationDate.today())
    val draftExpiryDate: LiveData<ExpirationDate> = _draftExpiryDate

    private val _infoSheetType = MutableLiveData(CouponRegistrationInfoSheetType.NONE)
    val infoSheetType: LiveData<CouponRegistrationInfoSheetType> = _infoSheetType

    private var initializedCouponId: String? = null
    private var existingGifticonForSave: Gifticon? = null

    fun initializeForm(couponId: String?, editTarget: Gifticon?) {
        if (couponId == null) {
            if (initializedCouponId != null || existingGifticonForSave != null) {
                resetCreateForm()
            }
            return
        }
        if (editTarget == null) return
        if (initializedCouponId == couponId) return

        val expiryDate = editTarget.expiryDate.toExpirationDate()
        _formState.value = editTarget.toEditFormState(couponId)
        _selectedExpiryDate.value = expiryDate
        _draftExpiryDate.value = expiryDate
        initializedCouponId = couponId
        existingGifticonForSave = editTarget
    }

    fun updateBarcode(value: String) {
        updateFormState { copy(barcode = value) }
    }

    fun updatePlace(value: String) {
        updateFormState { copy(place = value) }
    }

    fun updateCouponName(value: String) {
        updateFormState { copy(couponName = value) }
    }

    fun updateWithoutBarcode(value: Boolean) {
        updateFormState {
            copy(
                withoutBarcode = value,
                barcode = if (value) "" else barcode
            )
        }
    }

    fun updateCouponType(value: CouponType) {
        updateFormState {
            copy(
                couponType = value,
                amount = if (value == CouponType.AMOUNT) amount else ""
            )
        }
    }

    fun updateAmount(value: String) {
        updateFormState { copy(amount = value.filter { it.isDigit() }) }
    }

    fun updateMemo(value: String) {
        updateFormState { copy(memo = value) }
    }

    fun updateThumbnailUri(value: String?) {
        updateFormState { copy(thumbnailUri = value) }
    }

    fun submit() {
        val state = _formState.value ?: CouponRegistrationFormState()
        val selectedExpiryDate = _selectedExpiryDate.value
        val validationMessage = validate(state, selectedExpiryDate)
        if (validationMessage != null) {
            _effect.emitEffect(CouponRegistrationEffect.ShowMessage(validationMessage))
            return
        }

        val confirmedExpiryDate = selectedExpiryDate ?: return
        saveGifticon(state.toSavePayload(confirmedExpiryDate, existingGifticonForSave))
    }

    fun openExpiryBottomSheet() {
        _draftExpiryDate.value = _selectedExpiryDate.value ?: ExpirationDate.today()
        _isExpiryBottomSheetVisible.value = true
    }

    fun dismissExpiryBottomSheet() {
        _isExpiryBottomSheetVisible.value = false
    }

    fun updateDraftExpiryDate(date: ExpirationDate) {
        _draftExpiryDate.value = date
    }

    fun confirmExpiryDate() {
        _selectedExpiryDate.value = _draftExpiryDate.value
        _isExpiryBottomSheetVisible.value = false
    }

    fun showBarcodeInfoSheet() {
        _infoSheetType.value = CouponRegistrationInfoSheetType.BARCODE_INFO
    }

    fun showNotificationInfoSheet() {
        _infoSheetType.value = CouponRegistrationInfoSheetType.NOTIFICATION_INFO
    }

    fun dismissInfoSheet() {
        _infoSheetType.value = CouponRegistrationInfoSheetType.NONE
    }

    private fun saveGifticon(payload: GifticonSavePayload) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val persistedImageUri = resolvePersistedImageUri(payload.imageUri, payload.existingGifticon)
                val gifticon = Gifticon(
                    id = payload.couponId?.toLongOrNull() ?: 0L,
                    name = payload.name,
                    brand = payload.brand,
                    expiryDate = payload.expiryDate,
                    barcode = payload.barcode,
                    imageUri = persistedImageUri,
                    memo = payload.memo,
                    isUsed = payload.existingGifticon?.isUsed ?: false,
                    type = payload.type
                )
                if (payload.couponId == null) {
                    addGifticonUseCase(gifticon)
                } else {
                    updateGifticonUseCase(gifticon)
                }
                _effect.emitEffect(CouponRegistrationEffect.RegistrationCompleted)
            } catch (e: Exception) {
                _effect.emitEffect(CouponRegistrationEffect.ShowMessage(e.message ?: CommonText.MESSAGE_COUPON_SAVE_FAILED))
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 수정 대상 쿠폰을 조회한다.
     */
    fun getGifticonForEdit(couponId: String?): LiveData<Gifticon?> {
        val id = parseCouponIdOrNull(couponId) ?: return nullLiveData()
        return getGifticonByIdUseCase(id).asLiveData()
    }

    fun consumeEffect() {
        _effect.consumeEffect()
    }

    private fun updateFormState(update: CouponRegistrationFormState.() -> CouponRegistrationFormState) {
        val current = _formState.value ?: CouponRegistrationFormState()
        _formState.value = current.update()
    }

    private fun resetCreateForm() {
        _formState.value = CouponRegistrationFormState()
        _selectedExpiryDate.value = null
        _draftExpiryDate.value = ExpirationDate.today()
        initializedCouponId = null
        existingGifticonForSave = null
    }

    private fun validate(
        state: CouponRegistrationFormState,
        selectedExpiryDate: ExpirationDate?
    ): String? {
        return firstValidationError(
            validateRequired(state.place, ValidationMessages.STORE_REQUIRED),
            validateRequired(state.couponName, ValidationMessages.COUPON_NAME_REQUIRED),
            if (selectedExpiryDate == null) ValidationMessages.EXPIRY_DATE_REQUIRED else null
        )
    }

    private fun Gifticon.toEditFormState(couponId: String): CouponRegistrationFormState {
        val amountCoupon = type == GifticonType.AMOUNT
        return CouponRegistrationFormState(
            couponId = couponId,
            isEditMode = true,
            barcode = barcode,
            place = brand,
            couponName = name,
            withoutBarcode = barcode.isBlank(),
            couponType = if (amountCoupon) CouponType.AMOUNT else CouponType.EXCHANGE,
            amount = if (amountCoupon) parseGifticonMemoUseCase.extractAmountDigits(memo) else "",
            memo = parseGifticonMemoUseCase.extractDisplayMemo(memo, type),
            thumbnailUri = imageUri
        )
    }

    private fun CouponRegistrationFormState.toSavePayload(
        selectedExpiryDate: ExpirationDate,
        existingGifticon: Gifticon?
    ): GifticonSavePayload {
        return GifticonSavePayload(
            couponId = couponId,
            existingGifticon = existingGifticon,
            name = couponName.trim(),
            brand = place.trim(),
            barcode = if (withoutBarcode) "" else barcode.trim(),
            expiryDate = selectedExpiryDate.toEndOfDayDate(),
            imageUri = thumbnailUri,
            memo = toPersistedMemo(existingGifticon),
            type = couponType.toGifticonType()
        )
    }

    private fun CouponRegistrationFormState.toPersistedMemo(existingGifticon: Gifticon?): String? {
        return when (couponType) {
            CouponType.AMOUNT -> buildAmountGifticonMemoUseCase.forRegistration(
                amountDigits = amount,
                note = memo,
                previousMemo = existingGifticon?.memo
            )
            CouponType.EXCHANGE -> memo.trim().ifBlank { null }
        }
    }

    private fun CouponType.toGifticonType(): GifticonType {
        return when (this) {
            CouponType.EXCHANGE -> GifticonType.EXCHANGE
            CouponType.AMOUNT -> GifticonType.AMOUNT
        }
    }

    private suspend fun resolvePersistedImageUri(
        imageUri: String?,
        existingGifticon: Gifticon?
    ): String? {
        val selectedImageUri = imageUri?.takeIf { it.isNotBlank() } ?: return null
        return if (selectedImageUri == existingGifticon?.imageUri) {
            selectedImageUri
        } else {
            saveGifticonImageUseCase(selectedImageUri)
        }
    }

    private fun Date.toExpirationDate(): ExpirationDate {
        val calendar = Calendar.getInstance().apply { time = this@toExpirationDate }
        return ExpirationDate(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun ExpirationDate.toEndOfDayDate(): Date {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }
}
