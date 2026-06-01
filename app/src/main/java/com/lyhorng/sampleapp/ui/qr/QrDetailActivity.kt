package com.lyhorng.sampleapp.ui.qr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lyhorng.sampleapp.R
import com.lyhorng.sampleapp.databinding.ActivityQrDetailBinding

class QrDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrDetailBinding
    private var scannedData: String = ""
    private var scannedFormat: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromIntent()
        setupUI()
        setupClickListeners()
    }

    private fun getDataFromIntent() {
        scannedData = intent.getStringExtra("scanned_data") ?: ""
        scannedFormat = intent.getStringExtra("scanned_format") ?: "Unknown"
    }

    private fun setupUI() {
        binding.apply {
            tvScannedData.text = scannedData
            tvFormat.text = scannedFormat

            // Show different UI based on content type
            when {
                scannedData.startsWith("http://") || scannedData.startsWith("https://") -> {
                    btnAction.text = "Open Link"
                    btnAction.setOnClickListener { openUrl(scannedData) }
                }
                scannedData.contains("@") && scannedData.contains(".com") -> {
                    btnAction.text = "Send Email"
                    btnAction.setOnClickListener { sendEmail(scannedData) }
                }
                else -> {
                    btnAction.text = "Copy"
                    btnAction.setOnClickListener { copyToClipboard() }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnCopy.setOnClickListener { copyToClipboard() }
            btnScanAgain.setOnClickListener {
                finish() // Go back to scanner
            }
        }
    }

    private fun copyToClipboard() {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Scanned QR", scannedData)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        startActivity(Intent.createChooser(intent, "Send email"))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Optional: Clear sensitive data
    }
}