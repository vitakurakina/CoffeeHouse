package com.example.coffeehouse

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.content.Context
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class OffersFragment : Fragment(R.layout.fragment_offers) {

    private lateinit var qrImageView: ImageView
    private lateinit var bonusTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrImageView = view.findViewById(R.id.qr_code)
        bonusTextView = view.findViewById(R.id.bonusAmount)

        loadUserData()
    }

    private fun loadUserData() {
        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val qrToken = prefs.getString("qrToken", "")
        val bonusAmount = prefs.getInt("bonusAmount", 0)

        bonusTextView.text = bonusAmount.toString()

        if (!qrToken.isNullOrEmpty()) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                    qrToken,
                    BarcodeFormat.QR_CODE,
                    400,
                    400
                )
                qrImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
