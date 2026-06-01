/*package com.juntxo.model;

import java.io.*;
import java.util.*;

public class Cancion {

    private int id;
    private String titulo;
    private String path;
    private int duracion;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Cancion(int id, String titulo, int duracion, String path) {
        this.id = id;
        this.titulo = titulo;
        this.duracion = duracion;
        this.path = path;
    }
}
*/

package com.juntxo.model;

public class Cancion {

    private int id;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private String compositor;
    private String fecha; // año
    private String path;
    private int duracion; // en segundos

    // ---------------------
    // Constructor completo
    // ---------------------
    public Cancion(
        int id,
        String titulo,
        String artista,
        String album,
        String genero,
        String compositor,
        String fecha,
        int duracion,
        String path
    ) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.compositor = compositor;
        this.fecha = fecha;
        this.duracion = duracion;
        this.path = path;
    }

    // ---------------------
    // Getters y Setters
    // ---------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCompositor() {
        return compositor;
    }

    public void setCompositor(String compositor) {
        this.compositor = compositor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
