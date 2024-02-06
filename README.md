# global-logic-bci

Este servicio proporciona una funcionalidad completa para el registro y autenticación de usuarios utilizando JSON Web Tokens (JWT). Está construido con Java 11, Spring Boot 2.5.14 y Gradle 7.4.

# Funcionalidades
Registro de Usuarios: Permite a los usuarios registrarse a través del endpoint sign-up. 
Al registrarse, se genera un JSON Web Token que debe ser utilizado para la autenticación.

Autenticación de Usuarios: Los usuarios pueden iniciar sesión utilizando el endpoint login. 
Deben incluir el token JWT en el encabezado de autorización con el formato Bearer + token.

# Dependencias
El servicio utiliza las siguientes tecnologías y dependencias:

- Java 11
- Spring Boot 2.5.14
- Gradle 7.4

# Requisitos
- JDK 11
- Gradle (a partir de la version 5.0)

# Configuracion
En el archivo .properties del proyecto se encuentran las siguientes claves,
las cuales deben ser configuradas en este para poder correr el proyecto:

- #### jwt.secret: La clave secreta utilizada para firmar el token JWT.
- #### jwt.expiration: El tiempo de expiración del token JWT, en milisegundos.

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
# Uso
### Registro de Usuarios:

Envía una solicitud POST al endpoint /api/v1/auth/sign-up con el body correspondiente:

Al registrar el usuario, se obtendra un JWT y los datos del usuario creado.

### Autenticacion de usuario:
Envía una solicitud POST al endpoint /api/v1/auth/login con el body correspondiente
y el token JWT en el header el cual fue obtenido del registro de usuario:

Al autenticar al usuario se obtendran los datos del usuario junto con un nuevo token de acceso.


