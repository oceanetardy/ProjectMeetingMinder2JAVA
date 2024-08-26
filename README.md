# Meeting Minder API

API pour la gestion des salles de réunion et des réservations

Doc Swagger :  http://localhost:8080/meeting-minder/swagger-ui/index.html 

http://localhost:8080/meeting-minder 

 ## Gestion des rôles
Opérations liées à la gestion des rôles dans l'API


### GET

/api/roles/{id} : Obtenir un rôle par ID

### PUT
/api/roles/{id} : Mettre à jour un rôle par ID

### DELETE
/api/roles/{id} : Supprimer un rôle par ID

### PATCH
/api/roles/{id} : Mettre à jour partiellement un rôle par ID

### GET
/api/roles : Obtenir tous les rôles avec pagination

### POST
/api/roles : Créer un nouveau rôle

### DELETE
/api/roles : Supprimer tous les rôles


## Gestion des salles de réunion
Opérations liées à la gestion des salles de réunion dans l'application


### GET
/api/rooms/{id} : Obtenir une salle par ID

### PUT
/api/rooms/{id} : Mettre à jour une salle de réunion par ID

### DELETE
/api/rooms/{id} : Supprimer une salle par ID

### PATCH
/api/rooms/{id} : Mettre à jour partiellement une salle par ID

### GET
/api/rooms : Obtenir toutes les salles

### POST
/api/rooms : Créer une nouvelle salle de réunion

### DELETE
/api/rooms : Supprimer toutes les salles

## Gestion des réservations
Opérations liées à la gestion des réservations dans l'application



### GET
/api/reservations/{id} : Obtenir une réservation par ID

### PUT
/api/reservations/{id} : Mettre à jour une réservation par ID

### DELETE
/api/reservations/{id} : Supprimer une réservation par ID

### PATCH
/api/reservations/{id} : Mettre à jour partiellement une réservation par ID

### GET
/api/reservations : Obtenir toutes les réservations

### POST
/api/reservations : Créer une nouvelle réservation

### DELETE
/api/reservations : Supprimer toutes les réservations

## Gestion des utilisateurs
Opérations liées à la gestion des utilisateurs dans l'application



### GET

/api/users/{id} : Obtenir un utilisateur par ID

### PUT
/api/users/{id} : Mettre à jour un utilisateur par ID

### DELETE
/api/users/{id} : Supprimer un utilisateur par ID

### PATCH
/api/users/{id} : Mettre à jour partiellement un utilisateur par ID

### GET
/api/users : Obtenir tous les utilisateurs

### POST
/api/users : Créer un nouvel utilisateur

### DELETE
/api/users : Supprimer tous les utilisateurs