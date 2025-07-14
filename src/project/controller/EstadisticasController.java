
package project.controller;

import project.model.entities.HistorialDeEntrega;
import project.model.entities.Pedido;
import project.model.repositories.HistorialEntregaRepository;
import project.model.repositories.PedidoRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import java.awt.Color;
import java.awt.BasicStroke;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 * Controlador para la gestión y generación de estadísticas relacionadas con pedidos y entregas.
 * Encapsula la lógica de negocio para obtener datos de los repositorios y transformarlos
 * en formatos adecuados para la visualización, como gráficos.
 */
public class EstadisticasController {
   // Repositorios para acceder a los datos de pedidos e historiales de entrega
   private final PedidoRepository pedidoRepo;
   private final HistorialEntregaRepository historialRepo;
   // Formato de fecha para estandarizar la representación de fechas
   private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

   /*
    * Constructor de la clase EstadisticasController.
    * Inicializa los repositorios necesarios para la obtención de datos.
    */
   public EstadisticasController() {
      this.pedidoRepo = new PedidoRepository();
      this.historialRepo = new HistorialEntregaRepository();
   }

   /*
    * Prepara y consolida los datos de pedidos y tiempos de entrega para su uso en una gráfica.
    * Este método orquesta la obtención de datos de los repositorios, su procesamiento
    * para calcular pedidos por día y el tiempo promedio de entrega, y finalmente los organiza
    * en un mapa para facilitar su consumo por el método de generación de gráficos.
    */
   public Map<String, Object> prepararDatosParaGrafica() {
      // Obtener todos los pedidos y historiales de entrega
      List<Pedido> pedidos = pedidoRepo.obtenerTodos();
      List<HistorialDeEntrega> historiales = historialRepo.obtenerTodos();

      // Calcular el número de pedidos por día
      Map<String, Long> pedidosPorDia = calcularPedidosPorDia(pedidos);
      // Calcular las duraciones de entrega por día
      Map<String, List<Long>> duracionesPorDia = calcularDuracionesPorDia(historiales);
      // Calcular el tiempo promedio de entrega por día
      Map<String, Double> tiempoPromedioEntregaPorDia = calcularTiempoPromedioEntregaPorDia(duracionesPorDia);

      // Consolidar todas las fechas únicas y ordenarlas
      List<String> fechas = consolidarFechas(pedidosPorDia, tiempoPromedioEntregaPorDia);

      // Preparar los datos finales para la gráfica
      return organizarDatosParaGrafica(fechas, pedidosPorDia, tiempoPromedioEntregaPorDia);
   }

   /*
    * Calcula el número de pedidos agrupados por día de creación.
    */
   private Map<String, Long> calcularPedidosPorDia(List<Pedido> pedidos) {
      return pedidos.stream()
                   .collect(Collectors.groupingBy(pedido -> dateFormat.format(pedido.getFechaCreacion()), Collectors.counting()));
   }

   /*
    * Calcula las duraciones de entrega en minutos para cada día.
    * Solo considera historiales de entrega que están en estado "Entregado" y tienen fechas válidas.
    */
   private Map<String, List<Long>> calcularDuracionesPorDia(List<HistorialDeEntrega> historiales) {
      Map<String, List<Long>> duracionesPorDia = new HashMap<>();
      for (HistorialDeEntrega historial : historiales) {
         if (esHistorialDeEntregaValido(historial)) {
            long diffInMillies = Math.abs(historial.getFechaRegistro().getTime() - historial.getPedidoAsociado().getFechaCreacion().getTime());
            long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            String fechaEntrega = dateFormat.format(historial.getFechaRegistro());
            duracionesPorDia.computeIfAbsent(fechaEntrega, k -> new ArrayList<>()).add(diffInMinutes);
         }
      }
      return duracionesPorDia;
   }

   /*
    * Verifica si un historial de entrega es válido para el cálculo de duración.
    */
   private boolean esHistorialDeEntregaValido(HistorialDeEntrega historial) {
      return historial.getPedidoAsociado() != null &&
             historial.getPedidoAsociado().getFechaCreacion() != null &&
             historial.getFechaRegistro() != null &&
             "Entregado".equals(historial.getEstadoEntrega());
   }

   /*
    * Calcula el tiempo promedio de entrega por día.
    */
   private Map<String, Double> calcularTiempoPromedioEntregaPorDia(Map<String, List<Long>> duracionesPorDia) {
      return duracionesPorDia.entrySet().stream()
                   .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0)));
   }

   /*
    * Consolida y ordena las fechas únicas de pedidos y tiempos de entrega.
    */
   private List<String> consolidarFechas(Map<String, Long> pedidosPorDia, Map<String, Double> tiempoPromedioEntregaPorDia) {
      List<String> fechas = new ArrayList<>();
      fechas.addAll(pedidosPorDia.keySet());
      fechas.addAll(tiempoPromedioEntregaPorDia.keySet());
      return fechas.stream().distinct().sorted().collect(Collectors.toList());
   }

   /*
    * Organiza los datos procesados en listas separadas para su uso en la gráfica.
    */
   private Map<String, Object> organizarDatosParaGrafica(List<String> fechas, Map<String, Long> pedidosPorDia, Map<String, Double> tiempoPromedioEntregaPorDia) {
      List<String> xData = new ArrayList<>();
      List<Double> yDataPedidos = new ArrayList<>();
      List<Double> yDataTiempo = new ArrayList<>();

      for (String fecha : fechas) {
         xData.add(fecha);
         yDataPedidos.add(pedidosPorDia.getOrDefault(fecha, 0L).doubleValue());
         yDataTiempo.add(tiempoPromedioEntregaPorDia.getOrDefault(fecha, 0.0));
      }

      Map<String, Object> data = new HashMap<>();
      data.put("fechas", xData);
      data.put("pedidosPorDia", yDataPedidos);
      data.put("tiempoPromedioEntrega", yDataTiempo);
      return data;
   }

   /*
    * Genera un panel de gráfico que muestra estadísticas de pedidos y tiempo de entrega por día.
    * Este método utiliza los datos preparados por {@code prepararDatosParaGrafica} para construir
    * un gráfico de barras y líneas con JFreeChart, configurando los ejes y renderizadores
    * para una visualización clara.
    */
   public ChartPanel generarGraficaEstadisticasPanel() {
      Map<String, Object> data = prepararDatosParaGrafica();
      List<String> xData = (List<String>) data.get("fechas");
      List<Double> yDataPedidos = (List<Double>) data.get("pedidosPorDia");
      List<Double> yDataTiempo = (List<Double>) data.get("tiempoPromedioEntrega");

      // Si no hay datos, se imprime un mensaje y se retorna un panel vacío.
      if (xData.isEmpty()) {
         System.out.println("No hay datos disponibles para generar la gráfica.");
         return new ChartPanel(null);
      }

      // Crear los datasets para los pedidos y el tiempo de entrega
      DefaultCategoryDataset datasetPedidos = crearDatasetPedidos(xData, yDataPedidos);
      DefaultCategoryDataset datasetTiempo = crearDatasetTiempo(xData, yDataTiempo);

      // Crear el gráfico principal de barras
      JFreeChart chart = ChartFactory.createBarChart(
         "Estadísticas de Pedidos y Entrega por Día", // Título del gráfico
         "Día",                                     // Etiqueta del eje X
         "Número de Pedidos",                       // Etiqueta del eje Y principal
         datasetPedidos,                            // Dataset para las barras (pedidos)
         PlotOrientation.VERTICAL,                  // Orientación del gráfico
         true,                                      // Incluir leyenda
         true,                                      // Incluir tooltips
         false                                      // Incluir URLs
      );

      // Configurar el plot para el gráfico combinado
      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      configurarEjes(plot);
      configurarDatasetsYRenderers(plot, datasetTiempo);

      // Crear y retornar el panel del gráfico
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
      return chartPanel;
   }

   /*
    * Crea el dataset para el número de pedidos.
    */
   private DefaultCategoryDataset crearDatasetPedidos(List<String> xData, List<Double> yDataPedidos) {
      DefaultCategoryDataset datasetPedidos = new DefaultCategoryDataset();
      for (int i = 0; i < xData.size(); i++) {
         datasetPedidos.addValue(yDataPedidos.get(i), "Número de Pedidos", xData.get(i));
      }
      return datasetPedidos;
   }

   /*
    * Crea el dataset para el tiempo promedio de entrega.
    */
   private DefaultCategoryDataset crearDatasetTiempo(List<String> xData, List<Double> yDataTiempo) {
      DefaultCategoryDataset datasetTiempo = new DefaultCategoryDataset();
      for (int i = 0; i < xData.size(); i++) {
         datasetTiempo.addValue(yDataTiempo.get(i), "Tiempo Promedio de Entrega", xData.get(i));
      }
      return datasetTiempo;
   }

   /*
    * Configura los ejes del gráfico (rango y dominio).
    */
   private void configurarEjes(CategoryPlot plot) {
      // Configuración del eje de rango secundario (para el tiempo)
      NumberAxis rangeAxis1 = new NumberAxis("Tiempo (minutos)");
      rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis1.setLabelPaint(Color.BLUE);
      rangeAxis1.setTickLabelPaint(Color.BLUE);
      plot.setRangeAxis(1, rangeAxis1); // Asignar el eje secundario al plot

      // Configuración del eje de rango principal (para el número de pedidos)
      NumberAxis rangeAxis2 = (NumberAxis) plot.getRangeAxis();
      rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis2.setLabelPaint(Color.RED);
      rangeAxis2.setTickLabelPaint(Color.RED);
   }

   /*
    * Configura los datasets y renderizadores para el plot.
    */
   private void configurarDatasetsYRenderers(CategoryPlot plot, DefaultCategoryDataset datasetTiempo) {
      // Asignar el dataset de tiempo al eje secundario
      plot.setDataset(1, datasetTiempo);
      plot.mapDatasetToRangeAxis(1, 1); // Mapear el dataset de tiempo al segundo eje de rango

      // Configuración del renderizador para las barras (pedidos)
      BarRenderer renderer1 = new BarRenderer();
      renderer1.setSeriesPaint(0, new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 150));
      plot.setRenderer(1, renderer1); // Asignar el renderizador de barras al plot

      // Configuración del renderizador para la línea (tiempo de entrega)
      LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
      renderer2.setSeriesPaint(0, Color.RED);
      renderer2.setSeriesShapesVisible(0, true);
      renderer2.setSeriesStroke(0, new BasicStroke(3.0f)); // Grosor de la línea
      plot.setRenderer(0, renderer2); // Asignar el renderizador de línea al plot
   }
}
