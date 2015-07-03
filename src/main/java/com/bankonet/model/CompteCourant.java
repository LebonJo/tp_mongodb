package com.bankonet.model;

import com.bankonet.Compte;
import com.bankonet.Compte.TypeCompte;

public class CompteCourant extends Compte{
	
	public CompteCourant(String lib, float solde){
		super(lib, solde);
	}
	
	public CompteCourant(int id, String lib, float solde){
		super(id, lib, solde);
	}
	
	public String getTypeCompte(){
		return TypeCompte.COURANT.getTypeCompte();
	}
}
