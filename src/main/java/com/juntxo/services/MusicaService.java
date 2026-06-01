package com.juntxo.services;

import com.google.gson.Gson;
import com.juntxo.model.Cancion;
import com.mpatric.mp3agic.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javazoom.jlgui.basicplayer.BasicPlayer;
//import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Servicio para gestionar la música.
 * Incluye métodos para guardar y recordar canciones usando archivos JSON.
 */
public class MusicaService {

    BasicPlayer player = new BasicPlayer();

    /**
     * Lee el archivo musicas.json y devuelve la lista de canciones almacenadas.
     * @return Lista de canciones
     */
    public static List<Cancion> recordarMusica(String carpetaPath) {
        List<Cancion> canciones = new ArrayList<>();
        File carpeta = new File(carpetaPath);
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(carpeta.getName())) {
            Cancion[] cancionesArray = gson.fromJson(reader, Cancion[].class);
            if (cancionesArray != null) {
                canciones = Arrays.asList(cancionesArray);
            }
        } catch (IOException e) {
            System.err.println("Error al leer musicas.json: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(
                "Error inesperado al leer musicas.json: " + e.getMessage()
            );
        }
        return canciones;
    }

    /**
     * Reproduce todas las canciones en orden usando BasicPlayer.
     */
    public static void mk1(List<Cancion> musicas, BasicPlayer player) {
        if (musicas == null || musicas.isEmpty()) {
            System.out.println("No hay canciones disponibles.");
            return;
        }
        for (Cancion musica : musicas) {
            File archivo = new File(musica.getPath());
            if (archivo.exists()) {
                try {
                    player.open(archivo);
                    player.play();
                    System.out.println(
                        "▶️ Reproduciendo: " + musica.getTitulo()
                    );
                    Thread.sleep(musica.getDuracion() * 1000L);
                    player.stop();
                } catch (Exception e) {
                    System.out.println(
                        "Error al reproducir: " +
                            musica.getTitulo() +
                            " - " +
                            e.getMessage()
                    );
                }
            } else {
                System.out.println(
                    "❌ El archivo no existe: " + archivo.getPath()
                );
            }
        }
    }

    /**
     * Reproduce una canción aleatoria de la lista usando BasicPlayer.
     */
    public static void mk2(List<Cancion> musicas, BasicPlayer player) {
        if (musicas == null || musicas.isEmpty()) {
            System.out.println("No hay canciones disponibles.");
            return;
        }
        for (Cancion musica : musicas) {
            int randomIndex = (int) (Math.random() * musicas.size());
            Cancion cancionAleatoria = musicas.get(randomIndex);
            File archivoAleatorio = new File(cancionAleatoria.getPath());
            if (archivoAleatorio.exists()) {
                try {
                    player.open(archivoAleatorio);
                    player.play();
                    System.out.println(
                        "▶️ Reproduciendo aleatoria: " +
                            cancionAleatoria.getTitulo()
                    );
                    Thread.sleep(cancionAleatoria.getDuracion() * 1000L);
                    player.stop();
                } catch (Exception e) {
                    System.out.println(
                        "Error al reproducir: " +
                            cancionAleatoria.getTitulo() +
                            " - " +
                            e.getMessage()
                    );
                }
            } else {
                System.out.println(
                    "❌ El archivo no existe: " + archivoAleatorio.getPath()
                );
            }
        }
    }

    /**
     * Escanea una carpeta de música, extrae metadatos de los archivos mp3 usando mp3agic y guarda la información en musicas.json.
     */
    public static List<Cancion> guardarMusica(String carpetaPath) {
        int id = 1;
        Gson gson = new Gson();
        List<Cancion> canciones = new ArrayList<>();
        File carpeta = new File(carpetaPath);
        File[] files = carpeta.listFiles();
        if (files.length == 0) {
            System.err.println("La carpeta de música está vacía.");
            return null;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                try {
                    Mp3File mp3file = new Mp3File(file.getAbsolutePath());
                    AudioFile audioFile = AudioFileIO.read(file);
                    String titulo = file.getName();
                    String artista = "";
                    String album = "";
                    String genero = "";
                    String compositor = "";
                    String fecha = "";
                    int duracion = audioFile.getAudioHeader().getTrackLength();
                    if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                        if (
                            id3v2Tag.getTitle() != null &&
                            !id3v2Tag.getTitle().isEmpty()
                        ) titulo = id3v2Tag.getTitle();
                        if (id3v2Tag.getArtist() != null) artista =
                            id3v2Tag.getArtist();
                        if (id3v2Tag.getAlbum() != null) album =
                            id3v2Tag.getAlbum();
                        if (id3v2Tag.getGenreDescription() != null) genero =
                            id3v2Tag.getGenreDescription();
                        if (id3v2Tag.getComposer() != null) compositor =
                            id3v2Tag.getComposer();
                        if (id3v2Tag.getYear() != null) fecha =
                            id3v2Tag.getYear();
                    } else if (mp3file.hasId3v1Tag()) {
                        ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                        if (
                            id3v1Tag.getTitle() != null &&
                            !id3v1Tag.getTitle().isEmpty()
                        ) titulo = id3v1Tag.getTitle();
                        if (id3v1Tag.getArtist() != null) artista =
                            id3v1Tag.getArtist();
                        if (id3v1Tag.getAlbum() != null) album =
                            id3v1Tag.getAlbum();
                        if (id3v1Tag.getGenreDescription() != null) genero =
                            id3v1Tag.getGenreDescription();
                        if (id3v1Tag.getYear() != null) fecha =
                            id3v1Tag.getYear();
                    }
                    Cancion cancion = new Cancion(
                        id++,
                        titulo,
                        artista,
                        album,
                        genero,
                        compositor,
                        fecha,
                        duracion,
                        file.getAbsolutePath()
                    );
                    canciones.add(cancion);
                } catch (Exception e) {
                    System.err.println(
                        "No se pudo leer metadatos de: " +
                            file.getName() +
                            " - " +
                            e.getMessage()
                    );
                }
            }
        }
        try {
            File file = new File(carpeta.getName() + ".json");
            FileWriter writer = new FileWriter(file.getName());
            gson.toJson(canciones, writer);
            return canciones;
        } catch (IOException e) {
            System.err.println(
                "Error al guardar musicas.json: " + e.getMessage()
            );
        } catch (Exception e) {
            System.err.println(
                "Error inesperado al guardar musicas.json: " + e.getMessage()
            );
        }
        return canciones;
    }
}
