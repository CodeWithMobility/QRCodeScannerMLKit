package com.android4you.qrcodescanner.viewmodel

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
class QRScannerViewModelTest {

    private lateinit var viewModel: QRScannerViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = QRScannerViewModel()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `initial state should be default`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(null, state.scannedText)
            assertEquals(false, state.isBottomSheetVisible)
            assertEquals(false, state.isFlashlightOn)
            cancel()
        }
    }

    @Test
    fun `onQRCodeScanned updates scannedText and opens bottom sheet`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onQRCodeScanned("https://google.com")
            val state = awaitItem()
            assertEquals("https://google.com", state.scannedText)
            assertEquals(true, state.isBottomSheetVisible)
            cancel()
        }
    }

    @Test
    fun `dismissBottomSheet resets state`() = runTest {
        viewModel.onQRCodeScanned("test")
        viewModel.uiState.test {
            val scanned = awaitItem()
            assertEquals("test", scanned.scannedText)
            assertEquals(true, scanned.isBottomSheetVisible)

            viewModel.dismissBottomSheet()

            val dismissed = awaitItem()
            assertEquals(null, dismissed.scannedText)
            assertEquals(false, dismissed.isBottomSheetVisible)

            cancel()
        }
    }

    @Test
    fun `toggleFlashlight toggles state`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.toggleFlashlight()
            val toggled = awaitItem()
            assertEquals(true, toggled.isFlashlightOn)

            viewModel.toggleFlashlight()
            val toggledBack = awaitItem()
            assertEquals(false, toggledBack.isFlashlightOn)
            cancel()
        }
    }
}
