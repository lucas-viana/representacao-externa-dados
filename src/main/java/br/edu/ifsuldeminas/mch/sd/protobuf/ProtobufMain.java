package br.edu.ifsuldeminas.mch.sd.protobuf;

import java.io.File;
import java.util.Date;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

/**
 * Teste do Protocol Buffers: gera o arquivo person.protobuf, recupera o
 * objeto e imprime os dados recuperados.
 */
public class ProtobufMain {

	private static final String FILE_NAME = "person.protobuf";

	public static void main(String[] args) {
		Address address = new Address("Rua José", 20, "Por do Sol",
				"37.130-000", "Alfenas", "MG");
		Person emerson = new Person("Emerson Carvalho", "060.793.477-11",
				new Date(), address);

		// 1) Geração do arquivo
		ProtobufWriter.write(emerson, FILE_NAME);
		System.out.println("[Protobuf] Arquivo gerado: " + FILE_NAME
				+ " (" + new File(FILE_NAME).length() + " bytes)");

		// 2) Recuperação do objeto
		Person recovered = ProtobufReader.read(FILE_NAME);

		// 3) Impressão dos dados recuperados
		if (recovered != null) {
			System.out.println(recovered);
			System.out.println(recovered.getAddress());
		}
	}
}
