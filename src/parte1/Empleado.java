package parte1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author elena
 */
public class Empleado {

    private static final int NUMERO_EMPLEADOS_FICHERO = 5;
    private static final int MIN_EMPLEADOS_FICHERO = 1;
    private static final int MAX_EMPLEADOS_FICHERO = 99;

    private int codigo;
    private String nombre;
    private String direccion;
    private float salario;
    private float comision;

    private Empleado() {
    }

    private Empleado(int codigo, String nombre, String direccion,
            float salario, float comision) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.salario = salario;
        this.comision = comision;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public float getSalario() {
        return salario;
    }

    public float getComision() {
        return comision;
    }

    public RandomAccessFile crearFicheroEmpleados(String nombreFichero) throws FileNotFoundException, IOException {
        RandomAccessFile ficheroEmpleados;
        try (RandomAccessFile fichero = new RandomAccessFile(nombreFichero, "rw")) {
            fichero.seek(fichero.length());
            List<Empleado> empleados = crearEmpleadosAleatorios(NUMERO_EMPLEADOS_FICHERO);
            int numeroEmpleado = 1;
            for (Empleado empleado : empleados) {
                String datosEmpleado = new String(String.format("Empleado %d:\n Codigo empleado: %d \n Nombre: %s \n"
                        + "Dirección: %s \n Salario: %.2f \n Comisión: %.2f \n",
                        numeroEmpleado++, empleado.getCodigo(), empleado.getNombre(),
                        empleado.getDireccion(), empleado.getSalario(),
                        empleado.getComision()).getBytes(),
                        StandardCharsets.UTF_8);
                fichero.writeBytes(datosEmpleado);
            }
            ficheroEmpleados = fichero;
        }
        return ficheroEmpleados;
    }

    /**
     * Generador de empleados con datos aleatorios
     *
     * @param numeroEmpleados cantidad de empleados que se quieren crear
     * @return lista de empleados con datos aleatorios
     */
    public List<Empleado> crearEmpleadosAleatorios(int numeroEmpleados) {
        if (numeroEmpleados > MAX_EMPLEADOS_FICHERO) {
            throw new IllegalArgumentException("El máximo de empleados por fichero es 99");
        } else if (numeroEmpleados < MIN_EMPLEADOS_FICHERO) {
            throw new IllegalArgumentException("El número mínimo de empleados por fichero es 1");
        }
        List<Empleado> empleados = new ArrayList();
        String[] nombres = {"María", "Francisco", "Alejandro", "Elena", "Luismi", "Miriam"};
        String[] direcciones = {"Calle Juan Carlos I", "Calle Santa Sabina",
            "Plaza de España", "Calle falsa 123", "Fuencarral"};
        float[] comision = {10, 20, 25, 30, 33, 40, 55};
        float[] salario = {1000, 1100, 1200, 1300, 1400, 1500, 2000, 2200, 3000};
        for (int i = 1; i <= numeroEmpleados; i++) {
            Empleado empleado = new Empleado(i, nombres[getNumeroAleatorio(nombres.length) - 1],
                    direcciones[getNumeroAleatorio(direcciones.length) - 1],
                    salario[getNumeroAleatorio(salario.length) - 1],
                    comision[getNumeroAleatorio(comision.length) - 1]);
            empleados.add(empleado);
        }
        return empleados;
    }

    /**
     * Generador de números aleatorios para coger un dato de los arrays
     *
     * @param numeroMaximo el máximo será el tamaño del array
     * @return número aleatorio
     */
    private int getNumeroAleatorio(int numeroMaximo) {
        return (int) (Math.random() * numeroMaximo) + 1;
    }

    /**
     * Método que recorre el fichero y crea una lista de empleados
     *
     * @param nombreFichero del que se van a leer los datos
     * @return lista de empleados que aparecen en el fichero
     */
    private List<Empleado> leerDatos(String nombreFichero) {
        List<Empleado> empleados = new ArrayList();

        File file = new File(nombreFichero);
        try (RandomAccessFile archivo = new RandomAccessFile(file, "r")) {
            int posicion = 0;
            for (;;) {
                archivo.seek(posicion);
                String codigo = new String(archivo.readLine().getBytes(), StandardCharsets.UTF_8);
                if (codigo.contains("Empleado ")) {
                    posicion += codigo.length() + 1;
                }
                codigo = archivo.readLine();
                if (codigo.contains("Codigo")) {
                    posicion += codigo.length() + 1;
                    codigo = codigo.replace("Codigo empleado: ", "");
                    this.codigo = Integer.parseInt(codigo.trim());
                }
                archivo.seek(posicion);
                String nombre = archivo.readLine();
                if (nombre.contains("Nombre: ")) {
                    posicion += nombre.length() + 1;
                    this.nombre = nombre.replace("Nombre: ", "").trim();
                }
                archivo.seek(posicion);
                String direccion = new String(archivo.readLine().getBytes(), StandardCharsets.UTF_8);
                if (direccion.contains("Dirección: ")) {
                    posicion += direccion.length() + 1;
                    this.direccion = direccion.replace("Dirección: ", "").trim();
                }
                archivo.seek(posicion);
                String salario = new String(archivo.readLine().getBytes(), StandardCharsets.UTF_8);
                if (salario.contains("Salario: ")) {
                    posicion += salario.length() + 1;
                    this.salario = Float.parseFloat(salario.replace("Salario: ", "")
                            .replace(",", ".").trim());
                }
                archivo.seek(posicion);
                String comision = archivo.readLine();
                if (comision.contains("Comisión: ")) {
                    posicion += comision.length() + 1;
                    this.comision = Float.parseFloat(comision.replace("Comisión: ", "")
                            .replace(",", ".").trim());
                }
                empleados.add(new Empleado(this.codigo, this.nombre,
                        this.direccion, this.salario, this.comision));

                if (archivo.getFilePointer() == file.length()) {
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empleados;

    }

    private void crearXML(String nombreFichero)
            throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException {

        List<Empleado> empleados = leerDatos(nombreFichero);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "EMPLEADOS.XML", null);
            Element raiz = document.createElement("Empleados");
            document.getDocumentElement().appendChild(raiz);
            for (Empleado empleado : empleados) {
                Element empleadoXML = document.createElement("Empleado");
                raiz.appendChild(empleadoXML);
                Element codigoEmpleado = document.createElement("Codigo");
                codigoEmpleado.setTextContent(String.valueOf(empleado.getCodigo()));
                empleadoXML.appendChild(codigoEmpleado);
                Element nombre = document.createElement("Nombre");
                nombre.setTextContent(empleado.getNombre());
                empleadoXML.appendChild(nombre);
                Element direccion = document.createElement("Dirección");
                direccion.setTextContent(empleado.getDireccion());
                empleadoXML.appendChild(direccion);
                Element salario = document.createElement("Salario");
                salario.setTextContent(String.valueOf(empleado.getSalario()));
                empleadoXML.appendChild(salario);
                Element comision = document.createElement("Comisión");
                comision.setTextContent(String.valueOf(empleado.getComision()));
                empleadoXML.appendChild(comision);

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("EMPLEADO.xml"));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
        Empleado empleado = new Empleado();
        //   File file = new File("EMPLEADOS.DAT");
        //Creamos el fichero EMPLEADOS.DAT
        empleado.crearFicheroEmpleados("EMPLEADOS.DAT");
        //Creamos el fichero EMPLEADOS.XML usando DOM
        empleado.crearXML("EMPLEADOS.DAT");
        //  file.deleteOnExit();
    }

}
