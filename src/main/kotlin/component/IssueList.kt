package component

import com.simple.discussion.model.Issue
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import react.*
import react.dom.div

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
class IssueList : RComponent<IssueListProps, RState>() {
    override fun RBuilder.render() {
        props.issues.forEach { issue ->
            div {
                +"${issue.title}, ${issue.description}"
            }
        }
    }
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun RBuilder.issueList(handler: IssueListProps.() -> Unit): ReactElement {
    return child(IssueList::class) {
        this.attrs(handler)
    }
}

external interface IssueListProps: RProps {
    var issues: List<Issue>
}