package br.com.zup.edu.controller

import br.com.zup.edu.*
import br.com.zup.edu.dto.ChavePixDTO
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class DetalheChaveControllerTest{

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub

    @Test
    internal fun `deve retornar detalhes de uma chave pix`() {

        val identificador = UUID.randomUUID().toString()
        val pixId = 1L

        Mockito.
        `when`(grpcClient.consultaChavePixKeyManager(idPixRequest(pixId, identificador)))
            .thenReturn(ChavePixDetailResponse.newBuilder().build())

        val httpResponse = client.toBlocking().exchange<Any>("/api/cliente/$identificador/chave-pix/$pixId")

        assertNotNull(httpResponse)
        assertEquals(HttpStatus.OK, httpResponse.status)

    }

    fun idPixRequest(pixId: Long, identificador: String): IdPixRequest {

        return IdPixRequest.newBuilder()
            .setPixId(pixId)
            .setIdentificador(identificador)
            .build()
    }

}