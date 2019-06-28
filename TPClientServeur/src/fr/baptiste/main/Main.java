package fr.baptiste.main;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		
		
		String host = "127.0.0.1";
		int port = 5000;
		int port2 = 5001;
		int port3 = 5005;
		int port4 = 5003;
		ArrayList<String> hosts = new ArrayList<String>();
		ArrayList<Integer> ports = new ArrayList<Integer>();
		for(int i = 5000; i < 5010; i++){
			hosts.add(host);
			ports.add(i);			
			ServerPi ts = new ServerPi(host, i);
			ts.open();
		}
//		
//		ServerPi ts = new ServerPi(host, port);
//		ts.open();
//		ServerPi tserve = new ServerPi(host, port2);
//		tserve.open();
//		ServerPi ts3 = new ServerPi(host, port3);
//		ts3.open();
//		ServerPi tserve4 = new ServerPi(host, port4);
//		tserve4.open();
		System.out.println("Serveurs initialisés.");
		
		Thread th = new Thread(new ClientUnique(hosts,ports));
		th.start();

	}
}