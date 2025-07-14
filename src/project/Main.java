
package project;

public class Main {
   //Allegra Escalante C.I: 31.841.298
   //Juan Gonzalez C.I: 30.774.602
   /*
      El proyecto aplica de forma consistente el Principio de Responsabilidad Única (SRP) como eje central de SOLID. Cada componente tiene una única responsabilidad claramente delimitada:

      Clases de dominio (Cliente, Repartidor, Pedido): Gestionan exclusivamente los atributos y comportamientos básicos de sus entidades.

      Servicios (ClienteService, PedidoService): Contienen únicamente lógica de negocio (validaciones, reglas operativas).

      Repositorios (ClienteRepository): Se especializan en acceso a datos (operaciones CRUD) sin mezclar lógica.

      Controladores (AdministradorController): Coordinan flujos entre capas (interfaz → servicios) sin asumir funcionalidades ajenas.

      Interfaces (Repository<T>): Desacoplan contratos de implementaciones concretas.

      El SRP se implementó para:

      Facilitar el mantenimiento: Modificaciones en una capa (ej. lógica de negocio) no afectan otras (ej. acceso a datos).

      Permitir escalabilidad: Nuevas funcionalidades se integran modificando solo componentes específicos.

      Mejorar testabilidad: Cada unidad se verifica de forma aislada (ej. tests de servicios sin depender de repositorios reales).

      Aunque existen indicios del Principio Abierto/Cerrado (OCP) mediante abstracciones (interfaces), el SRP es el principio más transversal y deliberado en la arquitectura del sistema.
   */
}
