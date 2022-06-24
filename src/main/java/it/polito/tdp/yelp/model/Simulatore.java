package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulatore {
	
	//Parametri in ingresso
	private int x1; //num intervistatori disponibili: sempre << x2
	private int x2; // num utenti da intervistare: sempre <= this.grafo.vertexSet().size()
	
	//Parametri in uscita
	private int numGiorni;
	private List<Intervistatore> intervistatori;
	
	//Stato del mondo
	private List<User> utentiIntervistati;
    
	private Graph<User, DefaultWeightedEdge> grafo;
	
	//Coda degli eventi
	private PriorityQueue<Event> queue;

	public Simulatore(Graph<User, DefaultWeightedEdge> grafo) {
		super();
		this.grafo = grafo;
	}



	public void setX1(int x1) {
		this.x1 = x1;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public void init(int x1, int x2) {
		this.x1= x1;
		this.x2 = x2;
		
		this.utentiIntervistati = new ArrayList<>();
		this.intervistatori = new ArrayList<>();
		this.numGiorni = 0;
		
		this.queue = new PriorityQueue<>();
		
		for(int id = 0; id< x1; id++) {
			intervistatori.add(new Intervistatore(id));
		}
		
		for(Intervistatore i: intervistatori) { //PRE-CARICO LA CODA
			User intervistato = selezionaIntervistato(this.grafo.vertexSet());
			utentiIntervistati.add(intervistato);
			i.incrementaNumIntervistati();
			this.queue.add(new Event(1, EventType.DA_INTERVISTARE, intervistato, i));
			
		}
	}

	public void run() {
		while(!this.queue.isEmpty() && this.utentiIntervistati.size() < x2) { //Estraggo il primo evento e lo processo, studiandone le possibili conseguenze
			Event e = this.queue.poll();
			this.numGiorni = e.getGiorno();
			processEvent(e);
		}
	}

	private void processEvent(Event e) { //Processo il primo evento: giorno 0, DA_INTERVISTARE, utente , x1 = 0

		switch(e.getType()) {
		case DA_INTERVISTARE:
			if(Math.random() < 0.6) { // 60% dei casi
				//Scelgo tra gli utenti disponibili quale intervistare (grafo di similiarita maggiore)
				User prossimo = selezionaAdiacente(e.getUser());
				if(prossimo == null) {
					prossimo = selezionaIntervistato(this.grafo.vertexSet());
				}
				this.queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, prossimo, e.getIntervistatore()));
				this.utentiIntervistati.add(prossimo);
				e.getIntervistatore().incrementaNumIntervistati();
				
			}else if(Math.random() < 0.8) { 
				this.queue.add(new Event(
						e.getGiorno()+1,
						EventType.FERIE ,
						e.getUser(),
						e.getIntervistatore()
						));
			}else {// 20% dei casi: non è riuscito a portare a termine l'intervista,
                   // intervista il giorno successivo sempre lo stesso utente 
                   //(che c'era nell'evento del giorno prima)
				
                 this.queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE , e.getUser(), e.getIntervistatore()));

			}
			break;
			
			
			
		case FERIE: //LA CONSEGUENZA DELLE FERIE è UNA NUOVA INTERVISTA IL GIORNO SUCCECSSIVO!!!!!
			User vicino = selezionaAdiacente(e.getUser());
			if(vicino == null) {
				vicino = selezionaIntervistato(this.grafo.vertexSet());
			}
			
			this.queue.add( new Event(
					e.getGiorno()+1,
					EventType.DA_INTERVISTARE,
					vicino,
					e.getIntervistatore()
					)) ;
			
			this.utentiIntervistati.add(vicino);
			e.getIntervistatore().incrementaNumIntervistati();
			break;
		}
		
	}
	
	public List<UserSimiliarita> getUtenteSimile(User u) {
		List<UserSimiliarita> piuSimili = new ArrayList<>();
		int pesoMax = 0;
		for(User user: Graphs.neighborListOf(this.grafo, u)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(user, u)) > pesoMax) { //Scorro sui collegati e mi salvo il peso massimo
				pesoMax = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(user, u)); 
			}
		}
		//Qui ho il peso massimo
		for(User user: Graphs.neighborListOf(this.grafo, u)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(user, u)) == pesoMax) { //Aggiungo tutti quelli con oesi massimo alla lista di simili
				piuSimili.add(new UserSimiliarita(user, pesoMax));
			}
		}
		return piuSimili;
		}

	
	private User selezionaIntervistato(Collection<User> lista) {
		Set<User> candidati = new HashSet<User>(lista);
		candidati.removeAll(this.utentiIntervistati);
		
		if (candidati.size()==0)
			return null;
		
		int scelto = (int)(Math.random()*candidati.size());
		
		return (new ArrayList<User>(candidati)).get(scelto) ;
	}
	
	private User selezionaAdiacente(User u) {
		List<User> vicini = Graphs.neighborListOf(this.grafo, u);
		vicini.removeAll(this.utentiIntervistati);
		
		if(vicini.size()==0) {
			// vertice isolato
			// oppure tutti adiacenti già intervistati
			return null ;
		}
		
		double max = 0;
		for(User v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(u, v)); 
			if(peso > max)
				max = peso ;
		}
		
		List<User> migliori = new ArrayList<>();
		for(User v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(u, v)); 
			if(peso == max) {
				migliori.add(v) ;
			}
		}
		
		int scelto = (int)(Math.random()*migliori.size()) ;
		return migliori.get(scelto);
	}
	
	public List<Intervistatore> getIntervistatori() {
		return this.intervistatori;
	}

	public int getNumGiorni() {
		return numGiorni;
	}
}