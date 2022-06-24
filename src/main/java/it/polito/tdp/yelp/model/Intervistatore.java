package it.polito.tdp.yelp.model;

public class Intervistatore {
	private int id;
	private int numIntervistati;
	

	public Intervistatore(int id) {
		super();
		this.id = id;
		this.numIntervistati = 0;
	}

	public int getNumIntervistati() {
		return numIntervistati;
	}

	public void incrementaNumIntervistati() {
		this.numIntervistati ++;
	}

	public int getId() {
		return id;
	}

}
