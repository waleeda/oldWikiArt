package com.wikiart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wikiart.model.LayoutType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : CategoryItem> OptionsBottomSheet(
    state: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    ),
    categories: Array<T>? = null,
    selectedCategory: T? = null,
    layoutType: LayoutType,
    onApply: (T?, LayoutType) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var selected = selectedCategory
    var layout = layoutType
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories?.let { list ->
                LazyColumn {
                    items(list.toList()) { cat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selected = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = cat == selected,
                                onClick = { selected = cat }
                            )
                            Text(
                                text = stringResource(id = cat.nameRes()),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = layout == LayoutType.LIST,
                    onClick = { layout = LayoutType.LIST }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_layout_list),
                    contentDescription = stringResource(R.string.layout_list)
                )
                RadioButton(
                    selected = layout == LayoutType.COLUMN,
                    onClick = { layout = LayoutType.COLUMN }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_layout_grid),
                    contentDescription = stringResource(R.string.layout_grid)
                )
                RadioButton(
                    selected = layout == LayoutType.SHEET,
                    onClick = { layout = LayoutType.SHEET }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_layout_sheet),
                    contentDescription = stringResource(R.string.layout_sheet)
                )
            }
            Button(
                onClick = {
                    onApply(selected, layout)
                    scope.launch { state.hide(); onDismiss() }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("OK")
            }
        }
    }
}
