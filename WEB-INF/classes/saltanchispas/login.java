package saltanchispas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;



public class login extends HttpServlet {

    public void init(ServletConfig config)
	throws ServletException {
		super.init(config);
    }


	/* CONEXIONES DE LA BASE DE DATOS */
	/**
	* Devuelve conexion a MySQL
	*
	*/
	protected Connection obtenerConexion(HttpServletResponse response) throws Exception {
		String url = "jdbc:postgresql://localhost:5432/saltanchispas";
		try {

			Class.forName ( "org.postgresql.Driver" );
            Properties usuario = new Properties(); //Creamos el usuario con permisos de la base de datos
            usuario.setProperty("user", "postgres");
            usuario.setProperty("password", "admin");

			Connection con = DriverManager.getConnection(url,usuario);
			System.out.println("Conexion establecida con " + url + "...");
			return con;
		}
		catch (Exception e) {
			System.out.println("Conexion NO establecida con " + url);
			throw (e);
		}
	}

	/**
	* Este metodo ejecuta una sentencia de seleccion/consulta y muestra el resultado
	* @param con database connection
	* @param sqlStatement SQL SELECT statement to execute
	*/
	protected int iniciarSesion(Connection con, String nickname, String password) throws Exception {
		int resultado = 0;
		try {
			String consulta = "select id from usuarios where nickname = ? and password = ?";
			PreparedStatement stm=con.prepareStatement(consulta);
			stm.setString(1, nickname);
			stm.setString(2, password);
			ResultSet rs = stm.executeQuery( );
			if(rs.next()) { //Comprobamos que haya un resultado al menos
				resultado = rs.getInt("id"); //Devolvemos true
			}
			rs.close ();
		}
		catch (SQLException e) {
			System.out.println ( "Error ejecutando la sentencia SQL" );
			throw (e);
		}
		return resultado;
	}
	
	protected void modificarInsertarDatos(Connection con, String nickname, String nombre, String apellidos, String password,String correo )throws Exception{
		try {
			String consulta = "insert into usuarios values(?,?,?,?,?)";
			PreparedStatement stm=con.prepareStatement(consulta);
			stm.setString(1, nickname);
			stm.setString(2, nombre);
			stm.setString(3, apellidos);
			stm.setString(4, password);
			stm.setString(5, correo);
			stm.executeUpdate( );
		}
		catch (SQLException e) {
			System.out.println ( "Error ejecutando la sentencia SQL" );
			throw (e);
		}

	}
	
	public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws ServletException, IOException 
    {
	// If it is a get request forward to doPost()
	doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
		       HttpServletResponse response)
	throws ServletException, IOException 
    {
	// Obtenemos los parametros de la peticion 
	String nickname = request.getParameter("nickname");
	String nombre = request.getParameter("nombre");
	String apellidos = request.getParameter("apellidos");
	String password = request.getParameter("password");
	String correo = request.getParameter("correo");
	Connection con ;
	try{
	con = obtenerConexion(response); //Obtenemos conexion
	boolean ifa = false;
	modificarInsertarDatos(con, nickname, nombre,apellidos, password, correo);//Iniciar sesion con username password
	int id = iniciarSesion(con, nickname, password);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(
				"<!DOCTYPE html>"+

"<html>"+
	"<head>"+
		"<title>Saltan Chispas</title>"+
		"<meta name = \"author\" content = \"Alberto Pérez HErnández y Antón Pérez Vázquez\">"+
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+

		"<link rel=\"stylesheet\" href=\"../orange.css\">"+
		"<link rel=\"stylesheet\" href=\"index.css\">"+
	"</head>"+
	
	"<body>"+

		"<!-- Imagenes cabecera -->"+
		"<div class = \"cabecera\" >"+
			"<img class = \"logo\" src = \"../imagenes/logoL.png\" alt= \"logo en vista de pc\" width=\"300\" height=\"100\">"+
			"<img class = \"iniciarSesion\" src = \"../imagenes/login.png\" alt= \"icono de login\" height=\"55\">"+
		"</div>"+

		"<!-- MENU -->"+
		"<div class = \"menu\">"+
			"<a class = \"menus\" href = \"../index.html\">Inicio</a>"+
			"<a class = \"menus\" href = \"../actualidad/index.html\">Actualidad</a>"+
			"<a class = \"menus\" href =\"../foro/index.html\">Foro</a>"+
			"<a class = \"menus\" href = \"../campeonatos/f1.html\">Campeonatos</a>"+
			"<a class = \"menus\" href = \"../galeria/index.html\">Galería</a>"+
			"<a class = \"menus\" href = \"../miperfil/index.html\">Mi Perfil</a>"+
			"<a class = \"menus\" href = \"../sobrenosotros/index.html\">Sobre Nosotros</a>"+
			
		"</div>"+
        "<!-- CAMPOS -->"+
		"<div class = \"cuerpo\">"+
			"<p>"+
				"<div class = \"etiqueta1\">"+
					"<label for = \"nickname\">Nickname</label>"+
				"</div>"+

				"<div class = \"campo1\">"+
					"<input type=\"text\" id=\"nickname\" placeholder=\""+nickname+"\" size=\"16\" required>"+
				"</div>"+

				"<div class = \"etiqueta2\">"+
					"<label for = \"nombre\">Nombre</label>"+ 
				"</div>"+

				"<div class = \"campo2\">"+
					"<input type=\"text\" id=\"nombre\" placeholder=\""+nombre+"\" size=\"16\" required>"+
				"</div>"+

				"<div class = \"etiqueta3\">"+

					"<label for = \"apellidos\">Apellidos</label>"+
				"</div>"+

				"<div class = \"campo3\">"+
					"<input type=\"text\" id=\"apellidos\" placeholder=\""+apellidos+"\" size=\"16\" required>"+
				"</div>"+

				"<div class = \"etiqueta4\">"+
					"<label for = \"contrasenha\">Contraseña</label>"+
				"</div>"+

				"<div class = \"campo4\">"+
					"<input type=\"password\" id=\"contrasenha\" size=\"16\" required>"+
				"</div>"+

				"<div class = \"etiqueta5\">"+
					"<label for = \"email\">Email</label>"+
				"</div>"+

				"<div class = \"campo5\">"+
					"<input type=\"email\" id=\"email\" placeholder=\""+correo+"\" size=\"16\" required>"+
				"</div>"+

			"</P>"+
			"<div class = \"botonborrar\">"+
				"<button id=\"borrarButton\">Borrar</button>"+
			"</div>"+
			"<script>"+
				"const element = document.getElementById(\"borrarButton\");"+			
				"element.addEventListener('click', borrarElementos);"+
				"function borrarElementos(){"+
					
					"document.getElementById(\"nickname\").value = \"\";"+
					"document.getElementById(\"nombre\").value = \"\";"+
					"document.getElementById(\"apellidos\").value = \"\";"+
					"document.getElementById(\"contrasenha\").value = \"\";"+
					"document.getElementById(\"email\").value = \"\";"+

				"}"+	

			"</script>"+

			"<div class = \"botonguardar\">"+
				"<button id=\"guardarButton\">Guardar</button>"+
			"</div>"+

			"<script>"+
				"const element2 = document.getElementById(\"guardarButton\");"+			
				"element2.addEventListener('click', guardarElementos);"+
				"function guardarElementos(){"+
					
					"var nickname = document.getElementById(\"nickname\").value;"+
					"var nombre = document.getElementById(\"nombre\").value;"+
					"var apellidos = document.getElementById(\"apellidos\").value;"+
					"var contrasenha = document.getElementById(\"contrasenha\").value;"+
					"var email = document.getElementById(\"email\").value;"+
					"location.href = \"../miperfil/index?nickname=\"+nickname=+\"&nombre=\"+nombre+\"&apellidos=\"+apellidos+\"&password=\"+contrasenha+\"&correo=\"+email;"+			
				"}"+	
			
			"</script>"+
		"</div>"+
		"<div align=\"center\"> INFORMACIÓN GUARDADA CORRECTAMENTE PARA EL USUARIO #"+ String.valueOf(id) + "</div>"+

       "<!-- PIE DE PAGINA-->"+

		"<div class=\"piePagina\">"+
			
			"<h3 class=\"pieDePagina\">"+
				"Este sitio web no es oficial y no está asociado en ningún modo con el grupo de compañías de la Fórmula 1. F1, FORMULA ONE, FORMULA 1, FIA FORMULA ONE WORLD CHAMPIONSHIP, GRAND PRIX y marcas relacionadas son marcas registradas de Formula One Licensing B.V."+	
			"</h3>"+

		"</div>"+
		
	"</body>"+
"</html>"+
"");
		
	}catch(Exception e){

		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String docType =
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
				"Transitional//EN\">\n";
			out.println(docType +
					"<HTML>\n" +
					"<HEAD><TITLE>Bienvenido a DAW</TITLE></HEAD>\n" +
					"<BODY BGCOLOR=\"#FDF5E6\">\n" +
					"<p>" + errors.toString() +"</p>" +
					"</BODY></HTML>");
	}
}

    public void destroy() 
    {
    }
}