# ChatroomRmi
 a simple chatroom with rmi java
Le reprtoir : chatproj contient deux projets.
projet client et projet serveur.

Les deux projets sont des projets intellij Idea et ils sont compatible avec Eclipse.

# Architecture:
    Le package rmi de chaque projet contient la couche service: les deux interfaces du client et serveur
    Le client il connait rien à part l'interface chatroom
    Le serveur connait que l'interface du client.
    
    Le package meetier contier l'implementation des interfaces(chatroomImpl dans le projet serveur, userIml dans le projet client)

# on peut generer le jar executable par la commande : mvn package pour chaque pojet.