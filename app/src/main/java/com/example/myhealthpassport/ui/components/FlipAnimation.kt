package com.example.myhealthpassport

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.myhealthpassport.ui.navigation.Screen

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
            .background(Color.White),
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
            color = Color.Black,
        )
    }
}


@Composable
fun FlipAnimation(navController: NavController) {

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
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        /**
         * Adaptive Flip Card
         */

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            val cardWidth = maxWidth * 0.7f

            Box(
                modifier = Modifier
                    .width(cardWidth)
                    .aspectRatio(1f)
                    .graphicsLayer {
                        rotationY = rotateCardY
                        cameraDistance = zAxisDistance
                    }
                    .clip(RoundedCornerShape(24.dp))
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

        Spacer(modifier = Modifier.height(24.dp))

        /**
         * Flip Button
         */

        if (isCardFlipped) {
            OutlinedButton(
                onClick = { navController.navigate(Screen.HealthInfo.route) },
                border = BorderStroke(1.dp, Color(0xFF1E88E5))
            ) {
                Text("Save Data", color = Color.Black)
            }
        } else {
            OutlinedButton(
                onClick = { navController.navigate(Screen.GetHealthInfo.route) },
                border = BorderStroke(1.dp, Color(0xFF1E88E5))
            ) {
                Text("Get Data", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /**
         * Emergency Contacts Section
         */

        Text(
            text = "Emergency Contacts",
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Show Emergency Contacts",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    navController.navigate(Screen.EmergencyContacts.route)
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Emergency Contacts",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * AI Features Cards
         */

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            /**
             * Recommendation Agent
             */

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
            ) {

                ElevatedCard(
                    onClick = {
                        navController.navigate(Screen.AgentScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.picturedoctorwithstethoscope),
                        contentDescription = "Doctor",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Recommendation Agent",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            /**
             * Medical Report Analyzer
             */

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
            ) {

                ElevatedCard(
                    onClick = {
                        navController.navigate(Screen.HealthAiScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.groupmedicalstaff),
                        contentDescription = "Medical Staff",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Medical Report Analyser",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
fun FlipAnimation1Preview(){
    FlipAnimation(navController = rememberNavController())
}