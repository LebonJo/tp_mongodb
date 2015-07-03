package com.bankonet.conseiller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.bankonet.dao.BanqueDao;
import com.bankonet.model.CompteCourant;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MainAppliConseiller {
	
	public static void main(String[] args){
		
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		BanqueDao database = new BanqueDao();
		
		System.out.println("***** APPLICATION CONSEILLER BANCAIRE *****");
		System.out.println("\n");
		System.out.println("0. Arreter le programme");
		System.out.println("1. Ouvrir un compte");
		System.out.println("\n");
		
		Scanner inputScan = new Scanner(System.in);
		boolean boucle = true;
		while(boucle){
			System.out.println("Veuillez choisir une option");
			String input = inputScan.nextLine();
			if(input.equals("0")){
				database.closeConnection();
				System.out.println("Arrêt de l'application");
				boucle = false;
				break;
			}
			if(input.equals("1")){
				System.out.println("Veuillez saisir le nom du client : ");
				String clientName = inputScan.nextLine();
				System.out.println("Veuillez saisir le prénom du client : ");
				String clientFName = inputScan.nextLine();
				System.out.println("Veuillez saisir le login du client : ");
				String clientLogin = inputScan.nextLine();
				String clientMdp = "secret";
				
				database.insertNewCompte(clientName, clientFName, clientLogin, clientMdp);
								
			}
			else{
				System.out.println("Saisie erronnée. Veuillez choisir une option");
			}
		}
		
	}

}
