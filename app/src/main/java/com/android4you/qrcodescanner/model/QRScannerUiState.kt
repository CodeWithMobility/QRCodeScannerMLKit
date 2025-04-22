package com.android4you.qrcodescanner.model

data class QRScannerUiState(
    val scannedText: String? = null,
    val isBottomSheetVisible: Boolean = false,
    val isFlashlightOn: Boolean = false
)
