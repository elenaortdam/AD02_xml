/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parte2;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author elena
 */
public class SAX extends DefaultHandler{

    /**
     * Clase para almacenar los datos el XML de libros
     */
    private static class Libro {

        private int year;
        private String titulo;
        private String apellido;
        private String nombre;
        private String editorial;
        private BigDecimal precio;

        /**
         * Sobreescribimos toString() para dar un formato concreto a cada libro
         * que aparece en el XML
         *
         * @return libro del XML formateado
         */
        @Override
        public String toString() {
            return String.format("Año: %d \nTítulo: %s \n"
                    + "Autor: %s, %s \nEditorial: %s\n"
                    + "Precio: %s€",
                    year, titulo, apellido, nombre, editorial, precio);

        }
    }

    /**
     * Booleanos para comprobar si tienen esos atributos
     */
    private boolean tieneTitulo;
    private boolean tieneApellido;
    private boolean tieneNombre;
    private boolean tieneEditorial;
    private boolean tienePrecio;

    private List<Libro> libros;
    private final Libro libro = new Libro();
    private StringBuilder datos;

    public List<Libro> getLibros() {
        return this.libros;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("libro")) {
            String year = attributes.getValue("año").trim();
            this.libro.year = Integer.parseInt(year);
            if (this.libros == null) {
                this.libros = new ArrayList<>();
            }
        } else if (qName.equalsIgnoreCase("titulo")) {
            this.tieneTitulo = true;
        } else if (qName.equalsIgnoreCase("apellido")) {
            this.tieneApellido = true;
        } else if (qName.equalsIgnoreCase("nombre")) {
            this.tieneNombre = true;
        } else if (qName.equalsIgnoreCase("editorial")) {
            this.tieneEditorial = true;
        } else if (qName.equalsIgnoreCase("precio")) {
            this.tienePrecio = true;
        }
        this.datos = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        //Quitamos los espacios del texto y los saltos de línea
        String textoFormateado = datos.toString().trim().replaceAll("[\t\n]",
                "");
        /**
         * Comprobamos si tiene un atributo y lo vamos rellenando. Una vez
         * asignado el atributo se pone a false para que no vuelva a entrar en
         * el if
         */
        if (this.tieneTitulo) {
            this.libro.titulo = textoFormateado;
            this.tieneTitulo = false;
        } else if (this.tieneApellido) {
            this.libro.apellido = (textoFormateado);
            this.tieneApellido = false;
        } else if (this.tieneNombre) {
            this.libro.nombre = (textoFormateado);
            this.tieneNombre = false;
        } else if (this.tieneEditorial) {
            this.libro.editorial = (textoFormateado);
            this.tieneEditorial = false;
        } else if (this.tienePrecio) {
            this.libro.precio = (new BigDecimal(textoFormateado));
            this.tienePrecio = false;
        }
        if (qName.equalsIgnoreCase("libro")) {
            this.libros.add(libro);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        datos.append(new String(ch, start, length));
    }

    public void visualizarEtiquetasXMLconSAX()
            throws SAXException, IOException {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        SAX sax = new SAX();
        xmlReader.setContentHandler(sax);
        InputSource inputSource = new InputSource("libros.xml");
        xmlReader.parse(inputSource);
        List<SAX.Libro> libros = sax.getLibros();
        for (int i = 0; i < libros.size(); i++) {
            System.out.printf("Libro %d: \n", i + 1);
            System.out.println(libros.get(i));
            System.out.println();
        }

    }

    public static void main(String[] args) throws IOException, SAXException {
        SAX sax = new SAX();
        sax.visualizarEtiquetasXMLconSAX();
    }
}
