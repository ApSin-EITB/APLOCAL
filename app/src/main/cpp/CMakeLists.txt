cmake_minimum_required(VERSION 3.10)

project(wireguard_backend)

add_library(
        wggo
        SHARED
        wireguard_backend.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        wggo
        ${log-lib}
)
