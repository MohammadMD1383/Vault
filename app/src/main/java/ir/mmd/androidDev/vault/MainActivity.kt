package ir.mmd.androidDev.vault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.mmd.androidDev.vault.ui.component.SearchField
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.add

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			VaultTheme {
				MainComponent()
			}
		}
	}
	
	override fun onPause() {
		super.onPause()
		finish()
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainComponent() {
	var searchFieldExpanded by remember { mutableStateOf(false) }
	val searchTerm = remember { mutableStateOf("") }
	val listState = rememberLazyListState()
	val fabExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
	
	BackHandler(searchFieldExpanded) {
		searchFieldExpanded = false
	}
	
	Scaffold(
		topBar = {
			Surface(
				modifier = Modifier.shadow(8.dp)
			) {
				AnimatedContent(
					targetState = searchFieldExpanded,
					label = "TopAppBar & SearchField",
					transitionSpec = {
						if (targetState) {
							slideInVertically { height -> -height } + fadeIn() togetherWith
								slideOutVertically { height -> height } + fadeOut()
						} else {
							slideInVertically { height -> height } + fadeIn() togetherWith
								slideOutVertically { height -> -height } + fadeOut()
						}
					}
				) {
					if (it) {
						SearchField(searchTerm) { searchFieldExpanded = false }
					} else {
						CenterAlignedTopAppBar(
							title = { Text(stringResource(R.string.app_name)) },
							navigationIcon = {
								IconButton(onClick = { /*TODO*/ }) {
									Icon(Icons.Rounded.Settings, "Settings")
								}
							},
							actions = {
								IconButton(onClick = { searchFieldExpanded = true }) {
									Icon(Icons.Rounded.Search, "Search")
								}
							}
						)
					}
				}
			}
		},
		floatingActionButton = {
			ExtendedFloatingActionButton(
				{ Text("Add") },
				{ Icon(Icons.Rounded.Add, "Add") },
				expanded = fabExpanded,
				onClick = { /*TODO*/ }
			)
		}
	) { paddingValues ->
		LazyColumn(
			contentPadding = paddingValues.add(8.dp, 8.dp, 8.dp, 88.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp),
			state = listState,
		) {
			repeat(10) {
				item {
					Card(
						modifier = Modifier.fillMaxWidth()
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween,
							modifier = Modifier
								.fillMaxWidth()
								.padding(16.dp)
						) {
							Text("My National Code")
							IconButton(onClick = { /*TODO*/ }) {
								Icon(Icons.Rounded.MoreVert, "More")
							}
						}
					}
				}
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		MainComponent()
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		MainComponent()
	}
}
