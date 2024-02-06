# global-logic-bci

Este servicio proporciona una funcionalidad completa para el registro y autenticación de usuarios utilizando JSON Web Tokens (JWT). Está construido con Java 11, Spring Boot 2.5.14 y Gradle 7.4.

# Requisitos
- JDK 11
- Gradle (a partir de la version 5.0)

# Configuracion
En el archivo .properties del proyecto se encuentran las siguientes claves,
las cuales deben ser configuradas en este para poder correr el proyecto:

- #### jwt.secret: La clave secreta utilizada para firmar el token JWT.
- #### jwt.expiration: El tiempo de expiración del token JWT, en milisegundos.

# Instrucciones de uso
### Clonar el repositorio: 

git clone https://github.com/Deemeg/global-logic-bci.git

### Navegar al directorio del proyecto:

cd /global-logic-bci/bci-global-logic
### Compilar el proyecto: 
Utiliza Gradle para compilar el proyecto.
./gradlew build

### Ejecutar el servicio: 
Una vez compilado con éxito, puedes ejecutar el servicio utilizando el siguiente comando:

./gradlew bootRun
Este comando inicia la aplicación Spring Boot.

### Acceder al servicio: 
Una vez que la aplicación se haya iniciado correctamente, puedes acceder al servicio a través de tu navegador web o utilizando herramientas como Postman.

url: http://localhost:8080

# Endpoints
### Registro de Usuarios:
- Método: POST
- Ruta: /api/v1/auth/sign-up
- Cuerpo de la solicitud (JSON):
  {
  "name": "String",
  "email": "String",
  "password": "String",
  "phones": [
  {
  "number": long,
  "citycode": int,
  "countrycode": "String"
  }
  ]
  }

### Autenticacion de Usuarios:
- Método: POST
- Ruta: /api/v1/auth/login
- Cuerpo de la solicitud (JSON):
  {
  "email": "String",
  "password": "String",
  }
- Header: Authorization: Bearer token
# Funcionalidades
### Registro de Usuarios:

Envía una solicitud POST al endpoint /api/v1/auth/sign-up con el body correspondiente:

Al registrar el usuario, se obtendra un JWT y los datos del usuario creado.

### Autenticacion de usuario:
Envía una solicitud POST al endpoint /api/v1/auth/login con el body correspondiente
y el token JWT en el header el cual fue obtenido del registro de usuario:

Al autenticar al usuario se obtendran los datos del usuario junto con un nuevo token de acceso.


