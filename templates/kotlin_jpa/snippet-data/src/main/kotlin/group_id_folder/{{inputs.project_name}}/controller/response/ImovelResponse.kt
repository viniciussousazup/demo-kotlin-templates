package {{inputs.group_id}}.{{inputs.project_name}}.controller.response

import {{inputs.group_id}}.{{inputs.project_name}}.enums.TipoImovel

data class ImovelResponse(

    val id: String,

    val cep: String,

    val endereco: String,

    val numero: Long,

    val tipoImovel: TipoImovel,

    val andar: Long?
)