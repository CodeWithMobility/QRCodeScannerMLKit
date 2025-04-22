package com.android4you.qrcodescanner.viewmodel

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QRScannerViewModelInstrumentedTest {

    private lateinit var viewModel: QRScannerViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = QRScannerViewModel()
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun copyToClipboard_putsValueInClipboard() {
        val clipboard = mockk<ClipboardManager>(relaxed = true)
        val spyContext = spyk(context)

        every { spyContext.getSystemService(Context.CLIPBOARD_SERVICE) } returns clipboard

        viewModel.onQRCodeScanned("mockk-text")
        viewModel.copyToClipboard(spyContext)

        verify {
            clipboard.setPrimaryClip(
                match { it.getItemAt(0).text == "mockk-text" }
            )
        }
    }

    @Test
    fun shareText_shouldLaunchIntentChooser() {
        val spyContext = spyk(context)
        every { spyContext.startActivity(any()) } just Runs

        viewModel.onQRCodeScanned("Share this text")
        viewModel.shareText(spyContext)

        verify {
            spyContext.startActivity(withArg {
                assertEquals(Intent.ACTION_CHOOSER, it.action)
            })
        }
    }

    @Test
    fun openInBrowser_shouldLaunchBrowserIntent() {
        val spyContext = spyk(context)
        every { spyContext.startActivity(any()) } just Runs

        viewModel.onQRCodeScanned("https://openai.com")
        viewModel.openInBrowser(spyContext)

        verify {
            spyContext.startActivity(withArg {
                assertEquals(Intent.ACTION_VIEW, it.action)
                assertEquals(Uri.parse("https://openai.com"), it.data)
            })
        }
    }
}
