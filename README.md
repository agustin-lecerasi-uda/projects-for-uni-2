

🚀🚀 Sistema de procesamiento de texto enfocado en la arquitectura cliente-servidor. 🚀🚀

--------------------------------------------------------------------------------------------------------------------------
Funciones:

	-Comunicar cliente con servidor 
	-Contar las vocales un texto
	-Contar las consonantes de un texto.
	-Contar las matusculas.
	-Y contar la longitud.
---------------------------------------------------------------------------------------------------------------------------	
🛠️ Sobre el código: 🛠️
Cliente

	-Implementa socket para la comunicación entre cliente y servidor. 
	-BufferedReader y PrintWriter para intercambiar información con el objeto socket.
	-Y el contacto inicial entra en un try{} catch para manejar exepciones.
	-Tanto la comunicación con la consola como con el servidor entran dentro de un buqle while.

Servidor

	-Implementa dos metodos nuevos, ManejarCliente y procesar Comando.
	-ManejarCliente() solo se encarga de recivir el mensaje y pasarcelo a procesarComando().
	-ProcesarComando() es quien va a validar el mensaje y procesar o delegar la tarea correspondiente.
	
