package br.com.zup.edu.controller

import br.com.zup.edu.*
import br.com.zup.edu.dto.ChaveDTO
import br.com.zup.edu.dto.toLocalDateTime
import com.google.protobuf.Timestamp
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*

@MicronautTest
internal class ListaTodasChavesControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub

    @Test
    internal fun `deve retornar uma lista de ChaveDTO`() {
        val identificador = UUID.randomUUID().toString()

        Mockito.`when`(
            grpcClient.listaTodasChaves(
                ClienteRequest.newBuilder()
                    .setIdentificador(identificador)
                    .build()
            )
        )
            .thenReturn(chaveDTO(identificador))

        val request = HttpRequest.GET<Any>("/api/cliente/$identificador/chave-pix/")
        val httpResponse = client.toBlocking().exchange(request, List::class.java)

        assertNotNull(httpResponse.body())
        assertEquals(HttpStatus.OK ,httpResponse.status)
        assertTrue(httpResponse.body()!!.size == 2)

    }

    fun chaveDTO(identificador: String): ChavesResponse {

        val lista = mutableListOf(
            ChaveResponse.newBuilder()
                .setPixId(1L)
                .setIdentificador(identificador)
                .setTipoChave(TipoChave.CHAVE_ALEATORIA)
                .setChave(UUID.randomUUID().toString())
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .setCriadoEm(Timestamp.newBuilder().build())
                .build(),
            ChaveResponse.newBuilder()
                .setPixId(2L)
                .setIdentificador(identificador)
                .setTipoChave(TipoChave.CHAVE_ALEATORIA)
                .setChave(UUID.randomUUID().toString())
                .setTipoConta(TipoConta.CONTA_POUPANCA)
                .setCriadoEm(Timestamp.newBuilder().build())
                .build()
        )

        return ChavesResponse.newBuilder()
            .addAllChaveResponse(lista)
            .build()
    }

}