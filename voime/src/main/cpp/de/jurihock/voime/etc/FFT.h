#pragma once

#include <voime/Header.h>

#include <voime/etc/FFT/PocketFFT.h>
#include <voime/etc/FFT/PrettyFastFFT.h>

using FFT = std::conditional_t<std::is_same_v<fft_t, double>, PocketFFT, PrettyFastFFT>;
