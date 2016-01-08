package ruleta;

public class ConductorRuletaOnLine {

	public static void main(String[] args) {		
		Croupier croupier = new Croupier();
		CroupierMonitor observador = new CroupierMonitor();
		
		croupier.addObserver(observador);
		
		ServidorRuleta server = new ServidorRuleta(croupier);
		server.initServer();
	}
}
