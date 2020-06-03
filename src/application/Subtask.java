package application;

import java.time.LocalDate;

public class Subtask extends Task{
	private static int idIterator = 0;
	private final int ID = idIterator;
	private LocalDate dateDebut;
	private String description;
	private Etats statut;
	private boolean enCours = false;
	private int father;
	private String name;
	
	public Subtask() {
		this.name = "tâche N°" + ID;
		this.dateDebut = LocalDate.now();
		this.description = "tâche N°" + ID;
		this.statut = Etats.enCours;
		idIterator++;
	}
	public Subtask(String name) {
		super(name);
		super.setDescription("sous tâche N°"+ID);
		idIterator++;
	}
	public Subtask(LocalDate dateDebut) {
		super(dateDebut);
		super.setDescription("sous tâche N°"+ID);
		idIterator++;

	}
	
	public Subtask(String name,LocalDate dateDebut, String description) {
		super(name,dateDebut, description);
		idIterator++;

	}
	public Subtask(String name, LocalDate dateDebut, String description, Etats statut) {
		super(name,dateDebut, description, statut);
		idIterator++;
		
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEnCours() {
		return enCours;
	}
	public void setEnCours(boolean enCours) {
		this.enCours = enCours;
	}
	public int getID() {
		return ID;
	}
	public LocalDate getDateDebut() {
		return dateDebut;
	}
	public int getFATHER() {
		return father;
	}
	public void setStatut(Etats statut) {
		this.statut = statut;
	}

	public Etats getStatut() {
		return statut;
	}

	public void setFather(int father) {
		this.father = father;
	}

	@Override
	public String toString() {
		return "Subtask [ID=" + ID + ", dateDebut=" + super.getDateDebut() + ", description=" + super.getDescription() + ", statut=" + super.getStatut()+ ", enCours=" + super.isEnCours() + ", FATHER=" + this.father + "]";
	}
	public String showToString() {
		return this.name;
	}
}
