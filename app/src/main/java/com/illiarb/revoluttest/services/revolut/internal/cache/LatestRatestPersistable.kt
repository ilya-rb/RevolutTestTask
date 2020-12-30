package com.illiarb.revoluttest.services.revolut.internal.cache

import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

internal data class LatestRatestPersistable(
    var baseRate: RatePersistable = RatePersistable(),
    var rates: MutableList<RatePersistable> = mutableListOf()
) : Persistable {

    override fun readExternal(input: DataInput) {
        baseRate.readExternal(input)
        input.readPersistableList(rates) { RatePersistable() }
    }

    override fun deepClone(): Persistable {
        return LatestRatestPersistable(
            baseRate = baseRate.deepClone(),
            rates = rates.map { it.deepClone() }.toMutableList()
        )
    }

    override fun writeExternal(output: DataOutput) {
        baseRate.writeExternal(output)
        output.writePersistableList(rates)
    }

    private fun <T : Persistable> DataOutput.writePersistableList(list: Collection<T>) {
        if (list.isNotEmpty()) {
            writeInt(list.size)

            list.forEach {
                it.writeExternal(this)
            }
        }
    }

    private fun <T : Persistable> DataInput.readPersistableList(
        outList: MutableCollection<T>,
        creator: () -> T
    ) {
        val size = readInt()
        for (i in 0 until size) {
            outList.add(creator().also { it.readExternal(this) })
        }
    }
}