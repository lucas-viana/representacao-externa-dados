package br.edu.ifsuldeminas.mch.sd.messagepack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

/**
 * Responsável pela desserialização do objeto Person a partir do arquivo
 * MessagePack. Os campos são desempacotados na MESMA ordem em que foram
 * gravados pelo MessagePackWriter e os POJOs do tutorial (Person e
 * Address) são reconstruídos via construtores.
 */
public class MessagePackReader {

	public static Person read(String fileName) {
		MessageUnpacker unpacker = null;
		Person person = null;

		try {
			unpacker = MessagePack.newDefaultUnpacker(new FileInputStream(fileName));

			String name = unpacker.unpackString();
			String cpf = unpacker.unpackString();
			long birthDay = unpacker.unpackLong();

			String patio = unpacker.unpackString();
			int number = unpacker.unpackInt();
			String neighborhood = unpacker.unpackString();
			String zipCode = unpacker.unpackString();
			String city = unpacker.unpackString();
			String state = unpacker.unpackString();

			Address address = new Address(patio, number, neighborhood,
					zipCode, city, state);
			person = new Person(name, cpf, new Date(birthDay), address);
			System.out.println("[MessagePack] Objeto desserializado com sucesso.");
		} catch (Exception e) {
			System.out.println("[MessagePack] Objeto não pode ser desserializado.");
			e.printStackTrace();
		} finally {
			try {
				if (unpacker != null)
					unpacker.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return person;
	}
}
