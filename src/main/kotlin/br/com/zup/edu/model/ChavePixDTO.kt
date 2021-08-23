package br.com.zup.edu.model

import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import br.com.zup.edu.validation.Pix
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Pix
@Introspected
class ChavePixDTO(
    @field:NotNull
    val tipoChave: TipoChave,
    @field:Size(max = 77)
    val chave: String,
    @field:NotNull
    val tipoConta: TipoConta
) {

}
