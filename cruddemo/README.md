# CRUD Demo – Employee REST API (Spring Boot + PostgreSQL)

API REST per la gestione di dipendenti (employees), sviluppata come progetto di apprendimento su Spring Boot con persistenza reale tramite **JPA EntityManager** e database **PostgreSQL**.

Questo progetto rappresenta il secondo step di un percorso didattico più ampio: dopo un primo progetto CRUD in memoria ([Studente API](https://github.com/juscarni/Studente--API)), questo introduce la persistenza dei dati con `EntityManager`, in preparazione alla migrazione verso `JpaRepository` (Spring Data JPA).

## Tecnologie utilizzate

- **Java 21**
- **Spring Boot 4.0.7**
- **Hibernate ORM 7.2** (JPA EntityManager)
- **PostgreSQL** (database relazionale)
- **Maven**
- **Lombok** (riduzione del boilerplate su getter/setter/costruttori)
- **Jackson** (`tools.jackson` / `JsonMapper`) per la serializzazione JSON e gli aggiornamenti parziali (PATCH)
- **Spring Boot DevTools** (hot reload in sviluppo)

## Architettura

Il progetto segue un'architettura a livelli classica:

```
Controller (REST)  →  Service (logica di business)  →  DAO (EntityManager)  →  Database (PostgreSQL)
```

- **`EmployeeRestController`** — espone gli endpoint REST, delega tutta la logica al Service
- **`EmployeeService`** — orchestrazione, validazione dei dati in ingresso, gestione delle eccezioni di dominio
- **`EmployeeDAOImpl`** — accesso ai dati tramite `EntityManager` (persist, merge, find, remove)
- **`Employee`** — entità JPA mappata sulla tabella `employees`

La responsabilità della validazione è centralizzata nel Service: il DAO riceve sempre dati già verificati (ad esempio, un id già confermato esistente prima di un'operazione di delete o update).

## Funzionalità

- Operazioni CRUD complete su un'entità `Employee`
- Ricerca per ID e per email
- Aggiornamento parziale (**PATCH**) tramite `Map<String, Object>` e `JsonMapper.updateValue()`, con protezione esplicita contro la modifica dell'id tramite payload
- Gestione centralizzata delle eccezioni con una classe custom `EmployeeNotFoundException`, tradotta in risposte HTTP appropriate (404 Not Found, 400 Bad Request) con corpo JSON strutturato:
  ```json
  {
    "message": "there is not an employee in the database with the id : 25",
    "timestamp": 1784920835987,
    "status": 404
  }
  ```
- Popolamento automatico di dati di esempio all'avvio dell'applicazione (`@PostConstruct`)

## Endpoint disponibili

| Metodo | Endpoint | Descrizione |
|---|---|---|
| GET | `/api/employees` | Restituisce tutti gli impiegati |
| GET | `/api/employees/{id}` | Restituisce un impiegato tramite ID |
| GET | `/api/employees/email/{email}` | Restituisce un impiegato tramite email |
| POST | `/api/employees` | Crea un nuovo impiegato |
| PATCH | `/api/employees/{id}` | Aggiorna parzialmente un impiegato esistente |
| DELETE | `/api/employees/{id}` | Elimina un impiegato tramite ID |

### Esempio di richiesta POST

```json
{
  "firstName": "Mario",
  "lastName": "Rossi",
  "email": "mario.rossi@test.com"
}
```

### Esempio di richiesta PATCH

```json
{
  "email": "nuovo.indirizzo@test.com"
}
```

> Nota: il payload di un PATCH non può contenere la chiave `"id"` — un tentativo di modifica dell'id viene rifiutato con un errore 400.

## Come avviare il progetto

1. Assicurati di avere un'istanza PostgreSQL in esecuzione e configura le credenziali in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/employee
   spring.datasource.username=<tuo_utente>
   spring.datasource.password=<tua_password>
   ```
2. Avvia l'applicazione:
   ```bash
   ./mvnw spring-boot:run
   ```
3. L'applicazione parte di default sulla porta `8080`.

## Documentazione Swagger

L'API è documentata tramite **springdoc-openapi**, con Swagger UI configurata su un path personalizzato:

```properties
springdoc.swagger-ui.path=/index.html
```

Una volta avviata l'applicazione, la documentazione interattiva è disponibile su:

```
http://localhost:8080/index.html
```

Da qui è possibile esplorare tutti gli endpoint, i modelli di richiesta/risposta e testare direttamente le chiamate senza bisogno di Postman.

## Note

Questo progetto fa parte di un percorso di apprendimento in più tappe sullo sviluppo backend con Spring Boot:

1. CRUD in memoria ([Studente API](https://github.com/juscarni/Studente--API)) — logica di base senza persistenza
2. **DAO con EntityManager + PostgreSQL (questo progetto)** — introduzione della persistenza reale
3. DAO con EntityManager + MySQL
4. Migrazione verso `JpaRepository` (Spring Data JPA)

## Autore

Nsayi Juscarni Geoffroy
Progetto realizzato a scopo didattico per approfondire Spring Boot, JPA/Hibernate e la progettazione di API REST con persistenza reale.
