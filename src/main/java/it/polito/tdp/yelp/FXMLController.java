/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
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
    	String stringaDaConvertire = txtN.getText();
    	try {
    		int minReview = Integer.parseInt(stringaDaConvertire);

    		Integer anno = cmbAnno.getValue(); //lo salvo come oggetto Integer e non come int per poter fare il controllo dell'if sottostante, ovvero verificare che non sia null (non posso confrontare un int con null ma un oggetto si)
    		if(anno == null) {
    			txtResult.setText("Selezionare un anno!\n");
    		    return;
    		}
    		String msg = this.model.creaGrafo(minReview, anno);
    		txtResult.setText(msg);
    		cmbUtente.getItems().clear(); //cancello quello che c'era prima perchè è riferito al grafo precedente: ESSENZIALEEE
    		cmbUtente.getItems().addAll(model.getUsersWithReviews());	
    	    btnUtenteSimile.setDisable(false);
    		
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserire un numero valido!\n");
    		return;
    	}
    	
    	
    	
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {

    	//Dato un utente devo prendermi tutti gli adiacenti, i pesi degli archi
    	//e vedere qual'è il peso maggiore e restituire una lista con tutti gli adiacenti
    	//con quel peso
    	
    	//TODO controlli dei parametri di input
    	//TODO stampare correttamente come da esempio su pdf il risultato
    	User u = cmbUtente.getValue();
    	
    	if(u == null) {
    		txtResult.setText("Selezionare un utente dal menù a tendina, dopo aver creato il grafo.");
    	    return;
    	}
    	List<User> vicini = model.utentiPiuSimili(u);
    	
    	txtResult.setText("Utenti piu vicini a " + u + ":\n");
    	
    	for(User u2: vicini) {
    		txtResult.appendText(""+u2.toString()+"\n");
    	}
    	
    	
    }
    
    @FXML
    void doSimula(ActionEvent event) {

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

        btnUtenteSimile.setDisable(true);
        
        for(int year=2005; year<= 2013; year++) {
            cmbAnno.getItems().add(year);
        }
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	 }
}
