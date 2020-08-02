import component.issueList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import react.*
import react.dom.div
import kotlin.browser.window

@UnstableDefault
class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val status = checkHealth()
            setState {
                this.status = status
            }
        }
    }

    override fun RBuilder.render() {
        div {
            +(state.status ?: "Checking...")
        }
        issueList {}
    }

    private suspend fun checkHealth(): String {
        return window
            .fetch(
                "http://localhost:8080/health_check",
                RequestInit(mode = RequestMode.CORS)
            )
            .await()
            .text()
            .await()
    }
}

external interface AppState : RState {
    var status: String?
}

