package br.edu.ifsuldeminas.mch.sd.avro;

import java.io.File;
import java.util.Date;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

public class AvroMain {

	private static final String FILE_NAME = "person.avro";

	public static void main(String[] args) {
		Address address = new Address("Rua José", 20, "Por do Sol",
				"37.130-000", "Alfenas", "MG");
		Person emerson = new Person("Emerson Carvalho", "060.793.477-11",
				new Date(), address);

		AvroWriter.write(emerson, FILE_NAME);
		System.out.println("[Avro] Arquivo gerado: " + FILE_NAME
				+ " (" + new File(FILE_NAME).length() + " bytes)");

		Person recovered = AvroReader.read(FILE_NAME);

		// imprime:
		if (recovered != null) {
			System.out.println(recovered);
			System.out.println(recovered.getAddress());
		}
	}
}
