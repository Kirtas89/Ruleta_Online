package ruleta;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteRuleta implements Runnable {
	
	protected Socket clientSocket = null;
    private BufferedReader entrada;
	private DataOutputStream salida;
	private int numApostado, cantidadApostada;
	private Croupier croupier;
	
	public ClienteRuleta(Socket clientSocket, Croupier croupier) {
        this.clientSocket = clientSocket;
        this.croupier = croupier;
    }
	
	//GET Y SET
	public int getNumApostado() {
		return numApostado;
	}

	public void setNumApostado(int numApostado) {
		this.numApostado = numApostado;
	}

	public int getCantidadApostada() {
		return cantidadApostada;
	}

	public void setCantidadApostada(int cantidadApostada) {
		this.cantidadApostada = cantidadApostada;
	}

	//METODO RUN
	public void run() {
        try {        	
        	entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			salida = new DataOutputStream(clientSocket.getOutputStream());
			char respuesta;
			int dineroGanado;
            
            //CONTENIDO HILO
			salida.writeUTF("Bienvenido al casino Ruleta On Line");	
			
			do {
				//NUMERO A APOSTAR
				do {
					salida.writeUTF("\n\rIntroduzca el número al que desea apostar (Solo números entre 1 y 36).");
					setNumApostado(Integer.parseInt(entrada.readLine()));
				} while ((getNumApostado() < 1) || (getNumApostado() > 36));
				//CANTIDAD A APOSTAR
				salida.writeUTF("\n\rIntroduzca la cantidad a apostar.");
				setCantidadApostada(Integer.parseInt(entrada.readLine()));
				
				//Registro apuesta y confirmo que se haya aceptado
				if (!croupier.addJugador(this)) {
					salida.writeUTF("\n\rLo sentimos, las apuestas para la siguiente partida ya han sido cerradas.");
					salida.writeUTF("\n\r¿Desea participar en la siguiente?");
					respuesta = entrada.readLine().charAt(0);
					//En caso de no aceptar, se corta la conexión
					if (respuesta == 'N' || respuesta == 'n') {
						salida.writeUTF("\n\rHa sido un placer. Vuelva pronto.");
						salida.close();
						entrada.close();					
					}
				}
				//El cliente espera a que se realice la partida			
				salida.writeUTF("\n\rEspere hasta que se terminen de realizar todas las apuestas.");
				croupier.jugadoresEnEspera();
				
				
				//Se comprueba que número ha sido el ganador y si el cliente ha ganado o no
				salida.writeUTF("\n\rEl número ganador ha sido el " + croupier.getNumeroGanador());
				if (croupier.getNumeroGanador() == this.getNumApostado()) {
					dineroGanado = getCantidadApostada()*36;
					salida.writeUTF("\n\rEnhorabuena. Ha resultado usted ganador. Sus ganancias ascienden a " + dineroGanado + ".");
					croupier.getMinSaldo();
				} else {
					salida.writeUTF("\n\rLo sentimos, más suerte la próxima vez.");
				}
				//Se ofrece jugar en la siguiente partida
				salida.writeUTF("\n\r¿Desea participar en la siguiente partida?");
				respuesta = entrada.readLine().charAt(0);
				//Si se dice que no, se corta la conexión
				if (respuesta == 'N' || respuesta == 'n') {
					salida.writeUTF("\n\rHa sido un placer. Vuelva pronto.");
					salida.close();
					entrada.close();
				}
			} while ((respuesta == 'S' || respuesta == 's'));
			
        } catch (IOException e) {
        	System.out.println("Conexión cerrada");
            //e.printStackTrace();        	
        } finally {
        	try {
				salida.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				entrada.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
