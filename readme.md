Dieser Xtext-Editor soll die Entwicklung von TVTower mit Eclipse unterstützten.
Die Sprache wird nicht komplett unterstützt.
Ziel ist aber insb. eine schnellere Navigation innerhalb einer Datei und das leichtere Auffinden von Typen.

* F3 - gehe zu (return-types, Funktionsparameter, extends, import ...)
* shift-ctrl F3 - Find Model Element
* ctrl-o quick outline
* Outline-View - Übersicht über definierte Typen, Funktionen...

Für das Aufsetzen der Entwicklungsumgebung kann die Oomph-Setup-Datei `https://raw.githubusercontent.com/TVTower/TVTBlitzmaxEditor/master/TVTBlitzmax.setup` verwendet werden (URL kopieren und als Github-Projekt anlegen).

Die Autovervollständigung sollte nicht verwendet werden.
Zum einen liefert sie nur wenig Hilfreiches und ist bei den großen Dateien zu langsam (Neuparsen des Präfix nötig).

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
* Die Compileroptionen können semantisch nicht unterstützt werden. Im Moment fange ich sie einfach weg. Für den Fall dass es zwei Varianten der Methodensignatur gibt, aber der Body erhalten bleibt, wurde der Lexer so angepasst, dass nur die letzte Signatur für den Parser sichtbar ist. Damit bleibt die Struktur syntaktisch korrekt; auch wenn vielleicht die falsche Option gewählt wurde.

## TODOs

* Syntax
    * Interface/Struct/Type (extern)
    * Local, Global in File modelliert - Navigation aus Methode heraus
    * even better comment recognition (fri*endrem*ove)
* Default-Import statt Warnungen unterdrücken (Auslieferung im Plugin)
* file members für outline auch zusammenfassen?
* noch mehr Schlüsselworte ins "Anything" (highlighting)
* offizielle update site? (Snapshot unter https://www.nittka.de/download/tvtower)