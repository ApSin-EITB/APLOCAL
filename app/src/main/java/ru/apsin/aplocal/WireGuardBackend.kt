package ru.apsin.aplocal

object WireGuardBackend {
    init {
        System.loadLibrary("wggo")
    }

    external fun startTunnel(config: String): Int
    external fun stopTunnel(): Int
}

