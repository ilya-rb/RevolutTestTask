package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.libs.tools.ResourceResolver

class TestResourceResolver : ResourceResolver {
    override fun getString(id: Int): String = "string"
}