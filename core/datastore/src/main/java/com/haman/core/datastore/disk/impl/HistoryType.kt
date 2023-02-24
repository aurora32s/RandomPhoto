package com.haman.core.datastore.disk.impl

enum class HistoryType {
    CLEAN, DIRTY, REMOVE, READ, NONE;

    companion object {
        fun getHistoryType(type: Int) = values().first { it.ordinal == type }
    }
}