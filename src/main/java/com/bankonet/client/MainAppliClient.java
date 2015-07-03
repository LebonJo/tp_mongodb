package com.bankonet.client;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bankonet.Compte;
import com.bankonet.dao.BanqueDao;
import com.bankonet.model.Client;

public class MainAppliClient {
	
	public static void main(String[] args){
		
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		BanqueDao database = new BanqueDao();
		
		// Je cr�e un user par defaut dont les attributs sont null
		Client user = new Client();
		
		Scanner inputScan = new Scanner(System.in);
		boolean boucle = true;
		while(boucle){
			if(user.getNom() != null){ //-- si le nom n'est pas null, �a veut dire que j'ai r�ussi � me connecter (on y rentre donc pas le premier coup)
				System.out.println("Veuillez choisir une option");
				String input = inputScan.nextLine();
				if(input.equals("0")){
					database.closeConnection();
					System.out.println("Arr�t de l'application");
					boucle = false;
					break;
				} else if(input.equals("1")){
					//-- Pas besoin d'appeler la base puisque j'ai reconstruit mon client � partir de cette derni�re
					//-- je n'ai donc plus qu'� afficher les comptes de l'objet user
					user.afficherCompte();
				} else if(input.equals("2")){
					user.afficherCompte();
					System.out.println("Veuillez choisir le num�ro du compte � cr�diter");
					int indice = Integer.parseInt(inputScan.nextLine());
					Compte cptACrediter = user.getCompte(indice);
					if(cptACrediter != null){
						System.out.println("Veuillez choisir le montant � cr�diter");
						float montant = new Float(inputScan.nextLine());
						cptACrediter.crediter(montant);
						database.updateClient(user);
						System.out.println("Virement valid� ! Le nouveau solde est de : " + cptACrediter.getSolde());
					} else {
						System.out.println("Mauvais num�ro !");
					}
				} else {
					System.out.println("Option non valide !");
				}
			} else {
				System.out.println("Veuillez saisir votre login");
				String login = inputScan.nextLine();
				System.out.println("Veuillez saisir votre mot de passe");
				String password = inputScan.nextLine();

				//-- Je cherche un user correspondant � la description
				//-- Si je le trouve je le met dans mon user
				Client searchedUser = database.getClient(login, password);
				
				if(searchedUser != null){
					user = searchedUser;
					System.out.println("***** APPLICATION CLIENT *****");
					System.out.println("\n");
					System.out.println("0. Arreter le programme");
					System.out.println("1. Consulter les soldes des comptes");
					System.out.println("2. Effectuer un d�p�t");
					System.out.println("\n");
				} else {
					System.out.println("Identifiants incorrects !");
				}
			}
		}
	}

}
