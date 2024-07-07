package fyi.atom.lifesuite.dev

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

fun <T> Flow<T>.logEach(tag: String? = null): Flow<T> =
    onEach {
        if (tag != null) {
            Timber.tag(tag).i(it.toString())
        } else {
            Timber.i(it.toString())
        }
    }
