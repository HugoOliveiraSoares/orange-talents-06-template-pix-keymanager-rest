package br.com.zup.edu.controller

import br.com.zup.edu.ChavePixRequest
import br.com.zup.edu.KeyManagerGrpcServiceGrpc
import br.com.zup.edu.dto.ChavePixDTO
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/cliente/{identificador}")
class RegistraController(
    @Inject val grpcClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub
) {

    @Post("/chave-pix")
    fun registraChave(identificador: String ,@Body @Valid chavePix: ChavePixDTO): HttpResponse<Any> {

        val request = ChavePixRequest.newBuilder()
            .setIdentificador(identificador)
            .setTipoChave(chavePix.tipoChave)
            .setChave(chavePix.chave)
            .setTipoConta(chavePix.tipoConta)
            .build()

        try {
            val response = grpcClient.registraChavePix(request)

            val uri = HttpResponse.uri("/api/cliente/$identificador/chave-pix/${response.pixId}")
            return HttpResponse.created( uri )

        } catch (e: StatusRuntimeException) {

            val description = e.status.description
            val statusCode = e.status.code

            if (statusCode == Status.Code.INVALID_ARGUMENT)
                return HttpResponse.badRequest(description)

            if (statusCode == Status.Code.ALREADY_EXISTS)
                return HttpResponse.unprocessableEntity<Any?>().body(description)

            if (statusCode == Status.Code.NOT_FOUND)
                return HttpResponse.notFound(description)

            return HttpResponse.serverError(e.message)

        }

    }

}



