package com.bankonet.model;

import java.util.ArrayList;
import java.util.List;

import com.bankonet.Compte;

public class Client{
	private String nom;
	private String prenom;
	private String login;
	private String password;
	private List<Compte> compteCourantList = new ArrayList<Compte>();
	private List<Compte> compteEpargneList = new ArrayList<Compte>();
	
	// Constructeur
	public Client(String nom, String prenom){
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public Client(String nom, String prenom, List compteCourantList, List compteEpargneList){
		this.nom = nom;
		this.prenom = prenom;
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
	
	public List getComptes(){
		List result = new ArrayList();
		result.add(this.compteCourantList);
		result.add(this.compteEpargneList);
		return result;
	}
}
