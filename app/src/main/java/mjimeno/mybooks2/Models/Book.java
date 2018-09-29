package mjimeno.mybooks2.Models;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private static int generarId() {
        return id=id+1;
    }
    static{
        //Rellenar libros de ejemplos
        agregarLibros(new BookItem(generarId(),"El Señor de los Anillos","J.R.R. Tolkien"
                ,"29/07/1956","Su historia se desarrolla en la Tercera Edad del Sol de la Tierra Media, un lugar ficticio poblado por hombres y otras razas antropomorfas como los hobbits, los elfos o los enanos, así como por muchas otras criaturas reales y fantásticas"
                ,"http://t3.gstatic.com/images?q=tbn:ANd9GcQcbgn26UtyF_Di0Z05LyOzv4g3qGMz4QEEyR38vozUQ380JP2m"));
        agregarLibros(new BookItem(generarId(),"El Código da Vinci ","Dan Brown"
                ,"29/04/2003","El libro narra los intentos de Robert Langdon, Profesor de Iconografía Religiosa de la Universidad Harvard, para resolver el misterioso asesinato de Jacques Saunière ocurrido en el Museo del Louvre en París"
                ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScg-2NuBUFlQ2oiPmCitDzjQwVOgbw1tbqRTlwnYceJsgHp7JX3g"));
        agregarLibros(new BookItem(generarId(),"El Alquimista ","Paulo Coelho"
                ,"29/04/1988","El libro trata sobre los sueños y los medios que utilizamos para alcanzarlos, sobre el azar en nuestra vida y las señales que se presentan a lo largo de esta."
                ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQD21letDykfl-zeo_8c_3E6NORhgP49JYQ4jcrPbbrZHZMH0y4aQ"));
        agregarLibros(new BookItem(generarId(),"Crepúsculo  ","Stephenie Meyer"
                ,"20/11/2009","Cuando Isabella Swan se muda a Forks, una pequeña localidad del estado de Washington en la que nunca deja de llover, piensa que es lo más aburrido que le podía haber ocurrido en la vida."
                ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVyPXlhFcu6JpfeODHlVfqqXKdrh1BPp_1MpdghkEgmhFnBXLxUQ"));
    }

    @NonNull


    private static void agregarLibros(BookItem item){ // metodo para agregar libros a la lista
        ITEMS.add(item);
        MAPA_ITEMS.put(String.valueOf(item.Identificador),item);

     //   La lista de libros estática ITEMS actuará como origen de datos para el adaptador de la lista que se creará.
    //    El mapa MAPA_ITEMS será el índice de búsqueda del cuál se obtendrá los datos a través de un identificador.
    }

    public static class BookItem {
        //creamos la clase book con sus atributos y su constructor
        public final int Identificador;
        public final String Titulo;
        public final String Autor;
        public final String FechaPublicacion;
        public final String Descripcion;
        public final String URL_Imagen;

        public BookItem(int identificador, String titulo, String autor, String fechaPublicacion, String descripcion, String URL_Imagen) {
            Identificador = identificador;
            Titulo = titulo;
            Autor = autor;
            FechaPublicacion = fechaPublicacion;
            Descripcion = descripcion;
            this.URL_Imagen = URL_Imagen;
        }
    }
}