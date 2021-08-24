package br.com.zup.edu.dto

import br.com.zup.edu.ChavePixDetailResponse
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
        Instant
            .ofEpochSecond(criadoEm.seconds, criadoEm.nanos.toLong())
            .atZone(ZoneId.of("America/Sao_Paulo"))
            .toLocalDateTime()

    )

}