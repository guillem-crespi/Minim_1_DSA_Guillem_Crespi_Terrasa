# Projecte DSA - Mínim 1 (Drone Manager)

## Descripció del projecte
Aquest repositori conté l'entrega del Mínim 1 de l'assignatura. El projecte consisteix en una API REST desenvolupada en Java (arquitectura de 3 capes) per gestionar una flota de drons, els seus pilots i assignar plans de vol. Tal com indica la normativa de l'examen, no s'ha fet cap "push" intermedi per evitar compartir codi; s'ha pujat tota la feina de cop en finalitzar l'exercici.

## Desglossament de l'enunciat
S'han complert tots els requeriments demanats a la pràctica:
- **Part 1 (Models i Interfície):** S'han dissenyat les classes del domini (`Drone`, `Pilot`, `FlightPlan`, `Position`) i s'ha definit la interfície `DroneManager` amb els mètodes operatius.
- **Part 2 (Manager i API REST):**
    - S'ha implementat la classe `DroneManagerImpl` (Singleton) que gestiona les dades en memòria. S'han usat estructures eficients: `HashMap` per tenir accessos en O(1) i una pila `Stack` (LIFO) per al magatzem de reparacions.
    - S'han creat les rutes del servei REST (GET, POST, PUT) per exposar la lògica del Manager.
    - S'han fet les proves unitàries.

## Estat actual
El projecte està totalment completat. Les rutes REST s'han provat correctament. Per comprovar la lògica interna, he fet aquests 4 tests amb JUnit (i tots passen en verd):
- `test1_LlistarDrons`: Comprova que s'obté la llista de drons ordenada.
- `test2_EnviarAlMagatzem`: Verifica que un dron entra correctament a la llista de manteniment.
- `test3_RepararDronLIFO`: Comprova que en demanar una reparació, surt l'últim dron que ha entrat al magatzem (comportament Stack).
- `test4_CrearPlaDeVol`: Verifica que al crear un pla de vol, les hores estimades se sumen correctament al dron implicat.

## Errors / problemes trobats
- He tingut força problemes amb la nomenclatura al codi, ja que he anat mesclant la paraula "drone" i "drons" a les variables i a les rutes de l'API. Això em donava errors, ja que no em trobava els endpoints o les variables, però ja ho he unificat tot i funciona.
## S'ha afegit posteriorment
- He afegit captures on comprovo que funcionen correctament més funcions del programa.
---
**Autor:** Guillem Crespi Terrasa