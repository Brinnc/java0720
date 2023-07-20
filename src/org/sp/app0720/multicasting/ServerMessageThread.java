package org.sp.app0720.multicasting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

//다수의 접속 클라이언트들이 각각 독립된 대화를 유지하기 위한 쓰레드 정의
//쓰레드란, 하나의 프로세스 내에서 독립적으로 수행될 수 있는 또 하나의 세부 실행 단위
public class ServerMessageThread extends Thread {
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;

	public ServerMessageThread(Socket socket, JTextArea area) {
		this.area = area;
		// 얻어진 소켓으로부터 스트림을 얻어놓지
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//말하기
	public void sendMsg(String msg) {
		try {
			buffw.write(msg + "\n");
			buffw.flush(); // 출력스트림 계열은 flush()해줘야함
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void run() {
		while (true) {
			// 접속한 클라이언트마다 read() write()를 독립적으로 실행되어야 함 -> 쓰레드
			String msg = null;
			try {
				// 1) 클라이언트의 말을 듣고
				msg = buffr.readLine();
				area.append(msg + "\n");

				/*
				// 2) 말하기 -> 나의 메서드만 호출하지 말고, 나와 동시에 살아가는 다른 쓰레드 인스터스의 메서드도 호출
				for(int i=0;) {
					sendMsg(msg);
					
				}
				*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
