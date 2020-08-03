import com.simple.discussion.model.Issue
import component.issueForm
import component.issueList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import react.*
import react.dom.div
import kotlin.browser.window
import kotlin.js.json

@ImplicitReflectionSerializer
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
        issueForm {
            onSubmit = {
                val target = it.target as HTMLFormElement
                val title: String =  target.get("title").unsafeCast<HTMLInputElement>().value
                val description: String =  target.get("description").unsafeCast<HTMLTextAreaElement>().value

                val mainScope = MainScope()
                mainScope.launch {
                    postIssue(Issue(title = title, description = description))
                }
            }
        }
    }

    private suspend fun postIssue(issue: Issue): Issue = coroutineScope {
        val jsonString = window
            .fetch(
                "http://localhost:8080/issues",
                RequestInit(
                    mode = RequestMode.CORS,
                    method = "POST",
                    headers = json(
                        "Content-Type" to "application/json"
                    ),
                    body = Json.stringify(issue)
                )
            )
            .await()
            .text()
            .await()

        console.log(jsonString)
        Json.parse<Issue>(jsonString)
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

