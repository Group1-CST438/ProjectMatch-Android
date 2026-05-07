package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.HexPanel
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle
import com.projectmatch.android.viewmodel.AuthViewModel

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onSignup: () -> Unit,
) {
    val user by authViewModel.user.collectAsState()
    val loading by authViewModel.loading.collectAsState()

    LaunchedEffect(user) {
        if (user != null) onSuccess()
    }

    PageBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "ProjectMatch",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ButtonAccent,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign in to your account",
                fontSize = 14.sp,
                color = TextMuted,
            )
            Spacer(Modifier.height(32.dp))

            HexPanel(
                modifier = Modifier.fillMaxWidth(),
                cut = 16.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Welcome back",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )
                    Text(
                        text = "Sign in with your Google account to access your projects and matches.",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(8.dp))

                    // Google OAuth button
                    HexChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable { authViewModel.signInWithGoogle() },
                        cut = 10.dp,
                        background = ButtonPrimary,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = TextPrimary,
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Text("G", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Continue with Google",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", fontSize = 13.sp, color = TextSubtle)
                Text(
                    text = "Sign up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ButtonAccent,
                    modifier = Modifier.noRippleClickable(onSignup),
                )
            }
        }
    }
}
