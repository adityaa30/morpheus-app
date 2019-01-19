import socket
import ssl
from urllib.request import urlopen

host = socket.gethostname()
port = 8000
sock = socket.socket()
sock.connect((host, port))

# start the video stream
CONTEXT =