package com.rafal.instafunt


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import com.rafal.instafunt.ui.theme.InstaFuntTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    viewModel: CommentsViewModel = viewModel()
) {
    var darkMode by remember { mutableStateOf(false) }
    InstaFuntTheme(darkTheme = darkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.shadow(4.dp),
                    title = { Text(text = "Comments") },
                    actions = {
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it }
                        )
                    }
                )
            },
            bottomBar = {
                BottomBar(
                    modifier = Modifier.shadow(4.dp),
                    onCommentAdd = { commentMessage ->
                        viewModel.viewModelScope.launch {
                            viewModel.addComment(commentMessage)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Content(
                modifier = Modifier.padding(paddingValues),
                comments = viewModel.uiState.value,
                onRemoveComment = { commentId, coroutineScope ->
                    coroutineScope.launch {
                        viewModel.removeComment(commentId)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier,
    comments: List<Comment>,
    onRemoveComment: (String, CoroutineScope) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(
            items = comments,
            key = { it.id }
        ) { comment ->
            val coroutineScope = rememberCoroutineScope() // Utwórz korutynowy zasięg

            Comment(
                modifier = Modifier.animateItemPlacement(),
                comment = comment,
                onRemoveComment = { commentId -> onRemoveComment(commentId, coroutineScope) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Comment(
    modifier: Modifier = Modifier,
    comment: Comment,
    onRemoveComment: (String) -> Unit,
) {
    var showDetails by remember(comment.id) { mutableStateOf(false) }
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier
            .clickable { showDetails = !showDetails }
            .padding(16.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = comment.message,
                    fontSize = 14.sp
                )
                AnimatedVisibility(visible = showDetails) {
                    Icon(
                        modifier = Modifier.clickable { onRemoveComment(comment.id) },
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(visible = showDetails) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = comment.date.toString(),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.End,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomBar(
    modifier: Modifier = Modifier,
    onCommentAdd: (String) -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val message = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message.value,
            onValueChange = { message.value = it }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            enabled = message.value.isNotBlank(),
            onClick = { onCommentAdd(message.value) }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                Text(text = "Add")
            }
        }
    }
}
