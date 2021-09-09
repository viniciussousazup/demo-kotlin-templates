package {{inputs.group_id}}.{{inputs.project_name}}.service

import {{inputs.group_id}}.{{inputs.project_name}}.controller.request.ImovelRequest
import {{inputs.group_id}}.{{inputs.project_name}}.controller.response.ImovelResponse
import {{inputs.group_id}}.{{inputs.project_name}}.controller.response.PageResponse
import org.springframework.data.domain.Pageable

interface ImovelService {

    fun create(imovelRequest: ImovelRequest) : ImovelResponse

    fun edit(imovelRequest: ImovelRequest, id : String) : ImovelResponse

    fun list(pageable: Pageable) : PageResponse<ImovelResponse>

    fun delete(id : String)
}