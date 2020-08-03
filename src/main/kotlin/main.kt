import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import react.dom.render
import kotlin.browser.document

@ImplicitReflectionSerializer
@UnstableDefault
fun main() {
    render(document.getElementById("root")) {
        child(App::class) {}
    }
}
