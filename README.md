[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/mediathekview/MServer.svg?branch=master)](https://travis-ci.org/mediathekview/MServer)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mediathekview_MServer&metric=alert_status)](https://sonarcloud.io/dashboard?id=mediathekview_MServer)

# MServer
Server zum crawlen der Mediatheken. Teil von [MediathekView](https://github.com/mediathekview).

# Entwicklung

## Code auschecken
```bash
mkdir mediathekview
cd mediathekview
git clone https://github.com/mediathekview/MServer.git
git clone https://github.com/mediathekview/Mlib.git
```

## Bauen und starten an der Kommandozeile
```bash
cd MServer
./gradlew run
```

## Einstellungen

Kompression zu xz Datei deaktivieren:
```bash
export NOCOMPRESS=y
java -jar MServer.jar
```

## Entwicklung mit Netbeans
* Verzeichnisse `MLib` und `MServer` mit Netbeans öffnen

## Entwicklung mit Eclipse
* Falls noch nicht vorhanden: [Plugin buildship](https://projects.eclipse.org/projects/tools.buildship) installieren
* Projekt `MServer` als Gradle-Projekt importieren. `MLib` wird automatisch mit importiert.


# Überblick der Crawler

| Crawler | liest Mediathek | beinhaltet Sender | bestückt Sender | entspricht Develop |
|---------|-----------|--------|---------|--|
| 3sat|3sat-Mediathek|3sat |3sat|x|
| ARD|ARD-Mediathek|Alpha, BR, Das Erste, HR, MDR, NDR, ONE, Radio Bremen, RBB, SR, SWR, WDR, tagesschau24|ARD, MDR, NDR, Radio Bremen, RBB, SWR| x|
| ARTE|ARTE-Mediathek|ARTE.DE, ARTE.FR|ARTE.DE, ARTE.FR||
| BR|BR-Mediathek|BR|BR||
| DW|DW-Mediathek|DW|DW||
| HR|HR-Mediathek|HR|HR||
| KIKA|KIKA-Mediathek|KIKA|KIKA||
| ORF|ORF-Mediathek|ORF1, ORF2, ORF3, ORFSport|ORF|x|
| PHOENIX|PHOENIX-Mediathek|PHOENIX|PHOENIX|x|
| SR|SR-Mediathek|SR|SR|x|
| SRF|SRF-Mediathek|SRF1, SRF2, SRFinfo|SRF|x|
| WDR|WDR-Mediathek|WDR|WDR||
| ZDF|ZDF-Mediathek|ZDF, ZDFneo, ZDFinfo|ZDF|x|