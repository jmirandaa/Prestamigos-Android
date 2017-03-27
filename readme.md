# Prestamigos

[![N|Android](https://www.android.com/static/2016/img/logo-android-green_2x.png)]()

Prestamigos es una aplicación para Android creada con el objetivo de mostrar una posible implementación de una aplicación sencilla para gestionar deudas y préstamos haciendo uso de Material Design y las librerías de terceros más comunes para ofrecer la funcionalidad requerida.

La aplicación permite:

  - Registrar nuevos usuarios, iniciar sesión, cerrar sesión y establecer nuevas contraseñas en caso de pérdida
  - Ver las deudas que debo y las que me deben. Filtrarlas por el nombre del usuario.
  - Añadir nuevas deudas, tanto para nuevos usuarios como para usuarios existentes.
  - Ver el historial de las deudas saldadas
  - Obtener un resumen de las deudas
  - Guardar los destinatarios de las deudas en un listado de amigos
  - Editar los datos del usuario

### Librerías de terceros usadas

* [ButterKnife](http://jakewharton.github.io/butterknife/) - Permite acceder a los elementos de la interfaz de usuario a través de anotaciones de forma sencilla
* [Retrofit](http://square.github.io/retrofit/) - Cliente HTTP que agiliza las llamadas a servicios de terceros y convierte las respuestas JSON a las clases a través de Jackson
* [Jackson](https://github.com/FasterXML/jackson) - Permite convertir la información contenida en un JSON a clases que sigan esa estructura de forma automática
* [EventBus](https://github.com/greenrobot/EventBus) - Permite una comunicación ágil y elegante entre clases a través suscriptores. Usado para comunicar la respuesta de las peticiones HTTP con las actividades y fragmentos, de forma que al recibir la información actualizan la interfaz de usuario según necesiten

Aunque en este proyecto no se usa al no hacer un uso intensivo de imágenes, Glide (https://github.com/bumptech/glide) también es muy recomendado para el manejo de imágenes, sobretodo dentro de RecyclerViews.

Licencia de uso
----

GNU GPL


