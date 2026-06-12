package br.edu.ifsuldeminas.mch.sd.messagepack;

import java.io.File;
import java.util.Date;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

/**
 * Teste do MessagePack: gera o arquivo person.msgpack, recupera o objeto e
 * imprime os dados recuperados.
 */
public class MessagePackMain {

	private static final String FILE_NAME = "person.msgpack";

	public static void main(String[] args) {
		Address address = new Address("Rua José", 20, "Por do Sol",
				"37.130-000", "Alfenas", "MG");
		Person emerson = new Person("Emerson Carvalho", "060.793.477-11",
				new Date(), address);

		// 1) Geração do arquivo
		MessagePackWriter.write(emerson, FILE_NAME);
		System.out.println("[MessagePack] Arquivo gerado: " + FILE_NAME
				+ " (" + new File(FILE_NAME).length() + " bytes)");

		// 2) Recuperação do objeto
		Person recovered = MessagePackReader.read(FILE_NAME);

		// 3) Impressão dos dados recuperados
		if (recovered != null) {
			System.out.println(recovered);
			System.out.println(recovered.getAddress());
		}
	}
}
