package nexup_backend_challenge.src.modelo

import nexup_backend_challenge.src.auxiliar.Horario
import java.time.LocalTime

class Supermercado (val id: Int, val nombreSupermerado: String, val horario: Horario) {

    //Declaraciòn de colecciones para producto y stocks (un par clave/valor para cada producto)
    val coleccionProductos: MutableMap<Int, Producto> = mutableMapOf()
    val coleccionStocks: MutableMap<Int, Stock> = mutableMapOf()
    val listaVentas : MutableList<Venta> = ArrayList()

    //Registrar una venta de un producto. Retorna el precio total de la venta
    fun registrarVenta (idProducto: Int, cantidadVenta: Int): Double{

        //Validar la existencia del producto y su stock
        val producto = coleccionProductos[idProducto] ?: throw Exception("Producto no encontrado")
        val stock = coleccionStocks[idProducto] ?: throw Exception("Stock no encontrado")

        //Validar la venta según la cantidad requerida y su stock actual
        if (stock.cantidadDisponible < cantidadVenta){
            throw Exception("La cantidad de productos no es suficiente para realizar la venta")
        }

        // Si la venta supera las excepciones anteriores, se actualiza el stock
        stock.cantidadDisponible -= cantidadVenta

        //Calcular el total de la venta
        val total = cantidadVenta * producto.precioProducto
        val venta = Venta(idProducto, cantidadVenta, total)
        listaVentas.add(venta)

        return total
    }


    // Calcular la cantidad vendida de un producto determinado
    fun cantidadVendidaDeProducto (idProducto: Int): Int{

        //Filtrar las ventas por idProducto
        return listaVentas.filter{
            it.idProducto == idProducto
        }.sumOf {
            //Sumar las ventas del producto
            it.cantidadVenta
        }
    }


    //Obtener ingresos totales. Devuelve la suma de los ingresos de las ventas del supermercado
    fun ingresosTotales(): Double {
        return listaVentas.sumOf { it.total }
    }

    //Verificar mediante el dìa y la hora, si el supermercado està abierto
    fun estaAbierto(dia: String, hora: LocalTime): Boolean {
        val horarioSemana = horario.horarioSemana
        val horarioFinDeSemana = horario.horarioFinDeSemana

        //Segùn el dìa ingresado, verificar el horario ingresado
        return when (dia) {
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" -> {
                hora.isAfter(horarioSemana.horaApertura) && hora.isBefore(horarioSemana.horaCierre)
            }

            "Sábado", "Domingo" -> {
                hora.isAfter(horarioFinDeSemana.horaApertura) && hora.isBefore(horarioFinDeSemana.horaCierre)
            }
            //devuelve false si el horario ingresado, està fuera del horario de atenciòn (hora apertura - hora cierre)
            else -> false
        }
    }

}