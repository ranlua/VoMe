package ran.day.voime.plug

import com.sun.jna.Native
import ran.day.voime.jna.JnaCallback
import ran.day.voime.jna.JnaPointerByReference
import ran.day.voime.jna.JnaResultByReference

@Suppress("KotlinJniMissingFunction", "FunctionName")
open class AudioPluginFactory {

  init {
    Native.register(AudioPluginFactory::class.java, "voicesmith")
  }

  external fun voicesmith_plugin_open(name: String, callback: JnaCallback, pointer: JnaPointerByReference, result: JnaResultByReference) : Boolean
  external fun voicesmith_plugin_setup(input: Int, output: Int, samplerate: Int, blocksize: Int, channels: Int, pointer: JnaPointerByReference, result: JnaResultByReference) : Boolean
  external fun voicesmith_plugin_set(param: String, value: String, pointer: JnaPointerByReference, result: JnaResultByReference) : Boolean
  external fun voicesmith_plugin_start(pointer: JnaPointerByReference, result: JnaResultByReference) : Boolean
  external fun voicesmith_plugin_stop(pointer: JnaPointerByReference, result: JnaResultByReference) : Boolean
  external fun voicesmith_plugin_close(pointer: JnaPointerByReference, result: JnaResultByReference) : Boolean

}
