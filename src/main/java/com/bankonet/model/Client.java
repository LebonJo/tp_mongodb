package com.bankonet.model;

import java.util.ArrayList;
import java.util.List;

import com.bankonet.Compte;

public class Client{
	private String identifiant;
	private String nom;
	private String prenom;
	private String login;
	private String password;
	private List<Compte> compteCourantList = new ArrayList<Compte>();
	private List<Compte> compteEpargneList = new ArrayList<Compte>();
	
	// Constructeur par défaut
	public Client(){};
	
	// Constructeur
	public Client(String nom, String prenom, String login, String password){
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.password = password;
	}
	
	// Constructeur utilisé dans le dao pour recréer des clients à partir de la base -> on connait donc l'identifiant (objetId mongo)
	public Client(String identifiant, String nom, String prenom, String login, String password, List<Compte> compteCourantList, List<Compte> compteEpargneList){
		this.identifiant = identifiant;
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.password = password;
		this.compteCourantList = compteCourantList;
		this.compteEpargneList = compteEpargneList;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getLogin(){
		return this.login;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public String getIdentifiant(){
		return this.identifiant;
	}
	
	public void setIdentifiant(String identifiant){
		this.identifiant = identifiant;
	}
	
	public int getNbCC(){
		return this.compteCourantList.size();
	}
	
	public int getNbCE(){
		return this.compteEpargneList.size();
	}
	
	public List<Compte> getComptes(){
		List<Compte> result = new ArrayList<Compte>();
		for(Compte compte : this.compteCourantList){
			result.add(compte);
		}
		for(Compte compte : this.compteEpargneList){
			result.add(compte);
		}
		return result;
	}
	
	public String toString(){
		return "ID: " + getIdentifiant() + ", NOM : " + getNom() + ", PRENOM : " + getPrenom() + ", LOGIN : " + getLogin() + ", NB CC ; " + getNbCC() + ", NB CE : " + getNbCE();
	}
}
