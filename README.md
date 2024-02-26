# Bookmarks
## Autores:
|Nombre          |Correo                         |Cuenta                       |
|----------------|-------------------------------|-----------------------------|
|Sergio Antonio Olivares del Angel| sa.olivares.2020@alumnos.urjc.es|Mercii01|
|Pablo Antolín Martínez|p.antolin.2020@alumnos.urjc.es|Pbantolin12|
|Izan Ruiz Ballesteros|i.ruizba.2021@alumnos.urjc.es|Etheko|
|Blas Vita Ramos|b.vita.2020@alumnos.urjc.es|Blasetvrtumi|
|Diego Rodríguez Gallego|d.rodriguezgal.2020@alumnos.rujc.es|DiRoGa|


## Trello
Se utilizara la herramienta de trabajo "Trello" para facilitar la coordinación del equipo durante esta practica. El link es el siguiente: [EntrePaginas](https://trello.com/w/entrepaginas)

## Entidades
- Usuarios
- Libros  
- Reseñas  
- Comunidades
- Autor
- Genero
- Publicación

En cuanto a las relaciones entre las entidades, podemos notar que un usuario puede buscar libros y realizar reseñas. Un usuario puede unirse a comunidades y solicitar a un administrador que su pagina se convierta en pagina de autor. Este autor puede modificar su información para ser buscado mas fácilmente por los usuarios, así como poder añadir libros que ha escrito. Todos los libros tienen un genero al que se le asocia para realizar búsquedas de manera mas sencilla. Finalmente cualquier usuario que sea parte de una comunidad podrá realizar publicaciones.

## Permisos de los usuarios
-   Anónimo: Este usuario puede buscar libros, consultar reseñas y consultar foro de la comunidad.
-   Usuario registrado: El usuario registrado puede realizar las funciones del usuarios anónimo y ademas podrá modificar listas (por leer y leídos), modificar reseñas (solo podrá añadir reseñas de libros que haya marcado como leídos), unirse a comunidades, publicar en comunidades, crear comunidades, eliminar y añadir miembros a las comunidades (si se es administrador de comunidad), añadir, eliminar y editar sus publicaciones en la comunidad, solicitar rango de autor y modificar datos de su perfil de autor asi como la informacion de los libros sobre los que tiene permiso (Es autor del libro).
-   Usuario administrador: Este usuario puede realizar las mismas acciones que el usuario registrado, pero ademas podrá modificar bases de datos, otorgar rango de autor a usuarios registrados y expulsar usuarios registrados.

## Imágenes
En cuanto a las entidades que tienen asociadas imágenes, podemos encontrar 
- Usuarios: Los usuarios tienen la oportunidad de añadir una imagen de perfil, aunque si no desean añadir una, se les asignara automáticamente.
- Libros: A todos los libros se les asociara una imagen de portada.
- Comunidades: Todas las comunidades podrán añadir un icono de comunidad.
- Autor: Los autores tienen una imagen asociada para ser reconocidos fácilmente en su pagina.
- Publicación: En las publicaciones de comunidades se podrán adjuntar una o varias imágenes.

## Gráficos
Se crearan distintos gráficos, estos se pueden dividir en 3 tipos. 

### Los gráficos de usuario
-   Gráfico de páginas leídas por usuario al mes. // *Gráfico de líneas* 
-   Géneros más leídos por usuario. // *Gráfica de araña*

### Gráficos generales
-   Ranking de libros más populares (más leídos). // *Gráfica de tablas*
-   Ranking de libros mejor valorados. // *Gráfica de tablas*

### Gráficos de administrador
-   Diversos gráficos de estadísticas globales de uso de la aplicación. // *Sparklines, Gráfica de pastel, Gráficos de líneas*

## Tecnología complementaria
Para complementar la pagina, se incluirían las siguientes tecnologías:
-   Sistema de notificaciones por correo electrónico.
-   Utilizar una API REST para escoger una cantidad fija de libros y publicarlos en la base de datos propia de la aplicación

## Algoritmos o consulta avanzada
Se implementará un algoritmo de recomendaciones de libros en la página principal en función de la cantidad de libros que un usuario haya marcado como leídos según los atributos de los libros.

## Pantallas
- Inicio de sesión:

<img height="100%" src="src\main\resources\static\assets\documentation\login.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Pantalla de acceso para usuarios registrados y administradores</p>

- Registro:

<img height="100%" src="src\main\resources\static\assets\documentation\signup.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Aquí es donde un usuario se da de alta</p>

- Búsqueda de libros:

<img height="100%" src="\src\main\resources\staticassets\documentation\searchResults.png" width="50%"/>
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

- Foro:

<img height="100%" src="src\main\resources\static\assets\documentation\forum.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Menú del foro donde se muestran las comunidades y posts de las mismas a los que el usuario pertenece</p>

- Perfil:

<img height="100%" src="src\main\resources\static\assets\documentation\profile.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Pantalla de visualización de perfil con varias opciones como habilitar la edición</p>

- Comunidad:

<img height="100%" src="src\main\resources\static\assets\documentation\community.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Pantalla de acceso para usuarios registrados y administradores</p>

- Modificar perfil:

<img height="100%" src="src\main\resources\static\assets\documentation\editProfile.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Menú de edición de la información al completo del usuario, como por ejemplo la contraseña</p>

- Publicación:

<img height="100%" src="src\main\resources\static\assets\documentation\post.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Pantalla de visualización de una publicación individual de una comunidad de la que se forme parte</p>

- Lista de miembros de comunidad:

<img height="100%" src="src\main\resources\static\assets\documentation\memberList.png" width="50%"/>
<p style="text-align: left; font-size: 9px">Lista de miembros de una comunidad del usuario junto a información general de los mismos</p>

## Mapa de navegación

<img src="src\main\resources\static\assets\documentation\map.png"/>



