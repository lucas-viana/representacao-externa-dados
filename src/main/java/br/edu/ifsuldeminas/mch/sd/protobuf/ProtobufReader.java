package br.edu.ifsuldeminas.mch.sd.protobuf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

public class ProtobufReader {

	public static Person read(String fileName) {
		InputStream file = null;
		Person person = null;

		try {
			file = new FileInputStream(fileName);
			PersonProtos.Person protoPerson = PersonProtos.Person.parseFrom(file);

			PersonProtos.Address protoAddress = protoPerson.getAddress();
			Address address = new Address(
					protoAddress.getPatio(),
					protoAddress.getNumber(),
					protoAddress.getNeighborhood(),
					protoAddress.getZipCode(),
					protoAddress.getCity(),
					protoAddress.getState());

			person = new Person(
					protoPerson.getName(),
					protoPerson.getCpf(),
					new Date(protoPerson.getBirthDay()),
					address);
			System.out.println("[Protobuf] Objeto desserializado com sucesso.");
		} catch (Exception e) {
			System.out.println("[Protobuf] Objeto não pode ser desserializado.");
			e.printStackTrace();
		} finally {
			try {
				if (file != null)
					file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return person;
	}
}
