package br.com.zup.edu.controller

import br.com.zup.edu.*
import br.com.zup.edu.dto.ChavePixDTO
import com.github.javafaker.Faker
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class RegistraControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub

    @Test
    internal fun `deve retornar OK para uma chave registrada`() {

        val identificador = UUID.randomUUID().toString()
        val tipoChave = TipoChave.CPF
        val chave = "60315408006"

        Mockito.
        `when`(grpcClient.registraChavePix(chavePixRequest(identificador, tipoChave, chave)))
            .thenReturn(ChavePixResponse.newBuilder().setPixId(1).build())

        val requisicao = ChavePixDTO(
            tipoChave = tipoChave,
            chave = chave,
            tipoConta = TipoConta.CONTA_CORRENTE
        )

        val request = HttpRequest.POST("/api/cliente/$identificador/chave-pix", requisicao)
        val httpResponse = client.toBlocking().exchange(request, ChavePixDTO::class.java)

        assertNotNull(httpResponse)
        assertEquals(HttpStatus.CREATED, httpResponse.status)
        assertTrue(httpResponse.header("Location")!!.contains("1"))

    }

    @Test
    internal fun `deve retornar BAD_REQUEST para dados invalidos`() {

        val identificador = UUID.randomUUID().toString()
        val tipoChave = TipoChave.CPF
        val chave = ""

        val requisicao = ChavePixDTO(
            tipoChave = tipoChave,
            chave = chave,
            tipoConta = TipoConta.CONTA_CORRENTE
        )

        val request =HttpRequest.POST("/api/cliente/$identificador/chave-pix", requisicao)

        val error = assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(request, ChavePixDTO::class.java)
        }

        assertNotNull(error)
        assertEquals(HttpStatus.BAD_REQUEST, error.status)

    }

    @Test
    internal fun `deve retornar NOT_FOUND para cliente inexistente no Itau`() {

        val identificador = UUID.randomUUID().toString()
        val tipoChave = TipoChave.CPF
        val chave = "60315408006"

        val requisicao = ChavePixDTO(
            tipoChave = tipoChave,
            chave = chave,
            tipoConta = TipoConta.CONTA_CORRENTE
        )

        Mockito.
        `when`(grpcClient.registraChavePix(chavePixRequest(identificador, tipoChave, chave)))
            .thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val request =HttpRequest.POST("/api/cliente/$identificador/chave-pix", requisicao)

        val error = assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(request, ChavePixDTO::class.java)
        }

        println(error.message)

        assertNotNull(error)
        assertEquals(HttpStatus.NOT_FOUND, error.status)

    }

    @Test
    internal fun `deve retornar UNPROCESSABLE_ENTITY para chave ja cadastrada`() {

        val identificador = UUID.randomUUID().toString()
        val tipoChave = TipoChave.CPF
        val chave = "60315408006"

        val requisicao = ChavePixDTO(
            tipoChave = tipoChave,
            chave = chave,
            tipoConta = TipoConta.CONTA_CORRENTE
        )

        Mockito.
        `when`(grpcClient.registraChavePix(chavePixRequest(identificador, tipoChave, chave)))
            .thenThrow(StatusRuntimeException(Status.ALREADY_EXISTS))

        val request =HttpRequest.POST("/api/cliente/$identificador/chave-pix", requisicao)

        val error = assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(request, ChavePixDTO::class.java)
        }

        assertNotNull(error)
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.status)

    }

    fun chavePixRequest(identificador: String, tipoChave: TipoChave, chave: String): ChavePixRequest {

        return ChavePixRequest.newBuilder()
            .setIdentificador(identificador)
            .setTipoChave(tipoChave)
            .setChave(chave)
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

    }

    fun chavePixDTO(tipoChave: TipoChave, chave: String): ChavePixDTO {

        return ChavePixDTO(
            tipoChave,
            chave,
            TipoConta.CONTA_CORRENTE
        )

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoClientStubFactory {
        @Singleton
        internal fun stubMock() = Mockito.mock(KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub::class.java)
    }
}
