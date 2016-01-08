package ruleta;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorRuleta {
	
	private final int PUERTO = 5000;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private Croupier croupier = null;
	
	public ServidorRuleta() {
		this.croupier = new Croupier();
	}
	
	public ServidorRuleta(int limiteMaximo, int minSaldo, boolean ApuestasActivas) {
		this.croupier = new Croupier(limiteMaximo, minSaldo, ApuestasActivas);
	}
	
	public ServidorRuleta(Croupier croupier) {
		this.croupier = croupier;
	}
	
	public void initServer() {
		try {
			//Crea socket servidor para escuchar en puerto 5000
			serverSocket = new ServerSocket(PUERTO);
			//socket = new Socket();
			System.out.println("Esperando una conexión:");
			//Inicia el socket, ahora está esperando una conexión por parte del cliente
			while(true){
				socket = serverSocket.accept();
				
				System.out.println("Cliente conectado");
				
				Thread a = new Thread(new ClienteRuleta(socket, croupier));//CREO THREAD
				a.start();//EJECUTO THREAD	
			}//FIN_WHILE								
		}//FIN_TRY
		catch(Exception ex){
			System.out.println("Error: " + ex.getMessage());
		}
		finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
