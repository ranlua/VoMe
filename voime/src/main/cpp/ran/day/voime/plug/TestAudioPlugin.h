#pragma once

#include <voime/Header.h>

#include <voime/etc/JNA.h>
#include <voime/io/AudioPipeline.h>
#include <voime/plug/AudioPlugin.h>

#include <voime/fx/StereoChainEffect.h>
#include <voime/fx/DelayEffect.h>
#include <voime/fx/PitchTimbreShiftEffect.h>

class TestAudioPlugin final : public AudioPlugin {

public:

  TestAudioPlugin(jna_callback* callback);
  ~TestAudioPlugin();

  void setup(const std::optional<int> input,
             const std::optional<int> output,
             const std::optional<float> samplerate,
             const std::optional<size_t> blocksize,
             const std::optional<size_t> channels) override;

  void set(const std::string& param,
           const std::string& value) override;

  void start() override;
  void stop() override;

private:

  jna_callback* callback;

  struct {

    std::optional<int> input;
    std::optional<int> output;
    std::optional<float> samplerate;
    std::optional<size_t> blocksize;
    std::optional<size_t> channels;

  } config;

  struct {

    std::shared_ptr<AudioPipeline> pipeline;
    std::shared_ptr<StereoChainEffect<DelayEffect, PitchTimbreShiftEffect>> effects;

  } state;

};
