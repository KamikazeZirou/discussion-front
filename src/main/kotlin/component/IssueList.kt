package component

import com.simple.discussion.model.Issue
import kotlinx.css.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import react.*
import styled.css
import styled.styledDiv
import styled.styledSpan

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
class IssueList : RComponent<IssueListProps, RState>() {
    override fun RBuilder.render() {
        props.issues.forEach { issue ->
            styledDiv {
                css {
                    borderBottom = "1px solid lightGray"
                }
                styledSpan {
                    css {
                        fontFamily = "Roboto"
                        fontWeight = FontWeight("Normal")
                        fontSize = LinearDimension("16px")
                        letterSpacing = LinearDimension("0.15px")
                    }
                    +issue.title
                }

                // TODO ラベルを表示する
                styledDiv {
                    css {
                        fontFamily = "Roboto"
                        fontWeight = FontWeight("Normal")
                        fontSize = LinearDimension("12px")
                        color = Color.slateGray
                        letterSpacing = LinearDimension("0.4px")
                    }
                    +"不具合"
                }
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