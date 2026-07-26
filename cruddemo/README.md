# CRUD Demo – Employee REST API (Spring Boot + PostgreSQL + Spring Security)

API REST per la gestione di dipendenti (employees), sviluppata come progetto di apprendimento su Spring Boot con persistenza reale tramite **JPA/Hibernate**, database **PostgreSQL**, e autenticazione/autorizzazione basata su ruoli con **Spring Security**.

Questo progetto rappresenta il secondo step di un percorso didattico più ampio: dopo un primo progetto CRUD in memoria ([Studente API](https://github.com/juscarni/Studente--API)), questo introduce la persistenza dei dati reale, relazioni tra entità e un sistema di sicurezza completo.

## Tecnologie utilizzate

- **Java 21**
- **Spring Boot 4.0.7**
- **Spring Security** (autenticazione Basic Auth + autorizzazione basata su ruoli)
- **Hibernate ORM 7.2** (JPA)
- **PostgreSQL** (database relazionale)
- **Maven**
- **Lombok** (riduzione del boilerplate su getter/setter/costruttori)
- **Jackson** (`tools.jackson` / `JsonMapper`) per la serializzazione JSON e gli aggiornamenti parziali (PATCH)
- **springdoc-openapi** (documentazione Swagger UI)
- **Spring Boot DevTools** (hot reload in sviluppo)

## Architettura

Il progetto segue un'architettura a livelli classica:

```
Controller (REST) → Service (logica di business) → DAO (EntityManager) → Database (PostgreSQL)
                                                              ↑
                                              Security (autenticazione/autorizzazione)
```

- **`EmployeeRestController`** — espone gli endpoint REST, delega tutta la logica al Service
- **`EmployeeService`** — orchestrazione, validazione dei dati in ingresso, gestione delle eccezioni di dominio, gestione transazionale delle operazioni composite (es. creazione di un employee con un ruolo)
- **`EmployeeDAOImpl` / `RoleDAOImpl`** — accesso ai dati tramite `EntityManager`
- **`Employee` / `Role`** — entità JPA in relazione **Many-to-Many** (un employee può avere più ruoli, un ruolo può appartenere a più employee)
- **`EmployeeUserDetailsService`** — implementazione custom di `UserDetailsService`, riutilizza `EmployeeDAO` per recuperare l'utente e i suoi ruoli al momento del login
- **`SecurityConfig`** — configura la catena di filtri di sicurezza (`SecurityFilterChain`), le regole di autorizzazione per endpoint e il `PasswordEncoder`

La responsabilità della validazione è centralizzata nel Service: il DAO riceve sempre dati già verificati.

## Sicurezza: autenticazione e autorizzazione

L'autenticazione utilizza **HTTP Basic Auth**, con le password degli utenti salvate in forma hashata (**BCrypt**) nella colonna `password` della tabella `employees`.

L'autorizzazione è basata sui ruoli assegnati a ciascun employee (`USER`, `MANAGER`, `ADMIN`), gestiti tramite una relazione Many-to-Many con la tabella `roles`:

| Metodo | Endpoint | Ruoli autorizzati |
|---|---|---|
| GET | `/api/employees/**` | `USER`, `MANAGER`, `ADMIN` |
| POST | `/api/employees/**` | `MANAGER`, `ADMIN` |
| PATCH | `/api/employees/**` | `MANAGER`, `ADMIN` |
| DELETE | `/api/employees/**` | `ADMIN` |

La protezione **CSRF** è disabilitata, poiché l'API è stateless (nessuna sessione/cookie, autenticazione ad ogni richiesta tramite header `Authorization`).

### Struttura del database (relazione Many-to-Many)

```
employees ←──── employee_roles ────→ roles
   id                employee_id       id
   email             role_id           role
   password
   first_name
   last_name
```

## Funzionalità

- Operazioni CRUD complete su un'entità `Employee`
- Ricerca per ID e per email
- Aggiornamento parziale (**PATCH**) tramite `Map<String, Object>` e `JsonMapper.updateValue()`, con protezione esplicita contro la modifica dell'id tramite payload
- Gestione centralizzata delle eccezioni con classi custom (`EmployeeNotFoundException`, `EmployeeExceptionHandler`), tradotta in risposte HTTP appropriate (404, 400, 401, 403) con corpo JSON strutturato:
  ```json
  {
    "message": "there is not an employee in the database with the id : 25",
    "timestamp": 1784920835987,
    "status": 404
  }
  ```
- Autenticazione HTTP Basic Auth con password hashate (BCrypt)
- Autorizzazione granulare per endpoint, basata sui ruoli dell'utente autenticato
- Popolamento automatico di dati di esempio all'avvio dell'applicazione tramite `CommandLineRunner` (`DataSeeder`), con logica *find-or-create* per evitare la duplicazione dei ruoli in database

## Endpoint disponibili

| Metodo | Endpoint | Descrizione | Ruoli richiesti |
|---|---|---|---|
| GET | `/api/employees` | Restituisce tutti gli impiegati | USER, MANAGER, ADMIN |
| GET | `/api/employees/{id}` | Restituisce un impiegato tramite ID | USER, MANAGER, ADMIN |
| GET | `/api/employees/email/{email}` | Restituisce un impiegato tramite email | USER, MANAGER, ADMIN |
| POST | `/api/employees` | Crea un nuovo impiegato | MANAGER, ADMIN |
| PATCH | `/api/employees/{id}` | Aggiorna parzialmente un impiegato esistente | MANAGER, ADMIN |
| DELETE | `/api/employees/{id}` | Elimina un impiegato tramite ID | ADMIN |

Tutte le richieste richiedono autenticazione HTTP Basic Auth (header `Authorization: Basic <credenziali>`).

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

### Esempio di risposta in caso di autorizzazione insufficiente

```json
{
    "timestamp": "2026-07-26T13:22:46.070Z",
    "status": 403,
    "error": "Forbidden",
    "message": "Forbidden",
    "path": "/api/employees/5"
}
```

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
3. L'applicazione parte di default sulla porta `8080`. Al primo avvio, `DataSeeder` popola automaticamente il database con employee di esempio, ciascuno con una password (hashata) e un ruolo assegnato.

## Documentazione Swagger

L'API è documentata tramite **springdoc-openapi**, con Swagger UI configurata su un path personalizzato:

```properties
springdoc.swagger-ui.path=/index.html
```

Una volta avviata l'applicazione, la documentazione interattiva è disponibile su:

```
http://localhost:8080/index.html
```

## Note

Questo progetto fa parte di un percorso di apprendimento in più tappe sullo sviluppo backend con Spring Boot:

1. CRUD in memoria ([Studente API](https://github.com/juscarni/Studente--API)) — logica di base senza persistenza
2. **CRUD con persistenza reale, relazioni Many-to-Many e Spring Security (questo progetto)**
3. Migrazione verso `JpaRepository` (Spring Data JPA)
4. Autenticazione con JWT (in programma)

## Autore

Nsayi Juscarni Geoffroy
Progetto realizzato a scopo didattico per approfondire Spring Boot, JPA/Hibernate, la progettazione di API REST e Spring Security.

- [LinkedIn](https://linkedin.com/in/juscarni-geoffroy-nsayi-950a8b306)
- [GitHub](https://github.com/juscarni)