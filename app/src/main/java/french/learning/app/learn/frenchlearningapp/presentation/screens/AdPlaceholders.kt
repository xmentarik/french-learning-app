package french.learning.app.learn.frenchlearningapp.presentation.screens

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

@Composable
fun BannerAdPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.LightGray, RoundedCornerShape(24.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("AdMob Banner Placeholder")
    }
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
