package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private Graph<User, DefaultWeightedEdge> grafo;
	private Map<String, User> idMap;
	
	private Simulatore sim;
	
	public Model() {
		super();
		this.dao = new YelpDao();
	}
	
	public void creaGrafo(int n, int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.getVertici(n, idMap);
		
		//AGgiungo i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo gli archi
		for(Adiacenza a: this.dao.getAdiacenze(anno, idMap)) {
			if(this.grafo.containsVertex(a.getU1()) && this.grafo.containsVertex(a.getU2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getU1(), a.getU2(), a.getPeso());
			}
		}
		 System.out.println("Grafo creato!");
		 System.out.println("#VERTICI: "+ this.grafo.vertexSet().size());
		 System.out.println("#ARCHI: "+ this.grafo.edgeSet().size());
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public List<User> getVertici(){
		List<User> vertici = new ArrayList<>(this.grafo.vertexSet());
		
		Collections.sort(vertici, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		return vertici;
	}
	
	public List<UserSimiliarita> getPiuSimili(User user) {
		List<UserSimiliarita> piuSimili = new ArrayList<>();
		int gradoSimiliaritaMax = 0;
		for(User u: Graphs.neighborListOf(this.grafo, user)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(user, u)) > gradoSimiliaritaMax) {
				gradoSimiliaritaMax = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(user, u));
			}
		}
		
		for(User u: Graphs.neighborListOf(this.grafo, user)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(user, u)) == gradoSimiliaritaMax) {
				UserSimiliarita userSim = new UserSimiliarita(u, gradoSimiliaritaMax);
				piuSimili.add(userSim);
			}
		}
		return piuSimili;
	}
	
	public void simula(int x1, int x2) {
		sim = new Simulatore(this.grafo);
		sim.init(x1, x2);
		sim.run();
	}
	
	public List<Intervistatore> getIntervistatori() {
		if(sim == null) {
			return null;
		}
		return this.sim.getIntervistatori();
	}

	public int getNumGiorni() {
		if(sim == null) {
			return -1;
		}
		return this.sim.getNumGiorni();
	}
}
