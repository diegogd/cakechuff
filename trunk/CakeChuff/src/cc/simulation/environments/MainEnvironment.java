package cc.simulation.environments;

public class MainEnvironment extends Environment {
	public void run(){
		/** 
		 *  Iniciar Hilos de procesos
		 */
		Environment blisterSub = new BlisterSubsystemEnvironment();
		Environment cakeSub = new CakeSubsystemEnvironment();
		Environment qaSub = new QualitySubsystemEnvironment();
		
		/**  
		 *  Leer estado de activadores
		 *  |
		 *  Accionar Activadores
		 *  |
		 *  Comprobar Sensores
		 *  |
		 *  Notificar Automatas
		 *  |
		 *  Sincronizar hilos (si necesario)
		 */
	}
}
