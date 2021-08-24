package br.com.zup.edu.exceptionHandler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExceptionHandlerTest{

    private lateinit var request: HttpRequest<Any>

    @BeforeEach
    internal fun setUp() {
        request = HttpRequest.GET("/")
    }

    @Test
    internal fun `deve retornar NOT_FOUND para Status NOT_FOUD`() {

        val exception = StatusRuntimeException(Status.NOT_FOUND
            .withDescription("Chave não encontrada"))

        val response = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
        assertEquals("Chave não encontrada", response.body()?.toString())

    }

    @Test
    internal fun `deve retornar BAD_REQUEST para Status INVALID_ARGUMENT`() {

        val exception = StatusRuntimeException(Status.INVALID_ARGUMENT)

        val response = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertEquals("Dados invalidos", response.body()?.toString())

    }

    @Test
    internal fun `deve retornar UNPROCESSABLE_ENTITY para Status ALREADY_EXISTS`() {

        val exception = StatusRuntimeException(Status.ALREADY_EXISTS
            .withDescription("Chave já existente"))

        val response = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
        assertEquals("Chave já existente", response.body()?.toString())

    }

    @Test
    internal fun `deve retornar INTERNAL_SERVER_ERROR`() {

        val exception = StatusRuntimeException(Status.UNKNOWN)

        val response = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertTrue(response.body()?.toString()!!.contains("Não foi possivel completar a requisição devido ao erro:"))

    }

}