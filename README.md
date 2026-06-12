# Representação Externa de Dados em Sistemas Distribuídos

**Disciplina:** Sistemas Computacionais Distribuídos 
**Grupo:** Aryane Cassemiro, Letícia Carvalho, Lucas Viana

---

## Estrutura do projeto

```
representacao-externa/
├── pom.xml                      → projeto Maven (dependências e build)
├── proto/person.proto           → arquivo auxiliar: esquema do Protobuf
├── lib/                         → bibliotecas (.jar) utilizadas
├── src/main/resources/
│   └── person.avsc              → arquivo auxiliar: esquema do Avro
└── src/main/java/br/edu/ifsuldeminas/mch/sd/
    ├── pojos/                   → Person e Address (POJOs do tutorial)
    ├── serialization/           → tutorial base: Serializable
    ├── xml/                     → tutorial base: XStream
    ├── json/                    → tutorial base: json-io
    ├── protobuf/                → ProtobufWriter, ProtobufReader, ProtobufMain
    │                              e PersonProtos (gerado pelo protoc)
    ├── avro/                    → AvroWriter, AvroReader, AvroMain
    └── messagepack/             → MessagePackWriter, MessagePackReader, MessagePackMain
```


##  Como compilar e executar

**Pré-requisitos:** JDK 17+ e Maven

### No Eclipse
1. `File > Import > Maven > Existing Maven Projects` e selecione a pasta do projeto;
2. Aguarde o download das dependências;
3. Clique com o botão direito em `ProtobufMain`, `AvroMain` ou `MessagePackMain`
   e escolha `Run As > Java Application`.

Sem Maven, também é possível criar um projeto Java comum e adicionar os `.jar`
da pasta `lib/` ao *Build Path* (`Build Path > Add JARs`), como feito em aula.

### Pela linha de comando
```bash
mvn compile
java -cp "target/classes;lib/*" br.edu.ifsuldeminas.mch.sd.protobuf.ProtobufMain
java -cp "target/classes;lib/*" br.edu.ifsuldeminas.mch.sd.avro.AvroMain
java -cp "target/classes;lib/*" br.edu.ifsuldeminas.mch.sd.messagepack.MessagePackMain
```

## Resumo da implementação

###  Protocol Buffers (`br.edu.ifsuldeminas.mch.sd.protobuf`)
1. O esquema foi definido no arquivo auxiliar [`proto/person.proto`](proto/person.proto)
   (mensagens `Person` e `Address`, com a data como `int64` em milissegundos);
2. A classe `PersonProtos.java` foi gerada com o compilador `protoc` (v3.25.5):
   `protoc --proto_path=proto --java_out=src/main/java proto/person.proto`
   — a classe gerada já está no projeto, então não é preciso ter o protoc para rodar;
3. `ProtobufWriter` converte o POJO em mensagem Protobuf (padrão *builder*) e grava
   os bytes com `writeTo()`; `ProtobufReader` lê com `parseFrom()` e reconstrói os POJOs.

###  Apache Avro (`br.edu.ifsuldeminas.mch.sd.avro`)
1. O esquema foi definido no arquivo auxiliar [`src/main/resources/person.avsc`](src/main/resources/person.avsc) (JSON);
2. `AvroWriter` carrega o esquema, monta um `GenericRecord` com os dados do POJO
   (sem geração de código) e grava com `DataFileWriter`, que **embute o esquema
   no arquivo** (formato autodescritivo);
3. `AvroReader` usa `DataFileReader`, que lê o esquema do próprio arquivo,
   recupera o `GenericRecord` e reconstrói os POJOs.

### MessagePack (`br.edu.ifsuldeminas.mch.sd.messagepack`)
1. Não há esquema: `MessagePackWriter` empacota os campos um a um
   (`packString`, `packInt`, `packLong`) em ordem fixa;
2. `MessagePackReader` desempacota na mesma ordem e reconstrói os POJOs;
3. A data é gravada como `long` (milissegundos desde a *epoch*).

## Bibliotecas utilizadas (pasta `lib/`)

| Tecnologia | Biblioteca (Maven) | Versão |
|---|---|---|
| Protobuf | `com.google.protobuf:protobuf-java` | 3.25.5 |
| Avro | `org.apache.avro:avro` (+ jackson, slf4j, commons-*) | 1.11.4 |
| MessagePack | `org.msgpack:msgpack-core` | 0.9.8 |
| XML (tutorial) | `com.thoughtworks.xstream:xstream` (+ mxparser, xmlpull) | 1.4.21 |
| JSON (tutorial) | `com.cedarsoftware:json-io` | 4.14.0 |


##  Respostas às perguntas do trabalho

### 1 Quais vantagens e desvantagens cada tecnologia apresenta?

**Protocol Buffers**
- Formato binário muito compacto e rápido; contrato formal e fortemente tipado
  (`.proto`); evolução de esquema segura (campos numerados); suporte oficial a
  dezenas de linguagens; base do gRPC.
- Exige etapa extra de compilação (`protoc`) e código gerado; o binário não é
  legível por humanos; leitor e escritor precisam conhecer o mesmo `.proto`.

**Apache Avro**
- Esquema escrito em JSON e embutido no próprio arquivo (autodescritivo —
  qualquer leitor consegue interpretar os dados); dispensa geração de código
  (`GenericRecord`); excelente evolução de esquema (resolução escritor × leitor);
  padrão no ecossistema de Big Data.
- O esquema embutido gera *overhead*: para um único objeto produziu o maior
  arquivo (738 bytes) — o custo só compensa quando muitos registros são gravados
  no mesmo container; API mais verbosa e mais dependências transitivas.

**MessagePack**
- O mais simples de usar ("JSON binário"): sem esquema, sem código gerado, API
  direta (`packX`/`unpackX`); gerou o menor arquivo (85 bytes); bindings para
  muitas linguagens.
- Sem contrato formal: a ordem e os tipos dos campos ficam por conta do
  programador (escrita e leitura precisam combinar manualmente); sem suporte
  nativo a evolução de esquema; mais propenso a erros em sistemas grandes.

### 2 Em quais tipos de sistemas cada formato costuma ser utilizado?

- **Protobuf:** comunicação entre microsserviços (gRPC), APIs internas de
  alto desempenho, aplicações Google, mobile e IoT — onde há contrato bem
  definido entre as partes e a banda/latência importam.
- **Avro:** ecossistema de Big Data e streaming: Hadoop, Spark, Kafka
  (com Schema Registry), *data lakes* e ETL — onde os dados ficam armazenados
  por muito tempo e o esquema evolui com frequência.
- **MessagePack:** caches (Redis), filas leves, IoT e sistemas embarcados,
  jogos online, logs (Fluentd) e APIs que querem "JSON menor e mais rápido"
  sem adotar esquemas.

### 3 Qual formato gerou o menor arquivo?

O **MessagePack (85 bytes)**, seguido de perto pelo Protobuf (93 bytes).
O MessagePack venceu porque grava apenas os valores em sequência (nem nomes de
campos nem *tags*); o Protobuf adiciona ~1 byte de *tag* por campo. O Avro
ficou com o maior arquivo (738 bytes) por embutir o esquema JSON no container —
custo fixo que se dilui quando há muitos registros.

### 4 Qual formato foi mais simples de implementar?

O **MessagePack**: sem arquivo de esquema, sem geração de código e sem
bibliotecas transitivas — apenas empacotar e desempacotar os campos na mesma
ordem. O Avro vem em seguida (exigiu escrever o `person.avsc`, mas dispensou
código gerado). O Protobuf foi o mais trabalhoso: escrever o `.proto` e
compilá-lo com o `protoc` antes de poder programar.

### 5 Qual formato parece mais adequado para Sistemas Distribuídos modernos?

O Protocol Buffers. Ele combina arquivo quase tão pequeno quanto o do
MessagePack com o que sistemas distribuídos modernos mais precisam: **contrato
formal entre serviços independentes** (o `.proto` documenta e valida a
comunicação), evolução de esquema sem quebrar clientes antigos,
interoperabilidade entre linguagens e integração nativa com gRPC, padrão
atual de comunicação entre microsserviços. A ressalva: em pipelines de
streaming/Big Data (Kafka, Spark), o Avro costuma ser a escolha mais
adequada, justamente pelo esquema autodescritivo e pela resolução
escritor × leitor.


