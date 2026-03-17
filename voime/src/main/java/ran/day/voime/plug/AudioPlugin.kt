package ran.day.voime.plug

import ran.day.voime.etc.Log
import ran.day.voime.io.onError
import ran.day.voime.io.toAudioEventCode
import ran.day.voime.io.toLogPriority
import ran.day.voime.jna.JnaCallback
import ran.day.voime.jna.JnaPointerByReference
import ran.day.voime.jna.JnaResultByReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class AudioPlugin(val name: String) : AutoCloseable, JnaCallback {

  private val crs = CoroutineScope(Dispatchers.Main)
  private val jna = AudioPluginFactory()
  private val ref = JnaPointerByReference()
  private val res = JnaResultByReference()

  private var error: ((exception: AudioPluginException) -> Unit)? = null
  private var state: Boolean = false

  val isStarted: Boolean
    get() = state and ref.isNotNull

  init {
    res.result { res ->
      jna.voime_plugin_open(name, this, ref, res)
    }.onFailure { throw it }
  }

  fun setup(input: Int, output: Int, samplerate: Int, blocksize: Int, channels: Int) {
    res.result { res ->
      jna.voime_plugin_setup(
        input, output, samplerate, blocksize, channels,
        ref, res)
    }.onFailure { throw it }
  }

  fun set(param: String, value: String) {
    res.result { res ->
      jna.voime_plugin_set(
        param, value,
        ref, res)
    }.onFailure { throw it }
  }

  fun start() {
    res.result { res ->
      jna.voime_plugin_start(ref, res)
    }.onSuccess {
      state = true
    }.onFailure {
      state = false
    }.onFailure { throw it }
  }

  fun stop() {
    res.result { res ->
      jna.voime_plugin_stop(ref, res)
    }.also {
      state = false
    }.onFailure { throw it }
  }

  override fun close() {
    res.result { res ->
      jna.voime_plugin_close(ref, res)
    }.also {
      state = false
    }.onFailure { throw it }
  }

  override fun callback(code: Int, data: String) {
    crs.launch {
      code.toAudioEventCode?.let { event ->
        Log.p(event.toLogPriority, "${name}: ${event} ${data}")
        event.onError {
          res.result { res ->
            jna.voime_plugin_stop(ref, res)
          }.also {
            state = false
          }.also {
            error?.invoke(AudioPluginException(event, data))
          }.onFailure { throw it }
        }
      }
    }
  }

  fun onError(callback: (exception: AudioPluginException) -> Unit) {
    error = callback
  }

}
