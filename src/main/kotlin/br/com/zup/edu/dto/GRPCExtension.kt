package br.com.zup.edu.dto

import br.com.zup.edu.ChavePixDetailResponse
import br.com.zup.edu.ChaveResponse
import br.com.zup.edu.ChavesResponse
import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ChavePixDetailResponse.toDTO(): ChavePixDetail {

    return ChavePixDetail(

        pixId,
        identificador,
        tipoChave,
        chave,
        nome,
        cpf,
        nomeInstituicao,
        agencia,
        numeroDaConta,
        tipoConta,
        toLocalDateTime(criadoEm)

    )

}

fun ChavesResponse.toDTO(): List<ChaveDTO> {

    val chaves = mutableListOf<ChaveDTO>()

    chaveResponseList.forEach {
        chaves.add(
            ChaveDTO(
                it.pixId,
                it.identificador,
                it.tipoChave,
                it.chave,
                it.tipoConta,
                toLocalDateTime(it.criadoEm)
            )
        )
    }

    return chaves

}

fun toLocalDateTime(time: Timestamp): LocalDateTime {

    return Instant
        .ofEpochSecond(time.seconds, time.nanos.toLong())
        .atZone(ZoneId.of("America/Sao_Paulo"))
        .toLocalDateTime()

}