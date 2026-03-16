package ran.day.voime.service

import android.os.Binder

class AudioServiceBinder(val service: AudioService) : Binder()
