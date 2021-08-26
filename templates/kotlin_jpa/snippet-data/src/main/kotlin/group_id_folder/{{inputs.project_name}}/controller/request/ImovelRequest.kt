package {{inputs.group_id}}.{{inputs.project_name}}.controller.request

import {{inputs.group_id}}.{{inputs.project_name}}.entity.Imovel
import {{inputs.group_id}}.{{inputs.project_name}}.enums.TipoImovel
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ImovelRequest(

    @field:NotNull
    val cep: String,

    @field:NotBlank
    val endereco: String,

    @field:NotNull
    val numero: Long,

    @field:NotNull
    val tipoImovel: TipoImovel,

    val andar: Long?
) {
    fun toEntity(): Imovel {
        return Imovel(
            cep = this.cep,
            andar = this.andar,
            endereco = this.endereco,
            numero = this.numero,
            tipoImovel = this.tipoImovel
        )
    }

    fun valid(): ImovelRequest {
        if (this.tipoImovel == TipoImovel.APARTAMENTO && this.andar == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Imoveis do tipo apartamento devem informar o andar."
            )
        }
        return this
    }
}