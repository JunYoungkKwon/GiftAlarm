package com.example.gifticonalarm.ui.feature.shared.cashusage
import com.example.gifticonalarm.ui.feature.shared.text.CommonText

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.BuildAmountGifticonMemoUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.ParseGifticonMemoUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import com.example.gifticonalarm.ui.feature.shared.livedata.consumeEffect
import com.example.gifticonalarm.ui.feature.shared.livedata.emitEffect
import com.example.gifticonalarm.ui.feature.shared.util.buildUsageHistoryEntry
import com.example.gifticonalarm.ui.feature.shared.util.parseCouponIdOrNull
import com.example.gifticonalarm.ui.feature.shared.validation.ValidationMessages
import com.example.gifticonalarm.ui.feature.shared.validation.firstValidationError
import com.example.gifticonalarm.ui.feature.shared.validation.validateAtMost
import com.example.gifticonalarm.ui.feature.shared.validation.validatePositiveAmount
import com.example.gifticonalarm.ui.feature.shared.validation.validateRequired
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 금액권 사용 내역 추가 화면 상태.
 */
data class CashUsageAddUiState(
    val couponId: String = "",
    val brand: String = "",
    val title: String = "",
    val currentBalance: Int = 0,
    val amountText: String = "",
    val storeText: String = "",
    val usedAtText: String = "",
    val isSaving: Boolean = false
)

sealed interface CashUsageAddEffect {
    data class ShowMessage(val message: String) : CashUsageAddEffect
    data object Saved : CashUsageAddEffect
}

/**
 * 금액권 사용 내역 추가 ViewModel.
 */
@HiltViewModel
class CashUsageAddViewModel @Inject constructor(
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val updateGifticonUseCase: UpdateGifticonUseCase,
    private val parseGifticonMemoUseCase: ParseGifticonMemoUseCase,
    private val buildAmountGifticonMemoUseCase: BuildAmountGifticonMemoUseCase
) : ViewModel() {
    private val _uiState = MutableLiveData(CashUsageAddUiState())
    val uiState: LiveData<CashUsageAddUiState> = _uiState

    private val _effect = MutableLiveData<CashUsageAddEffect?>()
    val effect: LiveData<CashUsageAddEffect?> = _effect

    private var currentGifticon: Gifticon? = null
    private var loadedCouponId: String? = null
    private var observeJob: Job? = null

    fun load(couponId: String) {
        if (loadedCouponId == couponId) return
        val id = parseCouponIdOrNull(couponId) ?: return
        loadedCouponId = couponId
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            getGifticonByIdUseCase(id).collectLatest { gifticon ->
                currentGifticon = gifticon
                val balance = parseGifticonMemoUseCase.extractAmountInt(gifticon?.memo)
                updateUiState {
                    copy(
                        couponId = couponId,
                        brand = gifticon?.brand.orEmpty(),
                        title = gifticon?.name.orEmpty(),
                        currentBalance = balance
                    )
                }
            }
        }
    }

    fun updateAmount(value: String) {
        updateUiState {
            copy(
                amountText = value.filter { it.isDigit() }
            )
        }
    }

    fun updateStore(value: String) {
        updateUiState { copy(storeText = value) }
    }

    fun updateUsedAt(value: String) {
        updateUiState { copy(usedAtText = value) }
    }

    fun save() {
        val state = _uiState.value ?: return
        val gifticon = currentGifticon ?: return
        val store = state.storeText.trim()
        val usedAt = state.usedAtText.trim()
        val usedAmount = state.amountText.toIntOrNull() ?: 0
        val validationMessage = firstValidationError(
            validatePositiveAmount(usedAmount, ValidationMessages.AMOUNT_REQUIRED),
            validateRequired(store, ValidationMessages.STORE_REQUIRED),
            validateRequired(usedAt, ValidationMessages.USED_DATE_REQUIRED),
            validateAtMost(usedAmount, state.currentBalance, ValidationMessages.AMOUNT_EXCEEDS_BALANCE)
        )
        if (validationMessage != null) {
            _effect.emitEffect(CashUsageAddEffect.ShowMessage(validationMessage))
            return
        }

        val remaining = (state.currentBalance - usedAmount).coerceAtLeast(0)
        val updatedMemo = buildAmountGifticonMemoUseCase.forUsageHistory(
            previousMemo = gifticon.memo,
            remainAmount = remaining,
            latestUsageEntry = buildUsageHistoryEntry(
                store = store,
                usedAtText = usedAt,
                usedAmount = usedAmount
            )
        )

        viewModelScope.launch {
            setSaving(true)
            runCatching {
                updateGifticonUseCase(gifticon.copy(memo = updatedMemo, lastModifiedAt = Date()))
            }.onSuccess {
                _effect.emitEffect(CashUsageAddEffect.Saved)
            }.onFailure {
                _effect.emitEffect(CashUsageAddEffect.ShowMessage(CommonText.MESSAGE_CASH_USAGE_SAVE_FAILED))
            }
            setSaving(false)
        }
    }

    private fun updateUiState(
        transform: CashUsageAddUiState.() -> CashUsageAddUiState
    ) {
        val current = _uiState.value ?: CashUsageAddUiState()
        _uiState.value = current.transform()
    }

    private fun setSaving(isSaving: Boolean) {
        updateUiState { copy(isSaving = isSaving) }
    }

    fun consumeEffect() {
        _effect.consumeEffect()
    }
}
