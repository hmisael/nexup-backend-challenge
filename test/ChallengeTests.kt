import nexup_backend_challenge.src.auxiliar.Horario
import nexup_backend_challenge.src.auxiliar.HorarioFinDeSemana
import nexup_backend_challenge.src.auxiliar.HorarioSemana
import nexup_backend_challenge.src.modelo.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalTime

class ChallengeTests {

    private lateinit var cadena: Cadena
    private lateinit var superSur: Supermercado
    private lateinit var superNorte: Supermercado
    private lateinit var superEste: Supermercado
    private lateinit var superOeste: Supermercado
    private lateinit var arveja: Producto
    private lateinit var poroto: Producto
    private lateinit var garbanzo: Producto
    private lateinit var lentejon: Producto
    private lateinit var soja: Producto
    private lateinit var quinoa: Producto

    private lateinit var horario1: Horario
    private lateinit var horarioSemana1: HorarioSemana
    private lateinit var horarioFinDeSemana1: HorarioFinDeSemana

    private lateinit var horario2: Horario
    private lateinit var horarioSemana2: HorarioSemana
    private lateinit var horarioFinDeSemana2: HorarioFinDeSemana

    @BeforeEach
    fun setUp() {

        cadena = Cadena("Superlógico")


        //Creación de dos horarios de atención distintos
        horarioSemana1 = HorarioSemana(
            LocalTime.of(9, 30),
            LocalTime.of(21, 30)
        )
        horarioFinDeSemana1 = HorarioFinDeSemana(
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        )
        horario1 = Horario(horarioSemana1, horarioFinDeSemana1)

        horarioSemana2 = HorarioSemana(
            LocalTime.of(9, 0),
            LocalTime.of(21, 30)
        )
        horarioFinDeSemana2 = HorarioFinDeSemana(
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        )
        horario2 = Horario(horarioSemana2, horarioFinDeSemana2)

        //Instanciar Supermercados por zona
        superSur = Supermercado(1, "Superlógico Sur", horario2)
        superNorte = Supermercado(2, "Superlógico Norte", horario1)
        superEste = Supermercado(3, "Superlógico Este", horario2)
        superOeste = Supermercado(4, "Superlógico Oeste", horario1)

        cadena.coleccionSupermercados.add(superSur)
        cadena.coleccionSupermercados.add(superNorte)
        cadena.coleccionSupermercados.add(superEste)
        cadena.coleccionSupermercados.add(superOeste)


        //Instanciar productos
        arveja = Producto(1, "Arveja", 10.0)
        poroto = Producto(2, "Poroto", 20.0)
        garbanzo = Producto(3, "Garbanzo", 30.0)
        lentejon = Producto(4, "Lentejón", 45.0)
        soja = Producto(5, "Soja", 50.0)
        quinoa = Producto(6, "Quinoa", 65.0)

        //Carga de Productos y stock para SuperNorte
        superSur.coleccionProductos[arveja.idProducto] = arveja
        superSur.coleccionStocks[arveja.idProducto] = Stock(100)

        superSur.coleccionProductos[poroto.idProducto] = poroto
        superSur.coleccionStocks[poroto.idProducto] = Stock(50)

        superSur.coleccionProductos[garbanzo.idProducto] = garbanzo
        superSur.coleccionStocks[garbanzo.idProducto] = Stock(200)

        //Carga de Productos y stock para SuperNorte

        superNorte.coleccionProductos[lentejon.idProducto] = lentejon
        superNorte.coleccionStocks[lentejon.idProducto] = Stock(150)

        superNorte.coleccionProductos[poroto.idProducto] = poroto
        superNorte.coleccionStocks[poroto.idProducto] = Stock(100)

        superNorte.coleccionProductos[quinoa.idProducto] = quinoa
        superNorte.coleccionStocks[quinoa.idProducto] = Stock(50)

        //Carga de Productos y stock para SuperEste

        superEste.coleccionProductos[soja.idProducto] = soja
        superEste.coleccionStocks[soja.idProducto] = Stock(40)

        superEste.coleccionProductos[quinoa.idProducto] = quinoa
        superEste.coleccionStocks[quinoa.idProducto] = Stock(100)

        //Carga de Productos y stock para SuperOeste

        superOeste.coleccionProductos[poroto.idProducto] = poroto
        superOeste.coleccionStocks[poroto.idProducto] = Stock(50)

        superOeste.coleccionProductos[arveja.idProducto] = arveja
        superOeste.coleccionStocks[arveja.idProducto] = Stock(50)

        superOeste.coleccionProductos[quinoa.idProducto] = quinoa
        superOeste.coleccionStocks[quinoa.idProducto] = Stock(10)


    }

    //Registrar una serie de ventas a ser utilizada en cada Test que lo requiera
    private fun registrarVentas() {

        assertEquals(150.0, superSur.registrarVenta(1, 15))
        assertEquals(200.0, superSur.registrarVenta(2, 10))
        assertEquals(300.0, superSur.registrarVenta(3, 10))

        assertEquals(1000.0, superNorte.registrarVenta(2, 50))
        assertEquals(450.0, superNorte.registrarVenta(4, 10))
        assertEquals(130.0, superNorte.registrarVenta(6, 2))

        assertEquals(100.0, superEste.registrarVenta(5, 2))
        assertEquals(650.0, superEste.registrarVenta(6, 10))

        assertEquals(100.0, superOeste.registrarVenta(2, 5))
        assertEquals(200.0, superOeste.registrarVenta(1, 20))
        assertEquals(65.0, superOeste.registrarVenta(6, 1))
    }


    //Mostrar una excepción cuando se desee registrar una venta que supere el stock disponible
    @Test
    fun testVentaSinStock() {
        assertThrows<Exception> {
            superSur.registrarVenta(1, 101)
        }
    }

    @Test
    fun testObtenerCantidadVendida() {
        registrarVentas()
        val cantidad = superSur.cantidadVendidaDeProducto(1)
        assertEquals(15, cantidad)
    }


    @Test
    fun testObtenerIngresosTotales() {
        registrarVentas()
        assertEquals(3345.0, cadena.obtenerIngresosTotales())
    }



    @Test
    fun testObtenerSupermercadoConMasVentas() {
        registrarVentas()

        println("Supermercado con más ventas: "+cadena.obtenerSupermercadoConMasIngresos())

        // Verificar que el supermercado con más ventas sea el esperado
        assertEquals("${superNorte.nombreSupermerado} (${superNorte.id})." +
                "Ingresos totales: ${superNorte.ingresosTotales()}", cadena.obtenerSupermercadoConMasIngresos())
    }


    @Test
    fun testObtenerProductosMasVendidos() {
        registrarVentas()

        // Obtener los 5 productos más vendidos de la cadena
        val productosMasVendidos = cadena.obtenerCincoProductosMasVendidos()

        println("Productos más vendidos: $productosMasVendidos")

        // Verificar que los productos más vendidos sean correctos
        assertEquals("Poroto: 65 - Arveja: 35 - Quinoa: 13 - Garbanzo: 10 - Lentejón: 10", productosMasVendidos)
    }

    @Test
    fun testObtenerSupermercadosAbiertos() {

        //Día y horario de consulta
        val dia = "Jueves"
        val hora = LocalTime.of(9, 29)

        val supermercadosAbiertos = cadena.obtenerSupermercadosAbiertos(dia, hora)

        //Convertir la lista al formato solicitado
        val resultadoEsperado = supermercadosAbiertos.joinToString(separator = ", ") {
            "${it.nombreSupermerado} (${it.id})"
        }

        assertEquals("Superlógico Sur (1), Superlógico Este (3)", resultadoEsperado)
    }

}