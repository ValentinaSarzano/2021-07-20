package it.polito.tdp.yelp.db;

import java.util.List;

import it.polito.tdp.yelp.model.User;

public class TestDao {

	public static void main(String[] args) {
		
		YelpDao dao = new YelpDao();
		
		//System.out.println(String.format("Users: %d\nBusiness: %d\nReviews: %d\n", 
				//dao.getAllUsers().size(), dao.getAllBusiness().size(), dao.getAllReviews().size()));
		
		List<User> listaUtenti = dao.getUsersWithReviews(200);
		System.out.println(listaUtenti);
		System.out.println("Numero utenti con un numero di recensioni maggiori di 200: "+listaUtenti.size());
	
		
	
	}

}
