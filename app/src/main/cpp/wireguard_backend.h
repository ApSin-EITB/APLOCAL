#ifndef WIREGUARD_BACKEND_H
#define WIREGUARD_BACKEND_H

extern "C" {

int startTunnel(const char* config);
int stopTunnel();

}

#endif // WIREGUARD_BACKEND_H
