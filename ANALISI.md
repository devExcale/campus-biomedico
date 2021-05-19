# Analisi DB

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
- **Prestazione** ( <ins>id_prestazione</ins>, costo, descrizione )
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
    descrizione    varchar(32),
    costo          float NOT NULL
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
