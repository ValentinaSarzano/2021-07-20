package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<User, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private Map<String, User> idMapUser;
	private List<User> listaUtenti;
	
	public Model() {
		this.dao = new YelpDao();
	}
	
	public String creaGrafo(int n, int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); //Non orientato e semplice
	    this.idMapUser = new HashMap<>();
	    
	    //Popolo la idMapUsers con TUTTI GLI UTENTI
	    for(User u: dao.getAllUsers()) {
	    	idMapUser.put(u.getUserId(), u);
	    }
	    
	    this.listaUtenti = dao.getUsersWithReviews(n); //Lista riempita con utenti che hanno fatto almeno un numero n di revisioni
	    
	    //Aggiungo i vertici
	    Graphs.addAllVertices(this.grafo, listaUtenti);
	
	    //Aggiungo gli archi
	    for(User u1: this.listaUtenti) {
	    	for(User u2: this.listaUtenti) {
	    		if(!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId()) < 0) { //u1.getUserId().compareTo(u2.getUserId()) < 0 se la stringa di u1 è minore della stringa di u2 scarto i doppioni (ovvero il doppio controllo che necessariamente fa a causa del ciclo for annidato) 
	    			int revisioniInComune = dao.getPeso(u1, u2, anno);
	    			if(revisioniInComune > 0) {
	    				//aggiungo l'arco con peso = revisioniInComune
	    				Graphs.addEdge(this.grafo, u1, u2, revisioniInComune);
	    			}
	    			
	    		}
	    	}
	    }
	    
	    /*System.out.println("Grafo creato.\n");
	    System.out.println("#VERTICI: " + grafo.vertexSet().size());
	    System.out.println("#ARCHI: "+ grafo.edgeSet().size());
	    */
	
	    return "Grafo creato con "+ this.grafo.vertexSet().size() + " vertici e "+ this.grafo.edgeSet().size() + " archi.\n";
	}
	
	public List<User> getUsersWithReviews(){
		return this.listaUtenti;
	}
	
	public List<User> utentiPiuSimili(User u){
		List<User> result = new ArrayList<>();//Dato un utente devo prendermi tutti gli adiacenti, i pesi degli archi
    	                                      //e vedere qual'è il peso maggiore e restituire una lista con tutti gli adiacenti
    	                                      //con quel peso
		int pesoMax = 0;
		
		//Ciclo sugli archi adiacenti a u
		for(DefaultWeightedEdge e : this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)> pesoMax) {
				pesoMax = (int) this.grafo.getEdgeWeight(e);
			}
		}
		
		for(DefaultWeightedEdge e : this.grafo.edgesOf(u)) {
			if((int)this.grafo.getEdgeWeight(e) == pesoMax) {
				User vicino = Graphs.getOppositeVertex(this.grafo, e, u);
				result.add(vicino);
			}
	    }
		return result;
	}
	
}
