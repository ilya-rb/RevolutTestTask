package com.illiarb.revoluttest.libs.tools.internal

import android.content.Context
import com.illiarb.revoluttest.libs.tools.ResourceResolver
import javax.inject.Inject

internal class AndroidResourceResolver @Inject constructor(
    private val context: Context
) : ResourceResolver {

    override fun getString(id: Int): String = context.getString(id)
}