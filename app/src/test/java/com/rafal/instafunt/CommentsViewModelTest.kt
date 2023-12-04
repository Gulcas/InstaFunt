package com.rafal.instafunt

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
class CommentsViewModelTest {
    private var savedComment: Comment? = null
    private var removedComment: Comment? = null
    private val instance = CommentsViewModel(
        object : CommentRepository {
            override suspend fun save(comment: Comment) {
                savedComment = comment
            }

            override suspend fun remove(comment: Comment) {
                removedComment = comment
            }
        }
    )

    @Test
    fun `screen should start without any comments`() {
        // given

        // when

        // then
        assertEquals(emptyList<Comment>(), instance.uiState.value)
    }

    @Test
    suspend fun `added message should be displayed on UI`() {
        // given
        val commentMessage = "some message"

        // when
        instance.addComment(commentMessage)

        // then
        assertTrue(instance.uiState.value.any { comment -> comment.message == commentMessage })
    }

    @Test
    suspend fun `added message should be saved in repository`() {
        // given
        val commentMessage = "some message"

        // when
        instance.addComment(commentMessage)

        // then
        assertTrue(savedComment?.message == commentMessage)
    }

    @Test
    suspend fun `removed comment should be removed from UI`() {
        // given
        val commentMessage = "some message"
        instance.addComment(commentMessage)
        val comment = instance.uiState.value.first()

        // when
        instance.removeComment(comment.id)

        // then
        assertTrue(comment !in instance.uiState.value)
    }

    @Test
    suspend fun `removed comment should be removed from repository`() {
        // given
        val commentMessage = "some message"
        instance.addComment(commentMessage)
        val comment = instance.uiState.value.first()

        // when
        instance.removeComment(comment.id)

        // then
        assertEquals(comment, removedComment)
    }
}