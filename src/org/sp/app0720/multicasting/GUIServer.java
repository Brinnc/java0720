package org.sp.app0720.multicasting;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUIServer extends JFrame {
	JPanel p_north;
	JTextField t_port;
	JButton bt;
	JTextArea area;
	JScrollPane scroll;
	ServerSocket server; // 대화용이 아닌 접속자 감지용 소켓

	Thread thread; // 메인 쓰레드가 대기 상태에 빠지지 않도록 하기위한 별도의 쓰레드
	
	Vector<ServerMessageThread> vec; //자바의 컬렉션프레임웤 중 순서있는 객체를 다루는 리스트의 자식 (=arrayList)
													//동기화를 지원하므로, 멀티쓰레드에서 안정적

	public GUIServer() {
		p_north = new JPanel();
		t_port = new JTextField("7777", 10);
		bt = new JButton("서버ON");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		vec=new Vector();

		// 조립
		p_north.add(t_port);
		p_north.add(bt);
		add(p_north, BorderLayout.NORTH);
		add(scroll);

		setSize(300, 400);
		setVisible(true);
		setBounds(500, 200, 300, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 서버 가동
				thread = new Thread() {
					public void run() {
						runServer();

					}
				};
				thread.start();
			}
		});
	}

	// 서버 가동하기
	public void runServer() {

		int port = Integer.parseInt(t_port.getText());
		try {
			server = new ServerSocket(port); // 서버생성
			area.append("서버 생성\n");
			area.append("서버 가동 및 접속자 감지중...\n");

			while (true) {
				// 얻어진 소켓은 대화형 소켓이므로 접속한 상대방과의 통신을 위한 스트림을 얻을 수 있음
				//클라이언트가 접속을 하면,
				Socket socket = server.accept(); // 1인용서버, 여러사람이 접속하려면 accept()를 루프문을 돌려야함
				area.append("접속자 감지~*\n"); 
				
				ServerMessageThread smt=new ServerMessageThread(socket, area);
				smt.start();
				
				//접속자 명단에 대화용 쓰레드를 추가
				vec.add(smt);
				area.append("현재 접속자 수 "+vec.size()+"명\n");
				
				/* -> 쓰레드로 옮김
				BufferedReader buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				area.append("접속자 감지~*\n"); 

				//접속한 클라이언트마다 read() write()를 독립적으로  실행되어야 함 -> 쓰레드 
				String msg = null;
				// 1) 클라이언트의 말을 듣고
				msg = buffr.readLine();
				area.append(msg + "\n");

				// 2) 말하기
				buffw.write(msg + "\n");
				buffw.flush(); // 출력스트림 계열은 flush()해줘야함
				*/
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GUIServer();
	}
}
