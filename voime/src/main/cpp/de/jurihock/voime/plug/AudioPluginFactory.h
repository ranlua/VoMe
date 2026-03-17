#pragma once

#include <voime/Header.h>

#include <voime/etc/JNA.h>

jna bool voime_plugin_open(const char* name, jna_callback* callback, jna_pointer* pointer, jna_result* result);
jna bool voime_plugin_setup(int input, int output, int samplerate, int blocksize, int channels, jna_pointer* pointer, jna_result* result);
jna bool voime_plugin_set(const char* param, const char* value, jna_pointer* pointer, jna_result* result);
jna bool voime_plugin_start(jna_pointer* pointer, jna_result* result);
jna bool voime_plugin_stop(jna_pointer* pointer, jna_result* result);
jna bool voime_plugin_close(jna_pointer* pointer, jna_result* result);
