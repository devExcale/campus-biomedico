# <div align="center">ESAME DI STATO ISTITUTO TECNICO INDUSTRIALE a.s. 2020-2021</div>

### <div align="center">Indirizzo: ITIA - INFORMATICA E TELECOMUNICAZIONI ARTICOLAZIONE INFORMATICA</div>

## <div align="center">Elaborato di: INFORMATICA e SISTEMI E RETI</div>

***Il candidato (che potrà eventualmente avvalersi delle conoscenze e competenze maturate attraverso esperienze di PCTO,
stage o formazione in azienda) svolga la seguente prova.***

Il Campus Bio-Medico "Rosalind Franklin" comprende due presidi:
un Centro di Ricerca Medica con sede a Roma e una Clinica con sede a Firenze.

Il Centro di Ricerca Medica di Roma ha una sede in un ambiente costituito da due padiglioni. In un padigione si svolgono
convegni e corsi di aggiornamento per il personale sanitario, si trovano alcuni locali dove si effettuano ricerche di
laboratorio e in alcuni ambulatori vengono erogate prestazioni sanitarie quali visite mediche ed esami diagnostici. Il
secondo padiglione della struttura è completamente dedicato alla degenza dei pazienti.

La Clinica di Firenze si occupa solo di visite mediche e di esami diagnostici, sia in regime ambulatorio che di degenza.

Per effettuare campagne di prevenzione attraverso uno screening sulla popolazione il Campus predispone un Camper,
appositamente attrezzato.

Il Camper si sposta nelle varie regioni italiane per consentire ai cittadini di fruire gratuitamente delle visite di
screening.

I dati relativi al paziente, le prestazioni erogate (visite mediche, esami diagnostici)
e le anamnesi devono essere inviati dai medici all'archivio centrale del Campus. Dallo stesso Camper i medici devono
poter prenotare gli esami diagnostici di cui i pazienti potrebbero aver bisogno, presso una delle due sedi
dell'Istituto.

Tutti i risultati degli screening effettuati dal Campus devono essere messi a disposizione del Ministero della Salute
per elaborazioni a fini statistici.

L'attività del Campus Bio-Medico viene gestita attraverso il Sito web a cui accedono medici, pazienti e personale
amministrativo. I pazienti possono prenotare le varie prestazioni direttamente sul Sito previa registrazione. Una volta
fatta la prenotazione riceveranno una email di conferma con l'indicazione di data e ora. Il personale medico accede al
Sito per controllare la lista dei pazienti del giorno e per inserire le anamnesi. I medici che sono all'interno del
Camper prenotano gli esami sempre a nome del paziente.

Il personale amministrativo accede al Sito per la gestione di corsi, convegni e pagamento delle prestazioni effettuate.

Il personale medico e quello amministrativo accede alla parte del Sito a loro riservato attraverso user e password.

Si vogliono memorizzare le informazioni su:

- Medici: anagrafica, email, cellulare;
- Pazienti: anagrafica, anamnesi, email, cellulare;
- Prestazioni: codice, tipo (visita medica, analisi, radiografia, ecc), costo;
- Prenotazioni: codice, data e ora della visita;
- Corsi e convegni: codice, descrizione, data, ora inizio ed ora fine.

Il candidato analizzi la realtà di riferimento, fornendo anche uno schema che evidenzi le principali funzionalità del
sistema, formuli le necessarie ipotesi iniziali e aggiuntive.

Individui una soluzione che a suo motivato giudizio sia la più idonea a sviluppare i seguenti punti:

1. fornisca una possibile soluzione per il progetto della WAN che consenta la comunicazione fra tutte le sedi dislocate
   nei diversi punti del territorio nazionale, motivando le scelte fatte;

2. fornisca una possibile soluzione per il progetto delle reti LAN facendo riferimento agli standard utilizzati, alla
   topologia di rete, disegnando lo schema fisico e lo schema logico della rete, spiegando le scelte fatte in relazione
   al posizionamento dei centri stella e dei locali, al cablaggio verticale e orizzontale. Disegni in dettaglio
   l'architettura dei centri stella e descriva gli elementi hardware utilizzati. Definisca il piano di indirizzamento
   delle reti;

3. fornisca una soluzione per la segmentazione fisica delle reti definendo il piano di indirizzamento per le sottoreti
   create;

4. definisca i servizi offerti dai server ai client della LAN e i servizi offerti dai server per fornire al Campus
   visibilità all’esterno; fornisca una soluzione per la presenza di server ipotizzando la loro collocazione interna
   alla struttura e la loro protezione; si individui anche una soluzione per la sicurezza della LAN;

5. proponga le diverse soluzioni, alternative alla collocazione dei server all’interno della struttura, esponendone
   vantaggi e svantaggi;

6. fornisca una soluzione per la segmentazione logica delle reti definendo il piano di indirizzamento per le sottoreti
   create;

7. la comunicazione in rete comporta l’esposizione delle trasmissioni dei dati a numerosi rischi di violazione. Il
   candidato proponga una tecnica per garantire la segretezza dei dati che viaggiano in linea;

8. a progettazione della base di dati per la gestione delle informazioni relative ai medici, ai pazienti, alle
   prestazioni, alle prenotazioni e ai corsi e/o convegni in particolare si richiede:
    1. il modello concettuale E-R;
    2. il corrispondente modello logico relazionale;

9. l’implementazione in linguaggio SQL della base di dati progettata;

10. lo sviluppo in linguaggio SQL delle query che consentono di:
    1. visualizzare cognome e nome dei pazienti prenotati dal 01-05-2021 al 31-05-2021, per una prestazione specificata
       attraverso il codice, l’elenco è ordinato per cognome in modo crescente;
    2. visualizzare per ogni corso di aggiornamento il codice, la descrizione e il numero di corsisti;
    3. visualizzare il tipo della prestazione con il maggior numero di prenotazioni;
    4. visualizzare per ogni medico il cognome, il nome e la data in cui hanno visitato più di 10 pazienti. L’elenco è
       ordinato, in modo crescente, per cognome e per data;

11. il progetto attraverso wireframe del sito web del Campus Bio-Medico ed in particolare delle pagine web che
    consentono le seguenti funzioni:
    1. la registrazione dei pazienti;
    2. la prenotazione delle prestazioni da parte dei pazienti con relativo invio dell'email di conferma;
    3. la visualizzazione della lista dei pazienti prenotati in una determinata data da parte del personale medico;
    4. l'inserimento dell'anamnesi per un determinato paziente;

12. la codifica delle pagine individuate nei linguaggi più idonei.