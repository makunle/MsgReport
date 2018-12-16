import socket, threading, pickle
from wxpy import *

serverAddr = ('173.82.114.224', 9999)

me = None
bot = None

tcpServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcpServer.bind(serverAddr)


def mainService(socket, addr):
    global bot
    print('client connect: %s' % str(addr))
    try:
        data = socket.recv(1024)
        if data:
            print('get msg: %s' % str(data))
            socket.close()

            me = bot.friends()[0]
            me.send(str(data))

            if(str(data) == 'exit'):
                exit()
    except:
        print('connect break: %s' % str(addr))


def mainServerThread():
    global tcpServer, bot, me

    bot = Bot()
    me = bot.friends()[0]
    me.send('login')

    tcpServer.listen(15)
    while True:
        sock, addr = tcpServer.accept()
        service = threading.Thread(target=mainService, args=(sock, addr))
        service.start()

if __name__ == '__main__':
    mainThread = threading.Thread(target=mainServerThread)
    mainThread.start()
