import java.sql.*;
import java.sql.Date;
import java.util.*;

public class App {
    private static List<Cliente> clientes = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Connection con;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto", "root", "agb20040603");
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        int opcion;
        do {
            System.out.println("Bienvenido a Agencia de Viajes Ponchito");
            System.out.println("1. Consultar folleto");
            System.out.println("2. Simular reservación de viaje");
            System.out.println("3. Reservar viaje");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    consultarFolleto();
                    break;
                case 2:
                    simularReservacion();
                    break;
                case 3:
                    reservarViaje();
                    break;
                case 4:
                    System.out.println("Gracias por utilizar Agencia de Viajes Ponchito");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione nuevamente.");
            }
        } while (opcion != 4);
    }

    private static void consultarFolleto() {
        try {
            // Solicitar al usuario que elija un país
            System.out.println("Elija un país para consultar las ciudades disponibles:");
            System.out.println("1. España");
            System.out.println("2. Inglaterra");
            System.out.println("3. Francia");
            System.out.print("Seleccione un país: ");
            int opcionPais = scanner.nextInt();
            String paisElegido = "";
            switch (opcionPais) {
                case 1:
                    paisElegido = "España";
                    break;
                case 2:
                    paisElegido = "Inglaterra";
                    break;
                case 3:
                    paisElegido = "Francia";
                    break;
                default:
                    System.out.println("Opción inválida. Saliendo...");
                    return;
            }
    
            // Construir y ejecutar la consulta SQL para obtener las ciudades del país elegido
            String consulta = "SELECT nombre FROM Ciudad WHERE pais = ?";
            PreparedStatement stmt = con.prepareStatement(consulta);
            stmt.setString(1, paisElegido);
            ResultSet rs = stmt.executeQuery();
    
            // Mostrar las ciudades disponibles en el folleto
            System.out.println("Ciudades disponibles en el folleto de " + paisElegido + ":");
            while (rs.next()) {
                System.out.println(rs.getString("nombre"));
            }
    
            // Preguntar al usuario si desea ver hoteles asociados a una ciudad
            System.out.println("¿Desea ver hoteles asociados a alguna de estas ciudades? (S/N)");
            String opcionHoteles = scanner.next();
            scanner.nextLine(); // Consumir el salto de línea
            if (opcionHoteles.equalsIgnoreCase("S")) {
                // Solicitar al usuario que ingrese el nombre de la ciudad
                System.out.println("Ingrese el nombre de la ciudad para ver los hoteles asociados:");
                String ciudad = scanner.nextLine();
                // Mostrar hoteles asociados a la ciudad seleccionada
                mostrarHotelesPorCiudad(ciudad);
            }

            System.out.println("\n¿Desea ver circuitos relacionados con " + paisElegido + "? (S/N)");
        String opcionCircuitos = scanner.next();
        if (opcionCircuitos.equalsIgnoreCase("S")) {
            mostrarCircuitosPorPais(paisElegido);
        }
        } catch (Exception e) {
            System.out.println("Error al consultar el folleto: " + e.getMessage());
        }
    }

    private static void mostrarHotelesPorCiudad(String ciudad) {
        try {
            // Consultar los hoteles asociados a la ciudad en la base de datos
            String consulta = "SELECT nombre, direccion FROM Hotel WHERE ciudad = ?";
            PreparedStatement stmt = con.prepareStatement(consulta);
            stmt.setString(1, ciudad);
            ResultSet rs = stmt.executeQuery();
    
            // Mostrar los hoteles asociados a la ciudad
            System.out.println("Hoteles en " + ciudad + ":");
            while (rs.next()) {
                String nombreHotel = rs.getString("nombre");
                String direccionHotel = rs.getString("direccion");
                System.out.println("Nombre: " + nombreHotel);
                System.out.println("Dirección: " + direccionHotel);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar hoteles: " + e.getMessage());
        }
    }

    private static void mostrarCircuitosPorPais(String pais) {
        try {
            // Construir y ejecutar la consulta SQL para obtener los circuitos relacionados con el país
            String consultaCircuitos = "SELECT * FROM Circuito WHERE paisSalida = ?";
            PreparedStatement stmtCircuitos = con.prepareStatement(consultaCircuitos);
            stmtCircuitos.setString(1, pais);
            ResultSet rsCircuitos = stmtCircuitos.executeQuery();
    
            // Mostrar los circuitos relacionados con el país
            System.out.println("\nCircuitos relacionados con " + pais + ":");
            while (rsCircuitos.next()) {
                System.out.println("Identificador: " + rsCircuitos.getString("identificador"));
                System.out.println("Descripción: " + rsCircuitos.getString("descripcion"));
                System.out.println("Ciudad de salida: " + rsCircuitos.getString("ciudadSalida"));
                System.out.println("Ciudad de llegada: " + rsCircuitos.getString("ciudadLlegada"));
                System.out.println("Duración: " + rsCircuitos.getInt("duracion") + " días");
                System.out.println("Precio: $" + rsCircuitos.getInt("precio"));
                System.out.println("---------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar circuitos: " + e.getMessage());
        }
    }

    private static void simularReservacion() {
        System.out.println("Iniciando simulación de reservación de viaje...");

        System.out.print("Ingrese su nombre: ");
        String nombreCliente = scanner.nextLine();

        // Solicitar al usuario que elija un país
        System.out.println("Elija un país para simular la reservación:");
        System.out.println("1. España");
        System.out.println("2. Inglaterra");
        System.out.println("3. Francia");
        System.out.print("Seleccione un país: ");
        int opcionPais = scanner.nextInt();
        String paisElegido = "";
        String ciudad = "";
        switch (opcionPais) {
            case 1:
                paisElegido = "España";
                ciudad = "Madrid";
                break;
            case 2:
                paisElegido = "Inglaterra";
                ciudad = "Londres";
                break;
            case 3:
                paisElegido = "Francia";
                ciudad = "Lyon";
                break;
            default:
                System.out.println("Opción inválida. Saliendo...");
                return;
        }

        String[] circuitosDisponibles = obtenerCircuitosDisponibles(paisElegido);

        // Capturar información adicional del viaje
        System.out.println("Ingrese el número de personas: ");
        int numeroPersonas = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        // Preguntar al cliente si desea incluir un circuito
        System.out.println("¿Desea incluir un circuito? (S/N)");
    String opcionCircuito = scanner.nextLine();
    String descripcionCircuito = "";
    if (opcionCircuito.equalsIgnoreCase("S")) {
        if (circuitosDisponibles.length > 0) {
            System.out.println("Circuitos disponibles:");
            for (int i = 0; i < circuitosDisponibles.length; i++) {
                System.out.println((i + 1) + ". " + circuitosDisponibles[i]);
            }
            System.out.print("Seleccione un circuito: ");
            int opcionTipoCircuito = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            if (opcionTipoCircuito >= 1 && opcionTipoCircuito <= circuitosDisponibles.length) {
                descripcionCircuito = circuitosDisponibles[opcionTipoCircuito - 1];
            } else {
                System.out.println("Opción inválida. No se agregará un circuito.");
            }
        } else {
            System.out.println("No hay circuitos disponibles para este país.");
        }
    } else {
        descripcionCircuito = "No se incluyó un circuito.";
    }
        // Generar una fecha de salida y llegada aleatoria
        Date fechaSalida = generarFechaAleatoria();
        Date fechaLlegada = generarFechaAleatoria();

        // Establecer un costo aleatorio para el viaje
        double costoViaje = generarCostoAleatorio();

        // Presentar información de la simulación
        presentarInformacionSimulacion(nombreCliente, paisElegido, ciudad, fechaSalida, fechaLlegada, numeroPersonas, costoViaje, descripcionCircuito);

        // Insertar la simulación en la base de datos
        insertarSimulacionEnBD(nombreCliente, fechaSalida, fechaLlegada, numeroPersonas, costoViaje);
    }

    private static Date generarFechaAleatoria() {
        long offset = Timestamp.valueOf("2024-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2025-12-31 00:00:00").getTime();
        long diff = end - offset + 1;
        return new Date(offset + (long) (Math.random() * diff));
    }
    
    private static double generarCostoAleatorio() {
        return 1000 + Math.random() * 6000; // Rango de costo entre 500 y 2000
    }

    private static String[] obtenerCircuitosDisponibles(String pais) {
        // Establecer conexión a la base de datos
        String[] circuitos = {}; // Inicializar el array vacío
    
        try {
            // Construir y ejecutar la consulta SQL para obtener los circuitos
            String consultaCircuitos = "SELECT descripcion FROM Circuito WHERE paisSalida = ?";
            PreparedStatement stmtCircuitos = con.prepareStatement(consultaCircuitos, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmtCircuitos.setString(1, pais);
            ResultSet rsCircuitos = stmtCircuitos.executeQuery();
    
            // Contar la cantidad de resultados para inicializar el array
            rsCircuitos.last();
            int rowCount = rsCircuitos.getRow();
            rsCircuitos.beforeFirst();
    
            circuitos = new String[rowCount];
    
            // Almacenar los circuitos en el array
            int index = 0;
            while (rsCircuitos.next()) {
                circuitos[index++] = rsCircuitos.getString("descripcion");
            }
    
            // Cerrar conexión
            rsCircuitos.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener circuitos disponibles: " + e.getMessage());
        }
    
        return circuitos;
    }
    

    private static void reservarViaje() {
        System.out.println("Ingrese su nombre:");
        String nombre = scanner.nextLine();
        
        Cliente cliente = buscarClienteEnBD(nombre);
        if (cliente == null) {
            System.out.println("Ingrese su dirección:");
            String direccion = scanner.nextLine();
            System.out.println("Seleccione el tipo de pago:");
            System.out.println("1. Tarjeta de crédito");
            System.out.println("2. Transferencia bancaria");
            int tipoPago = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            cliente = new Cliente(nombre, direccion, tipoPago);
            registrarClienteEnBD(cliente);
        } else {
            System.out.println("Cliente encontrado. Verificando información...");
            System.out.println("Nombre: " + cliente.getNombre());
            System.out.println("Dirección actual: " + cliente.getDireccion());
            System.out.println("Tipo de pago actual: " + cliente.getTipoPago());
            System.out.println("¿Es correcta esta información? (S/N)");
            String respuesta = scanner.nextLine();
            if (!respuesta.equalsIgnoreCase("S")) {
                // Si la información no es correcta, permitir al cliente actualizarla
                actualizarInformacionCliente(cliente, respuesta);
                return;
            }
        }
        int idSimulacion = (int) (Math.random() * 1000);
        Simulacion simulacion = buscarSimulacionEnBD(cliente.getNombre());
        if (simulacion != null) {
            System.out.println("\nSimulación asociada encontrada:");
            System.out.println("Fecha de salida: " + simulacion.getFechaSalida());
            System.out.println("Fecha de llegada: " + simulacion.getFechaLlegada());
            System.out.println("Número de personas: " + simulacion.getNumeroPersonas());
            System.out.println("Costo del viaje: $" + simulacion.getCostoViaje());
            System.out.println("¿Son correctos estos datos de la simulación? (S/N)");
            String respuesta = scanner.nextLine();
            if (!respuesta.equalsIgnoreCase("S")) {
                // Si la simulación no es correcta, eliminarla de la base de datos y volver al menú principal
                eliminarSimulacionEnBD(cliente.getNombre());
                return;
            }
            // Si la simulación es correcta, insertar la reserva en la base de datos
            insertarReservacionEnBD(idSimulacion, new Date(System.currentTimeMillis()), true);
            System.out.println("Reserva realizada con éxito.");
            System.out.println(" \n \n ID de reserva: "+ idSimulacion);
            System.out.println("---------------------------------------------");

            System.out.println("\n \n \n Procesado en pago....");

            String nombreCliente = cliente.getNombre(); // Obtener el nombre del cliente
            double costoSimulacion = simulacion.getCostoViaje(); // Obtener el costo de la simulación
    
            // Calcular el costo total del viaje sumando el costo de la simulación
            double costoTotal = calcularCostoTotal(cliente, costoSimulacion);
    
            // Imprimir el costo total
            System.out.println("Costo del viaje para " + nombreCliente + ": $" + costoTotal);
            System.out.println("---------------------------------------------");
        } else {
            System.out.println("No se encontró ninguna simulación asociada a este cliente.");
        }
    }
    
    private static int obtenerIdSimulacion(String nombreCliente) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM Simulacion WHERE nombreCliente = ?");
            stmt.setString(1, nombreCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el ID de la simulación: " + e.getMessage());
        }
        return -1; // Retornar -1 en caso de error
    }

    private static double calcularCostoTotal(Cliente cliente, double costoSimulacion) {
        
        // Consultar el costo de la simulación en la base de datos
        String nombreCliente = cliente.getNombre();
        String costoQuery = "SELECT costoViaje FROM Simulacion WHERE nombreCliente = ?";
        
        try {
            PreparedStatement pstmt = con.prepareStatement(costoQuery);
            pstmt.setString(1, nombreCliente);
            ResultSet rs = pstmt.executeQuery();
            
            // Verificar si se encontró un resultado
            if (rs.next()) {
                costoSimulacion = rs.getDouble("costoViaje"); // Obtener el costo de la simulación del resultado
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el costo de la simulación: " + e.getMessage());
        }
        
        
        return costoSimulacion;
    }

    private static void eliminarSimulacionEnBD(String nombreCliente) {
    try {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM Simulacion WHERE nombreCliente = ?");
        stmt.setString(1, nombreCliente);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Simulación eliminada de la base de datos.");
        } else {
            System.out.println("No se pudo eliminar la simulación.");
        }
    } catch (SQLException e) {
        System.out.println("Error al eliminar la simulación de la base de datos: " + e.getMessage());
    }
}
    private static Simulacion buscarSimulacionEnBD(String nombreCliente) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Simulacion WHERE nombreCliente = ?");
            stmt.setString(1, nombreCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Simulacion simulacion = new Simulacion(nombreCliente);
                simulacion.setFechaSalida(rs.getDate("fechaSalida"));
                simulacion.setFechaLlegada(rs.getDate("fechaLlegada"));
                simulacion.setNumeroPersonas(rs.getInt("numeroPersonas"));
                simulacion.setCostoViaje(rs.getDouble("costoViaje"));
                return simulacion;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar simulación en la base de datos: " + e.getMessage());
        }
        return null;
    }

    private static void actualizarInformacionCliente(Cliente cliente, String respuesta) {       
        if (respuesta.equalsIgnoreCase("N")) {
            // Eliminar los datos del cliente de la base de datos
            try {
                PreparedStatement stmt = con.prepareStatement("DELETE FROM Cliente WHERE nombre = ?");
                stmt.setString(1, cliente.getNombre());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Información del cliente eliminada de la base de datos.");
                } else {
                    System.out.println("Error al eliminar información del cliente.");
                }
            } catch (SQLException e) {
                System.out.println("Error al eliminar información del cliente de la base de datos: " + e.getMessage());
            }
        } else {
            System.out.println("Los datos son correctos. No se realizarán cambios.");
        }
        return;
    }

    private static void registrarClienteEnBD(Cliente cliente) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Cliente (nombre, Direccion, TipoPago) VALUES (?, ?, ?)");
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDireccion());
            stmt.setInt(3, cliente.getTipoPago());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cliente registrado exitosamente.");
            } else {
                System.out.println("Error al registrar cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente en la base de datos: " + e.getMessage());
        }
    }

    private static Cliente buscarClienteEnBD(String nombre) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Cliente WHERE nombre = ?");
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String direccion = rs.getString("Direccion");
                int tipoPago = rs.getInt("TipoPago");
                return new Cliente(nombre, direccion, tipoPago);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente en la base de datos: " + e.getMessage());
        }
        return null;
    }

    private static void actualizarClienteEnBD(String nombre, String nuevaDireccion, int nuevoTipoPago) {
        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE Cliente SET Direccion = ?, TipoPago = ? WHERE nombre = ?");
            stmt.setString(1, nuevaDireccion);
            stmt.setInt(2, nuevoTipoPago);
            stmt.setString(3, nombre);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Información del cliente actualizada exitosamente.");
            } else {
                System.out.println("Error al actualizar información del cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar información del cliente en la base de datos: " + e.getMessage());
        }
    }


    private static void presentarInformacionSimulacion(String nombreCliente, String pais, String ciudad, Date fechaSalida, Date fechaLlegada, int numeroPersonas, double costoViaje, String circuito) {
        System.out.println("\nInformación de la simulación:");
        System.out.println("Cliente: " + nombreCliente);
        System.out.println("País: " + pais);
        System.out.println("Ciudad: " + ciudad);
        System.out.println("Fecha de salida: " + fechaSalida);
        System.out.println("Fecha de llegada: " + fechaLlegada);
        System.out.println("Número de personas: " + numeroPersonas);
        System.out.println("Costo del viaje: $" + costoViaje);
        System.out.println("Circuito: " + circuito);
    }

    private static void insertarSimulacionEnBD(String nombreCliente, Date fechaSalida, Date fechaLlegada, int numeroPersonas, double costoViaje) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Simulacion (nombreCliente, fechaSalida, fechaLlegada, numeroPersonas, costoViaje) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, nombreCliente);
            stmt.setDate(2, fechaSalida);
            stmt.setDate(3, fechaLlegada);
            stmt.setInt(4, numeroPersonas);
            stmt.setDouble(5, costoViaje);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Simulación guardada en la base de datos.");
            } else {
                System.out.println("Error al guardar simulación en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar simulación en la base de datos: " + e.getMessage());
        }
    }

    private static void insertarReservacionEnBD(int idSimulacion, Date fechaReservacion, boolean validada) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Reservacion (idSimulacion, fechaReservacion, validada) VALUES (?, ?, ?)");
            stmt.setInt(1, idSimulacion);
            stmt.setDate(2, fechaReservacion);
            stmt.setBoolean(3, validada);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reservación guardada en la base de datos.");
            } else {
                System.out.println("Error al guardar la reservación.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la reservación en la base de datos: " + e.getMessage());
        }
    }

    private static double obtenerCostoCircuito(String descripcionCircuito) {
        double costoCircuito = 0.0;
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT precio FROM Circuito WHERE descripcion = ?");
            pstmt.setString(1, descripcionCircuito);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                costoCircuito = rs.getDouble("precio");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el costo del circuito: " + e.getMessage());
        }
        return costoCircuito;
    }


    private static class Cliente {
        private String nombre;
        private String direccion;
        private int tipoPago;

        public Cliente(String nombre, String direccion, int tipoPago) {
            this.nombre = nombre;
            this.direccion = direccion;
            this.tipoPago = tipoPago;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDireccion() {
            return direccion;
        }

        public int getTipoPago() {
            return tipoPago;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        
        public void setTipoPago(int tipoPago) {
            this.tipoPago = tipoPago;
        }
    }

    private static ReservacionViaje crearReservacion(Cliente cliente) {
        // Crear una nueva reservación de viaje
        ReservacionViaje reservacion = new ReservacionViaje();
    
    
        return reservacion;
    }

    private static class Simulacion {
        private String nombreCliente;
        private List<String> opciones;
        private Date fechaSalida;
        private Date fechaLlegada;
        private int numeroPersonas;
        private double costoViaje;

        public Simulacion(String nombreCliente) {
            this.nombreCliente = nombreCliente;
            this.opciones = new ArrayList<>();
        }


        

        public void agregarOpcion(String opcion) {
            opciones.add(opcion);
        }

        public void setFechaSalida(Date fechaSalida) {
            this.fechaSalida = fechaSalida;
        }

        public void setFechaLlegada(Date fechaLlegada) {
            this.fechaLlegada = fechaLlegada;
        }

        public void setNumeroPersonas(int numeroPersonas) {
            this.numeroPersonas = numeroPersonas;
        }

        public void setCostoViaje(double costoViaje) {
            this.costoViaje = costoViaje;
        }

        public Date getFechaSalida() {
            return fechaSalida;
        }

        public Date getFechaLlegada() {
            return fechaLlegada;
        }

        public int getNumeroPersonas() {
            return numeroPersonas;
        }

        public double getCostoViaje() {
            return costoViaje;
        }

        public List<String> getOpciones() {
            return opciones;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            sb.append("Cliente: ").append(nombreCliente).append("\n");
            sb.append("Opciones seleccionadas:\n");
            for (String opcion : opciones) {
                sb.append("- ").append(opcion).append("\n");
            }
            return sb.toString();
        }
    }

    private static class ReservacionViaje {
        private List<String> opciones;

        public ReservacionViaje() {
            this.opciones = new ArrayList<>();
        }

        public void agregarOpcion(String opcion) {
            opciones.add(opcion);
        }

        public List<String> getOpciones() {
            return opciones;
        }
    }
}