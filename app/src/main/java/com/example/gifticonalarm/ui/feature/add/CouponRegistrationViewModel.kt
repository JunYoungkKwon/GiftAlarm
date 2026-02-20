package com.example.gifticonalarm.ui.feature.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.AddGifticonUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.SaveGifticonImageUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDate
import com.example.gifticonalarm.ui.feature.add.bottomsheet.CouponRegistrationInfoSheetType
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

@HiltViewModel
class CouponRegistrationViewModel @Inject constructor(
    private val addGifticonUseCase: AddGifticonUseCase,
    private val updateGifticonUseCase: UpdateGifticonUseCase,
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val saveGifticonImageUseCase: SaveGifticonImageUseCase
) : ViewModel() {
    private companion object {
        const val AMOUNT_MEMO_PREFIX = "금액권:"
        const val NOTE_PREFIX = "메모:"
    }

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

        _formState.value = CouponRegistrationFormState(
            couponId = couponId,
            isEditMode = true,
            barcode = editTarget.barcode,
            place = editTarget.brand,
            couponName = editTarget.name,
            withoutBarcode = editTarget.barcode.isBlank(),
            couponType = if (editTarget.type == GifticonType.AMOUNT) CouponType.AMOUNT else CouponType.EXCHANGE,
            amount = if (editTarget.type == GifticonType.AMOUNT) extractAmountFromMemo(editTarget.memo) else "",
            memo = extractNoteFromMemo(editTarget.memo, editTarget.type),
            thumbnailUri = editTarget.imageUri
        )
        _selectedExpiryDate.value = editTarget.expiryDate.toExpirationDate()
        _draftExpiryDate.value = editTarget.expiryDate.toExpirationDate()
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
            _effect.value = CouponRegistrationEffect.ShowMessage(validationMessage)
            return
        }

        val memo = when (state.couponType) {
            CouponType.AMOUNT -> buildAmountMemo(state.amount, state.memo)
            CouponType.EXCHANGE -> state.memo.trim().ifBlank { null }
        }

        saveGifticon(
            couponId = state.couponId,
            existingGifticon = existingGifticonForSave,
            name = state.couponName.trim(),
            brand = state.place.trim(),
            barcode = if (state.withoutBarcode) "" else state.barcode.trim(),
            expiryDate = selectedExpiryDate!!.toEndOfDayDate(),
            imageUri = state.thumbnailUri,
            memo = memo,
            type = when (state.couponType) {
                CouponType.EXCHANGE -> GifticonType.EXCHANGE
                CouponType.AMOUNT -> GifticonType.AMOUNT
            }
        )
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

    private fun saveGifticon(
        couponId: String?,
        existingGifticon: Gifticon?,
        name: String,
        brand: String,
        barcode: String,
        expiryDate: Date,
        imageUri: String?,
        memo: String?,
        type: GifticonType
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val persistedImageUri = imageUri
                    ?.takeIf { it.isNotBlank() }
                    ?.let { selectedImageUri ->
                        if (selectedImageUri == existingGifticon?.imageUri) {
                            selectedImageUri
                        } else {
                            saveGifticonImageUseCase(selectedImageUri)
                        }
                    }
                val gifticon = Gifticon(
                    id = couponId?.toLongOrNull() ?: 0L,
                    name = name,
                    brand = brand,
                    expiryDate = expiryDate,
                    barcode = barcode,
                    imageUri = persistedImageUri,
                    memo = memo,
                    isUsed = existingGifticon?.isUsed ?: false,
                    type = type
                )
                if (couponId == null) {
                    addGifticonUseCase(gifticon)
                } else {
                    updateGifticonUseCase(gifticon)
                }
                _effect.value = CouponRegistrationEffect.RegistrationCompleted
            } catch (e: Exception) {
                _effect.value = CouponRegistrationEffect.ShowMessage(e.message ?: "쿠폰 저장에 실패했어요.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 수정 대상 쿠폰을 조회한다.
     */
    fun getGifticonForEdit(couponId: String?): LiveData<Gifticon?> {
        val id = couponId?.toLongOrNull() ?: return MutableLiveData(null)
        return getGifticonByIdUseCase(id).asLiveData()
    }

    fun consumeEffect() {
        _effect.value = null
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

    private fun extractAmountFromMemo(memo: String?): String {
        if (memo.isNullOrBlank()) return ""
        val amountRegex = Regex("""금액권:\s*([0-9,]+)\s*원""")
        return amountRegex.find(memo)
            ?.groupValues
            ?.getOrNull(1)
            ?.replace(",", "")
            .orEmpty()
    }

    private fun extractNoteFromMemo(memo: String?, type: GifticonType): String {
        val rawMemo = memo.orEmpty().trim()
        if (rawMemo.isBlank()) return ""
        if (type == GifticonType.EXCHANGE && !rawMemo.startsWith(AMOUNT_MEMO_PREFIX)) {
            return rawMemo
        }

        val lines = rawMemo.lines().map { it.trim() }.filter { it.isNotBlank() }
        val noteLine = lines.firstOrNull { it.startsWith(NOTE_PREFIX) }
            ?.removePrefix(NOTE_PREFIX)
            ?.trim()
        if (!noteLine.isNullOrBlank()) {
            return noteLine
        }

        if (lines.firstOrNull()?.startsWith(AMOUNT_MEMO_PREFIX) == true) {
            return lines.drop(1).joinToString("\n").trim()
        }
        return rawMemo
    }

    private fun buildAmountMemo(amount: String, note: String): String? {
        val amountPart = amount.ifBlank { null }?.let { "$AMOUNT_MEMO_PREFIX ${it}원" }
        val notePart = note.trim().ifBlank { null }?.let { "$NOTE_PREFIX $it" }
        return when {
            amountPart != null && notePart != null -> "$amountPart\n$notePart"
            amountPart != null -> amountPart
            notePart != null -> notePart
            else -> null
        }
    }

    private fun validate(
        state: CouponRegistrationFormState,
        selectedExpiryDate: ExpirationDate?
    ): String? {
        return when {
            state.place.isBlank() -> "사용처를 입력해 주세요."
            state.couponName.isBlank() -> "쿠폰명을 입력해 주세요."
            selectedExpiryDate == null -> "유효기한을 선택해 주세요."
            else -> null
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
