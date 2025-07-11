package ru.apsin.aplocal

object WireGuardBackend {
    init {
        System.loadLibrary("wggo") // wggo → имя .so или .cpp библиотеки
    }

    external fun startTunnel(config: String): Int
    external fun stopTunnel(): Int
}
