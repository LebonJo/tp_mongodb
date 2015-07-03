package com.bankonet.model;

import com.bankonet.Compte;
import com.bankonet.Compte.TypeCompte;

public class CompteEpargne extends Compte{
	
	public CompteEpargne(String libelle, float solde){
		super(libelle, solde);
	}
	
	public CompteEpargne(int id, String libelle, float solde){
		super(id, libelle, solde);
	}
	
	public String getTypeCompte(){
		return TypeCompte.EPARGNE.getTypeCompte();
	}
}
