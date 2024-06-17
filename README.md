# Tetris Schulprojekt
##### Collaborators
- Omori101
- Petkovic
- Stanislav
- EyyAlda


# Die wichtigsten Git commands

### Beim ersten Einrichten (nur einmal notwendig)

- Füge eine E-Mail Adresse hinzu (sinnvoll die Gleiche wie auf Github zu benutzen)
```shell
git config --global user.email <E-Mail Adresse>
```
- Füge einen Benutzernamen hinzu
```shell
git config --global user.name "<Benutzername>"
```
- Setze den Standard Branch Name auf "main"
```shell
git config --global init.defaultBranch main
```


### Lokales Repository

- Erstelle ein lokales Repository (Github dient nur als Backup Repo)
```shell
git init
```

- verbinde lokales Repository mit dem erstellten Github Repository
```shell
git remote add <name (egal was)> <https repository Link>
```
- führe einmal aus:
```
git pull <name_vom_Repo(oben festgelegt)> <branch(meistens main)>
```
- und danach:
```
git branch --set-upstream-to=<Name von Repo>/<Branch> main
```
- dadurch wird festgelegt, woher gepullt und wohin gepushed werden soll

##### Um eine Datei in das Repo zu bewegen braucht man 2 Schritte
1. vormerken
Im staging Bereich befinden sich die Dateien, die man geplant hat zum Repo hinzuzufügen, aber es noch nicht vollständig gemacht hat.
Man muss Dateien erstmal vormerken, bevor sie ins Repo können.
Um Dateien vorzumerken, benutzt man:
```shell
git add <Dateiname/Ordnername>
```
Um alle geänderten Dateien gleichzeitig zu verschieben:
```shell
git add .
```
Um zu prüfen, welche Dateien vorgemerkt sind, benutzt man
```shell
git status
```
um allen Dateien die Vormerkung zu entfernen, benutzt man
```shell
git reset
```
Alle Dateien in diesem Bereich sind zu "Commit vorgemerkt".
2. ins Repo schieben
Um die vorgemerkten Dateien ins Repo zu schieben, benutzt man
```shell
git commit
```
Nachdem "Enter" gedrückt wurde öffnet sich ein Texteditor.
Bei allen Dateien, die ins Repo sollen muss das '#' entfernt werden.
	
Fertig.

### Mit entferntem Repo arbeiten
Um neue Dateien aus dem Repo zu bekommen, nutzt man
```shell
git pull
```
Dieser Command sollte **jedes** mal bevor am Projekt gearbeitet wird einmal ausgeführt werden.

Um die Änderungen in das entfernte Repo hochzuladen, benutzt man
```shell
git push
```
Beim Push wird man immer wieder nach seinen Zugangsdaten gefragt.
bei Benutzername die E-Mail Adresse von Github benutzen.
Bei Passwort muss man erstmal einen Token generieren, der dann angegeben wird. (**nicht** das Accountpasswort). Um einen Token zu generieren geht man auf der Webseite auf 
Accounteinstellungen --> Developer Settings --> Token(Classic) --> generate new Token --> stelle expiration Date auf "none" und wähle unten alle Berechtigungen aus.
Schreibe dir den Token irgendwo auf und behandle ihn wie ein Passwort.

**Immer darauf achten, was einem angezeigt wird und eventuell Anweisungen befolgen**

[für den Unterschied zwischen Merge und Rebase](https://youtu.be/zOnwgxiC0OA?si=eBJiTjI8ZlqYVQQ)

### Repo löschen
Um ein local git repository zu löschen muss man nur->.
```
rm -fr .git
```

Eingeben und dann ->.
```
git status
```

Um zu sehen ob es wirklich gelöscht wurde.
Wenn es geloscht wurde dann kommt eine fatal fehler meldung.
fatal: not a git repository (or any parent up to mount point /)
Stopping at filesystem boundary (GIT_DISCOVERY_ACROSS_FILESYSTEM not set).
