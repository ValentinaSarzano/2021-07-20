/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Intervistatore;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
import it.polito.tdp.yelp.model.UserSimiliarita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	int n = 0;
    	try {
    		n = Integer.parseInt(txtN.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("ERRORE: Inserire un numero valido minimo di precensioni pubblicate nel campo di testo!\n");
    	    return;
    	}
    	Integer anno = cmbAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("ERRORE: Selezionare un anno dal menu a tendina!\n");
    		return;
    	}
    	this.model.creaGrafo(n, anno);
    	btnUtenteSimile.setDisable(false);
    	cmbUtente.getItems().addAll(this.model.getVertici());
    	
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("#VERTICI: "+ this.model.nVertici()+"\n");
    	txtResult.appendText("#ARCHI: "+ this.model.nArchi()+"\n");
    	
    	
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	txtResult.clear();
    	btnSimula.setDisable(false);
    	User u = cmbUtente.getValue();
    	if(u == null) {
    		txtResult.appendText("ERRORE: Selezionare un utente dal menu a tendina!\n");
    	}
    	List<UserSimiliarita> piuSimili = new ArrayList<>(this.model.getPiuSimili(u));
    	txtResult.appendText("UTENTI PIU SIMILI A " + u + ":\n\n");
    	for(UserSimiliarita userSim: piuSimili) {
    		txtResult.appendText(userSim.getU() + "  GRADO:" + userSim.getGradoSimiliarita() + "\n");
    	}
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	if(!model.grafoCreato()) {
    		txtResult.appendText("ERRORE: crea prima il grafo selezionando un anno dal menù a tendina e inserendo un numero n di recensioni!\n");
    	}
    	int x1 = 0;
    	int x2 = 0;
    	try{
    		x1 = Integer.parseInt(txtX1.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("ERRORE: inserire un numero intero valido!\n");
    	}
    	try{
    		x2 = Integer.parseInt(txtX2.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("ERRORE: inserire un numero intero valido!\n");
    	}
    	if(x2 > this.model.getVertici().size()) {
    		txtResult.appendText("ERRORE: inserire un numero intero minore di " + this.model.getVertici().size()+ "!\n");
    	}
    	this.model.simula(x1, x2);
    	txtResult.appendText("Numero giorni necessari all'analisi: " + this.model.getNumGiorni() +"\n");
    	txtResult.appendText("ANALISI:\n ");
    	List<Intervistatore> intervistatori = new ArrayList<>(this.model.getIntervistatori());

    	for(Intervistatore i: intervistatori) {
    		txtResult.appendText("Intervistatore numero " + i.getId() + ", intervistati = " +i.getNumIntervistati() +"\n");
    	}
    }
    
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        for(int year = 2005; year <= 2013; year++) {
        	cmbAnno.getItems().add(year);
        }
      
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	btnUtenteSimile.setDisable(true);
    	btnSimula.setDisable(true);
    	
    }
}
