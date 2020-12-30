package com.illiarb.revoluttest.libs.ui.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.annotation.RawRes
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.illiarb.revoluttest.libs.ui.R
import com.illiarb.revoluttest.libs.ui.common.Text
import com.illiarb.revoluttest.libs.ui.ext.exhaustive

class StatefulRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewSwitcher(context, attrs) {

    private val viewSwitcher: ViewSwitcher
    private val animationView: LottieAnimationView
    private val animationViewCaption: TextView
    private val recyclerView: RecyclerView

    private val currentState: State
        get() = when (viewSwitcher.nextView) {
            is RecyclerView -> State.EMPTY
            else -> State.CONTENT
        }

    init {
        val view = inflate(context, R.layout.view_stateful_recycler_view, this)

        viewSwitcher = view.findViewById(R.id.srv_view_switcher)
        recyclerView = view.findViewById(R.id.srv_recycler_view)
        animationView = view.findViewById(R.id.srv_animation_view)
        animationViewCaption = view.findViewById(R.id.srv_animation_view_caption)

        viewSwitcher.inAnimation = AlphaAnimation(0f, 1f).apply {
            duration = ANIMATION_DURATION
        }

        viewSwitcher.outAnimation = AlphaAnimation(1f, 0f).apply {
            duration = ANIMATION_DURATION
        }
    }

    fun setAnimationViewRawRes(@RawRes resId: Int) {
        animationView.setAnimation(resId)
    }

    fun setAnimationViewCaption(text: Text) {
        when (text) {
            is Text.ResourceIdText -> animationViewCaption.setText(text.id)
        }.exhaustive
    }

    fun recyclerView(block: (RecyclerView) -> Unit) {
        block(recyclerView)
    }

    fun moveToState(newState: State) = invalidateState(newState)

    private fun invalidateState(newState: State) {
        when (newState) {
            State.CONTENT -> moveToContentState()
            State.EMPTY -> moveToEmptyState()
            State.ERROR -> {
                moveToEmptyState()
                animationView.pauseAnimation()
            }
        }.exhaustive
    }

    private fun moveToContentState() {
        if (currentState == State.EMPTY) {
            viewSwitcher.showPrevious()
        }
    }

    private fun moveToEmptyState() {
        if (currentState == State.CONTENT) {
            viewSwitcher.showPrevious()
            animationView.resumeAnimation()
        }
    }

    enum class State {
        CONTENT,
        EMPTY,
        ERROR
    }

    companion object {
        private const val ANIMATION_DURATION = 400L
    }
}