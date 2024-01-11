package es.florida.MongoDB;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;



public class Exercicis {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("libros");
		
		int id=1;
		MongoCursor<Document> cursor = coleccion.find().iterator();
		while (cursor.hasNext()) {
		//System.out.println(cursor.next().toJson());
		JSONObject obj = new JSONObject(cursor.next().toJson());
		System.out.println(obj.getString("Titol"));
		id=obj.getInt("Id")+1;
		}
		
		Document doc = new Document();
		doc.append("Id", id);
		doc.append("Titol", "si");
		doc.append("Autor", "tu");
		doc.append("Any_naixement", "1234 A.C");
		doc.append("Any_publicacio", "ayer");
		doc.append("Editorial", "Lenya");
		doc.append("Nombre_pagines", "muchas");
		coleccion.insertOne(doc);
		
		coleccion.updateOne(eq("Id", 14), new Document("$set", 
				new Document("Autor", "pep")));

		 cursor = coleccion.find().iterator();
		while (cursor.hasNext()) {
		System.out.println(cursor.next().toJson());
		}
		coleccion.deleteOne(eq("Id", 16));

		mongoClient.close();

	}

}
