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
import kotlinx.serialization.parseList
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import react.*
import react.dom.div
import kotlin.browser.window

@ImplicitReflectionSerializer
@UnstableDefault
class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        status = "Checking ..."
        issues = listOf()

        val mainScope = MainScope()
        mainScope.launch {
            val status = checkHealth()
            setState {
                this.status = status
            }
        }

        mainScope.launch {
            val fetchedIssues = fetchIssues()
            setState {
                this.issues = fetchedIssues
            }
        }
    }

    override fun RBuilder.render() {
        div {
            +state.status
        }
        issueList {
            issues = state.issues
        }
        issueForm {
            onPosted = {
                MainScope().launch {
                    val fetchedIssues = fetchIssues()
                    setState {
                        this.issues = fetchedIssues
                    }
                }
            }
        }
    }

    private suspend fun fetchIssues(): List<Issue> = coroutineScope {
        val jsonString = window
            .fetch(
                "http://localhost:8080/issues",
                RequestInit(mode = RequestMode.CORS)
            )
            .await()
            .text()
            .await()
        Json.parseList<Issue>(jsonString)
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
    var status: String
    var issues: List<Issue>
}

