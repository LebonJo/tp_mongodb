package com.bankonet;

public abstract class Compte{
	private String libelle;
	protected float solde;
	
	public Compte(String libelle, float solde){
		this.libelle = libelle;
		if(solde < 0f){
			System.out.println("On ne peux pas créer un compte avec un solde négatif !");
			this.solde = 0f;
		}else{
			this.solde = solde;
		}
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
		return "LIBELLE : \"" + getLibelle() + "\", SOLDE : " + getSolde() + "€";
	}
}
