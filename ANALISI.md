# <div align="center">Analisi DB</div>

## Ipotesi

- I pazienti, medici e amministratori possono effettuare il login sulla piattaforma attraverso il codice fiscale e la
  password;
- Ad una visita (prenotazione) possono presiedere più medici;
- Al paziente vengono registrate più anamnesi nel tempo;
- Dell'anagrafica è tutto obbligatorio tranne i dati di residenza;

## Diagramma E/R

<img src="img/Diagramma E-R.png" alt="Immagine diagramma E/R"/>

## Modello logico relazionale

*Nota: gli attributi sottolineati sono PK, gli attributi con l'asterisco sono FK*

- **Paziente** ( <ins>cod_f</ins>, nome, cognome, data_n, email, cellulare, via_residenza, citta, cap, password )
- **Anamnesi** ( <ins>id_anamnesi</ins>, descrizione, data, cod_f* )
- **Prestazione** ( <ins>id_prestazione</ins>, denominazione, costo, descrizione )
- **Medico** ( <ins>cod_f</ins>, nome, cognome, data_n, email, cellulare, via_residenza, citta, cap, password )
- **Prenotazione** ( <ins>id_prenotazione</ins>, data, ora, cod_p*, id_prestazione* )
- **Presidio** ( <ins>id_prenotazione*, cod_m*</ins> )
- **Corso** ( <ins>id_corso</ins>, descrizione, data, ora_inizio, ora_fine )
- **PartecipazioneCorso** ( <ins>id_corso*, cod_m*</ins> )
- **Amministratore** ( <ins>cod_f</ins>, nome, cognome, data_n, email, cellulare, via_residenza, citta, cap, password )

## Definizioni SQL

Si sceglie di implementare il database usando il RDBMS PostgreSQL, di conseguenza *qualcosa* condizioni:

- In caso la PK sia `integer`, nella definizione della tabella si userà `serial`, alias di `integer` + `AUTO INCREMENT`;
- In caso nella tabella ci siano due colonne di tipo `data` e `ora`, queste colonne verranno unificate sotto un'unica
  colonna di tipo `timestamp`, tipo che comprende sia data che ora;
- `TODO: integrità referenziale`;

### TABELLA `paziente`

```sql
CREATE TABLE paziente (
    cod_f         char(16) PRIMARY KEY,
    nome          varchar(32) NOT NULL,
    cognome       varchar(32) NOT NULL,
    data_n        date        NOT NULL,
    email         varchar(48) NOT NULL,
    cellulare     varchar(14) NOT NULL,
    via_residenza varchar(48),
    citta         varchar(24),
    cap           char(5),
    password      varchar(32) NOT NULL
);
```

### TABELLA `anamnesi`

```sql
CREATE TABLE anamnesi (
    id_anamnesi serial PRIMARY KEY,
    descrizione text                      NOT NULL,
    data        date default CURRENT_DATE NOT NULL,
    cod_p       char(16)                  NOT NULL,
    FOREIGN KEY (cod_p) REFERENCES paziente
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

### TABELLA `prestazione`

```sql
CREATE TABLE prestazione (
    id_prestazione serial PRIMARY KEY,
    denominazione  varchar(32) NOT NULL,
    costo          float       NOT NULL,
    descrizione    text        NOT NULL
);
```

### TABELLA `medico`

```sql
CREATE TABLE medico (
    cod_f         char(16) PRIMARY KEY,
    nome          varchar(32) NOT NULL,
    cognome       varchar(32) NOT NULL,
    data_n        date        NOT NULL,
    email         varchar(48) NOT NULL,
    cellulare     varchar(14) NOT NULL,
    via_residenza varchar(48),
    citta         varchar(24),
    cap           char(5),
    password      varchar(32) NOT NULL
);
```

### TABELLA `prenotazione`

```sql
CREATE TABLE prenotazione (
    id_prenotazione serial PRIMARY KEY,
    data            timestamp NOT NULL,
    cod_p           char(16)  NOT NULL,
    id_prestazione  integer   NOT NULL,
    FOREIGN KEY (cod_p) REFERENCES paziente
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_prestazione) REFERENCES prestazione
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

### TABELLA `presidio`

```sql
CREATE TABLE presidio (
    id_prenotazione integer  NOT NULL,
    cod_m           char(16) NOT NULL,
    PRIMARY KEY (id_prenotazione, cod_m),
    FOREIGN KEY (id_prenotazione) REFERENCES prenotazione
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (cod_m) REFERENCES medico
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

### TABELLA `corso`

```sql
CREATE TABLE corso (
    id_corso    serial PRIMARY KEY,
    descrizione text      NOT NULL,
    ora_inizio  timestamp NOT NULL,
    ora_fine    timestamp NOT NULL
);
```

### TABELLA `partecipazione_corso`

```sql
CREATE TABLE partecipazione_corso (
    id_corso integer  NOT NULL,
    cod_m    char(16) NOT NULL,
    PRIMARY KEY (id_corso, cod_m),
    FOREIGN KEY (id_corso) REFERENCES corso
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (cod_m) REFERENCES medico
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

### TABELLA `amministrazione`

```sql
CREATE TABLE amministrazione (
    cod_f         char(16) PRIMARY KEY,
    nome          varchar(32) NOT NULL,
    cognome       varchar(32) NOT NULL,
    data_n        date        NOT NULL,
    email         varchar(48) NOT NULL,
    cellulare     varchar(14) NOT NULL,
    via_residenza varchar(48),
    citta         varchar(24),
    cap           char(5),
    password      varchar(32) NOT NULL
);
```

## Queries

### Query 1.

*Visualizzare cognome e nome dei pazienti prenotati dal 01-05-2021 al 31-05-2021, per una prestazione specificata
attraverso il codice, l’elenco è ordinato per cognome in modo crescente.*

```sql
SELECT DISTINCT pa.nome, pa.cognome
FROM paziente pa
         INNER JOIN prenotazione pr on pa.cod_f = pr.cod_p
WHERE pr.data >= '2021-05-01'
  AND pr.data <= '2021-05-31'
  AND pr.id_prestazione = ?
ORDER BY cognome;
```

### Query 2.

*Visualizzare per ogni corso di aggiornamento il codice, la descrizione e il numero di corsisti.*

```sql
SELECT c.id_corso, c.descrizione, count(pc.cod_m) n_corsisti
FROM corso c
         INNER JOIN partecipazione_corso pc on c.id_corso = pc.id_corso
GROUP BY c.id_corso, c.descrizione;
```

### Query 3.

*Visualizzare il tipo della prestazione con il maggior numero di prenotazioni.*

```sql
SELECT p.id_prestazione, p.denominazione, t.n_prenotazioni
FROM prestazione p
         INNER JOIN (
    SELECT id_prestazione, count(id_prenotazione) n_prenotazioni
    FROM prenotazione p
    GROUP BY id_prestazione) t ON p.id_prestazione = t.id_prestazione
GROUP BY p.id_prestazione, p.denominazione
HAVING t.n_prenotazioni = MAX(t.n_prenotazioni);
```

### Query 4.

*Visualizzare per ogni medico il cognome, il nome e la data in cui hanno visitato più di 10 pazienti. L’elenco è
ordinato, in modo crescente, per cognome e per data.*

```sql
SELECT m.cognome, m.nome, pn.data, count(pn.id_prenotazione) n_visite
FROM medico m
         INNER JOIN presidio ps on m.cod_f = ps.cod_m
         INNER JOIN prenotazione pn on pn.id_prenotazione = ps.id_prenotazione
GROUP BY m.cognome, m.nome, pn.data
HAVING count(pn.id_prenotazione) > 10
ORDER BY m.cognome, m.nome, pn.data;
```