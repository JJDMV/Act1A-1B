package gestionficherosapp;

import java.io.File;
import java.io.IOException;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		
		//Con listRoots obtenemos la ruta del fichero y la guardamos en la variable carpetaDeTrabajo la cual hemos declarado arriba
		carpetaDeTrabajo = File.listRoots()[0];
		
		//Lanzamos la funcion actualiza para crear nuestra tabla que reune todos los ficheros
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; 
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}
		
	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {		
		
		//File, convierte una ruta en una variable de tipo "directorio/fichero" 
		//carpeta en la que estamos (variable carpetaDeTrabajo) con nombre del fichero/directorio seleccionado (variable arg0),
		
		File Carpeta = new File(carpetaDeTrabajo,arg0);
		
		Carpeta.mkdir();
		
		//La funcion actualiza refresca los ficheros/directorios y muestra la tabla actualizada
		
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File File = new File(carpetaDeTrabajo,arg0);
		
		try {
			File.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actualiza();
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
		//declaramos el fichero/directorio
		File File = new File(carpetaDeTrabajo,arg0);
		
		
		//Lo eliminamos con la funcion delete
		File.delete();
			
				
		//siempre que hagamos un cambio actualizaremos la tabla para mostrar ficheros/directorios
		actualiza();

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		//StringBuilder para variables de tipo texto
		StringBuilder strBuilder=new StringBuilder();
		
		//Declaramos el fichero
		File file = new File(carpetaDeTrabajo,arg0);
		
		
		//Comprobacion de existencia de fichero y permisos de XRW (Execution, Read, Write)
		if (file.exists() && file.canExecute() && file.canRead() && file.canWrite()) {	
					
			//asignamos texto a strBuilder
			//Título
			strBuilder.append("INFORMACIÓN DEL SISTEMA");
			
			//\n salto de linea 
			strBuilder.append("\n\n");
			
			//Nombre
			strBuilder.append("Nombre: ");
			
			//arg0, nombre del fichero
			strBuilder.append(arg0);
			strBuilder.append("\n");
			
			
			
			//Tipo: fichero o directorio
			strBuilder.append("Tipo de fichero: ");
			
			//Para comprobar si lo seleccionado es un archivo o un directorio usamos la funcion 'isDirectory()' 
			//Esta funcion devuelve True en caso de que sea un directorio y False en caso de que no lo sea
			//si no es un directorio será un fichero
			
			//if, si es Verdadero, devuelve 'Directorio', si es Falso devuelva 'Archivo'
			if (file.isDirectory()) {
				
				strBuilder.append("Directorio");
			}else {
				
				strBuilder.append("Archivo");
			}
			
			strBuilder.append("\n");
			//Ubicación
			strBuilder.append("Ubicacion: ");
			
			
			
			//funcion 'getPath()' para obtener la ruta del fichero/directorio
			strBuilder.append(file.getPath());
			strBuilder.append("\n");
			//Fecha de última modificación
			strBuilder.append("Ult. modificacion: ");
			
			//Para obtener la ultima modificacion del fichero/directorio
			strBuilder.append(file.lastModified());
			strBuilder.append("\n");
			//Si es un fichero oculto o no
			
			strBuilder.append("Fichero estado visibilidad: ");
			
			//con directorios/fichero ocultos, funcion 'isHidden()' la cual devuelve Verdadero o Falso
			if  (file.isHidden()) {
				strBuilder.append(" Oculto");
			}else {
				strBuilder.append(" Visible");
			}
			
			strBuilder.append("\n");
				
			//Si es un fichero: Tamaño en bytes
			strBuilder.append("Tamaño bytes: ");
			
			
			//funcion 'lenght()', devuelve el tamaño de lo que contenga la variable
			
			//En caso de ser un string, devolverá la cantidad de caracteres
			//En nuestro caso devolverá la cantidad de bytes del fichero/directorio
			strBuilder.append(file.length());
			strBuilder.append("\n");
			//Si es directorio: Número de elementos que contiene, 
			strBuilder.append("Numero elementos contenidos: ");
			
			
			//comprobamos que es un directorio
			if (file.isDirectory()) {
				
					//'list()' devolverá un array con todos los nombres que contiene el directorio
					//'lenght' para medir el resultado del list
					
				strBuilder.append(file.list().length);
				strBuilder.append("\n");

			
			}else {
				//En el caso de que no sea un directorio lo avisamos
				strBuilder.append("No es directorio");
				
			}
			
			
			//Si es directorio: Espacio libre, espacio total (bytes)
			strBuilder.append("Espacio Libre:");
			
			//'getFreeSpace()', numero de bytes libres del directorio
			strBuilder.append(file.getFreeSpace());
			strBuilder.append("\n");
			
			
			//'getTotalSpace()', espacio total del fichero
			strBuilder.append("Espacio total:");
			strBuilder.append(file.getTotalSpace());
			strBuilder.append("\n");
			
		}else {
			//else del primer if en el cual comprobamos si el fichero/directorio no es accesible
			
			//indicamos si el fichero no es accesible, 
			strBuilder.append("Fichero no accesible.");
			
		}
		
		return strBuilder.toString();

	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)	throws GestionFicherosException {
		// TODO Auto-generated method stub
		
		
		//Fichero/directorio de entrada
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Fichero/directorio de salida
		File file2 = new File(carpetaDeTrabajo,arg1);
		
		//fichero_de_entrada.renameTo(fichero_de_salida);
		
		file.renameTo(file2);
		
		//actualizamos la tabla.
		actualiza();
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		//controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		//controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}