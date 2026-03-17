#pragma once

#include <voime/Header.h>

#include <voime/io/AudioEventCode.h>
#include <voime/io/AudioEventCodeExtensions.h>

#include <eventpp/callbacklist.h>

class AudioEvent final : public eventpp::CallbackList<void(const AudioEventCode code, const std::string& data)> {

public:

  class Emitter {

  public:

    virtual ~Emitter() = default;

    virtual void subscribe(const Callback& callback) = 0;

  };

};
