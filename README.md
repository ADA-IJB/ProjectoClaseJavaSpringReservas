# MatriculasSpring

Aplicación REST para gestionar reservas en un instituto, esto incluye profesores, aulas y las franjas horarias disponibles. La API permite consultar, crear, actualizar y eliminar registros, y persiste los datos en MySQL mediante Spring Data JPA.

## Tecnologías

- Java 21
- Spring Boot 4.1.1 y Spring MVC
- Spring Data JPA / Hibernate
- MySQL

## Requisitos

- JDK 25 instalado y disponible (`java` y `javac`).
- MySQL en ejecución y permisos para crear o utilizar la base de datos `centro`.
- Postman para enviar peticiones a la API.
- Un navegador para el FrontEnd.

## Instalación y ejecución

1. Clona el repositorio y entra en el directorio del proyecto:

   ```bash
   git clone https://github.com/ADA-IJB/ProjectoClaseJavaSpringReservas.git
   cd ProjectoClaseJavaSpringReservas
   ```

2. Configura la conexión a MySQL en `src/main/resources/application.properties`. Ajusta la URL, el usuario y la contraseña a tu entorno. No publiques credenciales reales en el repositorio.

   La configuración actual apunta a `localhost:3306` y a la base de datos `matriculas`. La URL incluye `createDatabaseIfNotExist=true`; el usuario de MySQL debe tener permisos suficientes para crearla si todavía no existe.

3. Inicia la aplicación:

   ```bash
   ./mvnw spring-boot:run
   ```

   En Windows puedes utilizar:

   ```bat
   mvnw.cmd spring-boot:run
   ```

   Spring Boot inicia el servidor en `http://localhost:8080`. Hibernate está configurado para actualizar el esquema de la base de datos (`spring.jpa.hibernate.ddl-auto=update`).

## Uso desde Postman

1. Asegúrate de que MySQL y la aplicación estén en ejecución.
2. Crea una petición en Postman y utiliza `http://localhost:8080` como base URL.
3. Selecciona el método HTTP correspondiente y añade la ruta indicada en la tabla de endpoints.
4. Para `POST`, `PUT` y `PATCH`, selecciona **Body → raw → JSON** y establece `Content-Type: application/json`.
5. Envía la petición y consulta el código HTTP y el cuerpo de la respuesta.

### Ejemplos

Crear una reserva con `POST /reservas` (tienen que existir los demas objetos):

```json
{
   "fecha": "2026-12-10",
   "aula": {
      "numeroClase": "1",
      "tieneOrdenadores": true
   },
   "franjaHoraria": {
      "id": 1,
      "horaInicio": "20:05:00",
      "horaFin": "21:05:00"
   },
   "profesor": {
      "nombre": "Antonio",
      "apellidos": "Verde",
      "dni": "14445678X",
      "fechaNacimiento": "2001-10-10",
      "sueldo": 1000.10
   }
}
```

Crear un profesor con `POST /profesores`:

```json
{
   "profesor": {
      "nombre": "Antonio",
      "apellidos": "Verde",
      "dni": "14445678X",
      "fechaNacimiento": "2001-10-10",
      "sueldo": 1000.10
   }
}
```

Crear una franja horaria con `POST /franjasHorarias`:

```json
{
   "horaInicio": "10:31:31",
   "horaFin": "12:31:31"
}
```

Crear un aula con `POST /aulas`:

```json
{
   "numeroClase": "1",
   "tieneOrdenadores": true
}
```

Actualizar cualquiera de los objetos requiere que pongas su ide al final, ejemplo: `PUT /aulas/{id}`.

### Endpoints disponibles

| Método | Ruta | Uso | Respuestas principales |
|---|---|---|---|
| `GET` | `/aulas` | Listar aulas | `200 OK` |
| `GET` | `/aulas/{id}` | Consultar un aula | `200 OK`, `404 Not Found` |
| `POST` | `/aulas` | Crear un aula | `201 Created` |
| `PUT` | `/aulas/{id}` | Actualizar un aula existente | `200 OK`, `404 Not Found` |
| `DELETE` | `/aulas/{id}` | Eliminar un aula | `204 No Content` |
| `GET` | `/profesores` | Listar profesores | `200 OK` |
| `GET` | `/profesores/{id}` | Consultar un profesor | `200 OK`, `404 Not Found` |
| `POST` | `/profesores` | Crear un profesor | `201 Created` |
| `PUT` | `/profesores/{id}` | Actualizar un profesor existente | `200 OK`, `404 Not Found` |
| `DELETE` | `/profesores/{id}` | Eliminar un profesor | `204 No Content`, `404 Not Found` |
| `GET` | `/franjasHorarias` | Listar franjas horarias | `200 OK` |
| `GET` | `/franjasHorarias/{id}` | Consultar una franja horaria | `200 OK`, `404 Not Found` |
| `POST` | `/franjasHorarias` | Crear una franja horaria | `201 Created` |
| `PUT` | `/franjasHorarias/{id}` | Actualizar una franja horaria existente | `200 OK`, `404 Not Found` |
| `DELETE` | `/franjasHorarias/{id}` | Eliminar una franja horaria | `204 No Content`, `404 Not Found` |
| `GET` | `/reservas` | Listar reservas | `200 OK` |
| `GET` | `/reservas/{id}` | Consultar una reserva | `200 OK`, `404 Not Found` |
| `POST` | `/reservas` | Crear una reserva | `201 Created` |
| `PUT` | `/reservas/{id}` | Actualizar una reserva existente | `200 OK`, `404 Not Found` |
| `DELETE` | `/reservas/{id}` | Eliminar una reserva | `204 No Content`, `404 Not Found` |
| `GET` | `/reservas/{IdAula}/aula` | Aula de una reserva | `200 OK`, `404 Not Found` |
| `GET` | `/reservas/{IdFranjaHoraria}/aula` | franja horaria de una reserva | `200 OK`, `404 Not Found` |
| `GET` | `/reservas/{IdProfesor}/aula` | profesor de una reserva | `200 OK`, `404 Not Found` |

## Frontend

La carpeta `frontend2/` contiene una interfaz web en HTML, CSS y JavaScript puro (sin frameworks) para gestionar las reservas y hacer alguna consulta.

1. Arranca el backend (`./mvnw spring-boot:run`).
2. Abre el archivo index del frontend desde un navegador y ya estas listo para funcionar.

## Estructura del código

- `controller/`: controladores REST de reservas, aulas, franjas horarias y profesores.
- `entity/`: entidades JPA y sus campos.
- `repository/`: repositorios Spring Data JPA.
- `src/main/resources/application.properties`: configuración de la aplicación y de la conexión a MySQL.
- `frontend2/`: interfaz web estática (`index.html`, `css/`, `js/`).

## Uso de IA

No se ha usado la IA para ninguna parte de este proyecto.

## Problemas encontrados

La práctica ha ido bien mayormente, ya que usando de base el otro proyecto que se ha ido haciendo en clase se puede hacer todo lo que se pide en este sin mayor problema, igual sí que ha habido problemas que voy a listar ahora.

- Le puse OneToOne a la relación de las reservas y las franjas y no me di cuenta de que eso hacía que ninguna reserva pudiera tener la misma franja independientemente de cuando fuera, así que tuve que cambiarlo, no sé si lo ajuste bien del todo, pero funciona así que asumiré que si.
- Tuve bastantes problemas con la comprobación de que las franjas horarias estuvieran libres, ya que no conseguía que las detectara cuando eran iguales de ninguna manera, así que hice un equals en la clase de las franjas, y esto iba con el postman, pero no con el frontend, luego me di cuenta de que el frontend mandaba solo el id, ya que es lo único que hace falta para añadir el objeto y eso yo no lo sabía, así que cambie el equals por uno que mirara solo el id y ya funciona, no creo que este método este del todo bien pero no se me ha ocurrido otro.
- Al no usar ninguna IA en el proyecto tuve que buscarme la vida para hacer el frontend, intente hacerlo con el del otro proyecto y llegue considerablemente lejos, pero no le veía futuro, así que como Aitor García ya lo había hecho le pedí el suyo y lo modifiqué como pude, con un éxito considerable pero no del todo completo.
