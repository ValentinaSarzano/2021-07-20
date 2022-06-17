package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulatore {

	// Dati in ingresso
	private int x1;
	private int x2;
	
	// Dati in uscita
	
	// i giornalisti sono rappresentati da un numero compreso tra 0 e x1-1
	private List<Giornalista> giornalisti; 
	private int numeroGiorni; //tempo max di simulazione (per portare a termine l'analisi di mercato)
	
	// Modello del mondo: dovro sapere in ogni istante ogni giornalista chi sta intervistando e chi sono gli utenti gia intervistati e chi no
	private Set<User> intervistati; //quando li aggiungo devo fare in modo che questi utenti non vengano mai piu selezionati
	private Graph<User,DefaultWeightedEdge> grafo; //Lo memorizzo anche se non devo farlo effettivamente evolvere perchè servirà ai vari metodi
	
	// Coda degli eventi
	private PriorityQueue<Event> queue ;
	
	public Simulatore(Graph<User,DefaultWeightedEdge> grafo) {
		this.grafo = grafo ;
		
	}
	
	public void init(int x1, int x2) {
		this.x1 = x1;
		this.x2 = x2 ;
		
		this.intervistati = new HashSet<User>() ;
		
		this.numeroGiorni = 0 ;
		
		this.giornalisti = new ArrayList<>() ;
		for(int id = 0; id<=this.x1-1; id++) {
			this.giornalisti.add(new Giornalista(id)); //Creo i giornalisti, ciascuno dei quali inizialmente avrà 0 intervistati
		}
		
		// pre-carico la coda con le interviste del primo giorno: ATTENZIONE --> una volta che ho selezionato un intervistato non dovra piu essere possibile estrarlo nell'estrazione successiva (lo metto nella "blacklist" di persone da non prendere piu, ovvero nella lista di intervistati)
		// Per ogni giornalista metto nella coda l' intervista che deve fare il giorno successivo 
		for(Giornalista g: this.giornalisti) {
			User intervistato = selezionaIntervistato(this.grafo.vertexSet()) ; //Estrae a caso uno degli utenti ancora intervistabili (inizialmente tutti)
			
			this.intervistati.add(intervistato);
			g.incrementaNumeroIntervistati();
			
			//Tu, giornalista g, intervista questo utente domani (giorno 1)
			this.queue.add( new Event(1, EventType.DA_INTERVISTARE, intervistato, g));
		}
		
		
	}
	
	public void run() { // ELABORAZIONE DELLA CODA + CONDIZIONI DI TERMINAZIONE
		
		while(!this.queue.isEmpty() && this.intervistati.size()<x2) { //Finchè la coda non è vuota e finchè non ho intervistato almeno x2 persone --> vado avanti
			Event e = this.queue.poll() ; //Estraggo l'evento
			this.numeroGiorni = e.getGiorno(); //aggiorno la durata della simulazione (numero giorni della simulazione = numero giorni dell'ultimo elemento che ho estratto, il valore massimo --> visto che il tempo è crescente sicuramente l'ultimo che estraggo è il valore finale)
			
			processEvent(e); //Elaboro l'evento
		}
		
	}
	
	

	private void processEvent(Event e) {
		switch(e.getType()) {
		
		case DA_INTERVISTARE:
			
			double caso = Math.random();
			
			if(caso<0.6) {
				// caso I
				User vicino = selezionaAdiacente(e.getIntervistato());
				
				if(vicino == null) {
					vicino = selezionaIntervistato(this.grafo.vertexSet());
				}
				
				this.queue.add( new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE,vicino, e.getGiornalista())) ;
				
				this.intervistati.add(vicino);
				e.getGiornalista().incrementaNumeroIntervistati();
				
			} else if(caso < 0.8) {
				// caso II: 0.6 + 0.2 --> in entrambi i casi porta a termine l'intervista (caso I e caso II)
				this.queue.add(new Event(e.getGiorno()+1, EventType.FERIE, e.getIntervistato(), e.getGiornalista()));
			} else {
				// caso III: domani continuo con lo stesso utente perchè non sono riuscito a portare a termine l'intervista
				this.queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, e.getIntervistato(), e.getGiornalista()));
			}
			
			break;
		case FERIE:
			break;
		
		}
		
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	public int getNumeroGiorni() {
		return numeroGiorni;
	}
	
	/**
	 * Seleziona un intervistato dalla lista specificata, evitando 
	 * di selezionare coloro che sono già in this.intervistati
	 * @param lista
	 * @return
	 */
	private User selezionaIntervistato(Collection<User> lista) {
		Set<User> candidati = new HashSet<User>(lista); //Metto dentro al set la lista
		candidati.removeAll(this.intervistati); //Rimuovo tutti quelli che sono gia stati intervistati ottenendo l'insieme dei POTENZIALI CANDIDATI
		
		int scelto = (int)(Math.random()*candidati.size()); //Posizione dell'utente scelto 
		//Essendo candidati un set non ha un metodo get quindi devo prima trasformarlo in una lista per poi prendere l'iesimo elemento (con i = scelto)
		return (new ArrayList<User>(candidati)).get(scelto) ;
	}
	
	private User selezionaAdiacente(User u) {
		List<User> vicini = Graphs.neighborListOf(this.grafo, u);
		vicini.removeAll(this.intervistati);
		
		if(vicini.size()==0) {
			// vertice isolato
			// oppure tutti adiacenti già intervistati
			return null;
		}
		//Ora ho i vicini potenzialmente intervistabili
		double pesoMax = 0;
		for(User v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(u, v)); 
			if(peso > pesoMax)
				pesoMax = peso ;
		}
		//Creo una lista perche potrei trovare piu vicini con lo stesso pesoMax che quindi vanno bene 
		List<User> migliori = new ArrayList<>();
		for(User v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(u, v)); 
			if(peso == pesoMax) {
				migliori.add(v) ;
			}
		}
		//Scelgo a caso tra questi
		int scelto = (int)(Math.random()*migliori.size()); //Posizione dell'utente selezionato all'interno della lista "migliori"
		return migliori.get(scelto);
		
		
	}
	
}