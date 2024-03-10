# <p align="center">DonComanda</p>
 Es una aplicación que permite tomar notas de pedidos de un bar con la ayuda de las interfaces creadas con Java y JavaFX, así como generar facturas simplificadas o históricos de un día con JasperSoft y MySQL con JDBC

 ## Índice
 1. [Introducción](#introducción)
 2. [Lenguajes y programas utilizados](#lenguajes-y-programas-utilizados)
 3. [Funcionalidades](#funcionalidades)
 4. [Futuras mejoras](#futuras-mejoras)

 ## Introducción
  La principal motivación para crear este proyecto es poder aprobar el módulo de Desarrollo de Interfaces, es decir, que es un trabajo de clase. Sin embargo, eso no quiere decir que no le haya dedicado tiempo y esfuerzo al programa. De hecho, todo lo contrario, he tenido que dedicarle mucho tiempo a solucionar muchos errores que me han surgido con JasperSoft, así como con la interfaz gráfica, que si la desplegáis veréis que, tal vez, no es la cosa más bonita que hayáis podido ver.
  
 ## Lenguajes y programas utilizados
 Los lenguajes utilizados para crear DonComanda han sido:
 
  <div align="center">
   <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
   <img src="https://img.shields.io/badge/mysql-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
   <img src="https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white" alt="JavaFX">
  </div>

 Y los IDE utilizados han sido:

  <div align="center">
   <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white" alt="IntelliJ IDEA">
   <img src="https://img.shields.io/badge/MySQL_Workbench-%23224f70.svg?style=for-the-badge&logo=mysql&logoColor=orange" alt="MySQL">
   <img src="https://img.shields.io/badge/JasperSoft-0e67a4.svg?style=for-the-badge&logo=apache&logoColor=white" alt="JavaFX">
  </div>
 
 ## Funcionalidades
 Don comanda permite gestionar las consumiciones de un bar, ya sean en pedidos en mesa o sin mesa, a través de una interfaz gráfica que estará compuesta por un total de dos ventanas.
 
 La principal permitirá seleccionar los productos que se van a consumir, los cuales se irán agregando a una tabla para así mostrar: nombre del producto, precio, unidades, descuento e importe total de ese producto en concreto; también se mostrará la cantidad de artículos pedidos, como el total de unidades y el importe total de toda la comanda.
 Haciendo uso de la tabla junto a la calculadora integrada, se podrán eliminar productos o incrementar el número de unidades de forma directa, ya que sino también está la opción de aumentar en uno el número de unidades al clicar sobre el mismo producto varias veces.
 Una vez finalizado el pedido se podrá generar una factura simplificada del pedido con ayuda de la base de datos y Jaspersoft, que contendrá todos los datos básicos que necesita saber un cliente (casi los mismos datos que hay en la tabla). También se podrá generar un histórico del día, donde se mostrarán todas las ventas agrupadas para y así ver el número total de unidades vendidas como el importe total obtenido.

 Se podrá acceder a la pantalla secundaria para seleccionar una mesa, si ya había una cuenta empezada se pasarán los datos a la factura de esa mesa. Por tanto, esta pantalla mostrará el número de mesa y si si está libre u ocupada de una forma totalmente intuitiva.
 Cabe mencionar que si se cambia de mesa o se cierra el programa, las mesas seguirán manteniendo la lista de productos que tenían asociados.
 ## Futuras mejoras
 - Sin duda alguna, un aspecto a mejorar de la aplicación es el diseño de la interfaz, es altamente intuitivo, pero no es realmente agradable a la vista.
 - Otra mejora que me gustaría implementar sería la posibilidad de poder hacer la aplicación más personalizable, que no haya necesidad de tocar la base de datos o el código fuente.
