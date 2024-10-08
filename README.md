# Tetris Schulprojekt
##### Collaborators
- Omori101
- Petkovic
- Stanislav
- EyyAlda

# Mögliche Projektstruktur

### Main
**Aufgaben:**
- Startbildschirm 
- Startbutton
- Einstellungen
- About Page

### Game
**Aufgaben:**
- das Spiel an sich.

### Backend
**Aufgaben:**
- Speichert Historie/highscores und vllt. Profile in JSON
- erstellt einen Spieleordner in den Dokumenten mit den Dateien


## Java Klassen verbinden

- Klassen können importiert werden mit
  ```java
  import com.tetris.<modul>;
  ```
- statische Funktionen werden so eingebunden:
  ```java
  <modul>.<function>;
  ```
- Nicht statische Funktionen muss man direkt importieren:
  ```java
  import com.tetris.<modul>.<Funktion>;
- für nicht statische Funktionen muss man eine neue Instanz der Funktion erstellen:
  ```java
  <Funktion> <Name, die die Funktion haben soll> = new <Funktion>();
  ```
    
[Erklärung von ChatGPT](https://chatgpt.com/share/88c964fb-c28e-4f39-a37d-f2d72c69bbce)

## Wie benutzt man die Backend Funktionen?

### profilesList() (static)
Wird abgerufen durch:
```java
Backend.profilesList();
```
- gibt einem einen String-Array mit allen Profilnamen zurück

### readJSON() (static)

Wird abgerufen durch:
```java
Backend.readJSON(String type, String param, (String path))
```
Die funktion hat 3 modi:
- Languagepacks lesen (type = lang)
  Parameter param ist der Name der sprachdatei ohne .json und lang_
  Parameter path hat keinen Einfluss
- Profiledata lesen (type = profile)
 	- Parameter param ist der Name des Profils ohne .json und profile_
	- Parameter path hat keinen Einfluss
- custom (type = custom)
  	- Parameter param hat keinen Einfluss
  	- Parameter path gibt an, welche Datei gelesen werden soll

  Die Funktion gibt eine Hashmap in der Form <String, Object> zurück.
**wichtig: wenn kein type-Parameter angegeben ist, gibt die Funktion immer 'null' zurück**

### writeProfiles() (static)
Wird abgerufen durch:
```java
Backend.writeProfiles(Hashmap<String, Object> map, String profileName);
```
- Die Funktion gibt ```true```zurück, wenn es funktioniert hat und ```false```wenn nicht.
- In der Hashmap sollten die Daten drinstehen, die später in der Json-Datei sein sollen.

### readConfig() (static)
Wird abgerufen durch:
```java
Backend.readConfig(boolean isCached, Hashmap<String, Object> conf);
```
- Der Parameter isCached gibt an ob die Config schon mal geladen wurde (hilft, falls sie vom user gelöscht wurde sie wiederherzustellen) (normalerweise true)
- Der Hashmap-Parameter ist der aktuell geladene Zustand der Config. Wenn es keine Config-Datei gibt, wird automatisch eine Neue erstellt.
- Es wird eine Hashmap zurückgegeben

### writeConfig() (static)
Wird abgerufen durch:
```java
Backend.writeConfig(Hashmap<String, Object> conf);
```
- Gibt einen boolean zurück
- Wird auch zur wiederherstellung durch ein nichtvorhandensein durch die readConfig-Funktion ausgeführt


# Die wichtigsten Git commands

### Beim ersten Einrichten (nur einmal notwendig)

- Füge eine E-Mail Adresse hinzu (sinnvoll die Gleiche wie auf Github zu benutzen)
```bash
git config --global user.email <E-Mail Adresse>
```
- Füge einen Benutzernamen hinzu
```bash
git config --global user.name "<Benutzername>"
```
- Setze den Standard Branch Name auf "main"
```bash
git config --global init.defaultBranch main
```


### Lokales Repository

- Erstelle ein lokales Repository (Github dient nur als Backup Repo)
```bash
git init
```

- verbinde lokales Repository mit dem erstellten Github Repository
```bash
git remote add <name (egal was)> <https repository Link>
```
- führe einmal aus:
```bash
git pull <name_vom_Repo(oben festgelegt)> <branch(meistens main)>
```
- und danach:
```bash
git branch --set-upstream-to=<Name von Repo>/<Branch> main
```
- dadurch wird festgelegt, woher gepullt und wohin gepushed werden soll

##### Um eine Datei in das Repo zu bewegen braucht man 2 Schritte
1. vormerken
Im staging Bereich befinden sich die Dateien, die man geplant hat zum Repo hinzuzufügen, aber es noch nicht vollständig gemacht hat.
Man muss Dateien erstmal vormerken, bevor sie ins Repo können.
Um Dateien vorzumerken, benutzt man:
```bash
git add <Dateiname/Ordnername>
```
Um alle geänderten Dateien gleichzeitig zu verschieben:
```bash
git add .
```
Um zu prüfen, welche Dateien vorgemerkt sind, benutzt man
```bash
git status
```
um allen Dateien die Vormerkung zu entfernen, benutzt man
```bash
git reset
```
Alle Dateien in diesem Bereich sind zu "Commit vorgemerkt".
2. ins Repo schieben
Um die vorgemerkten Dateien ins Repo zu schieben, benutzt man
```bash
git commit
```
Nachdem "Enter" gedrückt wurde öffnet sich ein Texteditor.
Bei allen Dateien, die ins Repo sollen muss das '#' entfernt werden.
	
Fertig.

### Mit entferntem Repo arbeiten
Um neue Dateien aus dem Repo zu bekommen, nutzt man
```bash
git pull
```
Dieser Command sollte **jedes** mal bevor am Projekt gearbeitet wird einmal ausgeführt werden.

Um die Änderungen in das entfernte Repo hochzuladen, benutzt man
```bash
git push
```
Beim Push wird man immer wieder nach seinen Zugangsdaten gefragt.
bei Benutzername die E-Mail Adresse von Github benutzen.
Bei Passwort muss man erstmal einen Token generieren, der dann angegeben wird. (**nicht** das Accountpasswort). Um einen Token zu generieren geht man auf der Webseite auf 
Accounteinstellungen --> Developer Settings --> Token(Classic) --> generate new Token --> stelle expiration Date auf "none" und wähle unten alle Berechtigungen aus.
Schreibe dir den Token irgendwo auf und behandle ihn wie ein Passwort.

**Immer darauf achten, was einem angezeigt wird und eventuell Anweisungen befolgen**

[für den Unterschied zwischen Merge und Rebase](https://youtu.be/zOnwgxiC0OA?si=eBJiTjI8ZlqYVQQ)

[Falls es zu Problemen beim pull kommt](https://youtu.be/xN1-2p06Urc?si=ZpgbK8cw6etExXyy)

### Repo löschen
Um ein local git repository zu löschen muss man nur->.
```bash
rm -fr .git
```

Eingeben und dann ->.
```bash
git status
```

Um zu sehen ob es wirklich gelöscht wurde.
Wenn es geloscht wurde dann kommt eine fatal fehler meldung.
fatal: not a git repository (or any parent up to mount point /)
Stopping at filesystem boundary (GIT_DISCOVERY_ACROSS_FILESYSTEM not set).
