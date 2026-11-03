package com.example.myappreg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Creamos una clase para guardar los productos
data class Producto(val nombre: String, val precio: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                // Aquí guardamos la lista de todos los productos registrados
                val listaProductos = remember { mutableStateListOf<Producto>() }

                NavHost(navController = navController, startDestination = Pantalla.Lista.ruta) {

                    // PANTALLA 1: LISTA
                    composable(Pantalla.Lista.ruta) {
                        PantallaLista(
                            productos = listaProductos, // Le pasamos la lista a la pantalla
                            onNavigateToRegistro = {
                                val ruta = Pantalla.Registro.crearRuta("", "")
                                navController.navigate(ruta)
                            }
                        )
                    }

                    // PANTALLA 2: REGISTRO
                    composable(
                        route = Pantalla.Registro.ruta,
                        arguments = argumentosRegistro
                    ) { backStackEntry ->
                        PantallaRegistro(
                            onRegistrar = { nombreIngresado, precioIngresado ->
                                // 1. Agregamos el producto nuevo a la lista
                                listaProductos.add(Producto(nombreIngresado, precioIngresado))

                                // 2. Navegamos de vuelta a la lista usando los argumentos
                                val rutaConDatos = Pantalla.Registro.crearRuta(nombreIngresado, precioIngresado)
                                navController.navigate(Pantalla.Lista.ruta) {
                                    popUpTo(Pantalla.Lista.ruta) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    productos: List<Producto>,
    onNavigateToRegistro: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My App Reg") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToRegistro) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Buscar", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Lista de productos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (productos.isEmpty()) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(vertical = 4.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                    )
                }
            } else {
                // Si hay productos, los mostramos con su nombre y precio
                productos.forEach { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = producto.nombre, fontWeight = FontWeight.Bold)
                            Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(onRegistrar: (String, String) -> Unit) {
    var textoNombre by remember { mutableStateOf("") }
    var textoPrecio by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de producto") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("ID", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Nombre", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = textoNombre,
                onValueChange = { textoNombre = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Precio", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = textoPrecio,
                onValueChange = { textoPrecio = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (textoNombre.isNotBlank() && textoPrecio.isNotBlank()) {
                        onRegistrar(textoNombre, textoPrecio)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
            ) {
                Text("Registrar")
            }
        }
    }
}