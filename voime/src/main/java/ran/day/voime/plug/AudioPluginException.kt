package ran.day.voime.plug

import ran.day.voime.io.AudioEventCode

class AudioPluginException(val event: AudioEventCode, message: String) : Exception(message)
