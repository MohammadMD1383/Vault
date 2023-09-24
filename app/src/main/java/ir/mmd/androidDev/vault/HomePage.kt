package ir.mmd.androidDev.vault

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.ui.component.IconText
import ir.mmd.androidDev.vault.ui.component.SearchField
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.add
import ir.mmd.androidDev.vault.util.onNavigationResult

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomePage(navController: NavController, items: SnapshotStateMap<String, String>) {
	var searchFieldExpanded by remember { mutableStateOf(false) }
	val context = LocalContext.current
	val clipboardManager = LocalClipboardManager.current
	val hapticFeedback = LocalHapticFeedback.current
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
								IconButton(onClick = { navController.navigate("settings") { launchSingleTop = true } }) {
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
				expanded = fabExpanded, // todo: https://stackoverflow.com/a/70460377/13436464
				onClick = { navController.navigate("content/new") { launchSingleTop = true } }
			)
		}
	) { paddingValues ->
		AnimatedContent(targetState = items.isEmpty(), label = "Empty & List") { isEmpty ->
			if (isEmpty) Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxSize()
			) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(16.dp)
				) {
					Text(
						text = "Start adding new items by clicking on",
						textAlign = TextAlign.Center
					)
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						IconText(
							icon = Icons.Rounded.Add,
							text = "Add",
							modifier = Modifier
								.border(1.dp, MaterialTheme.colorScheme.onBackground, FloatingActionButtonDefaults.extendedFabShape)
								.padding(16.dp, 16.dp, 20.dp, 16.dp)
						)
					}
				}
			} else LazyColumn(
				contentPadding = paddingValues.add(8.dp, 8.dp, 8.dp, 88.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				state = listState,
			) {
				items(items.entries.toList()) { (key, content) ->
					var menuIsExpanded by remember { mutableStateOf(false) }
					
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.clip(CardDefaults.shape)
							.combinedClickable(
								onClick = {},
								onLongClick = {
									clipboardManager.setText(AnnotatedString(content))
									hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
									Toast
										.makeText(context, "Copied!", Toast.LENGTH_SHORT)
										.show()
								}
							)
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween,
							modifier = Modifier
								.fillMaxWidth()
								.padding(16.dp, 16.dp, 4.dp, 16.dp)
						) {
							Text(key)
							
							Box(contentAlignment = Alignment.Center) {
								IconButton(onClick = { menuIsExpanded = true }) {
									Icon(Icons.Rounded.MoreVert, "More")
								}
								
								DropdownMenu(
									expanded = menuIsExpanded,
									onDismissRequest = { menuIsExpanded = false }
								) {
									DropdownMenuItem(
										text = { IconText(Icons.Rounded.Edit, "Edit") },
										onClick = {
											menuIsExpanded = false
											navController.navigate("content/edit") { launchSingleTop = true }
											navController.currentBackStackEntry
												?.savedStateHandle
												?.set("data", key to content)
										}
									)
									
									DropdownMenuItem(
										text = { IconText(Icons.Rounded.DeleteOutline, "Delete") },
										onClick = {
											menuIsExpanded = false
											items.remove(key)
										}
									)
								}
							}
						}
					}
				}
			}
		}
	}
	
	navController.onNavigationResult<Pair<String, String>>("new") { (key, content) ->
		items[key] = content
	}
	
	navController.onNavigationResult<Pair<String, String>>("edit") { (key, value) ->
		items[key] = value
	}
}

@Preview
@Composable
private fun LightPreview() {
	val items = remember { mutableStateMapOf("credential" to "SECRET") }
	VaultTheme(darkTheme = false) {
		HomePage(rememberNavController(), items)
	}
}

@Preview
@Composable
private fun DarkPreview() {
	val items = remember { mutableStateMapOf("credential" to "SECRET") }
	VaultTheme(darkTheme = true) {
		HomePage(rememberNavController(), items)
	}
}
