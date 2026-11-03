package com.example.myappreg

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Pantalla(val ruta: String) {
    object Lista : Pantalla("lista")
    object Registro : Pantalla("registro?nombre={nombre}&precio={precio}") {
        fun crearRuta(nombre: String, precio: String): String {
            return "registro?nombre=$nombre&precio=$precio"
        }
    }
}

val argumentosRegistro = listOf(
    navArgument("nombre") { type = NavType.StringType; defaultValue = "" },
    navArgument("precio") { type = NavType.StringType; defaultValue = "" }
)