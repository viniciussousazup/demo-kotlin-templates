package {{group_id}}.{{project_name}}.service

import {{group_id}}.{{project_name}}.controller.request.ImovelRequest
import {{group_id}}.{{project_name}}.entity.Imovel
import {{group_id}}.{{project_name}}.enums.TipoImovel
import {{group_id}}.{{project_name}}.repository.ImovelRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.Optional

internal class ImovelServiceImplTest {

    private val imovelRepository: ImovelRepository = mock()
    private val imovelService: ImovelService = ImovelServiceImpl(imovelRepository)

    @Test
    fun `create with success`() {
        val imovelRequest = buildImovelRequestMock()

        val imovelMock = buildImovelMock()

        val argumentCaptorOfImovelRequest = argumentCaptor<Imovel>()

        whenever(imovelRepository.save(any<Imovel>())).thenReturn(imovelMock)

        val response = imovelService.create(imovelRequest)

        verify(imovelRepository).save(argumentCaptorOfImovelRequest.capture())

        assertNotNull(imovelMock.id, argumentCaptorOfImovelRequest.firstValue.id)
        assertEquals(imovelRequest.cep, argumentCaptorOfImovelRequest.firstValue.cep)
        assertEquals(imovelRequest.andar, argumentCaptorOfImovelRequest.firstValue.andar)
        assertEquals(imovelRequest.endereco, argumentCaptorOfImovelRequest.firstValue.endereco)
        assertEquals(imovelRequest.numero, argumentCaptorOfImovelRequest.firstValue.numero)
        assertEquals(imovelRequest.tipoImovel, argumentCaptorOfImovelRequest.firstValue.tipoImovel)

        assertEquals(imovelMock.id, response.id)
        assertEquals(imovelMock.cep, response.cep)
        assertEquals(imovelMock.andar, response.andar)
        assertEquals(imovelMock.endereco, response.endereco)
        assertEquals(imovelMock.numero, response.numero)
        assertEquals(imovelMock.tipoImovel, response.tipoImovel)
    }

    @Test
    fun `criar apartamento sem passar numero deve estourar erro`() {
        val imovelRequest = buildImovelRequestMock().copy(andar = null, tipoImovel = TipoImovel.APARTAMENTO)
        val exception = assertThrows(ResponseStatusException::class.java) { imovelService.create(imovelRequest) }
        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `edit apartamento sem passar numero deve estourar erro`() {
        val imovelRequest = buildImovelRequestMock().copy(andar = null, tipoImovel = TipoImovel.APARTAMENTO)

        val imovelMock = buildImovelMock()
        whenever(imovelRepository.findById(any())).thenReturn(Optional.of(imovelMock))

        val exception = assertThrows(ResponseStatusException::class.java) { imovelService.edit(imovelRequest, "1") }
        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `edit apartamento que nao existe deve estourar erro`() {
        val imovelRequest = buildImovelRequestMock().copy(andar = null, tipoImovel = TipoImovel.APARTAMENTO)

        val exception = assertThrows(ResponseStatusException::class.java) { imovelService.edit(imovelRequest, "1") }
        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }

    @Test
    fun `edit with success`() {

        val imovelRequest = buildImovelRequestMock().copy(numero = 106)

        val imovelMock = buildImovelMock()

        val argumentCaptorOfImovelRequest = argumentCaptor<Imovel>()
        val argumentCaptorOfImovelId = argumentCaptor<String>()

        whenever(imovelRepository.findById(any())).thenReturn(Optional.of(imovelMock))
        whenever(imovelRepository.save(any<Imovel>())).thenReturn(imovelMock.copy(numero = 106))

        val response = imovelService.edit(imovelRequest, imovelMock.id)

        verify(imovelRepository).findById(argumentCaptorOfImovelId.capture())
        verify(imovelRepository).save(argumentCaptorOfImovelRequest.capture())

        assertEquals(imovelMock.id, argumentCaptorOfImovelId.firstValue)

        assertEquals(imovelMock.id, argumentCaptorOfImovelRequest.firstValue.id)
        assertEquals(imovelRequest.cep, argumentCaptorOfImovelRequest.firstValue.cep)
        assertEquals(imovelRequest.andar, argumentCaptorOfImovelRequest.firstValue.andar)
        assertEquals(imovelRequest.endereco, argumentCaptorOfImovelRequest.firstValue.endereco)
        assertEquals(imovelRequest.numero, argumentCaptorOfImovelRequest.firstValue.numero)
        assertEquals(imovelRequest.tipoImovel, argumentCaptorOfImovelRequest.firstValue.tipoImovel)

        assertEquals(imovelMock.id, response.id)
        assertEquals(imovelMock.cep, response.cep)
        assertEquals(imovelMock.andar, response.andar)
        assertEquals(imovelMock.endereco, response.endereco)
        assertEquals(imovelRequest.numero, response.numero)
        assertEquals(imovelMock.tipoImovel, response.tipoImovel)
    }

    @Test
    fun `list with success`() {
        val imovelMock = listOf(buildImovelMock(), buildImovelMock().copy(id = "2", numero = 200))
        val pageRequest = PageRequest.of(0, 20)

        whenever(imovelRepository.findAll(pageRequest)).thenReturn(PageImpl(imovelMock))


        val response = imovelService.list(pageRequest)
        assertEquals(imovelMock[0].id, response.content[0].id)
        assertEquals(imovelMock[0].cep, response.content[0].cep)
        assertEquals(imovelMock[0].andar, response.content[0].andar)
        assertEquals(imovelMock[0].endereco, response.content[0].endereco)
        assertEquals(imovelMock[0].numero, response.content[0].numero)
        assertEquals(imovelMock[0].tipoImovel, response.content[0].tipoImovel)

        assertEquals(imovelMock[1].id, response.content[1].id)
        assertEquals(imovelMock[1].cep, response.content[1].cep)
        assertEquals(imovelMock[1].andar, response.content[1].andar)
        assertEquals(imovelMock[1].endereco, response.content[1].endereco)
        assertEquals(imovelMock[1].numero, response.content[1].numero)
        assertEquals(imovelMock[1].tipoImovel, response.content[1].tipoImovel)

        assertEquals(imovelMock.size.toLong(), response.totalElements)
        assertEquals(0, response.currentPage)
        assertEquals(1, response.totalPages)

    }

    @Test
    fun `delete with success`() {
        val id = "1"
        val argumentCaptorOfImovelId = argumentCaptor<String>()

        whenever(imovelRepository.existsById(id)).thenReturn(true)

        imovelService.delete(id)

        verify(imovelRepository).deleteById(argumentCaptorOfImovelId.capture())
        assertEquals(id, argumentCaptorOfImovelId.firstValue)
    }

    @Test
    fun `delete when not exist`() {
        val id = "1"

        whenever(imovelRepository.existsById(id)).thenReturn(false)

        imovelService.delete(id)

        verify(imovelRepository, times(0)).deleteById(id)
        verify(imovelRepository, times(1)).existsById(id)
    }

    private fun buildImovelMock(): Imovel {
        return Imovel(
            id = "1",
            cep = "12341564789",
            andar = null,
            endereco = "rua jose",
            numero = 105,
            tipoImovel = TipoImovel.CASA
        )
    }

    private fun buildImovelRequestMock(): ImovelRequest {
        return ImovelRequest(
            cep = "12341564789",
            andar = null,
            endereco = "rua jose",
            numero = 105,
            tipoImovel = TipoImovel.CASA
        )
    }

}