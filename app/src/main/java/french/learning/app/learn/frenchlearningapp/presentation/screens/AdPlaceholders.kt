package french.learning.app.learn.frenchlearningapp.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun BannerAdPlaceholder(modifier: Modifier = Modifier) {
    // Real AdMob Banner Implementation
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                // Use test ID for development: ca-app-pub-3940256099942544/6300978111
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun InterstitialAdPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0x33000000), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Interstitial Placeholder (show between lessons)")
    }
}
fun showInterstitialAd(context: Context) {
    val adRequest = AdRequest.Builder().build()

    // Use test ID: ca-app-pub-3940256099942544/1033173712
    InterstitialAd.load(
        context,
        "ca-app-pub-3940256099942544/1033173712",
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                val activity = context.findActivity()
                if (activity != null) {
                    interstitialAd.show(activity)
                }
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Handle the error
            }
        }
    )
}

// Extension function to find the Activity context
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
