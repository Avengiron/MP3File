package id3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Class qui lit les tags ID3v2.3 principaux d'un fichier MP3. Inspiree par la
 * bibliotheque ID3v1 processing.
 * @author Xavier
 */
public class ID3 {
  /** Nom de la musique */
  private String name;
  /** Titre de la musique */
  private String title;
  /** Album */
  private String album;
  /** Numero de piste */
  private String trackNumber;
  /** Artiste principal */
  private String artist;
  /** Annee */
  private String year;
  /** Genre */
  private String genre;
  /** Duree */
  private String duration;

  /** Constructeur */
  public ID3() {
    name = new String();
    title = new String();
    album = new String();
    trackNumber = new String();
    artist = new String();
    year = new String();
    genre = new String();
    duration = new String();
  }

  /**
   * Obtient le nom de la musique
   * @return Nom de la musique
   */
  public String getName() {
    return name;
  }

  /**
   * Obtient le titre de la musique
   * @return Titre de la musique
   */
  public String getTitle() {
    return title;
  }

  /**
   * Obtient l'album
   * @return Album
   */
  public String getAlbum() {
    return album;
  }

  /**
   * Obtient le numero de piste
   * @return Numero de piste
   */
  public String getTrackNumber() {
    return trackNumber;
  }

  /**
   * Obtient l'artiste principal
   * @return Artiste principal
   */
  public String getArtist() {
    return artist;
  }

  /**
   * Obtient l'annee
   * @return Annee
   */
  public String getYear() {
    return year;
  }

  /**
   * Obtient le genre de la musique
   * @return Genre de la musique
   */
  public String getGenre() {
    return genre;
  }

  /**
   * Obtient la duree de la musique
   * @return Duree de la musique
   */
  public String getDuration() {
    return duration;
  }

  /**
   * Lit les tags du fichier .mp3 passe en parametres. Le fichier doit se
   * trouver dans le dossier data. Si le fichier n'est pas encode en ID3v2.3,
   * les champs ne sont pas remplis.
   * @see https://id3.org/id3v2.3.0
   * @param filename Nom du fichier
   * @return True si la lecture s'est bien passee
   */
  public boolean readID3(String filename) {
    File music = null;
    try {
      music = new File("data\\" + filename);
    } catch (NullPointerException npe) {
      System.err.println("Erreur lors de la recuperation du fichier");
    }
    byte[] file = null;
    try {
      file = Files.readAllBytes(music.toPath());
    } catch (IOException ioe) {
      System.err.println("Le fichier doit se trouver dans le dossier data/");
      ioe.printStackTrace();
      System.exit(1);
    }

    // Ne gere que la version 3
    if (!hasID3v2(file) || getVersion(file) != 3) {
      System.out.println("Ne gere que la version ID3v2.3");
      return false;
    }

    // Nom du fichier = filename sans extension
    int nameLimit = filename.length() - 4; // ".mp3"
    name = filename.substring(0, nameLimit);

    int size = getSize(file);
    file = Arrays.copyOfRange(file, 0, size);

    int offset = 10;
    while (offset < size - 10) {
      String frameID = new String()
        + (char) file[offset + 0]
        + (char) file[offset + 1]
        + (char) file[offset + 2]
        + (char) file[offset + 3];
      int frameSize = 
          file[offset + 4] << 24
        | file[offset + 5] << 16
        | file[offset + 6] << 8
        | file[offset + 7];
      // offset+8 et offset+9 sont des bytes de flags, non lus
      offset += 10; 

      if (frameSize > 0) {
        byte[] valueRaw = Arrays.copyOfRange(file, offset, offset + frameSize);
        String value = getTag(valueRaw);

        switch (frameID) {
          case "TIT2" : title = value; break;
          case "TALB" : album = value; break;
          case "TRCK" : trackNumber = value; break;
          case "TPE2" : artist = value; break;
          case "TYER" : year = value; break;
          case "TCON" : genre = value; break;
          case "TLEN" : duration = value; break;
          default : break;
        }
      }
      offset += frameSize;
    }
    return true;
  }

  /**
   * Verifie la presence de tags ID3v2
   * @param file Fichier mp3
   * @return True si les 3 premiers caracteres sont "ID3"
   */
  private boolean hasID3v2(byte[] file) {
    return ( 
         (char) file[0] == 'I'
      && (char) file[1] == 'D'
      && (char) file[2] == '3');
  }

  /**
   * Verifie la version de l'ID3v2
   * @param file Fichier mp3
   * @return Version
   */
  private byte getVersion(byte[] file) {
    return file[3];
  }

  /**
   * Calcule la taille la partie du fichier dediee aux tags
   * @param file Fichier mp3
   * @return Taille en octets
   */
  private int getSize(byte[] file) {
    // Le 8eme bit est toujours 0
    return file[6] << 21
      | file[7] << 14
      | file[8] << 7
      | file[9];
  }

  /**
   * Transforme la trame de byte en chaine de caracteres
   * @param frame Trame. Correspond a la valeur du tag
   * @return Chaine de caractere correspondant a la valeur du tag
   */
  private String getTag(byte[] frame) {
    String tag = new String();
    for (byte b : frame) {
      tag += (char) b;
    }
    return tag;
  }

}
