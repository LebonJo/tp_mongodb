package com.bankonet.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.bankonet.Compte;
import com.bankonet.model.Client;
import com.bankonet.model.CompteCourant;
import com.bankonet.model.CompteEpargne;
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
	public void insertNewCompte(Client client){
		
		String clientName = client.getNom();
		String clientFName = client.getPrenom();
		String clientLogin = client.getLogin();
		String clientMdp = client.getPassword();
		
		//-- on cherche si un document est déjà présent concernant le meme client
		//-- je me sers du login comme discriminant (il sera donc unique)
		FindIterable<Document> result = clients.find(new Document().append("login", client.getLogin()));
		
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

	public List<Client> getAllClients() {
		List<Client> result = new ArrayList<Client>();
		
		FindIterable<Document> clientsIterable = clients.find();
		Iterator<Document> clientsIterator = clientsIterable.iterator();
		while(clientsIterator.hasNext()){
			Document clientFound = clientsIterator.next();
			
			List<Document> docCC = (List<Document>) clientFound.get("comptesCourants");
			List<Compte> listComptesCourants = new ArrayList<Compte>(); 
			for(Document doc : docCC){
				CompteCourant compte = new CompteCourant(doc.getString("libelle"), new Float(doc.getDouble("solde")));
				listComptesCourants.add(compte);
			}
			
			List<Document> docCE = (List<Document>) clientFound.get("comptesEpargnes");
			List<Compte> listComptesEpargnes = new ArrayList<Compte>(); 
			for(Document doc : docCE){
				CompteEpargne compte = new CompteEpargne(doc.getString("libelle"), new Float(doc.getDouble("solde")));
				listComptesEpargnes.add(compte);
			}
			
			result.add(new Client(clientFound.getObjectId("_id").toString(), clientFound.getString("nom"), clientFound.getString("prenom"),
								clientFound.getString("login"), clientFound.getString("password"), listComptesCourants, listComptesEpargnes));
		}
		
		return result;
	}

}
