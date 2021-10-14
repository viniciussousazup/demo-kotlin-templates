package {{group_id}}.{{project_name}}.controller

import {{group_id}}.{{project_name}}.controller.request.ImovelRequest
import {{group_id}}.{{project_name}}.controller.response.ImovelResponse
import {{group_id}}.{{project_name}}.controller.response.PageResponse
import {{group_id}}.{{project_name}}.enums.TipoImovel
import {{group_id}}.{{project_name}}.service.ImovelService

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

internal class ImovelControllerTest : BaseControllerTest() {

    @MockBean
    lateinit var imovelService: ImovelService

    @Test
    fun `create with success`() {

        val request = buildImovelRequestJson()

        val imovelRequest = buildImovelRequestObject()
        val imovelResponse = buildImovelResponseObject()
        whenever(imovelService.create(imovelRequest)).thenReturn(imovelResponse)
        this.mockMvc
            .perform(
                post("/imoveis")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(jsonPath("$.id", Matchers.`is`(imovelResponse.id)))
        verify(imovelService, times(1)).create(imovelRequest)
    }

    @Test
    fun `list with success`() {

        val imovelResponse = PageResponse(
            content = listOf(buildImovelResponseObject()),
            currentPage = 0,
            totalElements = 1L,
            totalPages = 1
        )
        val pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "andar")
        whenever(imovelService.list(pageRequest)).thenReturn(imovelResponse)

        this.mockMvc
            .perform(
                get("/imoveis")
                    .param("sort", "andar,desc")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.content", Matchers.notNullValue()))

        verify(imovelService, times(1)).list(pageRequest)
    }

    @Test
    fun `edit with success`() {

        val request = buildImovelRequestJson()
        val id = buildImovelResponseObject().id
        val imovelRequest = buildImovelRequestObject()
        val imovelResponse = buildImovelResponseObject()
        whenever(imovelService.edit(imovelRequest,id)).thenReturn(imovelResponse)

        this.mockMvc
            .perform(
                put("/imoveis/$id")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id", Matchers.`is`(id)))

        verify(imovelService, times(1)).edit(imovelRequest,id)


    }

    @Test
    fun `delete with success`() {

        val id = buildImovelResponseObject().id

        this.mockMvc
            .perform(
                delete("/imoveis/$id")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(imovelService, times(1)).delete(id)

    }

    private fun buildImovelResponseObject(): ImovelResponse =
        ImovelResponse(
            id = "1",
            cep = "38410-780",
            endereco = "R Brasil",
            numero = 42,
            tipoImovel = TipoImovel.CASA,
            andar = null
        )


    private fun buildImovelRequestObject(): ImovelRequest =
        ImovelRequest(
            cep = "38410-780",
            endereco = "R Brasil",
            numero = 42,
            tipoImovel = TipoImovel.CASA,
            andar = null
        )

    private fun buildImovelRequestJson(): String = """
        {
            "cep":"38410-780",
            "endereco":"R Brasil",
            "numero":"42",
            "tipoImovel":"CASA"
        }
    """
}