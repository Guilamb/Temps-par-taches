package application;

public enum Etats {
	enCours("c"),Fini("f"), enRetard("r"), Abandonné("a");
	
	private String court;
	private Etats(String court) {
		this.court = court;
	}
}
