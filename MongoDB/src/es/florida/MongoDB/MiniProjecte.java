package es.florida.MongoDB;

import static com.mongodb.client.model.Filters.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MiniProjecte {
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws InterruptedException, Exception {
		int opcio = 0;
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("libros");
		Thread.sleep(1000);
		do {
			System.out.println("\n Menu");
			System.out.println("-------\n");
			System.out.println("1.-Mostrar tos els titols de la biblioteca.");
			System.out.println("2.-Informacio dun llibre donada la id");
			System.out.println("3.-Afegir un nou llibre");
			System.out.println("4.-Modificar informacio dun llibre donada la id");
			System.out.println("5.-Borrar un llibre donada la id");
			System.out.println("6.-Salir");

			do {
				System.out.print("Donam el nombre de la opcio(1-6);");
				opcio = esUnNombre();
				System.out.println("");
			} while (opcio < 1 || opcio > 6);

			switch (opcio) {
			case 1:
				mostrarTitols(coleccion);
				break;
			case 2:
				informacioLlibre(coleccion);
				break;
			case 3:
				nouLlibre(coleccion);
				break;
			case 4:
				modificarLlibre(coleccion);
				break;
			case 5:
				borrarLlibre(coleccion);
				break;
			case 6:
				System.out.println("Ixit del programa...");
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + opcio);
			}

		} while (opcio != 6);

	}

	public static int esUnNombre() {

		try {
			return Integer.parseInt(reader.readLine());
		} catch (Exception e) {
			return -1;
		}
	}

	public static int esUnNombre(String num) {
		if (!num.isEmpty()) {
			try {
				return Integer.parseInt(num);
			} catch (Exception e) {
				//System.out.println("no era un nombre");
				return -1;
			}

		}
		return -2;
	}

	public static int posaUnNombre() {
		int num = -1;
		do {
			num = esUnNombre();
		} while (num < 0);
		return num;
	}
	
	public static int posaUnNombre(String mensaje,int valor) throws IOException {
		int auxiliar=0;
		do {
			System.out.println(mensaje + valor);
			String respuesta = reader.readLine();
			auxiliar = esUnNombre(respuesta);
			if(auxiliar>0) {
				valor=auxiliar;
			}
			
		} while (auxiliar == -1);
		return valor;
	}

	
	public static void mostrarTitols(MongoCollection<Document> coleccion) {

		MongoCursor<Document> cursor = coleccion.find().iterator();
		while (cursor.hasNext()) {

			JSONObject obj = new JSONObject(cursor.next().toJson());

			System.out.println("id=\"" + obj.getInt("Id") + "\"" + ", titol=\"" + obj.getString("Titol") + "\"");
		}
	}

	public static void informacioLlibre(MongoCollection<Document> coleccion) {
		int id;
		do {
			System.out.print("Donam el id a extraure informacio;");
			id = esUnNombre();
			System.out.println("");
		} while (id < 0);
		MongoCursor<Document> cursor = coleccion.find(eq("Id", id)).iterator();
		while (cursor.hasNext()) {

			JSONObject obj = new JSONObject(cursor.next().toJson());
			System.out.println("ID: " + obj.getInt("Id"));
			System.out.println("Titol: " + obj.getString("Titol"));
			System.out.println("Autor: " + obj.getString("Autor"));
			System.out.println("Any naixement: " + obj.getInt("Any_naixement"));
			System.out.println("Any_publicacio: " + obj.getInt("Any_publicacio"));
			System.out.println("Editorial: " + obj.getString("Editorial"));
			System.out.println("Pagines: " + obj.getInt("Nombre_pagines"));
		}
	}

	public static void nouLlibre(MongoCollection<Document> coleccion) throws IOException {
		int id = 0;
		MongoCursor<Document> cursor = coleccion.find().iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			id = obj.getInt("Id") + 1;
		}

		Document doc = new Document();
		doc.append("Id", id);
		System.out.println("Titol:");
		doc.append("Titol", reader.readLine());
		System.out.println("Autor:");
		doc.append("Autor", reader.readLine());

		System.out.println("Any de naixement:");
		doc.append("Any_naixement", posaUnNombre());
		System.out.println("Any de publicacio:");
		doc.append("Any_publicacio", posaUnNombre());

		System.out.println("Editorial:");
		doc.append("Editorial", reader.readLine());

		System.out.println("Nombre de paginess:");
		doc.append("Nombre_pagines", posaUnNombre());
		coleccion.insertOne(doc);

	}

	public static void borrarLlibre(MongoCollection<Document> coleccion) throws IOException {

		int id = posaUnNombre();
		coleccion.deleteOne(eq("Id", id));

	}

	public static void modificarLlibre(MongoCollection<Document> coleccion) throws IOException {

		int id;
		do {
			System.out.print("Donam el id a modificar informacio;");
			id = esUnNombre();
			System.out.println("\n Si es deixa en blanc el camp no es modificara\n");
		} while (id < 0);
		String titol = "";
		String autor = "";
		int any_naixement = 0;
		int any_publicacio = 0;
		String editorial = "";
		int pagines = 0;
		int auxiliar = 0;
		MongoCursor<Document> cursor = coleccion.find(eq("Id", id)).iterator();
		while (cursor.hasNext()) {

			JSONObject obj = new JSONObject(cursor.next().toJson());
			titol = obj.getString("Titol");
			autor = obj.getString("Autor");
			any_naixement = obj.getInt("Any_naixement");
			System.out.println(any_naixement);
			any_publicacio = obj.getInt("Any_publicacio");
			editorial = obj.getString("Editorial");
			pagines = obj.getInt("Nombre_pagines");
		}
		System.out.println("Titol: " + titol);
		String respuestaTitol = reader.readLine();
		if (!respuestaTitol.isEmpty()) {
			titol = respuestaTitol;
		}

		System.out.println("Autor: " + autor);
		String respuestaAutor = reader.readLine();
		if (!respuestaAutor.isEmpty()) {
			autor = respuestaAutor;
		}
		
		any_naixement=posaUnNombre("Any naixement: ",any_naixement);
		
		any_publicacio=posaUnNombre("Any de publicación: " ,any_publicacio);
		/*do {
			System.out.println("Any naixement: " + any_naixement);
			String respuestaAnyAny_naixement = reader.readLine();
			auxiliar = esUnNombre(respuestaAnyAny_naixement);
			if (auxiliar>0) {
				any_naixement = auxiliar;
			}
		} while (auxiliar ==-1 );

		do {
			System.out.println("Año de publicación: " + any_publicacio);
			String respuestaAnyPublicacion = reader.readLine();
			auxiliar = esUnNombre(respuestaAnyPublicacion);
			if (auxiliar >0) {
				System.out.println("entra");
				any_publicacio = auxiliar;
			}
		} while (auxiliar == -1);*/

		System.out.println("Editorial: " + editorial);
		String respuestaEditorial = reader.readLine();
		if (!respuestaEditorial.isEmpty()) {
			editorial = respuestaEditorial;
		}

		//se   puede hacer en una funcion, le pasas el texto para el  syso y la variable en el if un returnde aux y fuera del while return del valor pasado
		/*do {
			System.out.println("Número de páginas: " + pagines);
			String respuestaNumeroPaginas = reader.readLine();
			auxiliar = esUnNombre(respuestaNumeroPaginas);
			if (auxiliar >0) {
				pagines = auxiliar;
			}
		} while (auxiliar == -1);*/
		
		pagines=posaUnNombre("Número de páginas: " ,pagines);

		coleccion.updateOne(eq("Id", id),
				new Document("$set",
						new Document("Titol", titol).append("Autor", autor).append("Any_naixement", any_naixement)
								.append("Any_publicacio", any_publicacio).append("Editorial", editorial)
								.append("Nombre_pagines", pagines)));

	}

}
