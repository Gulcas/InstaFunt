package com.rafal.instafunt

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

data class Comment(
    val id: String,
    val message: String,
    val date: LocalDateTime
)

interface CommentRepository {
    suspend fun save(comment: Comment)
    suspend fun remove(comment: Comment)
}

class CommentsViewModel(private val repository: CommentRepository) : ViewModel() {
    constructor() : this(CommentRepositoryImpl())

    private val state = mutableStateOf(emptyList<Comment>())
    val uiState: State<List<Comment>>
        get() = state

    suspend fun addComment(commentMessage: String) {
        val comment = Comment(
            id = UUID.randomUUID().toString(),
            message = commentMessage,
            date = LocalDateTime.now()
        )
        repository.save(comment)
        state.value += comment
    }

    suspend fun removeComment(commentId: String) {
        val comment = state.value.find { comment -> comment.id == commentId }
            ?: return
        repository.remove(comment)
        state.value -= comment
    }
}

class CommentRepositoryImpl : CommentRepository {
    override suspend fun remove(comment: Comment) {
        delay(1000)
        if (Random.nextInt() % 100 >= 80) {
            throw IllegalStateException("Something went wrong")
        }
    }

    override suspend fun save(comment: Comment) {
        delay(1000)

    }
}
