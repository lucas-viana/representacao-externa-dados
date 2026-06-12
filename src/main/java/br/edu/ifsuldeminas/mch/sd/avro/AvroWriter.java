package br.edu.ifsuldeminas.mch.sd.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

/**
 * Responsável pela serialização do objeto Person em arquivo, no formato
 * Apache Avro. O POJO é convertido em um GenericRecord conforme o esquema
 * person.avsc (em src/main/resources) e gravado em um container Avro
 * (DataFileWriter), que embute o próprio esquema no arquivo.
 */
public class AvroWriter {

	public static void write(Person person, String fileName) {
		DataFileWriter<GenericRecord> dataFileWriter = null;

		try {
			Schema schema = new Schema.Parser()
					.parse(AvroWriter.class.getResourceAsStream("/person.avsc"));
			Schema addressSchema = schema.getField("address").schema();

			Address address = person.getAddress();
			GenericRecord addressRecord = new GenericData.Record(addressSchema);
			addressRecord.put("patio", address.getPatio());
			addressRecord.put("number", address.getNumber());
			addressRecord.put("neighborhood", address.getNeighborhood());
			addressRecord.put("zipCode", address.getZipCode());
			addressRecord.put("city", address.getCity());
			addressRecord.put("state", address.getState());

			GenericRecord personRecord = new GenericData.Record(schema);
			personRecord.put("name", person.getName());
			personRecord.put("cpf", person.getCpf());
			personRecord.put("birthDay", person.getBirthDay().getTime());
			personRecord.put("address", addressRecord);

			DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
			dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
			dataFileWriter.create(schema, new File(fileName));
			dataFileWriter.append(personRecord);
			System.out.println("[Avro] Objeto serializado com sucesso.");
		} catch (Exception e) {
			System.out.println("[Avro] Objeto não pode ser serializado.");
			e.printStackTrace();
		} finally {
			try {
				if (dataFileWriter != null)
					dataFileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
