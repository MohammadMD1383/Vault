package ir.mmd.androidDev.vault

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import ir.mmd.androidDev.vault.model.AppSettings
import ir.mmd.androidDev.vault.ui.component.ConfirmDialog
import ir.mmd.androidDev.vault.ui.component.IconText
import ir.mmd.androidDev.vault.ui.component.PreviewDialog
import ir.mmd.androidDev.vault.ui.component.SearchField
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.add
import ir.mmd.androidDev.vault.util.onNavigationResult

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomePage(navController: NavController, items: SnapshotStateMap<String, String>) {
	var searchFieldExpanded by remember { mutableStateOf(false) }
	val context = LocalContext.current
	val mainActivity = context as MainActivity
	val clipboardManager = LocalClipboardManager.current
	val hapticFeedback = LocalHapticFeedback.current
	var searchTerm by remember { mutableStateOf("") }
	var filterApplied by remember { mutableStateOf(false) }
	var fabExpanded by remember { mutableStateOf(true) }
	var previewKey by remember { mutableStateOf(null as String?) }
	var previewContent by remember { mutableStateOf(null as String?) }
	var keyToRemove by remember { mutableStateOf(null as String?) }
	var removeConfirmed by remember { mutableStateOf(false) }
	var keyToAdd by remember { mutableStateOf(null as String?) }
	val nestedScrollConnection = remember {
		object : NestedScrollConnection {
			override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
				if (consumed.y < -0 && fabExpanded) {
					fabExpanded = false
				} else if (consumed.y > 0 && !fabExpanded) {
					fabExpanded = true
				}
				return super.onPostScroll(consumed, available, source)
			}
		}
	}
	
	val copyContent = remember {
		{ content: String ->
			clipboardManager.setText(AnnotatedString(content))
			hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
			Toast
				.makeText(context, context.resources.getString(R.string.text_copied), Toast.LENGTH_SHORT)
				.show()
		}
	}
	
	val closePreview = remember {
		{
			previewKey = null
			previewContent = null
		}
	}
	
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
				) { targetState ->
					if (targetState) {
						SearchField(
							value = searchTerm,
							onDismissRequest = { searchFieldExpanded = false },
							onValueChange = {
								searchTerm = it
								filterApplied = it.isNotBlank()
							}
						)
					} else {
						CenterAlignedTopAppBar(
							title = { Text(stringResource(R.string.app_name)) },
							navigationIcon = {
								IconButton(onClick = { navController.navigate("settings") { launchSingleTop = true } }) {
									Icon(Icons.Rounded.Settings, stringResource(R.string.text_settings))
								}
							},
							actions = {
								IconButton(onClick = { searchFieldExpanded = true }) {
									Icon(Icons.Rounded.Search, stringResource(R.string.text_search))
								}
							}
						)
					}
				}
			}
		},
		floatingActionButton = {
			ExtendedFloatingActionButton(
				{ Text(stringResource(R.string.text_add)) },
				{ Icon(Icons.Rounded.Add, stringResource(R.string.text_add)) },
				expanded = fabExpanded,
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
						text = stringResource(R.string.text_empty_home),
						textAlign = TextAlign.Center
					)
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						IconText(
							icon = Icons.Rounded.Add,
							text = stringResource(R.string.text_add),
							modifier = Modifier
								.border(1.dp, MaterialTheme.colorScheme.onBackground, FloatingActionButtonDefaults.extendedFabShape)
								.padding(16.dp, 16.dp, 20.dp, 16.dp)
						)
					}
				}
			} else LazyColumn(
				contentPadding = paddingValues.add(8.dp, 0.dp, 8.dp, (32 + 56).dp),
				modifier = Modifier.nestedScroll(nestedScrollConnection)
			) {
				items(items.entries.toList(), key = { it.key }) { (key, content) ->
					val matchesFilter = !filterApplied || searchTerm.split(' ').all { it in key }
					var menuIsExpanded by remember { mutableStateOf(false) }
					var visible by remember {
						mutableStateOf(
							if (keyToAdd == key) {
								keyToAdd = null
								false
							} else true
						)
					}
					
					if (keyToRemove == key && removeConfirmed) {
						visible = false
						keyToRemove = null
						removeConfirmed = false
					}
					
					LaunchedEffect(Unit) {
						visible = true
					}
					
					AnimatedVisibility(
						visible = matchesFilter && visible,
						enter = fadeIn() + expandVertically() + scaleIn(),
						exit = fadeOut() + shrinkVertically() + scaleOut()
					) {
						DisposableEffect(Unit) {
							onDispose {
								if (matchesFilter && !visible) {
									items.remove(key)
									mainActivity.save()
								}
							}
						}
						
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 8.dp)
								.clip(CardDefaults.shape)
								.combinedClickable(
									onLongClick = { copyContent(content) },
									onClick = {
										previewKey = key
										previewContent = content
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
										Icon(Icons.Rounded.MoreVert, stringResource(R.string.text_more))
									}
									
									DropdownMenu(
										expanded = menuIsExpanded,
										onDismissRequest = { menuIsExpanded = false }
									) {
										DropdownMenuItem(
											text = { IconText(Icons.Rounded.Edit, stringResource(R.string.text_edit)) },
											onClick = {
												menuIsExpanded = false
												navController.navigate("content/edit") { launchSingleTop = true }
												navController.currentBackStackEntry
													?.savedStateHandle
													?.set("data", key to content)
											}
										)
										
										DropdownMenuItem(
											text = { IconText(Icons.Rounded.DeleteOutline, stringResource(R.string.text_delete)) },
											onClick = {
												menuIsExpanded = false
												keyToRemove = key
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
	}
	
	if (previewKey != null) {
		PreviewDialog(
			key = previewKey!!,
			content = previewContent!!,
			onDismissRequest = { closePreview() },
			onCopyRequest = {
				copyContent(previewContent!!)
				if (AppSettings.closePreviewAfterCopy) {
					closePreview()
				}
			},
			onEditRequest = {
				navController.navigate("content/edit") { launchSingleTop = true }
				navController.currentBackStackEntry
					?.savedStateHandle
					?.set("data", previewKey to previewContent)
				closePreview()
			},
			onDeleteRequest = {
				keyToRemove = previewKey
				closePreview()
			}
		)
	}
	
	if (keyToRemove != null) {
		ConfirmDialog(
			text = stringResource(R.string.confirm_delete_item),
			onConfirm = { removeConfirmed = true },
			onDismissRequest = { keyToRemove = null }
		)
	}
	
	navController.onNavigationResult<Pair<String, String>>("new", "edit") { mode, (key, content) ->
		if (mode == "new") {
			keyToAdd = key
		}
		
		items[key] = content
		mainActivity.save()
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
