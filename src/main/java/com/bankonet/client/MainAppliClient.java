package com.bankonet.client;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bankonet.dao.BanqueDao;
import com.bankonet.model.Client;

public class MainAppliClient {
	
	public static void main(String[] args){
		
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		BanqueDao database = new BanqueDao();
		
		Client user = new Client();
		
		Scanner inputScan = new Scanner(System.in);
		boolean boucle = true;
		while(boucle){
			if(user.getNom() != null){
				System.out.println("Veuillez choisir une option");
				String input = inputScan.nextLine();
				if(input.equals("0")){
					database.closeConnection();
					System.out.println("Arrêt de l'application");
					boucle = false;
					break;
				} else {
					System.out.println("Option non valide !");
				}
			} else {
				System.out.println("Veuillez saisir votre login");
				String login = inputScan.nextLine();
				System.out.println("Veuillez saisir votre mot de passe");
				String password = inputScan.nextLine();

				Client searchedUser = database.getClient(login, password);
				
				if(searchedUser != null){
					user = searchedUser;
					System.out.println("***** APPLICATION CLIENT *****");
					System.out.println("\n");
					System.out.println("0. Arreter le programme");
				} else {
					System.out.println("Identifiants incorrects !");
				}
			}
		}
	}

}
