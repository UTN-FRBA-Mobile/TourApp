# TourApp

Para correr la aplicación, se debe ejecutar json-server:
https://www.npmjs.com/package/json-server#getting-started

Para ejecutarlo:
- Ver la IP de la red local utilizando ifconfig.
- Configurar la IP en build.gradle -> API_URL.
- Abrir una terminal.
- Ir a la carpeta del proyecto.
- Correr el siguiente comando, reemplazando la IP de ejemplo por la recién configurada: json-server --watch db.json --host 192.168.0.218

Una vez hecho esto, se puede buildear y lanzar la aplicación.
