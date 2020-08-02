import kotlinx.coroutines.await
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import kotlin.browser.document
import kotlin.browser.window

suspend fun main() {
    document.write("Checking ...")
    checkHealth()
}

suspend fun checkHealth() {
    val result = window
        .fetch(
            "http://localhost:8080/health_check",
            RequestInit(mode = RequestMode.CORS)
        )
        .await()
        .text()
        .await()

    document.write(result)
}