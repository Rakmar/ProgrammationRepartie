package fr.baptiste.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientUnique implements Runnable {
	private Socket connexion = null;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	// Notre liste de commandes. Le serveur nous répondra différemment selon la
	// commande utilisée.
	private String[] listCommands = { "FULL", "DATE", "HOUR", "NONE" };
	private static int count = 0;
	private String name = "Client-";

	private ArrayList<Long> tableResponse = null;
	private ArrayList<Socket> connexions = null;

	long total = 0;
	int totalCount = 0;

	int numWorkers = 16000000;

	public ClientUnique(List<String> hosts, List<Integer> ports) {
		name += ++count;
		totalCount = hosts.size();
		connexions = new ArrayList<Socket>();
		tableResponse = new ArrayList<Long>();
		try {
			for (String host : hosts) {
				int compteur = 0;
				connexions.add(new Socket(host, ports.get(compteur)));
				compteur++;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {

			for (Socket connexion : connexions) {
				writer = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());
				// On envoie la commande au serveur

				String commande = getCommand();
				writer.write("" + numWorkers);
				// TOUJOURS UTILISER flush() POUR ENVOYER RÉELLEMENT DES INFOS
				// AU SERVEUR
				writer.flush();

				// On attend la réponse
				String response = read();

				tableResponse.add(Long.parseLong(response));
				total += Long.parseLong(response);

				System.out.println(tableResponse);
				writer.write("CLOSE");
				writer.flush();
				writer.close();
			}
			double pi = 4.0 * total / totalCount / numWorkers;
			System.out.println("PI : " + pi);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// Méthode qui permet d'envoyer des commandeS de façon aléatoire
	private String getCommand() {
		Random rand = new Random();
		return listCommands[rand.nextInt(listCommands.length)];
	}

	// Méthode pour lire les réponses du serveur
	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}

	public static void main(String[] args) {
		String host = "127.0.0.1";
		ArrayList<String> hosts = new ArrayList<String>();
		ArrayList<Integer> ports = new ArrayList<Integer>();
		for (int i = 5000; i < 5010; i++) {
			hosts.add(host);
			ports.add(i);
		}
		Thread th = new Thread(new ClientUnique(hosts, ports));
		th.start();
	}
}
