package {{group_id}}.{{project_name}}.controller

import {{group_id}}.{{project_name}}.entity.Imovel
import {{group_id}}.{{project_name}}.enums.TipoImovel
import {{group_id}}.{{project_name}}.repository.ImovelRepository
import org.hamcrest.Matchers
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
internal class ImovelControllerIntegrationTest : BaseControllerTest() {

    @Autowired
    lateinit var imovelRepository: ImovelRepository

    @Test
    fun `create with success`() {

        val requestMap = buildImovelRequestJson()
        val requestJson = JSONObject(requestMap).toString()
        this.mockMvc
            .perform(
                post("/imoveis")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
            .andExpect(jsonPath("$.cep", Matchers.`is`(requestMap["cep"])))
            .andExpect(jsonPath("$.endereco", Matchers.`is`(requestMap["endereco"])))
            .andExpect(jsonPath("$.numero", Matchers.`is`(requestMap.getValue("numero").toInt())))
            .andExpect(jsonPath("$.tipoImovel", Matchers.`is`(requestMap["tipoImovel"])))
    }

    @Test
    fun `create sem andar`() {

        val requestMap = buildImovelRequestJson().toMutableMap()
        requestMap["tipoImovel"] = "APARAMENTO"
        val requestJson = JSONObject(requestMap).toString()
        this.mockMvc
            .perform(
                post("/imoveis")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `list with success`() {

        imovelRepository.save(buildImovelEntity().copy(andar = 300))
        imovelRepository.save(buildImovelEntity().copy(andar = 100))
        imovelRepository.save(buildImovelEntity().copy(andar = 200))

        this.mockMvc
            .perform(
                get("/imoveis")
                    .param("sort", "andar,desc")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.content", Matchers.notNullValue()))
            .andExpect(jsonPath("$.content[0].andar", Matchers.`is`(300)))
            .andExpect(jsonPath("$.totalElements", Matchers.`is`(3)))
            .andExpect(jsonPath("$.totalPages", Matchers.`is`(1)))
            .andExpect(jsonPath("$.currentPage", Matchers.`is`(0)))

    }

    @Test
    fun `edit with success`() {

        val id = imovelRepository.save(buildImovelEntity().copy(numero = 100)).id
        val requestMap = buildImovelRequestJson()
        val requestJson = JSONObject(requestMap).toString()
        this.mockMvc
            .perform(
                put("/imoveis/$id")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
            .andExpect(jsonPath("$.cep", Matchers.`is`(requestMap["cep"])))
            .andExpect(jsonPath("$.endereco", Matchers.`is`(requestMap["endereco"])))
            .andExpect(jsonPath("$.numero", Matchers.`is`(requestMap.getValue("numero").toInt())))
            .andExpect(jsonPath("$.tipoImovel", Matchers.`is`(requestMap["tipoImovel"])))


    }

    @Test
    fun `edit sem andar `() {

        val id = imovelRepository.save(buildImovelEntity().copy(numero = 100)).id
        val requestMap = buildImovelRequestJson().toMutableMap()
        requestMap["tipoImovel"] = "APARAMENTO"
        val requestJson = JSONObject(requestMap).toString()
        this.mockMvc
            .perform(
                put("/imoveis/$id")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `edit sem ter criado `() {

        val id = UUID.randomUUID().toString()
        val requestMap = buildImovelRequestJson()
        val requestJson = JSONObject(requestMap).toString()
        this.mockMvc
            .perform(
                put("/imoveis/$id")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `delete with success`() {

        val id = imovelRepository.save(buildImovelEntity().copy(numero = 100)).id

        this.mockMvc
            .perform(
                delete("/imoveis/$id")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)


    }

    @Test
    fun `delete sem ter criado`() {

        val id = UUID.randomUUID().toString()

        this.mockMvc
            .perform(
                delete("/imoveis/$id")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)


    }

    private fun buildImovelRequestJson(): Map<String, String> = mapOf(
        "cep" to "38410-780",
        "endereco" to "R Brasil",
        "numero" to "42",
        "tipoImovel" to "CASA"
    )

    private fun buildImovelEntity(): Imovel = Imovel(
        id = UUID.randomUUID().toString(),
        cep = "12341564789",
        andar = null,
        endereco = "rua jose",
        numero = 105,
        tipoImovel = TipoImovel.APARTAMENTO
    )


}