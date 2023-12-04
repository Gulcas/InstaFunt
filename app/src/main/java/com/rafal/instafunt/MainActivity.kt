package com.rafal.instafunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rafal.instafunt.ui.theme.InstaFuntTheme
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaFuntTheme {
                val viewModel by viewModels<CommentsViewModel>()
                val comments by viewModel.uiState
                Column {
                    LazyColumn {
                        items(comments) { comment ->
                            Comment(comment)
                        }
                    }
                    Row {
                        val comment = remember {
                            mutableStateOf("")
                        }
                        TextField(
                            value = comment.value,
                            onValueChange = { comment.value = it }
                        )
//                        Button(onClick = { viewModel.addComment(comment.value) }) {
//                            Text(text = "Add")
//                        }

                        Button(onClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.addComment(comment.value)
                            }
                        }) {
                            Text(text = "Add")
                        }


                    }
                }
            }

        }
    }
}

@Composable
fun Comment(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = comment.message)
    }
}


