package br.com.zup.edu.controller

import br.com.zup.edu.Empty
import br.com.zup.edu.IdPixRequest
import br.com.zup.edu.KeyManagerGrpcServiceGrpc
import br.com.zup.edu.dto.ChavePixDTO
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class RemoveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub

    @Test
    internal fun `deve retornar OK`() {

        val identificador = UUID.randomUUID().toString()
        val pixId = 1L

        Mockito.
        `when`(grpcClient.deletaChavePix(chavePixRequest(identificador, pixId)))
            .thenReturn(Empty.newBuilder().build())

        val request = HttpRequest.DELETE("/api/cliente/$identificador/chave-pix/$pixId", "")
        val httpResponse = client.toBlocking().exchange(request, ChavePixDTO::class.java)

        assertNotNull(httpResponse)
        assertEquals(HttpStatus.OK, httpResponse.status)

    }

    @Test
    internal fun `deve retornar NOT_FOUND para chave nao encontrada`() {

        val identificador = UUID.randomUUID().toString()
        val pixId = 1L

        Mockito.
        `when`(grpcClient.deletaChavePix(chavePixRequest(identificador, pixId)))
            .thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val request = HttpRequest.DELETE("/api/cliente/$identificador/chave-pix/$pixId", "")

        val error = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, ChavePixDTO::class.java)
        }

        assertNotNull(error)
        assertEquals(HttpStatus.NOT_FOUND, error.status)

    }

    fun chavePixRequest(identificador: String, pixId: Long): IdPixRequest {

        return IdPixRequest.newBuilder()
            .setPixId(pixId)
            .setIdentificador(identificador)
            .build()

    }

}