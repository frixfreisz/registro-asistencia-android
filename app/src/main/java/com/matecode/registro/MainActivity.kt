package com.matecode.registro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matecode.registro.data.database.AppDatabase
import com.matecode.registro.data.informe.InformeMensual
import com.matecode.registro.data.viewmodel.GradoViewModel
import com.matecode.registro.data.viewmodel.GradoViewModelFactory
import com.matecode.registro.ui.AsistenciaScreen
import com.matecode.registro.ui.CalendarioScreen
import com.matecode.registro.ui.GradosScreen
import com.matecode.registro.ui.InformeMensualScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        val factory = GradoViewModelFactory(
            diaGradoDao = database.diaGradoDao(),
            alumnoDao = database.alumnoDao(),
            asistenciaDao = database.asistenciaDao(),
            mesCerradoDao = database.mesCerradoDao(),
            gradoDao = database.gradoDao()
        )

        setContent {

            val viewModel: GradoViewModel = viewModel(factory = factory)
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            var informeActual by remember {
                mutableStateOf<InformeMensual?>(null)
            }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {

                        Text(
                            text = "Menú",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )

                        NavigationDrawerItem(
                            label = { Text("Grados") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate("grados")
                            }
                        )

                        NavigationDrawerItem(
                            label = { Text("Calendario") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate("calendario/0")
                            }
                        )
                    }
                }
            ) {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Registro Escolar") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->

                    LaunchedEffect(Unit) {

                        viewModel.inicializarGradosSiNoExisten()

                       viewModel.verificarEImportarSiEstaVacio(this@MainActivity)


                    }



                    NavHost(
                        navController = navController,
                        startDestination = "grados",
                        modifier = Modifier.padding(padding)
                    ) {

                        composable(route = "grados") {

                            val grados by viewModel.grados.collectAsState()



                            GradosScreen(
                                grados = grados,
                                viewModel = viewModel,
                                onAbrirAsistencia = { /* navegación */ },
                                onAbrirCalendario = { /* navegación */ },
                                onInformeGenerado = { informe ->
                                    informeActual = informe
                                    navController.navigate("informe_mensual")
                                }
                            )
                        }

                        composable("asistencia/{gradoId}") { backStackEntry ->
                            val gradoId = backStackEntry.arguments
                                ?.getString("gradoId")
                                ?.toLong() ?: 0L

                            AsistenciaScreen(
                                viewModel = viewModel,
                                gradoId = gradoId,
                                onVolver = {
                                    navController.popBackStack()
                                },
                                onCerrarMes = {
                                    navController.navigate("calendario/$gradoId")
                                }
                            )
                        }

                        composable("calendario/{gradoId}") { backStackEntry ->
                            val gradoId = backStackEntry.arguments
                                ?.getString("gradoId")
                                ?.toLong() ?: 0L

                            CalendarioScreen(
                                viewModel = viewModel,
                                gradoId = gradoId,
                                onVolver = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("informe_mensual") {
                            informeActual?.let {
                                InformeMensualScreen(informe = it)
                            }
                        }

                    }
                }
            }
        }
    }
}
