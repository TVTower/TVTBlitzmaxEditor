Dieser Xtext-Editor soll die Entwicklung von TVTower mit Eclipse unterstützten.
Die Sprache wird nicht komplett unterstützt.
Ziel ist aber insb. eine schnellere Navigation innerhalb einer Datei (Outline und Quick-Outline) und das leichtere Auffinden von Typen (shift-ctrl F3 - Find Model Element).

Etwas Syntax-Highlighting bekommt man geschenkt sowie die Navigation zu (selbstdefinierten) Typen (Methoden/Funktionsdeklaration und Parameter).

Die Autovervollständigung sollte nicht verwendet werden.
Zum einen liefert sie nur wenig hilfreiches und ist bei den großen Dateien zu langsam (Neuparsen des Präfix nötig).

# Phase 1

* Dateien mit möglichst wenigen Syntaxfehlern parsen
* Outline für Typen, Funktionen und Methoden, damit man schneller sieht, was so definiert ist
* Abbilden des gesamten AST zu aufwändig - alles innerhalb der Methoden "wegparsen"
    * Damit hat man zwar keine Autovervollständigung und Navigation innerhalb der Methoden, aber es ist allemal besser als reine Textsuche

## Vorgehen für Syntaxerweiteurngen

* Grammatik-Inspiration durch https://github.com/woollybah/eclipsemax.git
* Parse-Fehler in Prod-Dateien identifizieren
* Tests um minimales Syntaxbeispiel erweitern - muss neuen Fehler liefern
* Grammatik anpassen bis alle Tests grün sind

## Was geht nicht

* Die verschiedenen Varianten zum Abschließen eines Blocks sind aus Tooling-Bau-Sicht ein Graus. Mit Leerzeichen getrennte Schlüsselwörter mit gleinem Präfix `End X` machen das Leben unnötig schwer. Warum nicht einfach immer `EndMethod`, `EndIf`... Den Programmabbruch mit `End` fange ich aktuell ab, indem ich `End` sofort gefolgt von einem Zeilenumbruch als separates Token behandele, das vor dem Parser versteckt wird.
* Die Compileroptionen können nicht vollstänndig unterstützt werden. Im Moment fange ich sie einfach weg. Das geht insb. in den Fällen schief, wo nur die Methodensignatur ausgetauscht wird, der Body aber erhalten bleibt (2xStart aber nur einmal Ende).

## TODOs

* Syntax
    * Local, Global in File modelliert - Navigation aus Methode heraus
* Default-Import statt Warnungen unterdrücken (Auslieferung im Plugin)
* F3 auf Imports ermöglichen (zur Datei springen)
* file members für outline auch zusammenfassen
    * zusammenfassen nur wenn mehr als 3 Member
* noch mehr Schlüsselworte ins "Anything" (highlighting)

* update site bauen und veröffentlichen
* Oomph-Setup
    * Editor
    * clonen von TVTower und Loc-Tool
* fragen wegen Start-Character - aus Dateien raus oder als hidden Token unterdrücken