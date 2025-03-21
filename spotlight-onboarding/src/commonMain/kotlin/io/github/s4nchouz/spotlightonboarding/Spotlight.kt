package io.github.s4nchouz.spotlightonboarding

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.positionInRoot
import io.github.s4nchouz.spotlightonboarding.model.SpotlightOnboardingState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun Spotlight(
    scrimColor: Color,
    state: SpotlightOnboardingState,
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = state.isVisible,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        val page = state.pages[state.currentPageIndex]

        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                val spotlightPath = Path().apply {
                    page.items.values.forEach { spotlightItem ->
                        val posInParent = spotlightItem.layoutCoordinates.positionInRoot()

                        val left = spotlightItem.contentPadding.calculateLeftPadding(layoutDirection).toPx()
                        val top = spotlightItem.contentPadding.calculateTopPadding().toPx()
                        val right = spotlightItem.contentPadding.calculateRightPadding(layoutDirection).toPx()
                        val bottom = spotlightItem.contentPadding.calculateBottomPadding().toPx()

                        if (spotlightItem.layoutCoordinates.isAttached) {
                            addRoundRect(
                                roundRect = RoundRect(
                                    left = posInParent.x - left,
                                    top = posInParent.y - top,
                                    right = posInParent.x + spotlightItem.layoutCoordinates.size.width + right,
                                    bottom = posInParent.y + spotlightItem.layoutCoordinates.size.height + bottom,
                                    radiusX = spotlightItem.cornerRadius.toPx(),
                                    radiusY = spotlightItem.cornerRadius.toPx(),
                                ),
                            )
                        }
                    }
                }

                clipPath(
                    path = spotlightPath,
                    clipOp = ClipOp.Difference,
                ) {
                    drawRect(
                        brush = SolidColor(scrimColor),
                        alpha = 1f,
                    )
                }
            },
        )
    }
}
