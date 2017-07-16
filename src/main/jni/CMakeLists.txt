cmake_minimum_required(VERSION 3.7)
project(speexjni)

set(CMAKE_C_STANDARD 99)

find_package(JNI REQUIRED)

find_library(SPEEXDSP_LIBRARY speex)

include_directories(
        ${JNI_INCLUDE_DIRS}
        ${PROJECT_SOURCE_DIR}/src/speex-1.2.0/include
)
set(
        SOURCE_FILES
        commons.h
        com_orctom_speex4j_SpeexEncoder.c
        com_orctom_speex4j_SpeexDecoder.c
)
add_library(
        speexjni SHARED
        ${SOURCE_FILES}
)
link_directories(
        ${PROJECT_SOURCE_DIR}/speex-1.2.0/lib
)
target_link_libraries(speexjni speex)

# Warnings should be enabled for GCC. Also turning off the NDEBUG flag
# since that remove asserts.
if (CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wstrict-prototypes -Wpointer-arith")
    set(CMAKE_C_FLAGS_RELEASE "-O3")
    set(CMAKE_C_FLAGS_RELWITHDEBINFO "-O2 -g")

    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wpointer-arith")
    set(CMAKE_CXX_FLAGS_RELEASE "-O3")
    set(CMAKE_CXX_FLAGS_RELWITHDEBINFO "-O2 -g")
endif ()

install(TARGETS speexjni DESTINATION ../resources/)
