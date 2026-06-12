package br.edu.ifsuldeminas.mch.sd.messagepack;

import java.io.FileOutputStream;
import java.io.IOException;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

/**
 * Responsável pela serialização do objeto Person em arquivo, no formato
 * MessagePack. Os campos do POJO são empacotados um a um, em ordem fixa
 * (formato sem esquema): a mesma ordem deve ser respeitada na leitura,
 * pelo MessagePackReader.
 */
public class MessagePackWriter {

	public static void write(Person person, String fileName) {
		MessagePacker packer = null;

		try {
			packer = MessagePack.newDefaultPacker(new FileOutputStream(fileName));

			packer.packString(person.getName());
			packer.packString(person.getCpf());
			packer.packLong(person.getBirthDay().getTime());

			Address address = person.getAddress();
			packer.packString(address.getPatio());
			packer.packInt(address.getNumber());
			packer.packString(address.getNeighborhood());
			packer.packString(address.getZipCode());
			packer.packString(address.getCity());
			packer.packString(address.getState());

			System.out.println("[MessagePack] Objeto serializado com sucesso.");
		} catch (Exception e) {
			System.out.println("[MessagePack] Objeto não pode ser serializado.");
			e.printStackTrace();
		} finally {
			try {
				if (packer != null)
					packer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
