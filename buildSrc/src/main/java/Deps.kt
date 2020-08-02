object Versions {
    const val kotlin = "1.3.72"
    const val npmReact = "16.13.1"
}

object Deps {
    object Kotlin {
        const val React = "org.jetbrains:kotlin-react:16.13.1-pre.105-kotlin-${Versions.kotlin}"
        const val ReactDom = "org.jetbrains:kotlin-react-dom:16.13.1-pre.105-kotlin-${Versions.kotlin}"
        const val Styled = "org.jetbrains:kotlin-styled:1.0.0-pre.110-kotlin-${Versions.kotlin}"
        const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5"
    }
}

object TestDeps {
}

object Npm {
    const val React = "react"
    const val ReactDom = "react-dom"
    const val StyledComponents = "styled-components"
    const val InlineStylePrefixer = "inline-style-prefixer"
}
