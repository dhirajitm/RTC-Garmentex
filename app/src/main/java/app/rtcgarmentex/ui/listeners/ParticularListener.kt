package app.rtcgarmentex.ui.listeners

import app.rtcgarmentex.data.ParticularModel

interface ParticularListener {
    fun deleteParticular(position: Int)
    fun updateParticular(position: Int, item: ParticularModel)
}