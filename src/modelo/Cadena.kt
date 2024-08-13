package nexup_backend_challenge.src.modelo

import java.time.LocalTime

class Cadena (val nombreCadena: String) {

    //Coleccion de supermercados
    val coleccionSupermercados: MutableList<Supermercado> = mutableListOf()

    //Obtener los 5 productos más vendidos de toda la cadena
    fun obtenerCincoProductosMasVendidos():  String {

        val todasLasVentas = coleccionSupermercados.flatMap {
            it.listaVentas // Obtener todas las ventas de todos los supermercados
        }

        val ventasPorProducto = todasLasVentas.groupBy {
            it.idProducto // Agrupación por producto
        }

        val cantidadProductosVendidosPorProducto = ventasPorProducto.mapValues {
            it.value.sumOf { venta ->
                venta.cantidadVenta // Sumar la cantidad de ventas por producto
            }
        }

        // Convertir Map a List para ordenar por cantidad vendida de productos
        val productosMasVendidos = cantidadProductosVendidosPorProducto.toList().sortedByDescending {
            it.second // Se convierte a una Lista con elementos de tipo par, siendo el segundo valor la cantidad usada para ordenar
        }

        //Recorrer la lista hasta su quinto elemento y mostrar datos
        var resultado = ""
        for (i in 0 until 5) {
            val producto = productosMasVendidos[i].first
            val cantidad = productosMasVendidos[i].second
            val supermercado = coleccionSupermercados.find { supermercado ->
                supermercado.coleccionProductos.containsKey(producto)
            }
            if (supermercado != null) {
                resultado += "${supermercado.coleccionProductos[producto]?.nombreProducto}: $cantidad - "
            } else {
                resultado += "Producto no encontrado: $producto"
            }
        }
        return resultado
    }

    //Obtener ingresos totales
    fun obtenerIngresosTotales(): Double {
        return coleccionSupermercados.sumOf { it.ingresosTotales() }
    }

    //Obtener el supermercado con mayor cantidad de ingresos por ventas
    fun obtenerSupermercadoConMasIngresos(): String {
        val supermercadoConMasVentas = coleccionSupermercados.maxByOrNull {
            it.ingresosTotales()
        }

        return "${supermercadoConMasVentas?.nombreSupermerado} (${supermercadoConMasVentas?.id})." +
                "Ingresos totales: ${supermercadoConMasVentas?.ingresosTotales()}"
    }

    //Obtener lista de supermercados abiertos según un horario ingresado
    fun obtenerSupermercadosAbiertos(dia: String, hora: LocalTime): List<Supermercado> {
        return coleccionSupermercados.filter { supermercado ->
            supermercado.estaAbierto(dia, hora)
        }
    }

}