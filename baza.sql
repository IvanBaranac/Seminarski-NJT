DROP DATABASE IF EXISTS prijemni;

CREATE DATABASE prijemni
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE prijemni;

CREATE TABLE korisnik (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    ime           VARCHAR(60)  NOT NULL,
    prezime       VARCHAR(60)  NOT NULL,
    email         VARCHAR(120) NOT NULL,
    lozinka       VARCHAR(100) NOT NULL,
    telefon       VARCHAR(30)      NULL,
    srednja_skola VARCHAR(120)     NULL,
    uloga         VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_korisnik_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE termin (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    datum           DATE          NOT NULL,
    vreme_pocetka   TIME          NOT NULL,
    vrsta_ispita    VARCHAR(30)   NOT NULL,
    adresa          VARCHAR(160)  NOT NULL,
    kapacitet       INT           NOT NULL,
    popunjeno_mesta INT           NOT NULL,
    cena            DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE prijava (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    kandidat_id     BIGINT        NOT NULL,
    datum_prijave   DATE          NOT NULL,
    status          VARCHAR(20)   NOT NULL,
    osnovna_cena    DECIMAL(10,2) NOT NULL,
    popust_procenat INT           NOT NULL,
    ukupna_cena     DECIMAL(10,2) NOT NULL,
    poziv_na_broj   VARCHAR(30)   NOT NULL,
    rok_za_uplatu   DATE          NOT NULL,
    nacin_placanja  VARCHAR(20)       NULL,
    datum_uplate    DATE              NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_prijava_poziv (poziv_na_broj),
    CONSTRAINT fk_prijava_kandidat FOREIGN KEY (kandidat_id) REFERENCES korisnik (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE stavka_prijave (
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    prijava_id BIGINT        NOT NULL,
    termin_id  BIGINT        NOT NULL,
    cena       DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_stavka (prijava_id, termin_id),
    CONSTRAINT fk_stavka_prijava FOREIGN KEY (prijava_id) REFERENCES prijava (id),
    CONSTRAINT fk_stavka_termin  FOREIGN KEY (termin_id)  REFERENCES termin (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO korisnik (ime, prezime, email, lozinka, telefon, srednja_skola, uloga) VALUES
('Marija', 'Jovanović', 'admin@fon.bg.ac.rs',
 '$2b$10$R1lSf3GOSq5UWMGRgZubXuSK2EDUFje2TyzvR2UuYhD38wPP8jtqW',
 '011/3950-800', NULL, 'ADMIN'),
('Petar', 'Petrović', 'pera@primer.rs',
 '$2b$10$bfYpNAbRblZAD6frJF/XK.slPio/Mg9Cl.7QjJOjVcCzbz2bVqmGy',
 '064/123-456', 'Gimnazija Sveti Sava, Beograd', 'KANDIDAT'),
('Jovana', 'Ilić', 'jovana@primer.rs',
 '$2b$10$bfYpNAbRblZAD6frJF/XK.slPio/Mg9Cl.7QjJOjVcCzbz2bVqmGy',
 '063/987-654', 'Matematička gimnazija, Beograd', 'KANDIDAT');

INSERT INTO termin (datum, vreme_pocetka, vrsta_ispita, adresa, kapacitet, popunjeno_mesta, cena) VALUES
(DATE_ADD(CURDATE(), INTERVAL  7 DAY), '10:00:00', 'MATEMATIKA',
 'FON, Jove Ilića 154, Beograd', 40, 0, 2500.00),
(DATE_ADD(CURDATE(), INTERVAL 10 DAY), '13:00:00', 'OPSTA_INFORMISANOST',
 'FON, Jove Ilića 154, Beograd', 40, 0, 2000.00),
(DATE_ADD(CURDATE(), INTERVAL 14 DAY), '10:00:00', 'MATEMATIKA',
 'Gimnazija Jovan Jovanović Zmaj, Zlatne grede 4, Novi Sad', 30, 0, 2500.00),
(DATE_ADD(CURDATE(), INTERVAL 21 DAY), '11:00:00', 'OPSTA_INFORMISANOST',
 'Gimnazija Jovan Jovanović Zmaj, Zlatne grede 4, Novi Sad', 30, 0, 2000.00),
(DATE_ADD(CURDATE(), INTERVAL 28 DAY), '10:00:00', 'MATEMATIKA',
 'FON, Jove Ilića 154, Beograd', 50, 0, 2500.00);

SELECT COUNT(*) AS korisnika FROM korisnik;
SELECT COUNT(*) AS termina FROM termin;
