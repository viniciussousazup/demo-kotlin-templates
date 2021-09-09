package {{inputs.group_id}}.{{inputs.project_name}}.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("hello")
class HelloController() {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun helloWord(): String {
        return "Hello World"
    }
}