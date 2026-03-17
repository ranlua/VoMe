#pragma once

#include <voime/Header.h>

#include <voime/etc/Debouncer.h>
#include <voime/fx/AudioEffect.h>
#include <voime/io/AudioBlockQueue.h>
#include <voime/io/AudioStream.h>

class AudioSource final : public AudioStream {

public:

  AudioSource(const std::optional<int> device = std::nullopt,
              const std::optional<float> samplerate = std::nullopt,
              const std::optional<size_t> blocksize = std::nullopt,
              const std::optional<size_t> channels = std::nullopt,
              const std::shared_ptr<AudioEffect> effect = nullptr,
              const std::shared_ptr<AudioBlockQueue> queue = nullptr);

  std::shared_ptr<AudioEffect> fx() const;
  std::shared_ptr<AudioBlockQueue> fifo() const;

protected:

  void callback(const std::span<float> samples) override;

  void onopen() override;
  void onstart() override;

private:

  const std::shared_ptr<AudioEffect> effect;
  const std::shared_ptr<AudioBlockQueue> queue;

  struct {

    struct {
      uint64_t inner;
      uint64_t outer;
    } index;

    Debouncer overflow;

  } state;

};
