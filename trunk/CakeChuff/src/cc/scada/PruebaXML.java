package cc.scada;

public class PruebaXML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SCADA s = new SCADA();
		
		System.out.println(s.sendAutomatonInfo1());
		
		s.setValues("ss1Info", "v1", "111");
		s.setValues("ss1Info", "v2", "111");
		s.setValues("ss1Info", "conveyorSpeed", "111");
		
		System.out.println("InfoAutomata 1 cambiada\n" +s.sendAutomatonInfo1());
		
		
		System.out.println(s.sendAutomatonInfo2());
		
		s.setValues("ss2Info", "conveyorSpeed", "888");
		
		System.out.println("InfoAutomata 2 cambiada\n" +s.sendAutomatonInfo2());
		
		
		System.out.println(s.sendAutomatonInfo3());
		
		s.setValues("ss3Info", "conveyorSpeed", "888");
		
		System.out.println("InfoAutomata 3 cambiada\n" +s.sendAutomatonInfo3());
		
		
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		String statistics = s.getStatistics("procesedPackages")+ "#"+
							s.getStatistics("faultyPackages")+ "#"+
							s.getStatistics("totalCakes");
		System.out.println("Estadisticas:" );
		System.out.println(statistics);
		
		s.setStatistics("totalCakes", "12564984200");
		s.setStatistics("procesedPackages", "12564984200");
		s.setStatistics("faultyPackages", "12564984200");
		System.out.println();
		
		statistics = s.getStatistics("procesedPackages")+ "#"+
							s.getStatistics("faultyPackages")+ "#"+
							s.getStatistics("totalCakes");
		System.out.println("Estadisticas cambiadas:" );
		System.out.println(statistics);
	}

}
