# API Mente Juego

Este es el backend para la aplicacion "Mente en Juego". Esta construido con Spring Boot, Kotlin y usamos MySQL como base de datos. Aqui manejamos toda la logica de usuarios, categorias, preguntas y el flujo del juego.

## Requisitos

Antes de arrancar, asegurate de tener esto instalado en tu maquina:

* Java JDK 17 o superior
* Docker Desktop (para la base de datos)
* IntelliJ IDEA (muy recomendado)
* Postman (para probar los endpoints)

## 1. Levantar la Base de Datos

Para no complicarnos instalando MySQL localmente, usamos Docker. En la raiz del proyecto ya tienes el archivo docker-compose.yml listo.

Solo abre tu terminal en la carpeta del proyecto y corre:

docker-compose up -d

Nota: Esto levanta MySQL en el puerto 3306. Revisa que no tengas otro MySQL corriendo en tu compu o te va a dar error de puerto ocupado.

Si quieres apagar la base de datos despues, usa:

docker-compose down

## 2. Correr la Aplicacion

Con la base de datos lista, ya puedes arrancar el servidor.

Si estas en Windows usa la terminal:
./gradlew.bat bootRun

Si estas en Mac o Linux:
./gradlew bootRun

O si prefieres hacerlo directo desde IntelliJ, busca la clase principal ApimentejuegoApplication.kt y dale al boton de Play.

La API quedara corriendo en: http://localhost:8080

## 3. Correr los Tests

Tenemos tests unitarios para asegurar que los servicios (User, Game, Category, etc.) funcionen bien y la logica no se rompa. Para correrlos todos de una:

En Windows:
./gradlew.bat test

En Mac o Linux:
./gradlew test

Si todo sale bien, veras un mensaje de BUILD SUCCESSFUL en la consola.

## 4. Ver el Coverage

Si quieres revisar que tanto codigo estan cubriendo los tests (para asegurar ese 100%), la forma mas facil es con la herramienta de IntelliJ:

1. Ve a la carpeta src/test/kotlin en la estructura del proyecto a la izquierda.
2. Dale clic derecho a la carpeta services.
3. Elige la opcion "Run 'Tests in 'services'' with Coverage" (es la opcion que tiene un icono de un escudo).
4. Cuando termine, se abrira una pestaña lateral llamada Coverage.
5. Ahi podras ver si las clases, metodos y lineas estan al 100% (se veran en verde).

## 5. Usar la Coleccion de Postman

En la carpeta raiz del proyecto encontraras el archivo .json con la coleccion de endpoints exportada.

Para usarla:

1. Abre Postman.
2. Dale al boton Import y carga el archivo JSON.
3. Si la coleccion usa variables, asegurate de configurar un entorno con la variable base_url en http://localhost:8080.

Te recomiendo seguir este orden para probar:
1. Crea primero un Usuario y algunas Categorias (endpoints POST).
2. Inicia un juego (POST /games/start).
3. Envia respuestas (POST /games/submit).

## Stack Tecnologico

* Kotlin
* Spring Boot 3
* Gradle (Kotlin DSL)
* MySQL
* JUnit 5 y Mockito