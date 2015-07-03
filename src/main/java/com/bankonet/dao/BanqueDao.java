package com.bankonet.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.bankonet.Compte;
import com.bankonet.Compte.TypeCompte;
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
	
	// Cr�ation d'un nouveau compte
	public void insertNewCompte(Client client){
		
		String clientName = client.getNom();
		String clientFName = client.getPrenom();
		String clientLogin = client.getLogin();
		String clientMdp = client.getPassword();
		
		//-- on cherche si un document est d�j� pr�sent concernant le meme client
		//-- je me sers du login comme discriminant (il sera donc unique)
		FindIterable<Document> result = clients.find(new Document().append("login", client.getLogin()));
		
		Iterator<Document> iterator = result.iterator();
		
		//-- si il existe, on met � jour la liste des comptes 
		if(iterator.hasNext()){
			Document clientFound = iterator.next();
			List<Document> listComptesCourants = (List<Document>) clientFound.get("comptesCourants");
			List<Document> listComptesEpargnes = (List<Document>) clientFound.get("comptesEpargnes");
			String libelleCree =  clientName + "_" + clientFName + "_COURANT_" + (listComptesCourants.size()+1);
			listComptesCourants.add(new Document().append("identifiant", listComptesCourants.size()+1).append("libelle", libelleCree).append("solde", 0f));
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
		//-- si il n'existe pas encore, on cr�e le document et on l'insert
		else{
			List<Document> listComptesCourants = new ArrayList<Document>();
			String libelleCree =  clientName + "_" + clientFName + "_COURANT_1";
			listComptesCourants.add(new Document().append("identifiant", 1).append("libelle", libelleCree).append("solde", 0f));
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
				CompteCourant compte = new CompteCourant(doc.getInteger("identifiant"), doc.getString("libelle"), new Float(doc.getDouble("solde")));
				listComptesCourants.add(compte);
			}
			
			List<Document> docCE = (List<Document>) clientFound.get("comptesEpargnes");
			List<Compte> listComptesEpargnes = new ArrayList<Compte>(); 
			for(Document doc : docCE){
				CompteEpargne compte = new CompteEpargne(doc.getInteger("identifiant"), doc.getString("libelle"), new Float(doc.getDouble("solde")));
				listComptesEpargnes.add(compte);
			}
			
			result.add(new Client(clientFound.getObjectId("_id").toString(), clientFound.getString("nom"), clientFound.getString("prenom"),
								clientFound.getString("login"), clientFound.getString("password"), listComptesCourants, listComptesEpargnes));
		}
		
		return result;
	}

	public Client getClient(String login, String password) {
		
		FindIterable<Document> clientsIterable = clients.find(new Document().append("login", login).append("password", password));
		Iterator<Document> clientsIterator = clientsIterable.iterator();
		if(clientsIterator.hasNext()){
			Document clientFound = clientsIterator.next();
			
			List<Document> docCC = (List<Document>) clientFound.get("comptesCourants");
			List<Compte> listComptesCourants = new ArrayList<Compte>();
			for(Document doc : docCC){
				CompteCourant compte = new CompteCourant(doc.getInteger("identifiant"), doc.getString("libelle"), new Float(doc.getDouble("solde")));
				listComptesCourants.add(compte);
			}
			
			List<Document> docCE = (List<Document>) clientFound.get("comptesEpargnes");
			List<Compte> listComptesEpargnes = new ArrayList<Compte>(); 
			for(Document doc : docCE){
				CompteEpargne compte = new CompteEpargne(doc.getInteger("identifiant"), doc.getString("libelle"), new Float(doc.getDouble("solde")));
				listComptesEpargnes.add(compte);
			}
			
			return new Client(clientFound.getObjectId("_id").toString(), clientFound.getString("nom"), clientFound.getString("prenom"),
								clientFound.getString("login"), clientFound.getString("password"), listComptesCourants, listComptesEpargnes);
		} else {
			return null;
		}
	}
	
	public void updateClient(Client client){
		
		String clientName = client.getNom();
		String clientFName = client.getPrenom();
		String clientLogin = client.getLogin();
		String clientMdp = client.getPassword();
		List<Compte> listComptes = client.getComptes();
		
		List<Document> listDocsCC = new ArrayList<Document>();
		List<Document> listDocsCE = new ArrayList<Document>();
		
		for(Compte compte : listComptes){
			if(compte.getTypeCompte().equals(TypeCompte.COURANT)){
				String libelleCree =  clientName + "_" + clientFName + "_COURANT_" + compte.getIdentifiant();
				listDocsCC.add(new Document().append("identifiant", compte.getIdentifiant()).append("libelle", libelleCree).append("solde", compte.getSolde()));
			}
			else if(compte.getTypeCompte().equals(TypeCompte.EPARGNE)){
				String libelleCree =  clientName + "_" + clientFName + "_EPARGNE_" + compte.getIdentifiant();
				listDocsCC.add(new Document().append("identifiant", compte.getIdentifiant()).append("libelle", libelleCree).append("solde", compte.getSolde()));
			}
		}
		
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
					.append("comptesCourants", listDocsCC)
					.append("comptesEpargnes", listDocsCE)));
	}

}
