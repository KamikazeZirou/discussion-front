package component

import com.simple.discussion.model.Issue
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import react.*
import react.dom.*
import styled.css
import styled.styledInput
import styled.styledTextArea
import kotlin.browser.window
import kotlin.js.json

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
class IssueForm : RComponent<IssueProps, IssueFormState>() {
    override fun IssueFormState.init() {
        title = ""
        description = ""
    }

    private val submitHandler: (Event) -> Unit = {
        it.preventDefault()

        val issue = Issue(
            title = state.title,
            description = state.description
        )

        MainScope().launch {
            val postedIssue = postIssue(issue)
            setState {
                title = ""
                description = ""
            }
            props.onPosted(postedIssue)
        }
    }

    override fun RBuilder.render() {
        form {
            attrs {
                method = FormMethod.post
                onSubmitFunction = submitHandler
            }
            fieldSet(classes = "pure-group") {
                div {
                    styledInput(type = InputType.text, name = "title") {
                        css {
                            width = LinearDimension("400px")
                            boxSizing = BoxSizing.borderBox
                        }
                        attrs {
                            value = state.title
                            placeholder = "Title"
                            onChangeFunction = { event ->
                                val target = event.target as HTMLInputElement
                                val value = target.value
                                setState {
                                    title = value
                                }
                            }
                        }
                    }
                }

                div {
                    styledInput(type = InputType.text, name = "labels") {
                        css {
                            width = LinearDimension("400px")
                            boxSizing = BoxSizing.borderBox
                        }
                        attrs {
                            placeholder = "Labels"
                            // TODO Label入力の実装
                        }
                    }
                }

                div {
                    styledTextArea {
                        css {
                            marginTop = LinearDimension("8px")
                            width = LinearDimension("400px")
                            boxSizing = BoxSizing.borderBox
                            resize = Resize.vertical
                        }
                        attrs {
                            name = "description"
                            placeholder = "Leave a comment"
                            value = state.description
                            onChangeFunction = { event ->
                                val target = event.target as HTMLTextAreaElement
                                val value = target.value
                                setState {
                                    description = value
                                }
                            }
                        }
                    }
                }
            }

            button {
                +"Submit new Issue"
                attrs {
                    onClickFunction = submitHandler
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
}

@UnstableDefault
fun RBuilder.issueForm(handler: IssueProps.() -> Unit): ReactElement {
    return child(IssueForm::class) {
        this.attrs(handler)
    }
}

external interface IssueProps : RProps {
    var onPosted: (Issue) -> Unit
}

external interface IssueFormState : RState {
    var title: String
    var description: String
}

