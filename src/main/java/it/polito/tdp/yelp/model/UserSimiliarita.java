package it.polito.tdp.yelp.model;

public class UserSimiliarita {
	private User u;
	private int gradoSimiliarita;
	
	public UserSimiliarita(User u, int gradoSimiliarita) {
		super();
		this.u = u;
		this.gradoSimiliarita = gradoSimiliarita;
	}

	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	public int getGradoSimiliarita() {
		return gradoSimiliarita;
	}

	public void setGradoSimiliarita(int gradoSimiliarita) {
		this.gradoSimiliarita = gradoSimiliarita;
	}
	

	
}
