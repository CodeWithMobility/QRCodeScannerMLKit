package com.android4you.qrcodescanner.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.android4you.qrcodescanner.model.QRScannerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QRScannerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QRScannerUiState())
    val uiState: StateFlow<QRScannerUiState> = _uiState

    fun onQRCodeScanned(text: String) {
        if (!_uiState.value.isBottomSheetVisible) {
            _uiState.value = _uiState.value.copy(
                scannedText = text,
                isBottomSheetVisible = true
            )
        }
    }

    fun dismissBottomSheet() {
        _uiState.value = _uiState.value.copy(
            isBottomSheetVisible = false,
            scannedText = null
        )
    }

    fun toggleFlashlight() {
        _uiState.value = _uiState.value.copy(
            isFlashlightOn = !_uiState.value.isFlashlightOn
        )
    }

    fun copyToClipboard(context: Context) {
        val clipManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipManager.setPrimaryClip(ClipData.newPlainText("QR Code", _uiState.value.scannedText))
    }

    fun shareText(context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, _uiState.value.scannedText)
        }
        context.startActivity(Intent.createChooser(intent, "Share QR Code"))
    }

    fun openInBrowser(context: Context) {
        val uri = Uri.parse(_uiState.value.scannedText)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}