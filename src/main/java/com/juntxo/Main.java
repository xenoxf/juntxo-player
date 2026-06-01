package com.juntxo;

import com.juntxo.model.Cancion;
import com.juntxo.services.MusicaService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import javazoom.jlgui.basicplayer.BasicPlayer;

/**
 * Reproductor de música Juntxo
 * Permite gestionar y reproducir canciones desde la consola.
 */
public class Main {

    public static void main(String[] args) {
        // Guardar metadatos de música en JSON
        Scanner sc = new Scanner(System.in);
        System.out.println("Carpetas disponibles: ");
        File dir = new File("/home/juniorxf/Musica/music-1");
        File[] carpetas = dir.listFiles();
        if (carpetas == null) {
            carpetas = new File[0];
        }
        for (File carpeta : carpetas) {
            if (carpeta != null && carpeta.isDirectory()) {
                System.out.println(
                    "- " + carpeta.getName() + "N: " + carpeta.getTotalSpace()
                );
            }
        }
        boolean found = false;
        AtomicBoolean stopped = new AtomicBoolean(false);
        AtomicBoolean playing = new AtomicBoolean(false);
        AtomicBoolean paused = new AtomicBoolean(false);
        Thread mk2 = new Thread(() -> {
            while (!stopped.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        List<Cancion> musicas = new ArrayList<>();
        while (found == false) {
            System.out.println("Ingrese el numero de la carpeta de musica: ");
            Long code = sc.nextLong();

            for (File carpetaPath : carpetas) {
                if (
                    carpetaPath != null &&
                    carpetaPath.isDirectory() &&
                    code.equals(carpetaPath.getTotalSpace())
                ) {
                    System.out.println("Recordando música....");
                    musicas = MusicaService.recordarMusica(
                        carpetaPath.getAbsolutePath()
                    );

                    if (musicas == null || musicas.size() == 0) {
                        musicas = MusicaService.guardarMusica(
                            carpetaPath.getAbsolutePath()
                        );
                        System.out.println("Guardando música....");
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No se encontró la carpeta indicada.");
            }
        }

        boolean x = true;

        BasicPlayer player = new BasicPlayer();
        String path = "/Musica";

        System.out.println("🎵 Reproductor Juntxo 🎵");
        System.out.println(
            "Comandos: help: Mas informacion, musica [id]? load [archivo], play, pause, resume, stop, volume [0-100], exit"
        );

        while (x) {
            System.out.print("$> ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;
            String[] parts = input.split(" ", 2);
            String cmd = parts[0].toLowerCase();

            try {
                switch (cmd) {
                    case "load":
                        if (parts.length > 1) {
                            File f = new File(parts[1]);
                            if (f.exists()) {
                                player.open(f);
                                System.out.println(
                                    "✅ Archivo cargado: " + f.getName()
                                );
                            } else {
                                System.out.println("❌ Archivo no encontrado");
                            }
                        } else {
                            System.out.println("⚠️ Debes indicar un archivo");
                        }
                        break;
                    case "play":
                        player.play();
                        System.out.println("▶️ Reproduciendo");
                        break;
                    case "pause":
                        player.pause();
                        System.out.println("⏸ Pausado");
                        break;
                    case "resume":
                        player.resume();
                        System.out.println("⏯ Reanudado");
                        break;
                    case "stop":
                        player.stop();
                        System.out.println("⏹ Detenido");
                        break;
                    case "ls":
                        System.out.println(path);
                        break;
                    case "carpeta":
                        if (parts.length < 2) {
                            System.out.println("Carpetas disponibles: ");
                            for (File carpeta : carpetas) {
                                System.out.println(
                                    "- " +
                                        carpeta.getName() +
                                        " N: " +
                                        carpeta.getTotalSpace()
                                );
                            }
                        } else {
                            String nameCarpeta = parts[1];
                            for (File carpeta : carpetas) {
                                if (
                                    carpeta.getTotalSpace() ==
                                    (Long.parseLong(nameCarpeta))
                                ) {
                                    System.out.println("Carpeta encontrada");
                                    System.out.println(
                                        "Ruta: " + carpeta.getAbsolutePath()
                                    );

                                    System.out.println("Guardando...");
                                    musicas = MusicaService.guardarMusica(
                                        carpeta.getAbsolutePath()
                                    );
                                    break;
                                }
                            }
                            System.out.println("No se encontro la carpeta");
                        }
                        break;
                    case "musica":
                        if (parts.length < 2) {
                            System.out.println("Músicas disponibles:");
                            for (Cancion cancion : musicas) {
                                System.out.println("- " + cancion.getTitulo());
                                System.out.println(
                                    "  Artista: " + cancion.getArtista()
                                );
                                System.out.println(
                                    "  Álbum: " + cancion.getAlbum()
                                );
                                System.out.println(
                                    "  Género: " + cancion.getGenero()
                                );
                                System.out.println(
                                    "  Compositor: " + cancion.getCompositor()
                                );
                                System.out.println(
                                    "  Año: " + cancion.getFecha()
                                );
                                System.out.println(
                                    "  Duración: " +
                                        cancion.getDuracion() +
                                        " seg"
                                );
                                System.out.println(
                                    "  Path: " + cancion.getPath()
                                );
                                System.out.println("  Id: " + cancion.getId());
                                System.out.println(
                                    "-----------------------------------------------------"
                                );
                            }
                        } else {
                            try {
                                int id = Integer.parseInt(parts[1]);
                                Cancion encontrada = null;
                                for (Cancion cancion : musicas) {
                                    if (cancion.getId() == id) {
                                        encontrada = cancion;
                                        break;
                                    }
                                }
                                if (encontrada != null) {
                                    File music = new File(encontrada.getPath());
                                    if (music.exists()) {
                                        player.open(music);
                                        player.play();
                                        System.out.println(
                                            "▶️ Reproduciendo: " +
                                                encontrada.getTitulo()
                                        );
                                        System.out.println(
                                            encontrada.getDuracion()
                                        );
                                    } else {
                                        System.out.println(
                                            "❌ El archivo no existe en la ruta guardada"
                                        );
                                    }
                                } else {
                                    System.out.println(
                                        "No existe una canción con ese ID"
                                    );
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println(
                                    "⚠️ El ID debe ser un número"
                                );
                            }
                        }
                        break;
                    case "mk2":
                        // Reproduce una canción aleatoria de la lista usando MusicaService
                        //MusicaService.mk2(musicas, player);
                        if (musicas == null || musicas.isEmpty()) {
                            System.out.println("No hay canciones disponibles.");
                            return;
                        }
                        for (Cancion musica : musicas) {
                            int randomIndex = (int) (Math.random() *
                                musicas.size());
                            Cancion cancionAleatoria = musicas.get(randomIndex);
                            File archivoAleatorio = new File(
                                cancionAleatoria.getPath()
                            );
                            if (archivoAleatorio.exists()) {
                                try {
                                    player.open(archivoAleatorio);
                                    player.play();
                                    System.out.println(
                                        "▶️ Reproduciendo aleatoria: " +
                                            cancionAleatoria.getTitulo()
                                    );
                                    Thread.sleep(
                                        cancionAleatoria.getDuracion() * 1000L
                                    );
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
                                    "❌ El archivo no existe: " +
                                        archivoAleatorio.getPath()
                                );
                            }
                        }
                        break;
                    case "mk1":
                        // Reproduce todas las canciones en orden usando MusicaService
                        MusicaService.mk1(musicas, player);
                        break;
                    case "help":
                        System.out.println("Comandos disponibles:");
                        System.out.println("play: Reproduce la canción");
                        System.out.println("pause: Pausa la canción");
                        System.out.println("resume: Reanuda la canción");
                        System.out.println("stop: Detiene la canción");
                        System.out.println("volume [0-100]: Cambia el volumen");
                        System.out.println(
                            "carpeta [NAME]: Cambia la carpeta de reproduccion"
                        );
                        System.out.println(
                            "musica [id]: Lista o reproduce por ID"
                        );
                        System.out.println(
                            "load [archivo] || [url]: Carga un archivo mp3"
                        );
                        System.out.println("exit: Sale del reproductor");
                        System.out.println(
                            "help: Mas informacion sobre los comandos"
                        );
                        System.out.println(
                            "mk1: Reproduccion automatica ordenada"
                        );
                        System.out.println(
                            "mk2: Reproduccion automatica aleatoria"
                        );
                        break;
                    case "volume":
                        if (parts.length > 1) {
                            try {
                                int vol = Integer.parseInt(parts[1]);
                                if (vol < 0 || vol > 100) {
                                    System.out.println(
                                        "⚠️ El volumen debe estar entre 0 y 100"
                                    );
                                } else {
                                    double gain = vol / 100.0; // rango 0.0 - 1.0
                                    player.setGain(gain);
                                    System.out.println(
                                        "🔊 Volumen: " + vol + "%"
                                    );
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println(
                                    "⚠️ Debes indicar un número válido"
                                );
                            }
                        } else {
                            System.out.println("⚠️ Debes indicar un número");
                        }
                        break;
                    case "exit":
                        player.stop();
                        sc.close();
                        System.out.println("👋 Saliendo del reproductor");
                        x = false;
                        return;
                    default:
                        System.out.println(
                            "Comando no válido. Escribe 'help' para ver los comandos disponibles."
                        );
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }
}
