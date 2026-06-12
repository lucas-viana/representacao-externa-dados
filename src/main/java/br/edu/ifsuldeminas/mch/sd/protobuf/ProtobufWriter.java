package br.edu.ifsuldeminas.mch.sd.protobuf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

public class ProtobufWriter {

	public static void write(Person person, String fileName) {
		OutputStream file = null;

		try {
			Address address = person.getAddress();

			PersonProtos.Address protoAddress = PersonProtos.Address.newBuilder()
					.setPatio(address.getPatio())
					.setNumber(address.getNumber())
					.setNeighborhood(address.getNeighborhood())
					.setZipCode(address.getZipCode())
					.setCity(address.getCity())
					.setState(address.getState())
					.build();

			PersonProtos.Person protoPerson = PersonProtos.Person.newBuilder()
					.setName(person.getName())
					.setCpf(person.getCpf())
					.setBirthDay(person.getBirthDay().getTime())
					.setAddress(protoAddress)
					.build();

			file = new FileOutputStream(fileName);
			protoPerson.writeTo(file);
			System.out.println("[Protobuf] Objeto serializado com sucesso.");
		} catch (Exception e) {
			System.out.println("[Protobuf] Objeto não pode ser serializado.");
			e.printStackTrace();
		} finally {
			try {
				if (file != null)
					file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
