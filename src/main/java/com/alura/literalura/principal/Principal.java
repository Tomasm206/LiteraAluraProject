package com.alura.literalura.principal;

import com.alura.literalura.dto.LibroDTO;
import com.alura.literalura.exeption.LibroRepetidoExeption;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvertirDatos;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvertirDatos conversor = new ConvertirDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    List<Libro> libros = null;

    LibroRepository repositorio;

    AutorRepository repositorioAutor;

    public Principal(LibroRepository repositorio, AutorRepository repositorioAutor) {
        this.repositorio = Objects.requireNonNull(repositorio, "El repositorio de libros no puede ser nulo");
        this.repositorioAutor = Objects.requireNonNull(repositorioAutor, "El repositorio de autores no puede ser nulo");
    }


//    public Principal(LibroRepository repositorio, AutorRepository repositorioAutor) {
//        this.repositorio = repositorio;
//        this.repositorioAutor = repositorioAutor;
//    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                Elije una opción:
                1 - Buscar libros
                2 - Mostrar los libros guardados
                3 - Mostrar autores registrados
                4 - Mostrar autores vivos en cierto año
                5 - Mostrar libros por idioma

                0 - Salir
                """;
            System.out.println(menu);

            try {
                // Intentamos leer la opción como un número entero
                System.out.print("Ingrese su opción: ");
                opcion = teclado.nextInt();
                teclado.nextLine(); // Consumir el salto de línea adicional

                // Evaluamos la opción del menú
                switch (opcion) {
                    case 1:
                        buscarLibroWeb();
                        break;
                    case 2:
                        buscarTodosLosLibros();
                        break;
                    case 3:
                        buscarAutoresRegistrados();
                        break;
                    case 4:
                        buscarAutoresVivosEnAnio();
                        break;
                    case 5:
                        buscarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            } catch (Exception e) {
                // Si ocurre una excepción, mostramos un mensaje de error
                System.out.println("---------------------------------");
                System.out.println("Error: Por favor, ingrese un número válido.");
                System.out.println("---------------------------------");
                teclado.nextLine(); // Limpiar el buffer de entrada
            }
        }
    }

    //metodo 1
    private void buscarLibroWeb() {
        System.out.println("Introduce el nombre del libro a buscar");
        var busqueda = teclado.nextLine();
        try {
            Optional<Libro> libroBase = repositorio.findByTituloContainsIgnoreCase(busqueda);
            if (libroBase.isPresent()) {
                throw new LibroRepetidoExeption("El libro ya existe en la base de datos");
            }
            var json = consumoAPI.consumir(URL_BASE + busqueda.replace(" ", "%20"));
            LibroDTO datos = conversor.obtenerDatos(json, LibroDTO.class);
            Optional<Autor> autorExistente = repositorioAutor.findByNombreIgnoreCase(datos.autores().get(0).nombre());
            Autor autor;
            if (autorExistente.isPresent()) {
                autor = autorExistente.get(); // Usar el autor existente
            } else {
                autor = new Autor(datos.autores().get(0)); // Crear nuevo autor si no existe
                autor = repositorioAutor.save(autor); // Guardar el nuevo autor para que esté gestionado
            }
            Libro libro = new Libro(datos);
            libro.setAutor(autor);

            repositorio.save(libro);
            System.out.println("Libro guardado con éxito.");

        } catch (LibroRepetidoExeption e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e.getMessage());
        }
    }
    //metodo 2
    private void buscarTodosLosLibros() {
        List<Libro> libros = repositorio.todosLosLibros();
        System.out.println("Libros encontrados:");
        libros.forEach(l -> System.out.println("---------- Libro --------------\n" +
                "Título: " + l.getTitulo() + "\n" +
                "Idioma: " + l.getIdioma() + "\n" +
                "Número de descargas: " + l.getNumDescargas() + "\n" +
                "Autor: " + l.getAutor().getNombre() +
                "\n---------------------" + "\n"));
    }
    //metodo 3
    private void buscarAutoresRegistrados() {
        List<Autor> autores = repositorio.todosLosAutores();
        if (autores == null || autores.isEmpty()) {
            System.out.println("No se encontraron autores.");
            return;
        }
        System.out.println("Autores encontrados: \n");
        autores.forEach(System.out::println);
    }

    //metodo 4
    private void buscarAutoresVivosEnAnio() {
        System.out.println("Introduce el año a buscar");
        var busqueda = teclado.nextInt();
        List<Autor> autores = repositorioAutor.encontrarAutoresPorAnio(busqueda);
        System.out.println("Autores encontrados:\n");
        autores.forEach(System.out::println);
    }
    //metodo 5
    private void buscarLibrosPorIdioma() {
        List<Libro> libros = null;
        boolean opcionValida = false;

        while (!opcionValida) {
            System.out.println("Elige un idioma: \n" +
                    "es - Español\n" +
                    "en - Inglés\n" +
                    "fr - Francés\n" +
                    "de - Alemán\n" +
                    "pt - Portugués\n");

            var idioma = teclado.nextLine();

            System.out.println("Idioma seleccionado: " + idioma);  // Verifica el idioma seleccionado

            switch (idioma) {
                case "es":
                case "en":
                case "fr":
                case "de":
                case "pt":
                    libros = repositorio.buscarPorIdioma(idioma);
                    opcionValida = true;
                    break;
                default:
                    System.out.println("Idioma no válido. Por favor, selecciona una opción correcta.\n");
                    break;
            }
        }

        if (libros != null && !libros.isEmpty()) {
            System.out.println("Libros encontrados: \n");
            libros.forEach(System.out::println);
        } else {
            System.out.println("No se encontraron libros para el idioma seleccionado.");
        }
    }
}
