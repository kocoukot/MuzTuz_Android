package com.muztus.game_level_feature

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muztus.core.compose.AlertDialogComp
import com.muztus.core.compose.LetterSelectAlertDialog
import com.muztus.core.theme.MTTheme
import com.muztus.domain_layer.model.HintModel
import com.muztus.game_level_feature.content.BottomBarContent
import com.muztus.game_level_feature.model.GameLevelAction
import com.muztus.level_select_feature.R

@Composable
fun GameLevelScreenContent(viewModel: GameLevelViewModel) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.coinToast > 0) { //todo fix later mb
        val msg = stringResource(id = R.string.not_enough_coins_toast, state.coinToast)
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    state.showHintAlert?.let { hint ->
        AlertDialogComp(
            dialogText = stringResource(
                id = R.string.user_hint_alert_text,
                stringResource(id = hint.hintName()),
                hint.hintCost()
            ),
            onOptionSelected = { viewModel.setInputActions(GameLevelAction.OnHintAlertDecision(it)) }
        )
    }


    state.showLetterAlert
        .takeIf { it.isNotEmpty() }
        ?.let { answer ->
            LetterSelectAlertDialog(answer) {
                if (it >= 0) viewModel.setInputActions(GameLevelAction.UseOneLetterHint(it))
            }
        }


    Surface(
        color = MTTheme.colors.background,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(MTTheme.colors.background)
        ) {

            state.data.hintsRow().filterIsInstance<HintModel.SongHint>()
                .first()
                .takeIf { it.isEnabled() }
                ?.let {
                    Text(
                        color = MTTheme.colors.white,
                        fontSize = 12.sp,
                        text = state.data.getLevelSongHint(),
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.TopEnd)
            ) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(id = R.drawable.img_free_coin_chest),
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painterResource(id = state.data.getLevelImage()),
                        modifier = Modifier,
                        contentDescription = null,
                    )
                }

                state.data.getLettersAmount()
                    .takeIf { it.isNotEmpty() }
                    ?.let { hintString ->
                        Text(
                            color = MTTheme.colors.buttonPressed,
                            fontSize = 16.sp,
                            text = hintString,
                            modifier = Modifier
                        )
                    }
                BottomBarContent(state.data, viewModel::setInputActions)
            }
        }
    }
}