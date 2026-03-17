#pragma once

#include <voime/Header.h>

#include <voime/etc/FIFO.h>
#include <voime/io/AudioBlock.h>

class AudioBlockQueue final : public FIFO<AudioBlock> {

public:

  ~AudioBlockQueue();

  void resize(const size_t queuesize, const size_t blocksize);

private:

  std::vector<std::shared_ptr<AudioBlock>> blocks;
  std::vector<float> memory;

};
