package com.example.myhealthpassport.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material3.Surface
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ui.navigation.Screen
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark

@Preview
@Composable
fun FlipAnimation1() {
    var isCardFlipped by remember { mutableStateOf(false) }
    val animDuration = 900
    val zAxisDistance = 10f //distance between camera and Card

    val frontColor by animateColorAsState(
        targetValue = if (isCardFlipped) Color(0xFF789FFF) else Color(0xFF282A31),
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    // rotate Y-axis with animation
    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    // text animation
    val textAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 0f else 1f,
        tween(durationMillis = 1500),
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp, 300.dp)
                .graphicsLayer {
                    rotationY = rotateCardY
                    cameraDistance = zAxisDistance
                }
                .clip(RoundedCornerShape(24.dp))
                .clickable { isCardFlipped = !isCardFlipped }
                .background(frontColor)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.graphicsLayer {
                alpha = if (isCardFlipped) 1f - textAlpha else textAlpha
            },
            text = if (isCardFlipped) "Reveal" else "Hide",
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}


@Composable
fun HomeScreen(navController: NavController) {

    var isCardFlipped by remember { mutableStateOf(false) }

    val animDuration = 1000
    val zAxisDistance = 10f

    val rotateCardY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    val frontImageAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 0f else 1f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    val backImageAlpha by animateFloatAsState(
        targetValue = if (isCardFlipped) 1f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = EaseInOut),
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        /**
         * Adaptive Flip Card
         */

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.8f) // Increased from 0.85f
                    .aspectRatio(1.15f)
                    .graphicsLayer {
                        rotationY = rotateCardY
                        cameraDistance = zAxisDistance
                    }
                    .clip(RoundedCornerShape(32.dp))
                    .clickable { isCardFlipped = !isCardFlipped }
            ) {

                Image(
                    painter = painterResource(id = R.drawable.handsmedicaldoctor),
                    contentDescription = "Front Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = frontImageAlpha),
                    contentScale = ContentScale.Crop
                )

                Image(
                    painter = painterResource(id = R.drawable.onlinedoctor),
                    contentDescription = "Back Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = backImageAlpha)
                        .scale(-1f, 1f),
                    contentScale = ContentScale.Crop
                )
            }
        }

        /**
         * Flip Button
         */

        if (isCardFlipped) {
            OutlinedButton(
                onClick = { navController.navigate(Screen.HealthInfo.route) },
                border = BorderStroke(1.5.dp, HealthBlueDark)
            ) {
                Text("Save Data", color = HealthBlue, fontWeight = FontWeight.W700)
            }
        } else {
            OutlinedButton(
                onClick = { navController.navigate(Screen.GetHealthInfo.route) },
                border = BorderStroke(1.5.dp, HealthBlueDark)
            ) {
                Text("Get Data", color = HealthBlue, fontWeight = FontWeight.W700)
            }
        }

        /**
         * Emergency Contacts Section
         */

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Account Services",
                style = MaterialTheme.typography.labelLarge,
                color = HealthBlueDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Surface(
                onClick = { navController.navigate(Screen.EmergencyContacts.route) },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ContactPhone,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = HealthBlue
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Emergency Contacts",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Quick access to medical help",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        /**
         * AI Features Cards
         */

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "AI Health Insights",
                style = MaterialTheme.typography.labelLarge,
                color = HealthBlueDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                /**
                 * Recommendation Agent
                 */
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(
                        onClick = { navController.navigate(Screen.AgentScreen.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.picturedoctorwithstethoscope),
                            contentDescription = "Doctor",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Health Agent",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }

                /**
                 * Medical Report Analyzer
                 */
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(
                        onClick = { navController.navigate(Screen.HealthAiScreen.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.groupmedicalstaff),
                            contentDescription = "Medical Staff",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Report Analyser",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}
