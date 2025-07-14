Nombre del Proyecto: Project

¡Bienvenido(a) a mi Proyecto!

Esta es una aplicación de escritorio desarrollada en Java Swing para la gestión de pedidos, clientes y repartidores. Utiliza JFreeChart para la visualización de datos, Gson para la manipulación de datos JSON, y gestiona la información de usuarios a través de un archivo JSON local.

---

Requisitos Previos

Para ejecutar esta aplicación, necesitarás tener instalado lo siguiente: Java Development Kit (JDK) 23 o superior**: Puedes descargarlo desde el sitio oficial de Oracle o a través de OpenJDK.

---

Estructura del Proyecto

La carpeta de la aplicación debe tener la siguiente estructura:

Project/
├── src/
│   └── project/
│       ├── controller/
│       │   ├── AdministradorController.java
│       │   ├── EstadisticasController.java
│       │   ├── LoginController.java
│       │   ├── LoginResult.java
│       │   └── RepartidorController.java
│       ├── data/
│       │   └── GestorJSON.java
│       ├── model/
│       │   ├── base/
│       │   │   ├── Persona.java
│       │   │   └── Repository.java
│       │   ├── entities/
│       │   │   ├── BaseDeDatos.java
│       │   │   ├── Cliente.java
│       │   │   ├── HistorialDeEntrega.java
│       │   │   ├── Pedido.java
│       │   │   ├── Repartidor.java
│       │   │   └── Usuario.java
│       │   ├── enums/
│       │   │   ├── Combo.java
│       │   │   └── MetodoDePago.java
│       │   └── repositories/
│       │       ├── ClienteRepository.java
│       │       ├── HistorialEntregaRepository.java
│       │       ├── PedidoRepository.java
│       │       ├── RepartidorRepository.java
│       │       └── UsuarioRepository.java
│       ├── resources/  # Carpeta para imágenes y otros recursos generales
│       │   ├── solido-abstracto-de-fondo-amarillo-brillante-del-sitio-de-la-pared-del-estudio-de-la-pendiente.jpg
│       │   ├── x.png
│       │   ├── cliente.png
│       │   ├── dashboard.png
│       │   ├── donut_chart_icon_207653.png
│       │   ├── estadisticas.png
│       │   ├── guion.png
│       │   ├── historialDeEntrega.png
│       │   ├── pedido.png
│       │   └── repartidor.png
│       ├── util/
│       │   └── ValidacionDeDato.java
│       └── view/
│           ├── components/
│           │   ├── DetalleButtonEditor.java
│           │   ├── DetalleButtonRenderer.java
│           │   ├── EntregadoButtonEditor.java
│           │   └── EntregadoButtonRenderer.java
│           ├── AdministradorDashboardJFrame.java
│           ├── LoginJFrame.java
│           ├── RepartidorDashboardJFrame.java
│           └── Logo.png  # La imagen Logo.png ubicada aquí
├── lib/
│   ├── jfreechart-1.x.x.jar
│   ├── jcommon-1.x.x.jar
│   └── gson-2.x.x.jar
├── data/
│   ├── BaseDeDatos.json
│   └── usuarios.json
├── build.xml
└── readme.txt


---

Ejecución desde NetBeans IDE
Si utilizas NetBeans IDE, sigue estos pasos para abrir y ejecutar el proyecto:

Abre NetBeans IDE.

Abre el Proyecto:

Ve a File (Archivo) -> Open Project... (Abrir Proyecto...).

Navega hasta la carpeta principal de la aplicación (Project/).

Selecciona la carpeta y haz clic en Open Project (Abrir Proyecto). NetBeans debería reconocer el proyecto basado en su estructura y el archivo build.xml.

Verifica las Librerías:

En la ventana Projects (Proyectos) de NetBeans, expande tu proyecto.

Busca la sección Libraries (Librerías).

Asegúrate de que los archivos jfreechart-1.5.6.jar, jcommon-1.0.24.jar, y gson-2.13.1.jar estén listados y referenciados correctamente. Si no lo están, haz clic derecho en Libraries (o Add JAR/Folder si es una opción) y añade manualmente los JARs desde la carpeta lib/ del proyecto.

Verifica la Carpeta de Datos:

Asegúrate de que la carpeta data/ (que contiene BaseDeDatos.json y GestorJSON.java) esté presente en el mismo directorio principal de tu proyecto (Project/). NetBeans generalmente maneja las rutas relativas a la raíz del proyecto, pero es crucial que esta carpeta exista y sea accesible.

Ejecutar la Aplicación:

En la ventana Projects (Proyectos) de NetBeans, navega hasta Source Packages -> project.view.

Haz clic derecho en LoginJFrame.java.

Selecciona Run File (Ejecutar Archivo).

Alternativamente, si el proyecto está configurado para reconocer LoginJFrame como la clase principal, puedes hacer clic en el botón Run Project (el icono de flecha verde) en la barra de herramientas de NetBeans.

La aplicación se iniciará y verás la ventana de login.

---

Base de Datos JSON y Gestión de Usuarios
La aplicación utiliza un archivo JSON central, BaseDeDatos.json (ubicado en el directorio data/), para almacenar toda la información del proyecto, incluyendo usuarios, repartidores, clientes, pedidos e historial de entregas.

Formato del Archivo BaseDeDatos.json
El archivo BaseDeDatos.json tiene una estructura que encapsula todas las entidades de la aplicación. A continuación, se muestra un fragmento representativo de su formato, incluyendo la sección de usuarios:

{
  "usuarios": [
    {
      "cedula": "4024215",
      "contrasena": "admin2025",
      "rol": "administrador"
    },
    {
      "cedula": "19674321",
      "contrasena": "clave123",
      "rol": "repartidor"
    }
    // ... otros usuarios
  ],
  "repartidor": [
    {
      "disponibilidad": false,
      "nombre": "Sofia Ramos",
      "numeroTelefono": "04123456789",
      "cedulaIdentidad": "10112233",
      "contrasena": "delivery1"
    }
    // ... otros repartidores
  ],
  "pedido": [
    {
      "idPedido": "PED1",
      "cliente": {
        "direccion": "Av. Principal, Res. Primavera, Casa 5",
        "nombre": "Luis Pérez",
        "numeroTelefono": "04249998877",
        "cedulaIdentidad": "87654321"
      },
      "repartidorAsignado": {
        "disponibilidad": false,
        "nombre": "Sofia Ramos",
        "numeroTelefono": "04123456789",
        "cedulaIdentidad": "10112233",
        "contrasena": "delivery1"
      },
      "direccionEntrega": "Av. Principal, Res. Primavera, Casa 5",
      "combo": "PARA4",
      "precioCombo": 27.99,
      "metodoPago": "EFECTIVO",
      "requiereCambio": true,
      "costoEntrega": 3.0,
      "vuelto": 5.0,
      "estado": "Entregado",
      "fechaCreacion": "Jun 29, 2025 6:30:00 PM",
      "fechaEntrega": "Jun 29, 2025 6:40:00 PM",
      "montoTotal": 30.99
    }
    // ... otros pedidos
  ],
  "cliente": [
    {
      "direccion": "Av. Principal, Res. Primavera, Casa 5",
      "nombre": "Luis Pérez",
      "numeroTelefono": "04249998877",
      "cedulaIdentidad": "87654321"
    }
    // ... otros clientes
  ],
  "historial": [
    {
      "idHistorial": "HIST47304",
      "pedidoAsociado": {
        "idPedido": "PED2",
        "cliente": {
          "direccion": "Av. Principal, Res. Primavera, Casa 5",
          "nombre": "Luis Pérez",
          "numeroTelefono": "04249998877",
          "cedulaIdentidad": "87654321"
        },
        "repartidorAsignado": {
          "disponibilidad": false,
          "nombre": "Carla Rodríguez",
          "numeroTelefono": "04265556677",
          "cedulaIdentidad": "22456109",
          "contrasena": "fingers2025"
        },
        "direccionEntrega": "Av. Principal, Res. Primavera, Casa 5",
        "combo": "PARA4",
        "precioCombo": 27.99,
        "metodoPago": "EFECTIVO",
        "requiereCambio": true,
        "costoEntrega": 3.0,
        "vuelto": 5.0,
        "estado": "Repartidor Reasignado",
        "fechaCreacion": "Jun 29, 2025 6:30:00 PM",
        "fechaEntrega": null,
        "montoTotal": 30.99
      },
      "repartidor": {
        "disponibilidad": false,
        "nombre": "Sofia Ramos",
        "numeroTelefono": "04123456789",
        "cedulaIdentidad": "10112233",
        "contrasena": "delivery1"
      },
      "fechaRegistro": "Jun 29, 2025 6:40:00 PM",
      "estadoEntrega": "Repartidor Reasignado",
      "ubicacionEntrega": "Av. Principal, Res. Primavera, Casa 5"
    }
    // ... otros historiales
  ]
}

Usuarios de Ejemplo (Solo para Desarrollo)
Los usuarios para iniciar sesión se encuentran en la sección "usuarios" del archivo BaseDeDatos.json. Para propósitos de desarrollo y prueba, puedes usar las siguientes credenciales de ejemplo:

Usuario: 4024215
Contraseña: admin2025

Usuario: 19674321
Contraseña: clave123

Usuario: 22456109
Contraseña: fingers2025


Librerías Utilizadas
Java Swing: Para la interfaz gráfica de usuario.

JFreeChart: Para la creación de gráficos y diagramas.

jfreechart-1.5.6.jar

jcommon-1.0.24.jar

Gson: Para la serialización y deserialización de objetos Java a JSON y viceversa.

gson-2.13.1.jar

