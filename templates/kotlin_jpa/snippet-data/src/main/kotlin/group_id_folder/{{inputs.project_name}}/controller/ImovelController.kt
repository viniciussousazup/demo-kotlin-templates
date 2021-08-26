package {{inputs.group_id}}.{{inputs.project_name}}.controller

import {{inputs.group_id}}.{{inputs.project_name}}.controller.request.ImovelRequest
import {{inputs.group_id}}.{{inputs.project_name}}.controller.response.ImovelResponse
import {{inputs.group_id}}.{{inputs.project_name}}.controller.response.PageResponse
import {{inputs.group_id}}.{{inputs.project_name}}.service.ImovelService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("imoveis")
class ImovelController(
    private val imovelService: ImovelService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody imovelRequest: ImovelRequest
    ): ImovelResponse {
        return imovelService.create(imovelRequest)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listAll(
        pageable : Pageable
    ): PageResponse<ImovelResponse> {
        return imovelService.list(
           pageable
        )
    }

    @PutMapping("/{imovelId}")
    @ResponseStatus(HttpStatus.OK)
    fun edit(
        @PathVariable("imovelId") imovelId: String,
        @Valid @RequestBody imovelRequest: ImovelRequest
    ): ImovelResponse {
        return imovelService.edit(
            imovelRequest, imovelId
        )
    }

    @DeleteMapping("/{imovelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable("imovelId") imovelId: String
    ) {
        imovelService.delete(
            imovelId
        )
    }

}