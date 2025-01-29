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

package com.google.jetstream.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountsSelectionItem(
    accountsSectionData: AccountsSectionData,
    modifier: Modifier = Modifier,
    key: Any? = null,
) {
    key(key) {
        Surface(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .aspectRatio(2f)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .clickable(onClick = accountsSectionData.onClick),
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = accountsSectionData.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                accountsSectionData.value?.let { nnValue ->
                    Text(
                        text = nnValue,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.alpha(0.75f)
                    )
                }
            }
        }
    }
}
