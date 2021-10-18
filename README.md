# MP3File
Bibliothèque d'extraction des tags ID3v2.3 principaux d'un fichier MP3. 
Inspirée par la bibliothèque ID3 (v1) processing.

Extrait les tags titre, album, numéro de piste, artiste principal, année, genre et durée. 

La fichier doit se trouver dans le dossier data/

```java
ID3 music = new ID3();    
if(music.readID3("VotreMusique.mp3")){
  System.out.println("Name : "   + music.getName());
  System.out.println("Title : "  + music.getTitle());
  System.out.println("Album : "  + music.getAlbum());
  System.out.println("Track  : " + music.getTrackNumber());
  System.out.println("Artist : " + music.getArtist());
  System.out.println("Year : "   + music.getYear());
  System.out.println("Genre : "  + music.getGenre());
  System.out.println("Duree : "  + music.getDuration());
}
```
