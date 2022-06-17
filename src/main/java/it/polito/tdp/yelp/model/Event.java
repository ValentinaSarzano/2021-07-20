package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event>{
	
	public enum EventType { //Tipi di eventi da simulare
		DA_INTERVISTARE, //caso III rientra sempre qui
		FERIE,
	}
	//Attributi dell'evento(indipendentemente dal tipo)
	private int giorno ;
	private EventType type ;
	private User intervistato ;
	private Giornalista giornalista ;
	
	public Event(int giorno, EventType type, User intervistato, Giornalista giornalista) {
		super();
		this.giorno = giorno;
		this.type = type ;
		this.intervistato = intervistato;
		this.giornalista = giornalista;
	}

	//Creo i GETTER + ordino per data (giorno)     
	public int getGiorno() {
		return giorno;
	}

	public EventType getType() {
		return type;
	}

	public User getIntervistato() {
		return intervistato;
	}

	public Giornalista getGiornalista() {
		return giornalista;
	}

	@Override
	public int compareTo(Event other) {
		return this.giorno - other.giorno; // ==0 se uguali, != 0 se diversi
	}
	

}