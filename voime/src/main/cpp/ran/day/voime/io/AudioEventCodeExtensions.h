#pragma once

#include <voime/Header.h>

#include <voime/io/AudioEventCode.h>

constexpr int operator!(AudioEventCode code) noexcept {
  return static_cast<int>(code);
}
