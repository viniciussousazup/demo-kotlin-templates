package {{inputs.group_id}}.{{inputs.project_name}}.service

import {{inputs.group_id}}.{{inputs.project_name}}.controller.request.ImovelRequest
import {{inputs.group_id}}.{{inputs.project_name}}.controller.response.ImovelResponse
import {{inputs.group_id}}.{{inputs.project_name}}.controller.response.PageResponse
import {{inputs.group_id}}.{{inputs.project_name}}.entity.Imovel
import {{inputs.group_id}}.{{inputs.project_name}}.repository.ImovelRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ImovelServiceImpl(
    private val imovelRepository: ImovelRepository
) : ImovelService {
    override fun create(imovelRequest: ImovelRequest): ImovelResponse {
        return imovelRepository.save(
            imovelRequest.valid().toEntity()
        ).toResponse()
    }

    override fun edit(imovelRequest: ImovelRequest, id: String): ImovelResponse {
        val imovelSalvo = imovelRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, id
        )
        return imovelRepository.save(
            imovelRequest.valid().toEntity().copy(id = imovelSalvo.id)
        ).toResponse()
    }

    override fun list(pageable: Pageable): PageResponse<ImovelResponse> {
        return PageResponse.of(
            imovelRepository.findAll(pageable).map { it.toResponse() })
    }

    override fun delete(id: String) {
        if (imovelRepository.existsById(id)) {
            imovelRepository.deleteById(id)
        }
    }

    private fun Imovel.toResponse(): ImovelResponse {
        return ImovelResponse(
            id = this.id,
            tipoImovel = this.tipoImovel,
            numero = this.numero,
            endereco = this.endereco,
            andar = this.andar,
            cep = this.cep
        )
    }
}