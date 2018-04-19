
import sys
import ctypes

import socket

hlldll = ctypes.WinDLL ('User32.dll')


def scroll_down (n):
    hlldll.mouse_event (0x0800, 0, 0, n, 0)


if len (sys.argv) > 1:
    scroll_down (int (sys.argv[1]))


udp_ip = "localhost" # change to the adapter ip ?
udp_port = 6666

sock = socket.socket (socket.AF_INET,
                      socket.SOCK_DGRAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.bind ((udp_ip, udp_port))

while True:
    data, addr = sock.recvfrom (1024)
    print ("got", data)

    sdata = data.decode ("ascii")
    parts = sdata.split (' ')
    if parts[0].upper() == "SCROLL":
        argv = int (parts[1])
        print ("Argv: [{}]".format (argv))
        scroll_down (argv)
