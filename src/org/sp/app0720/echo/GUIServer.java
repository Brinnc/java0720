package org.sp.app0720.echo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

	public GUIServer() {
		p_north = new JPanel();
		t_port = new JTextField("7777", 10);
		bt = new JButton("서버ON");
		area = new JTextArea();
		scroll = new JScrollPane(area);

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

			Socket socket = server.accept(); // 1인용서버, 여러사람이 접속하려면 accept()를 루프문을 돌려야함
			// 얻어진 소켓은 대화형 소켓이므로 접속한 상대방과의 통신을 위한 스트림을 얻을 수 있음
			BufferedReader buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			area.append("접속자 감지~*\n");

			while (true) {
				String msg = null;
				// 1) 클라이언트의 말을 듣고
				msg = buffr.readLine();
				area.append(msg + "\n");

				// 2) 말하기
				buffw.write(msg + "\n");
				buffw.flush(); // 출력스트림 계열은 flush()해줘야함
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GUIServer();
	}
}
