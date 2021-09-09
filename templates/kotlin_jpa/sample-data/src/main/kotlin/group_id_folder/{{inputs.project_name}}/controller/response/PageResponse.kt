package {{inputs.group_id}}.{{inputs.project_name}}.controller.response

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content : List<T>,
    val currentPage : Int,
    val totalElements : Long,
    val totalPages : Int
){
    companion object{
        fun <T> of(page : Page<T>) : PageResponse<T>{
            return PageResponse(
                content = page.content,
                currentPage = page.number,
                totalElements = page.totalElements,
                totalPages = page.totalPages
            )
        }

    }
}