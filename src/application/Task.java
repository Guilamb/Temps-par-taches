package application;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Task implements Serializable{
	
	private static int idIterator = 0;
	private final int ID = idIterator;
	private LocalDate dateDebut;
	private String description;
	private Etats statut;
	private boolean enCours = false;
	private ArrayList<Subtask> subtasks = new ArrayList<Subtask>();
	private final int SIZE = 10; //nb maximum de sous taches
	private List<Subtask> data;
	private int dureeEnMinutes;
	private String name;

	
	public Task() {
		this.name = "tâche N°" + ID;
		this.dateDebut = LocalDate.now();
		this.description = "tâche N°" + ID;
		this.statut = Etats.enCours;
		idIterator++;
	}
	public Task(String name) {
		this.name = name;
		this.dateDebut = LocalDate.now();
		this.description = "tâche N°" + ID;
		this.statut = Etats.enCours;
		idIterator++;
	}
	public Task(LocalDate dateDebut) {
		this.name = "tâche N°" + ID;
		this.dateDebut = dateDebut;
		this.description = "tâche N°" + ID;
		this.statut = Etats.enCours;
		idIterator++;
	}
	public Task(String name,LocalDate dateDebut, String description) {
		this(dateDebut);
		this.name = name;
		this.description = description;
		this.statut = Etats.enCours;
		idIterator++;
	}
	public Task(String name,LocalDate dateDebut, String description, Etats statut) {
		this(name,dateDebut,description);
		this.statut = statut;
		idIterator++;
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Etats getStatut() {
		return statut;
	}
	public void setStatut(Etats statut) {
		this.statut = statut;
	}
	public boolean isEnCours() {
		return enCours;
	}
	public void setEnCours(boolean enCours) {
		this.enCours = enCours;
	}
	public int getId() {
		return ID;
	}
	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public int getDureeEnMinutes() {
		return dureeEnMinutes;
	}
	public void setDureeEnMinutes(int dureeEnMinutes) {
		this.dureeEnMinutes = dureeEnMinutes;
	}

	@Override
	public String toString() {
		return "Task [ID=" + ID + ", dateDebut=" + dateDebut + ", description=" + description + ", statut=" + statut
				+ ", enCours=" + enCours + ", subtasks=" + subtasks.toString() + "]";
	}
	public List getSubtask() {
		return subtasks;
	}
	public List getSubtaskNames() {
		List<String> tmp = new ArrayList<String>();
		for(Subtask t : subtasks) {
			tmp.add(t.getName());
		}
		return tmp;
	}
	
	public void addSubtask(Subtask task) {
		this.subtasks.add(task);
		task.setFather(this.ID);
	}
	  

}
