package component

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseList
import model.Issue
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import react.*
import react.dom.div
import kotlin.browser.window

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
class IssueList : RComponent<RProps, IssueListState>() {
    override fun IssueListState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val issues = fetchIssues()
            setState {
                this.issues = issues
            }
        }
    }

    override fun RBuilder.render() {
        state.issues?.forEach { issue ->
            div {
                +issue.title
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
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun RBuilder.issueList(handler: RProps.() -> Unit): ReactElement {
    return child(IssueList::class) {
        this.attrs(handler)
    }
}

external interface IssueListState : RState {
    var issues: List<Issue>?
}
