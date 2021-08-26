package {{inputs.group_id}}.{{inputs.project_name}}.repository

import {{inputs.group_id}}.{{inputs.project_name}}.entity.Imovel
import org.springframework.data.repository.PagingAndSortingRepository

interface ImovelRepository : PagingAndSortingRepository<Imovel, String> {

}