package br.com.zup.edu.dto

import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import java.time.LocalDateTime

class ChavePixDetail(

    val pixId: Long,
    val identificador: String,
    val tipoChave: TipoChave,
    val chave: String,
    val nome: String,
    val cpf: String,
    val nomeInstituicao: String,
    val agencia: String,
    val numeroDaConta: String,
    val tipoConta: TipoConta,
    val criadoEm: LocalDateTime
) {
}