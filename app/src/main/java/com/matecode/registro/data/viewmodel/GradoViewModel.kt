package com.matecode.registro.data.viewmodel


import android.content.Context
import com.matecode.registro.data.dao.AlumnoDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matecode.registro.data.dao.AsistenciaDao
import com.matecode.registro.data.dao.DiaGradoDao
import com.matecode.registro.data.dao.GradoDao
import com.matecode.registro.data.dao.MesCerradoDao
import com.matecode.registro.data.entity.AlumnoEntity
import com.matecode.registro.data.entity.AsistenciaEntity
import com.matecode.registro.data.entity.DiaGradoEntity
import com.matecode.registro.data.entity.GradoEntity
import com.matecode.registro.data.entity.MesCerradoEntity
import com.matecode.registro.data.enums.Division
import com.matecode.registro.data.enums.EstadoAsistencia
import com.matecode.registro.data.enums.Grado
import com.matecode.registro.data.enums.TipoDia
import com.matecode.registro.data.enums.Turno
import com.matecode.registro.data.generator.CalendarioGenerator
import com.matecode.registro.data.informe.InformeMensual
import com.matecode.registro.data.informe.InformeMensualGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDate
import java.time.YearMonth

@Suppress("NewApi")
class GradoViewModel(
    private val diaGradoDao: DiaGradoDao,
    private val alumnoDao: AlumnoDao,
    private val asistenciaDao: AsistenciaDao,
    private val mesCerradoDao: MesCerradoDao,
    private val gradoDao: GradoDao,



    ) : ViewModel() {
    init {
        viewModelScope.launch {
            gradoDao.obtenerGrados().collect {
                _grados.value = it
            }
        }
    }

    private lateinit var gradoMap: Map<Pair<Grado, Division>, Long>

    suspend fun estaMesCerrado(): Boolean {

        val estado = mesCerradoDao.obtenerEstadoMes(
            currentGradoId,
            currentYearMonth.toString()
        )

        return estado?.cerrado == true
    }

    suspend fun esMesCerrado(): Boolean {
        val estadoMes = mesCerradoDao.obtenerEstadoMes(
            currentGradoId,
            currentYearMonth.toString()
        )
        return estadoMes?.cerrado == true
    }

    private val _mostrarDialogoJustificacion = MutableStateFlow(false)
    val mostrarDialogoJustificacion: StateFlow<Boolean> = _mostrarDialogoJustificacion

    private val _asistenciasDelDia = MutableStateFlow<List<AsistenciaEntity>>(emptyList())
    val asistenciasDelDia: StateFlow<List<AsistenciaEntity>> = _asistenciasDelDia

    private val _dias = MutableStateFlow<List<DiaGradoEntity>>(emptyList())
    val dias: StateFlow<List<DiaGradoEntity>> = _dias

    private val _alumnos = MutableStateFlow<List<AlumnoEntity>>(emptyList())
    val alumnos: StateFlow<List<AlumnoEntity>> = _alumnos

    private val _estado = MutableStateFlow("")
    val estado: StateFlow<String> = _estado

    private var currentGradoId: Long = 0L
    private lateinit var currentYearMonth: YearMonth

    private val _pendientesDelMes = MutableStateFlow<List<AsistenciaEntity>>(emptyList())
    val pendientesDelMes: StateFlow<List<AsistenciaEntity>> = _pendientesDelMes

    private val _grados = MutableStateFlow<List<GradoEntity>>(emptyList())
    val grados: StateFlow<List<GradoEntity>> = _grados

    // ============================================================
    // CARGAR DÍAS
    // ============================================================

    fun cargarDias(gradoId: Long, yearMonth: YearMonth) {

        currentGradoId = gradoId
        currentYearMonth = yearMonth

        viewModelScope.launch {

            val yearMonthString = yearMonth.toString()

            val cantidad = diaGradoDao.contarDiasDelMes(
                gradoId = gradoId,
                yearMonth = yearMonthString
            )

            if (cantidad == 0) {
                val diasGenerados = CalendarioGenerator.generarMes(
                    gradoId = gradoId,
                    yearMonth = yearMonth
                )
                diaGradoDao.insertarDias(diasGenerados)
            }

            val desde = yearMonth.atDay(1).toString()
            val hasta = yearMonth.atEndOfMonth().toString()

            val lista = diaGradoDao.obtenerDiasPorGrado(
                gradoId,
                desde,
                hasta
            )

            _dias.value = lista
        }
    }

    // ============================================================
    // CARGAR ALUMNOS
    // ============================================================

    fun cargarAlumnos(gradoId: Long) {
        viewModelScope.launch {
            alumnoDao.getAlumnosPorGrado(gradoId)
                .collect { lista ->
                    _alumnos.value = lista
                }
        }

    }


    // ============================================================
    // ACTUALIZAR TIPO DE DÍA
    // ============================================================

    fun actualizarTipoDia(
        gradoId: Long,
        fecha: String,
        nuevoTipo: TipoDia
    ) {
        viewModelScope.launch {
            val cerrado = mesCerradoDao.obtenerEstadoMes(
                currentGradoId,
                currentYearMonth.toString()
            )

            if (cerrado?.cerrado == true) {
                _estado.value = "El mes está cerrado. No se pueden modificar días."
                return@launch
            }

            diaGradoDao.actualizarTipoDia(
                gradoId = gradoId,
                fecha = fecha,
                nuevoTipo = nuevoTipo

            )

            cargarDias(currentGradoId, currentYearMonth)

            _estado.value = "Tipo de día actualizado"

            cargarDias(currentGradoId, currentYearMonth)
        }
    }

    // ============================================================
    // ALUMNOS POR GRADO
    // ============================================================

    fun alumnosPorGrado(gradoId: Long): Flow<List<AlumnoEntity>> {
        return alumnoDao.getAlumnosPorGrado(gradoId)
    }


    // ============================================================
    // MARCAR ASISTENCIA
    // ============================================================


    fun marcarAsistencia(
        alumnoId: String,
        fecha: String,
        estado: EstadoAsistencia
    ) {


        viewModelScope.launch {
            val cerrado = mesCerradoDao.obtenerEstadoMes(
                currentGradoId,
                currentYearMonth.toString()
            )

            if (cerrado?.cerrado == true) {
                _estado.value = "El mes está cerrado. No se pueden modificar asistencias."
                return@launch
            }

            val estadoMes = mesCerradoDao.obtenerEstadoMes(
                currentGradoId,
                currentYearMonth.toString()
            )

            if (estadoMes?.cerrado == true) {
                _estado.value = "El mes está cerrado"
                return@launch
            }
            val dia = dias.value.find { it.fecha == fecha }

            val permitido = dia?.tipoDia?.esDiaTrabajado() == true

            if (!permitido) {
                _estado.value = "No se puede tomar asistencia en este tipo de día"
                return@launch
            }

            val asistencia = AsistenciaEntity(
                alumnoDni = alumnoId,
                fecha = fecha,
                estado = estado,
                gradoId = currentGradoId,
            )

            asistenciaDao.insertarAsistencia(asistencia)

            cargarAsistenciasDelDia(fecha)
        }
    }
//------------------------------------------------------------------------------------------------
    //Base de datos de prueba de alumnos
    //--------------------------------------------------------------------------------------------

    fun importarAlumnosDesdeCsv(inputStream: InputStream) {

        viewModelScope.launch(Dispatchers.IO) {

            obtenerMapaDeGrados()

            val lineas = inputStream.bufferedReader().readLines()

            println("Cantidad de líneas CSV: ${lineas.size}")

            val listaAlumnos = mutableListOf<AlumnoEntity>()

            lineas.drop(1).forEach { linea ->

                val columnas = linea.split(",")

                if (columnas.size >= 17) {

                    val gradoTexto = columnas[15].trim()
                    val divisionTexto = columnas[16].trim()

                    val gradoEnum = Grado.valueOf(gradoTexto.uppercase())
                    val divisionEnum = Division.valueOf(divisionTexto.uppercase())

                    val gradoId = gradoMap[Pair(gradoEnum, divisionEnum)]

                    if (gradoId != null) {

                        val alumno = AlumnoEntity(
                            dniId = columnas[0],
                            apellido = columnas[1],
                            nombre = columnas[2],
                            fechaNacimiento = columnas[3],
                            sexo = columnas[4],
                            direccion = columnas[5],
                            localidad = columnas[6],
                            provincia = columnas[7],
                            pais = columnas[8],
                            telefono = columnas[9],
                            responsableTutor = columnas[10],
                            telefonoResponsableTutor = columnas[11],
                            ocupacion = columnas[12],
                            observaciones = columnas[13],
                            foto = columnas[14],
                            gradoId = gradoId
                        )

                        listaAlumnos.add(alumno)
                    }
                }
            }

            alumnoDao.insertarAlumnos(listaAlumnos)

            println("Insertados: ${listaAlumnos.size}")
        }
    }
    //---------------------------------------------------------------------------------------------
    //OBTENER MAPA DE GRADOS
    //---------------------------------------------------------------------------------------------

    private suspend fun obtenerMapaDeGrados() {
        val grados = gradoDao.obtenerTodos()

        gradoMap = grados.associate {
            Pair(it.grado, it.division) to it.id
        }
    }

    // ============================================================
    // cargar asistencia por
    // ============================================================
    fun cargarAsistenciasDelDia(fecha: String) {
        viewModelScope.launch {
            asistenciaDao
                .obtenerAsistenciasPorFechaYGrado(fecha, currentGradoId)
                .collect { lista ->
                    _asistenciasDelDia.value = lista
                }
        }
    }

    //------------------------------------------------------------------------------------------------
    //VERIFICAR DIA ANTERIOR
    //--------------------------------------------------------------------------------------------
    fun verificarDiaAnterior() {
        viewModelScope.launch {

            val ayer = LocalDate.now().minusDays(1).toString()

            val dia = dias.value.find { it.fecha == ayer } ?: return@launch

            val eraDiaTrabajable =
                dia.tipoDia == TipoDia.CLASE_NORMAL ||
                        dia.tipoDia == TipoDia.JORNADA_INSTITUCIONAL

            asistenciaDao
                .obtenerAsistenciasPorFechaYGrado(ayer, currentGradoId)
                .collect { lista ->

                    if (eraDiaTrabajable && lista.isEmpty()) {
                        _mostrarDialogoJustificacion.value = true
                    }
                }
        }
    }

    //-------------------------------------------------------------------------------------------------
    //JUSTIFICAR DIA ANTERIOR
    //--------------------------------------------------------------------------------------------
    fun justificarDiaAnterior(nuevoTipo: TipoDia) {
        viewModelScope.launch {

            val ayer = LocalDate.now().minusDays(1).toString()

            diaGradoDao.actualizarTipoDia(
                gradoId = currentGradoId,
                fecha = ayer,
                nuevoTipo = nuevoTipo
            )

            cargarDias(currentGradoId, currentYearMonth)

            _mostrarDialogoJustificacion.value = false
        }
    }

    //-----------------------------------------------------------------------------------------------
    //OCULTAR DIALOGO
    //-------------------------------------------------------------------------------------------
    fun ocultarDialogo() {
        _mostrarDialogoJustificacion.value = false
    }

    suspend fun puedeCerrarMes(gradoId: Long, yearMonth: YearMonth): Boolean {
        val desde = yearMonth.atDay(1).toString()
        val hasta = yearMonth.atEndOfMonth().toString()

        val pendientes = asistenciaDao.contarPendientesDelMes(gradoId, desde, hasta)

        return pendientes == 0
    }

    // --------------------------------------------------------------------------------------------
    // VER ALUMNOS POR DEFINIR INASISTENCIA
    //---------------------------------------------------------------------------------------------

    fun cargarPendientesDelMes(gradoId: Long, yearMonth: YearMonth) {
        viewModelScope.launch {

            val desde = yearMonth.atDay(1).toString()
            val hasta = yearMonth.atEndOfMonth().toString()

            val lista = asistenciaDao.obtenerPendientesDelMes(
                gradoId,
                desde,
                hasta
            )

            _pendientesDelMes.value = lista
        }
    }

    //--------------------------------------------------------------------------------------------
    //DEFINIR FALTA
    //--------------------------------------------------------------------------------------------
    fun definirFalta(
        asistencia: AsistenciaEntity,
        nuevoEstado: EstadoAsistencia
    ) {
        viewModelScope.launch {
            val cerrado = mesCerradoDao.obtenerEstadoMes(
                currentGradoId,
                currentYearMonth.toString()
            )

            if (cerrado?.cerrado == true) {
                _estado.value = "El mes está cerrado. No se pueden modificar faltas."
                return@launch
            }

            val actualizada = asistencia.copy(
                estado = nuevoEstado
            )

            asistenciaDao.insertarAsistencia(actualizada)
        }
    }
    //--------------------------------------------------------------------------------------------
    //METODO FINAL PARA CERRAR EL MES
    //--------------------------------------------------------------------------------------------

    fun intentarCerrarMes(
        gradoId: Long,
        yearMonth: YearMonth,
        onResultado: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val puede = puedeCerrarMes(gradoId, yearMonth)

            onResultado(puede)
        }
    }

    //---------------------------------------------------------------------------------------------
    //cerrar mes
    //---------------------------------------------------------------------------------------------

    fun cerrarMes(gradoId: Long, yearMonth: YearMonth) {

        viewModelScope.launch {

            mesCerradoDao.cerrarMes(
                MesCerradoEntity(
                    gradoId = gradoId,
                    yearMonth = yearMonth.toString()
                )
            )
        }
    }

    //--------------------------------------------------------------------------------------------
    //reabrir mes
    //--------------------------------------------------------------------------------------------

    fun reabrirMes(gradoId: Long, yearMonth: YearMonth) {

        viewModelScope.launch {

            mesCerradoDao.reabrirMes(
                gradoId,
                yearMonth.toString()
            )
        }
    }
    //  ----------------------------------------------------------------------------------------------
    //intentar cerrar mes
    //---------------------------------------------------------------------------------------------

    suspend fun mesEstaCerrado(
        gradoId: Long,
        yearMonth: YearMonth
    ): Boolean {

        return mesCerradoDao.estaCerrado(
            gradoId,
            yearMonth.toString()
        )
    }
    //-------------------------------------Seleccon de  grado-----------------------------------------------

    fun seleccionarGrado(gradoId: Long, yearMonth: YearMonth) {
        currentGradoId = gradoId
        currentYearMonth = yearMonth

        cargarDias(gradoId, yearMonth)
        cargarAlumnos(gradoId)
    }

    fun verificarEImportarSiEstaVacio(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {

            val cantidad = alumnoDao.contarAlumnos()

            if (cantidad == 0) {

                val inputStream = context.assets.open("alumnos.csv")

                val gradoMap = obtenerMapaDeGrados()

                importarAlumnosDesdeCsv(inputStream)

            }
        }
    }


    suspend fun inicializarGradosSiNoExisten() {

        val existentes = gradoDao.obtenerTodos()

        if (existentes.isNotEmpty()) return

        gradoDao.insert(
            GradoEntity(
                docenteId = 1,
                grado = Grado.CUARTO,
                division = Division.C,
                turno = Turno.MANIANA
            )
        )

        gradoDao.insert(
            GradoEntity(
                docenteId = 1,
                grado = Grado.QUINTO,
                division = Division.C,
                turno = Turno.MANIANA
            )
        )

        gradoDao.insert(
            GradoEntity(
                docenteId = 1,
                grado = Grado.SEXTO,
                division = Division.C,
                turno = Turno.MANIANA
            )
        )
    }


    //------------------------------------------------------------------------------------------
    //INFORME MENSUAL
    //-------------------------------------------------------------------------------------
    suspend fun generarInformeMensual(
        gradoId: Long,
        yearMonth: YearMonth
    ): InformeMensual {

        val desde = yearMonth.atDay(1).toString()
        val hasta = yearMonth.atEndOfMonth().toString()

        val alumnos = alumnoDao.getAlumnosPorGradoSuspend(gradoId)

        val asistencias = asistenciaDao.obtenerAsistenciasDelMes(
            gradoId,
            desde,
            hasta
        )

        val dias = diaGradoDao.obtenerDiasDelMes(
            gradoId,
            desde,
            hasta
        )

        // Obtener información del grado
        val gradoEntity = gradoDao.obtenerPorId(gradoId)

        val gradoNombre =
            "${gradoEntity.grado.displayName()} ${gradoEntity.division.displayName()}"

        val turnoNombre = gradoEntity.turno.displayName()

        val cicloNombre =
            if (gradoEntity.grado.ordinal <= 2) "Primer Ciclo"
            else "Segundo Ciclo"

        return InformeMensualGenerator.generar(
            alumnos = alumnos,
            asistencias = asistencias,
            dias = dias,
            yearMonth = yearMonth,
            grado = gradoNombre,
            turno = turnoNombre,

        )
    }
}