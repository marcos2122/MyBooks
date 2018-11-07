package mjimeno.mybooks2.Models;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Modelo de datos para los libros que  inflarán  la lista
 */
public class Book {

    /**
     * Arreglo de objetos que simula una fuente de datos
     */
    public static final List<BookItem> ITEMS = new ArrayList<BookItem>();

    /**
     * Mapa simulador de búsquedas de libros por id
     */
    public static final Map<String, BookItem> MAPA_ITEMS = new HashMap<String, BookItem>();


    public static List<Book.BookItem>getBooks()
    {
    // metodo obtener lista de todos los libros de la base de datos
        List<Book.BookItem> lista = Book.BookItem.listAll((Book.BookItem.class));
        return lista;
    }

    public static boolean exists(Book.BookItem bookItem,String key) {
        // metodo para saber si existe un libro determinado

       boolean existe = false;
         bookItem = Book.BookItem.findById(Book.BookItem.class,Long.parseLong(key));
         if (bookItem!=null) existe=true;
         return existe;
    }
    public static boolean existsbook(String position)
    {
        boolean existe = false;
        Book.BookItem bookItem = Book.BookItem.findById(Book.BookItem.class,Long.parseLong(position));
        if (bookItem!=null) existe=true;
        return existe;

    }

    public static void borrarLibro(String posicion)
    {
        Book.BookItem bookItems = Book.BookItem.findById(Book.BookItem.class, Long.parseLong(posicion));
        bookItems.delete();

    }



@Table (name="Books")
    public static class BookItem extends SugarRecord {
        //creamos la clase book con sus atributos y su constructor
@Column(name="Books")
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