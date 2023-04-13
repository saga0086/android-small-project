package com.example.showpointsgraph

import com.robinhood.spark.SparkAdapter

class PointAdapter(val len: Int): SparkAdapter() {
    var curIdx = 0
    val data = FloatArray(len)

    fun addPoint(p: Float) {
        if (curIdx < len) {
            data[curIdx++] = p
        } else {
            for (i in 1..len-1) {
                data[i-1] = data[i]
            }
            data[len-1] = p
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return curIdx
    }

    override fun getItem(index: Int): Any {
        return data[index]
    }

    override fun getY(index: Int): Float {
        return data[index]
    }
}