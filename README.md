<h1 style="text-align: center;">
📚Bookmarks
</h1>

<p style="text-align: center;">
   <img src="src\main\resources\static\assets\isotipoLight.svg" width="35%"/>
</p>


## ✅ Tabla de Contenido

- [Autores](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#autores)
- [Trello](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#autores)
- [Entidades](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#entidades)
- [Permisos de los usuarios](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#permisos-de-los-usuarios)
- [Imágenes](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#imágenes)
- [Gráficos](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#gráficos)
- [Tecnología complementaria](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#tecnología-complementaria)
- [Algoritmos o consulta avanzada](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#algoritmos-o-consulta-avanzada)
- [Pantallas](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#pantallas)
- [Mapa de navegación](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#mapa-de-navegación)
- [Instalación](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#instalación)
- [Participación](https://github.com/CodeURJC-DAW-2023-24/webapp03/tree/Database-Implementation?tab=readme-ov-file#participación)

## 💻 Autores:
|Nombre          |Correo                         |Cuenta                       |
|----------------|-------------------------------|-----------------------------|
|Sergio Antonio Olivares del Ángel| sa.olivares.2020@alumnos.urjc.es|[Mercii01](https://github.com/Mercii01)|
|Pablo Antolín Martínez|p.antolin.2020@alumnos.urjc.es|[Pbantolin12](https://github.com/Pbantolin12)|
|Izan Ruiz Ballesteros|i.ruizba.2021@alumnos.urjc.es|[Etheko](https://github.com/Etheko)|
|Blas Vita Ramos|b.vita.2020@alumnos.urjc.es|[Blasetvrtumi](https://github.com/Blasetvrtumi)|
|Diego Rodríguez Gallego|d.rodriguezgal.2020@alumnos.rujc.es|[DiRoGa](https://github.com/DiRoGa)|


## 🗂️ Trello
Se utilizara la herramienta de trabajo "Trello" para facilitar la coordinación del equipo durante esta practica. El link es el siguiente: [EntrePaginas](https://trello.com/w/entrepaginas)

## ⚛️ Entidades
- Usuarios
- Libros
- Reseñas
- Autor
- Genero

En cuanto a las relaciones entre las entidades, podemos notar que un usuario puede buscar libros y realizar reseñas. Un usuario puede unirse a comunidades y solicitar a un administrador que su pagina se convierta en pagina de autor. Este autor puede modificar su información para ser buscado mas fácilmente por los usuarios, así como poder añadir libros que ha escrito. Todos los libros tienen un genero al que se le asocia para realizar búsquedas de manera mas sencilla. Finalmente cualquier usuario que sea parte de una comunidad podrá realizar publicaciones.

## 👥 Permisos de los usuarios
-   Anónimo: Este usuario puede buscar libros, consultar reseñas y consultar foro de la comunidad.
-   Usuario registrado: El usuario registrado puede realizar las funciones del usuarios anónimo y ademas podrá modificar listas (por leer y leídos), modificar reseñas (solo podrá añadir reseñas de libros que haya marcado como leídos), unirse a comunidades, publicar en comunidades, crear comunidades, eliminar y añadir miembros a las comunidades (si se es administrador de comunidad), añadir, eliminar y editar sus publicaciones en la comunidad, solicitar rango de autor y modificar datos de su perfil de autor asi como la informacion de los libros sobre los que tiene permiso (Es autor del libro).
-   Usuario administrador: Este usuario puede realizar las mismas acciones que el usuario registrado, pero ademas podrá modificar bases de datos, otorgar rango de autor a usuarios registrados y expulsar usuarios registrados.

## 🖼️ Imágenes
En cuanto a las entidades que tienen asociadas imágenes, podemos encontrar
- Usuarios: Los usuarios tienen la oportunidad de añadir una imagen de perfil, aunque si no desean añadir una, se les asignara automáticamente.
- Libros: A todos los libros se les asociara una imagen de portada.
- Comunidades: Todas las comunidades podrán añadir un icono de comunidad.
- Autor: Los autores tienen una imagen asociada para ser reconocidos fácilmente en su pagina.
- Publicación: En las publicaciones de comunidades se podrán adjuntar una o varias imágenes.

## 📊 Gráficos
Se crearan distintos gráficos, estos se pueden dividir en 3 tipos.

### Los gráficos de usuario
-   Gráfico de páginas leídas por usuario al mes. // *Gráfico de líneas*
-   Géneros más leídos por usuario. // *Gráfica de araña*

### Gráficos generales
-   Ranking de libros más populares (más leídos). // *Gráfica de tablas*
-   Ranking de libros mejor valorados. // *Gráfica de tablas*

### Gráficos de administrador
-   Diversos gráficos de estadísticas globales de uso de la aplicación. // *Sparklines, Gráfica de pastel, Gráficos de líneas*

## 🪛 Tecnología complementaria
Para complementar la pagina, se incluirían las siguientes tecnologías:
-   Sistema de notificaciones por correo electrónico.
-   Exportación de listas de libros de usuarios a ficheros de valores separados por comas.

## 🧮 Algoritmos o consulta avanzada
Se implementará un algoritmo de recomendaciones de libros en la página principal en función de la cantidad de libros que un usuario haya marcado como leídos según los atributos de los libros.


## 📟 Pantallas
- Inicio de sesión:

<img height="100%" src="src\main\resources\static\assets\documentation\login.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Pantalla de acceso para usuarios registrados y administradores</p>  

- Registro:

<img height="100%" src="src\main\resources\static\assets\documentation\signup.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Aquí es donde un usuario se da de alta</p>  

- Búsqueda de libros:

<img height="100%" src="src\main\resources\static\assets\documentation\searchResults.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Pantalla que muestra coincidencias en la búsqueda de un libro</p>  

- Inicio:

<img height="100%" src="src\main\resources\static\assets\documentation\landingPage.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Pantalla principal que da acceso a todas las demás</p>  

- Administrador:

<img height="100%" src="src\main\resources\static\assets\documentation\administrator.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Panel de control de un administrador</p>  

- Crear/modificar libro:

<img height="100%" src="src\main\resources\static\assets\documentation\bookEdit.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Esta pantalla sirve tanto para crear como para modificar un libro y su información</p>  

- Consultar libro:

<img height="100%" src="src\main\resources\static\assets\documentation\bookInfo.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Aquí se puede consultar individualmente la información de un libro almacenado en una Base de Datos</p>  

- Perfil:

<img height="100%" src="src\main\resources\static\assets\documentation\profile.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Pantalla de visualización de perfil con varias opciones como habilitar la edición</p>  

- Modificar perfil:

<img height="100%" src="src\main\resources\static\assets\documentation\editProfile.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Menú de edición de la información al completo del usuario, como por ejemplo la contraseña</p>  

- Error:

<img height="100%" src="src\main\resources\static\assets\documentation\error.png" width="50%"/>  
<p style="text-align: left; font-size: 9px">Menú de error que muestra los detalles del mismo y que puede ser mostrado desde cualquier otra pantalla dependiendo del error.</p>  

## 🔀 Mapa de navegación

<img src="src\main\resources\static\assets\documentation\map.jpg"/>

## 🛠️ Instalación

### Requisitos previos:
Para poder instalar y ejecutar la aplicación correctamente es necesario:

- [Java Development Kit versión 17 Amazon corretto](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
- [MySQL 8.3.0](https://dev.mysql.com/downloads/mysql/)
- [Springboot 3.2.3](https://github.com/spring-projects/spring-boot/releases/tag/v3.2.3)
- [Maven 3.9.6](https://maven.apache.org/download.cgi)

1. Clona el repositorio a tu ruta deseada:
   `git clone https://github.com/CodeURJC-DAW-2023-24/webapp03`
2. Conectarse a la base de datos:
   `docker run -d --name bookmarksDB -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=bookmarks mysql:latest`
3. Hacer clean and Build del proyecto y ejecutar la aplicación (según IDE)
4. Abrir la aplicación en el navegador deseado:
   `start https://localhost:8443`

## 📋 Participación

| Miembro  |  Commit #1 | Commit #2  |  Commit #3 |  Commit #4 |  Commit #5 |  Archivo #1 |  Archivo #2 |   Archivo #3|  Archivo #4 |  Archivo #5 |
|---|---|---|---|---|---|---|---|---|---|---|
|  [Sergio Antonio Olivares del Angel](https://github.com/Mercii01) |   |   |   |   |   |   |   |   |   |   |
|  [Pablo Antolín Martínez](https://github.com/Pbantolin12) |   |   |   |   |   |   |   |   |   |   |
|  [Izan Ruiz Ballesteros](https://github.com/Etheko) |   |   |   |   |   |   |   |   |   |   |
|   [Blas Vita Ramos](https://github.com/Blasetvrtumi)|  [Updated README.md with screenshots](https://github.com/CodeURJC-DAW-2023-24/webapp03/commit/fac896b630f94a503d6852ac1ad5a3de1b0ca006)|  [Profile view and edition integrated](https://github.com/CodeURJC-DAW-2023-24/webapp03/commit/235f9aca8b8b561d0083c2685c25a1fd989b50a6)| [Dynamic search results loading with AJAX implemented](https://github.com/CodeURJC-DAW-2023-24/webapp03/commit/4369e591eb08d897bd05db7c6e4ab62ea00a782e)  |  [Profile image uploading and saving in database now implemented](https://github.com/CodeURJC-DAW-2023-24/webapp03/commit/94dd67b09f73bb4e907608d873b7f06c3e20db96) |  [Password change now fully implemented](https://github.com/CodeURJC-DAW-2023-24/webapp03/commit/393f1498c6b9ed1aa037ab18926deabf6fd76d86) | [EditProfilePageController.java](https://github.com/CodeURJC-DAW-2023-24/webapp03/blob/Database-Implementation/src/main/java/es/codeurjc/holamundo/controller/EditProfilePageController.java)  | [SearchResultsPageController.java](https://github.com/CodeURJC-DAW-2023-24/webapp03/blob/Database-Implementation/src/main/java/es/codeurjc/holamundo/controller/SearchResultsPageController.java)  | [User.java](https://github.com/CodeURJC-DAW-2023-24/webapp03/blob/Database-Implementation/src/main/java/es/codeurjc/holamundo/entity/User.java)  | [searchResultsPage.js](https://github.com/CodeURJC-DAW-2023-24/webapp03/blob/Database-Implementation/src/main/resources/static/js/searchResultsPage.js)  |  [editProfilePage.js](https://github.com/CodeURJC-DAW-2023-24/webapp03/blob/Database-Implementation/src/main/resources/static/js/editProfilePage.js) |
|   [Diego Rodríguez Gallego](https://github.com/DiRoGa)|   |   |   |   |   |   |   |   |   |   |

