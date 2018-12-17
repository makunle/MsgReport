import itchat
import socket
import threading
import pickle
import json
import sys
import itchat

key = 'mkey123'
host = 'localhost'
port = 9999

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind((host, port))

running = True

@itchat.msg_register(itchat.content.TEXT)
def text_reply(msg):
    print(msg.fromUserName)


def botSendMsg(msg, to):
    target = itchat.search_friends(nickName=to)[0]['UserName']
    itchat.send(msg, target)
	
def quite():
    global running,server
    running = False
    server.close()
    itchat.logout()
    print('quited')
    sys.exit(0)

def mainService(socket, addr):
    global key

    print('client connect: %s' % str(addr))
    try:
        data = socket.recv(1024)
        if data:
            msg = data.decode("utf-8")
            print("get msg %s from %s" % (msg, str(addr)))

            _json = json.loads(msg)

            _key = _json.get('key', '')
            _msg = _json.get('msg', '')
            _extra = _json.get('extra', '')
            _to = _json.get('to','')

            if(key != _key):
                return

            print('key:%s msg:%s extra:%s' % (_key, _msg, _extra))

            botSendMsg(_msg, _to)

            socket.send('success'.encode())

            if(extra == 'exit'):
               quite()
    except:
        print('connect break with:%s' % str(addr))
    finally:
        socket.close()


def socketServerThread():
    global server, running
    server.listen(15)
    while running:
        sock, addr = server.accept()
        service = threading.Thread(target=mainService, args=(sock, addr))
        service.start()

def botServerThread():
    itchat.auto_login(enableCmdQR=True)
    itchat.run(True)
	
def consleThread():
	while True:
		cmd = input()
		print('cmd %s' % cmd)
		if(cmd == 'exit'):
			quite()
			return


if __name__ == '__main__':
    serverThread = threading.Thread(target=socketServerThread)
    serverThread.start()

    botThread = threading.Thread(target=botServerThread)
    botThread.start()
	
    consleThread = threading.Thread(target=consleThread)
    consleThread.start()
	
