package br.edu.ifsuldeminas.mch.sd.avro;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

import br.edu.ifsuldeminas.mch.sd.pojos.Address;
import br.edu.ifsuldeminas.mch.sd.pojos.Person;

public class AvroReader {

	public static Person read(String fileName) {
		DataFileReader<GenericRecord> dataFileReader = null;
		Person person = null;

		try {
			DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>();
			dataFileReader = new DataFileReader<GenericRecord>(new File(fileName), datumReader);

			GenericRecord personRecord = dataFileReader.next();
			GenericRecord addressRecord = (GenericRecord) personRecord.get("address");

			Address address = new Address(
					addressRecord.get("patio").toString(),
					(Integer) addressRecord.get("number"),
					addressRecord.get("neighborhood").toString(),
					addressRecord.get("zipCode").toString(),
					addressRecord.get("city").toString(),
					addressRecord.get("state").toString());

			person = new Person(
					personRecord.get("name").toString(),
					personRecord.get("cpf").toString(),
					new Date((Long) personRecord.get("birthDay")),
					address);
			System.out.println("[Avro] Objeto desserializado com sucesso.");
		} catch (Exception e) {
			System.out.println("[Avro] Objeto não pode ser desserializado.");
			e.printStackTrace();
		} finally {
			try {
				if (dataFileReader != null)
					dataFileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return person;
	}
}
