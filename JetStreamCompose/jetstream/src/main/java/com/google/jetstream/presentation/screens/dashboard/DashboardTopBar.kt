/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.dashboard

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.FormFactor
import com.google.jetstream.presentation.components.shim.rememberUiMode
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.components.shim.tvSelectTarget
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.theme.IconSize
import com.google.jetstream.presentation.theme.LexendExa

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DashboardTopBar(
    items: List<Screens>,
    selectedScreen: Screens,
    showScreen: (Screens) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val (tabRow, avatar) = remember { FocusRequester.createRefs() }

    val uiMode = rememberUiMode()
    val onClickHandler: (Screens) -> Unit = remember(uiMode) {
        when (uiMode.formFactor) {
            FormFactor.Tv -> {
                { focusManager.moveFocus(FocusDirection.Down) }
            }

            else -> { it -> showScreen(it) }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .focusProperties {
                enter = {
                    when (selectedScreen) {
                        Screens.Profile -> avatar
                        else -> tabRow
                    }
                }
            }
            .focusGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            UserAvatar(
                modifier = Modifier
                    .size(32.dp)
                    .semantics {
                        contentDescription =
                            StringConstants.Composable.ContentDescription.UserAvatar
                    }
                    .focusRequester(avatar),
                selected = selectedScreen == Screens.Profile,
                onClick = { showScreen(Screens.Profile) }
            )
        }
        TopBarTabRow(
            tabs = items.slice(1..<items.size),
            selectedScreen = selectedScreen,
            onClick = onClickHandler,
            onTabSelected = showScreen,
            modifier = Modifier
                .weight(1f)
                .focusRequester(tabRow)
        )
        Spacer(modifier.weight(0.1f))
        JetStreamLogo(
            modifier = Modifier
                .alpha(0.75f)
                .padding(end = 8.dp),
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TopBarTabRow(
    tabs: List<Screens>,
    selectedScreen: Screens,
    onTabSelected: (Screens) -> Unit,
    onClick: (Screens) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = remember(tabs) {
        tabs.map { it to FocusRequester() }
    }

    var selectedScreenIndex by rememberSaveable(tabs) {
        mutableIntStateOf(selectedTabIndex(tabs, selectedScreen, 0))
    }

    selectedScreenIndex = selectedTabIndex(tabs, selectedScreen, selectedScreenIndex)

    LaunchedEffect(selectedScreenIndex) {
        items[selectedScreenIndex].second.tryRequestFocus()
    }

    TabRow(
        selectedTabIndex = selectedScreenIndex,
        divider = {},
        modifier = modifier
            .focusProperties {
                enter = {
                    if (selectedScreenIndex < items.size) {
                        items[selectedScreenIndex].second
                    } else {
                        items[0].second
                    }
                }
            }
            .focusGroup()
    ) {
        items.forEach { (screen, focusRequester) ->
            key(screen) {
                TopBarTab(
                    screen = screen,
                    selected = selectedScreen == screen,
                    onClick = { onClick(screen) },
                    onSelect = { onTabSelected(screen) },
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
        }
    }
}

private fun selectedTabIndex(
    tabs: List<Screens>,
    selectedScreen: Screens,
    previouslySelectedTabIndex: Int
): Int {
    val index = tabs.indexOf(selectedScreen) % tabs.size
    return when {
        index < 0 -> previouslySelectedTabIndex
        else -> index
    }
}

@Composable
private fun TopBarTab(
    screen: Screens,
    onClick: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Tab(
        selected = selected,
        modifier = Modifier
            .tvSelectTarget {
                onSelect()
            }
            .then(modifier),
        onClick = onClick,
    ) {
        TopBarTabContent(screen)
    }
}

@Composable
private fun TopBarTabContent(
    screen: Screens,
    modifier: Modifier = Modifier
) {
    if (screen.tabIcon != null) {
        Icon(
            screen.tabIcon,
            modifier = Modifier
                .padding(4.dp)
                .then(modifier),
            contentDescription = StringConstants.Composable
                .ContentDescription.DashboardSearchButton,
            tint = LocalContentColor.current,
        )
    } else {
        Text(
            modifier = modifier,
            text = screen.name,
            style = MaterialTheme.typography.titleSmall.copy(
                color = LocalContentColor.current
            )
        )
    }
}

@Composable
private fun JetStreamLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.PlayCircle,
            contentDescription = StringConstants.Composable
                .ContentDescription.BrandLogoImage,
            modifier = Modifier
                .padding(end = 4.dp)
                .size(IconSize)
        )
        Text(
            text = stringResource(R.string.brand_logo_text),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            fontFamily = LexendExa,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
