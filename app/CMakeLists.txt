# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

find_library ( cardano-lib cardano_c )

add_library( # Sets the name of the path variable.
             cardano-lib

             STATIC
             IMPORTED )

if (${ANDROID_ABI} STREQUAL "x86_64")
    set(CARDANO_LIB_TARGET "x86_64-linux-android")
elseif (${ANDROID_ABI} STREQUAL "x86")
    set(CARDANO_LIB_TARGET "i686-linux-android")
elseif (${ANDROID_ABI} STREQUAL "arm64-v8a")
    set(CARDANO_LIB_TARGET "aarch64-linux-android")
elseif (${ANDROID_ABI} STREQUAL "armeabi-v7a")
    set(CARDANO_LIB_TARGET "armv7-linux-androideabi")
else()
  set(CARDANO_LIB_TARGET "${ANDROID_ABI}")
endif ()

set_target_properties( # Specifies the target library.
                       cardano-lib

                       # Specifies the parameter you want to define.
                       PROPERTIES IMPORTED_LOCATION

                       # Provides the path to the library you want to import.
                       ${CMAKE_SOURCE_DIR}/cardano-c/${CARDANO_LIB_TARGET}/debug/libcardano_c.a
                       )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       cardano-lib )