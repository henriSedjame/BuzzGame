#Reactor / Spring webflux - Spring data R2DBC

### Reactor 
    
**Reactor** est librairie basée sur _les spécifications Reactive Streams_, permettant de créer des applications non bloquantes sur la JVM.

**Reactor** expose deux types principaux implémentant l'interface `Publisher<T>`:

- `Mono<T>` : représente une séquence réactive de 0 ou un item
- `Flux<T>` : représente une séquence réactive de 0 ou plusieurs items

Plusieurs opérateurs permettent de transformer les données émises:

Parmi les plus utilisées: `map, flatMap ...`

### Spring Webflux - Spring Data R2DBC

**Spring Webflux** se base sur le _projet Reactor_ pour permettre l'écriture d'api REST en mode réactif.

**Spring data R2DBC** utilise le protocole R2DBC basé sur les streams reactifs pour permettre la manipulation des données en base de manière réactif.

