package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		DA_INTERVISTARE,
		FERIE
	}
	
	private int giorno;
	private EventType type;
	private User user;
	private Intervistatore i;
	
	public Event(int giorno, EventType type, User user, Intervistatore i) {
		super();
		this.giorno = giorno;
		this.type = type;
		this.user = user;
		this.i = i;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	public Intervistatore getIntervistatore() {
		return i;
	}

	public void setIntervistatore(Intervistatore i) {
		this.i = i;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno-o.giorno;
	}
	
	

}
