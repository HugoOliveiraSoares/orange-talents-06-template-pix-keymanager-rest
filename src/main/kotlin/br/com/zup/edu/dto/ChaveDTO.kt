package br.com.zup.edu.dto

import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import java.time.LocalDateTime

class ChaveDTO(
    val pixId: Long,
    val identificador: String,
    val tipoChave: TipoChave,
    val chave: String,
    val tipoConta: TipoConta,
    val criadoEm: LocalDateTime
) {

}