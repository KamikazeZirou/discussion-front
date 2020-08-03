package component

import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onSubmitFunction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import org.w3c.dom.events.Event
import react.*
import react.dom.*

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
class IssueForm : RComponent<IssueProps, RState>() {
    private val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        props.onSubmit(it)
    }

    override fun RBuilder.render() {
        form {
            attrs {
                method = FormMethod.post
                onSubmitFunction = submitHandler
            }
            fieldSet(classes = "pure-group") {
                div {
                    label {
                        +"Title:"
                        input(type = InputType.text, name = "title") {}
                    }
                }

                div {
                    label {
                        +"Description:"
                        textArea {
                            attrs {
                                name = "description"
                            }
                        }
                    }
                }
            }

            button {
                +"Create"
            }
        }
    }
}

@UnstableDefault
fun RBuilder.issueForm(handler: IssueProps.() -> Unit): ReactElement {
    return child(IssueForm::class) {
        this.attrs(handler)
    }
}

external interface IssueProps : RProps {
    var onSubmit: (Event) -> Unit
}

external interface IssueFormState : RState {
    var title: String?
    var description: String?
}

