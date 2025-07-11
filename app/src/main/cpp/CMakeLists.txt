cmake_minimum_required(VERSION 3.18.1)
project(vpnbridge)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)

add_library(vpnbridge SHARED
        vpnbridge.cpp
)

# Получаем путь, переданный из Gradle
add_library(wg-go SHARED IMPORTED)

# 👇 Устанавливаем путь, полученный от Gradle
set_target_properties(wg-go PROPERTIES
        IMPORTED_LOCATION "${GO_WG_LIB}"
)

target_link_libraries(vpnbridge
        ${CMAKE_SOURCE_DIR}/../jniLibs/arm64-v8a/libwg-go.so
)

