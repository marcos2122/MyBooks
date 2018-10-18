package mjimeno.mybooks2.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Modelo de datos para los libros que  inflarán  la lista
 */
public class Book {
    private static int id = 0;

    /**
     * Arreglo de objetos que simula una fuente de datos
     */
    public static final List<BookItem> ITEMS = new ArrayList<BookItem>();

    /**
     * Mapa simulador de búsquedas de libros por id
     */
    public static final Map<String, BookItem> MAPA_ITEMS = new HashMap<String, BookItem>();
/*
    private static int generarId() {
        return id=id+1;
    }

    static{
        //Rellenar libros de ejemplos
        agregarLibros(new BookItem(generarId(),"J.R.R. Tolkien","Su historia se desarrolla en la Tercera Edad del Sol de la Tierra Media, un lugar ficticio poblado por hombres y otras razas antropomorfas como los hobbits, los elfos o los enanos, así como por muchas otras criaturas reales y fantásticas"
                ,"29/07/1956","El Señor de los Anillos"
                ,"http://t3.gstatic.com/images?q=tbn:ANd9GcQcbgn26UtyF_Di0Z05LyOzv4g3qGMz4QEEyR38vozUQ380JP2m"));


        agregarLibros(new BookItem(generarId(),"Dan Brown ","El libro narra los intentos de Robert Langdon, Profesor de Iconografía Religiosa de la Universidad Harvard, para resolver el misterioso asesinato de Jacques Saunière ocurrido en el Museo del Louvre en París"
                ,"29/04/2003","El Código da Vinci"
                ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScg-2NuBUFlQ2oiPmCitDzjQwVOgbw1tbqRTlwnYceJsgHp7JX3g"));

        agregarLibros(new BookItem(generarId(),"Paulo Coelho ","El libro trata sobre los sueños y los medios que utilizamos para alcanzarlos, sobre el azar en nuestra vida y las señales que se presentan a lo largo de esta."
                ,"29/04/1988","El Alquimista"
                ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQD21letDykfl-zeo_8c_3E6NORhgP49JYQ4jcrPbbrZHZMH0y4aQ"));

        agregarLibros(new BookItem(generarId(),"Stephenie Meyer  ","Cuando Isabella Swan se muda a Forks, una pequeña localidad del estado de Washington en la que nunca deja de llover, piensa que es lo más aburrido que le podía haber ocurrido en la vida."
                ,"20/11/2009","Crepúsculo"
                ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVyPXlhFcu6JpfeODHlVfqqXKdrh1BPp_1MpdghkEgmhFnBXLxUQ"));

    }

    @NonNull


    private static void agregarLibros(BookItem item){ // metodo para agregar libros a la lista
        ITEMS.add(item);
        MAPA_ITEMS.put(String.valueOf(item.Identificador),item);

     //   La lista de libros estática ITEMS actuará como origen de datos para el adaptador de la lista que se creará.
    //    El mapa MAPA_ITEMS será el índice de búsqueda del cuál se obtendrá los datos a través de un identificador.
    }
*/
    public static class BookItem {
        //creamos la clase book con sus atributos y su constructor
        public   int identificador;
        public   String author;
        public   String description;
        public   String publication_date;
        public   String title;
        public   String url_image;


    public BookItem(){}

    public BookItem(int identificador, String autor, String descripcion, String fechaPublicacion, String titulo, String URL_Imagen) {
        this.identificador = identificador;
        this.author = autor;
        this.description=descripcion;
        this.publication_date = fechaPublicacion;
        this.title = titulo;
        this.url_image = URL_Imagen;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }



    }
}