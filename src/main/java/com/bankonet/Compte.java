package com.bankonet;

public abstract class Compte{
	private int identifiant;
	private String libelle;
	protected float solde;
	
	public enum TypeCompte {
		COURANT("courant"), 
		EPARGNE("épargne");
		
		private final String typeCompte;
		
		TypeCompte(String typeCompte) {
			this.typeCompte = typeCompte;
		}
		
		public String getTypeCompte(){
			return this.typeCompte;
		}
	}
	
	public Compte(String libelle, float solde){
		this.libelle = libelle;
		if(solde < 0f){
			System.out.println("On ne peux pas créer un compte avec un solde négatif !");
			this.solde = 0f;
		}else{
			this.solde = solde;
		}
	}
	
	public Compte(int identifiant, String libelle, float solde){
		this.identifiant = identifiant;
		this.libelle = libelle;
		if(solde < 0f){
			System.out.println("On ne peux pas créer un compte avec un solde négatif !");
			this.solde = 0f;
		}else{
			this.solde = solde;
		}
	}

	public int getIdentifiant(){
		return this.identifiant;
	}
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public float getSolde() {
		return solde;
	}
	
	public String toString() {
		return "ID : " + getIdentifiant() + ", LIBELLE : \"" + getLibelle() + "\", SOLDE : " + getSolde() + "€";
	}
	
	public void crediter(float montant){
		this.solde = this.solde + montant;
	}
	
	public abstract String getTypeCompte();
}
