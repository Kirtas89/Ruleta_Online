package ruleta;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Croupier extends Observable {
	
	private int limiteMaximo, minSaldo, saldoBanca, numGanador;
	private boolean ApuestasActivas;
	private ArrayList<ClienteRuleta> jugadores;
	
	//Constructor por defecto
	public Croupier() {
		this.limiteMaximo = 1000;
		this.minSaldo = limiteMaximo*2;
		this.ApuestasActivas = true;
		this.jugadores = new ArrayList<>();
	}
	
	//Constructor por parámetros
	public Croupier(int limiteMaximo, int minSaldo, boolean ApuestasActivas) {
		this.limiteMaximo = limiteMaximo;
		this.minSaldo = minSaldo;
		this.ApuestasActivas = ApuestasActivas;
		this.jugadores = new ArrayList<>();
	}

	//GET Y SET
	public int getLimiteMaximo() {
		return limiteMaximo;
	}

	public void setLimiteMaximo(int limiteMaximo) {
		this.limiteMaximo = limiteMaximo;
	}

	public int getMinSaldo() {
		return minSaldo;
	}

	public void setMinSaldo(int minSaldo) {
		this.minSaldo = minSaldo;
	}

	public int getSaldoBanca() {
		return saldoBanca;
	}

	public void setSaldoBanca(int saldoBanca) {
		this.saldoBanca = saldoBanca;
	}
	
	public int getNumeroGanador() {
		return numGanador;
	}

	public boolean isApuestasActivas() {
		return ApuestasActivas;
	}

	public void setApuestasActivas(boolean apuestasActivas) {
		ApuestasActivas = apuestasActivas;
	}

	public ArrayList<ClienteRuleta> getJugadores() {
		return jugadores;
	}
	
	/** boolean addJugador(ClienteRuleta jugador)
	 * 
	 * Comentario: Método para añadir un jugador con su apuesta a la partida
	 * Precondición: Nada
	 * Entrada: Un hilo que representa un jugador y su apuesta
	 * Salida: Un boolean que indica si el jugador ha sido añadido o no a la partida
	 * Postcondición: El jugador queda registrado en la partida junto a su apuesta
	 * 
	 */
	public synchronized boolean addJugador(ClienteRuleta jugador) {
		boolean apuestaAceptada = false;
		if (ApuestasActivas) {
			this.jugadores.add(jugador);
			apuestaAceptada = true;
			setChanged();
			notifyObservers(this);
		}
		return apuestaAceptada;
	}
	
	/** int calculaNumeroAgraciado()
	 * 
	 * Comentario: Función que calcula el número ganador de la partida
	 * Precondición: Nada
	 * Entrada: Nada
	 * Salida: Un entero que representa el número ganador
	 * Postcondición: Se devuelve un entero asociado al nombre cuyo valor es el número agraciado de la partida
	 * 
	 */
	public int calculaNumeroAgraciado() {
		int res;
		Random tirada = new Random(35);
		res = tirada.nextInt();
		
		return res;
	}
	
	/** void aJugar()
	 * 
	 * Comentario: Metodo que calcula el número ganador y lo comunica a los jugadores
	 * Precondición: Nada
	 * Entrada: Nada
	 * Salida: Nada
	 * Postcondición: Se ha calculado el número ganador y la partida ha acabado
	 * 
	 */
	public void aJugar() {
		this.numGanador = calculaNumeroAgraciado();
		notifyAll();		
	}
	
	/** void pagarPremios(int premio)
	 * 
	 * Comentario: Metodo para descontar la cantidad ganada de todos los jugadores de la partida
	 * Precondicion: Nada
	 * Entrada: Un entero que representa el premio a pagar
	 * Salida: Nada
	 * Postcondición: El saldo de la banca queda actualizado
	 * 
	 */
	public void pagarPremios(int premio) {		
		setSaldoBanca(getSaldoBanca() - premio);
		setChanged();
		notifyObservers(this);
	}
	
	/** void jugadoresEnEspera()
	 * 
	 *  Comentario: Metodo para poner a los jugadores en espera
	 */
	public synchronized void jugadoresEnEspera() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
