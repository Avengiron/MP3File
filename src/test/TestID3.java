package test;

import id3.ID3;

/** 
 * Test de la class ID3
 * @author Xavier 
 */
public class TestID3 {
  
  public static void main(String[] args) {
    ID3 music = new ID3();    
    if(music.readID3("Maelstrom.mp3")){
      System.out.println("Name : "   + music.getName());
      System.out.println("Title : "  + music.getTitle());
      System.out.println("Album : "  + music.getAlbum());
      System.out.println("Track  : " + music.getTrackNumber());
      System.out.println("Artist : " + music.getArtist());
      System.out.println("Year : "   + music.getYear());
      System.out.println("Genre : "  + music.getGenre());
      System.out.println("Duree : "  + music.getDuration());
    }
  }
  
}
