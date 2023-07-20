package org.sp.app0720.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class ServerMain extends JFrame{

	//클라이언트의 접속을 받을 수 있는 객체를 서버소켓이라고 함
	//포트번호: 네트워크 프로그램 간의 구분번호 || 0~1024 > 시스템이사용하는 포트번호기때문에 사용x
	ServerSocket server;
	
	public ServerMain() {
		try {
			//1) 소켓 서버 생성
			server=new ServerSocket(7777);
			System.out.println("서버 생성");
			
			//접속자가 감지되면 소켓이 반환됨
			//접속한 이유가 대화를 나누기 위함이므로, 접속이 감지되었을 때 소켓이 반환되는 것은 당연함
			Socket socket=server.accept(); //다른 네트워크의 접속을 감지하는 메서드
			
			InetAddress add=socket.getInetAddress();
			String ip=add.getHostAddress();
			System.out.println("접속한 컴의 IP "+ip);
			
			//대화를 나누기 위한 스트림 얻기
			InputStream is=socket.getInputStream(); //접속한 자와 연관된 스트림을 얻을 수 있음
			InputStreamReader reader=new InputStreamReader(is);
			BufferedReader buffr=new BufferedReader(reader);
			String msg=null;
			while(true) {
				msg=buffr.readLine();
				//int data=reader.read(); //데이터가 읽혀지기 전까지 대기상태에 빠짐 
				System.out.println("받은 메세지 : "+msg);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}
}
