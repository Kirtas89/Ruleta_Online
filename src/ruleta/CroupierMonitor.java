package ruleta;

import java.util.Observable;
import java.util.Observer;

public class CroupierMonitor implements Observer {

	@Override
	public void update(Observable croupierObservado, Object arg1) {
		Thread a = new Thread(() -> {
			Croupier croupier = (Croupier) croupierObservado;
			int apuestaTotal;
			//Condicional para decidir que hace el hilo, si las ApuestasActivas están a TRUE, significa que aun se está apostando
			//y necesito controlar el limite de apuestas. En caso de que las ApuestasActivas estén a FALSE, significará que la partida
			//ha acabado y solo necesito que el update() controle el saldo de la banca.
			if (croupier.isApuestasActivas()) {
				//Calculo la cantidad de dinero apostada hasta el momento
				for (int i = 0; i < croupier.getJugadores().size(); i++) {
					apuestaTotal = croupier.getJugadores().get(i).getCantidadApostada();
					//Si la apuesta supera el límite máximo por partida, se cierran las apuestas
					if (apuestaTotal > croupier.getLimiteMaximo()) {
						croupier.setApuestasActivas(false);					
					}
				}
				//Compruebo que el saldo de la banca no ha bajado del mínimo permitido
			} else if (croupier.getMinSaldo() < croupier.getSaldoBanca()) {
				notifyAll();
			}		
		});
		a.start();
	}
}
