package com.bankonet.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BanqueDao {
	
	private MongoClient clientdb;
	private MongoDatabase database;
	private MongoCollection<Document> clients;
	
	public BanqueDao(){
		 clientdb = new MongoClient();
		 database = clientdb.getDatabase("banquedb"); 
		 clients = database.getCollection("clients");
	}
	
	public void closeConnection(){
		clientdb.close();
	}
	
	// Création d'un nouveau compte
	public void insertNewCompte(String clientName, String clientFName, String clientLogin, String clientMdp){
		
		//-- on cherche si un document est déjà présent concernant le meme client
		FindIterable<Document> result = clients.find(new Document()
				.append("nom", clientName)
				.append("prenom", clientFName)
				.append("login", clientLogin));
		
		Iterator<Document> iterator = result.iterator();
		
		//-- si il existe, on met à jour la liste des comptes 
		if(iterator.hasNext()){
			Document clientFound = iterator.next();
			List<Document> listComptesCourants = (List<Document>) clientFound.get("comptesCourants");
			List<Document> listComptesEpargnes = (List<Document>) clientFound.get("comptesEpargnes");
			String libelleCree =  clientName + "_" + clientFName + "_COURANT_" + (listComptesCourants.size()+1);
			listComptesCourants.add(new Document().append("libelle", libelleCree).append("solde", 0f));
			clients.updateOne(
				new Document()
					.append("nom", clientName)
					.append("prenom", clientFName)
					.append("login", clientLogin),
				new Document("$set" , new Document()
					.append("nom", clientName)
					.append("prenom", clientFName)
					.append("login", clientLogin)
					.append("password", clientMdp)
					.append("comptesCourants", listComptesCourants)
					.append("comptesEpargnes", listComptesEpargnes)));
		}
		//-- si il n'existe pas encore, on crée le document et on l'insert
		else{
			List<Document> listComptesCourants = new ArrayList<Document>();
			String libelleCree =  clientName + "_" + clientFName + "_COURANT_1";
			listComptesCourants.add(new Document().append("libelle", libelleCree).append("solde", 0f));
			List<Document> listComptesEpargnes = new ArrayList<Document>();
			clients.insertOne(
				new Document()
					.append("nom", clientName)
					.append("prenom", clientFName)
					.append("login", clientLogin)
					.append("password", clientMdp)
					.append("comptesCourants", listComptesCourants)
					.append("comptesEpargnes", listComptesEpargnes));
		}
	}

}
