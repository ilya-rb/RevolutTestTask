package com.illiarb.revoluttest.services.revolut.internal.cache

import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

internal data class RatePersistable(
    var imageUrl: String = "",
    var code: String = "",
    var rate: Float = 0f
) : Persistable {

    override fun readExternal(input: DataInput) {
        imageUrl = input.readString()
        code = input.readString()
        rate = input.readFloat()
    }

    override fun deepClone(): RatePersistable = this

    override fun writeExternal(output: DataOutput) {
        output.writeString(imageUrl)
        output.writeString(code)
        output.writeFloat(rate)
    }
}