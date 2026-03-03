package com.matecode.registro.data.generator

import com.matecode.registro.data.entity.DiaGradoEntity
import com.matecode.registro.data.enums.TipoDia
import java.time.DayOfWeek
import java.time.YearMonth
@Suppress("NewApi")
object CalendarioGenerator {

    fun generarMes(
        gradoId: Long,
        yearMonth: YearMonth
    ): List<DiaGradoEntity> {

        val diasDelMes = mutableListOf<DiaGradoEntity>()

        for (day in 1..yearMonth.lengthOfMonth()) {

            val fecha = yearMonth.atDay(day)


            val tipo = when (fecha.dayOfWeek) {
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY -> TipoDia.NO_LABORABLE
                else -> TipoDia.CLASE_NORMAL
            }

            diasDelMes.add(
                DiaGradoEntity(
                    gradoId = gradoId,
                    fecha = fecha.toString(),
                    tipoDia = tipo
                )
            )
        }

        return diasDelMes
    }
}


