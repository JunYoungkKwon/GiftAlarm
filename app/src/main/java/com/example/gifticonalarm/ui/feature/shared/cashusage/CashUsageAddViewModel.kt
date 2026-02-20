package com.example.gifticonalarm.ui.feature.shared.cashusage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val AMOUNT_MEMO_PREFIX = "금액권:"
private const val AMOUNT_NOTE_PREFIX = "메모:"

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
    private val updateGifticonUseCase: UpdateGifticonUseCase
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
        loadedCouponId = couponId
        val id = couponId.toLongOrNull() ?: return
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            getGifticonByIdUseCase(id).collectLatest { gifticon ->
                currentGifticon = gifticon
                val balance = extractAmount(gifticon?.memo)
                _uiState.value = (_uiState.value ?: CashUsageAddUiState()).copy(
                    couponId = couponId,
                    brand = gifticon?.brand.orEmpty(),
                    title = gifticon?.name.orEmpty(),
                    currentBalance = balance
                )
            }
        }
    }

    fun updateAmount(value: String) {
        _uiState.value = (_uiState.value ?: return).copy(
            amountText = value.filter { it.isDigit() }
        )
    }

    fun updateStore(value: String) {
        _uiState.value = (_uiState.value ?: return).copy(storeText = value)
    }

    fun updateUsedAt(value: String) {
        _uiState.value = (_uiState.value ?: return).copy(usedAtText = value)
    }

    fun save() {
        val state = _uiState.value ?: return
        val gifticon = currentGifticon ?: return
        val store = state.storeText.trim()
        val usedAt = state.usedAtText.trim()
        val usedAmount = state.amountText.toIntOrNull() ?: 0
        if (usedAmount <= 0) {
            _effect.value = CashUsageAddEffect.ShowMessage("사용 금액을 입력해 주세요.")
            return
        }
        if (store.isBlank()) {
            _effect.value = CashUsageAddEffect.ShowMessage("사용처를 입력해 주세요.")
            return
        }
        if (usedAt.isBlank()) {
            _effect.value = CashUsageAddEffect.ShowMessage("사용 날짜를 입력해 주세요.")
            return
        }
        if (usedAmount > state.currentBalance) {
            _effect.value = CashUsageAddEffect.ShowMessage("현재 잔액보다 큰 금액은 입력할 수 없어요.")
            return
        }

        val remaining = (state.currentBalance - usedAmount).coerceAtLeast(0)
        val updatedMemo = buildAmountMemo(
            previousMemo = gifticon.memo,
            remainAmount = remaining,
            store = store,
            usedAtText = usedAt
        )

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            runCatching {
                updateGifticonUseCase(gifticon.copy(memo = updatedMemo, lastModifiedAt = Date()))
            }.onSuccess {
                _effect.value = CashUsageAddEffect.Saved
            }.onFailure {
                _effect.value = CashUsageAddEffect.ShowMessage("사용 내역 저장에 실패했어요.")
            }
            _uiState.value = (_uiState.value ?: state).copy(isSaving = false)
        }
    }

    fun consumeEffect() {
        _effect.value = null
    }

    private fun extractAmount(memo: String?): Int {
        val amountRegex = Regex("""금액권:\s*([0-9,]+)\s*원""")
        return amountRegex.find(memo.orEmpty())
            ?.groupValues
            ?.getOrNull(1)
            ?.replace(",", "")
            ?.toIntOrNull()
            ?: 0
    }

    private fun buildAmountMemo(
        previousMemo: String?,
        remainAmount: Int,
        store: String,
        usedAtText: String
    ): String {
        val customMemo = extractMemoNote(previousMemo)
        val usageSummary = buildString {
            append("최근 사용: ")
            if (store.isNotBlank()) append(store) else append("사용처 미입력")
            if (usedAtText.isNotBlank()) append(" / $usedAtText")
        }

        return buildString {
            append("$AMOUNT_MEMO_PREFIX ${formatAmount(remainAmount)}원")
            append('\n')
            append("$AMOUNT_NOTE_PREFIX ")
            if (customMemo.isNotBlank()) {
                append(customMemo)
                append(" | ")
            }
            append(usageSummary)
        }
    }

    private fun extractMemoNote(memo: String?): String {
        val lines = memo.orEmpty().lines().map { it.trim() }.filter { it.isNotBlank() }
        return lines.firstOrNull { it.startsWith(AMOUNT_NOTE_PREFIX) }
            ?.removePrefix(AMOUNT_NOTE_PREFIX)
            ?.substringBefore(" | ")
            ?.trim()
            .orEmpty()
    }

    private fun formatAmount(amount: Int): String {
        return String.format(Locale.getDefault(), "%,d", amount)
    }

}
