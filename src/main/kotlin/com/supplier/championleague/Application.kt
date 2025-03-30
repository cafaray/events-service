package main.kotlin.com.supplier.championleague

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.spi.CDI
import jakarta.inject.Inject
import main.kotlin.com.supplier.championleague.config.FirebaseConfig

@QuarkusMain
@ApplicationScoped
class Application : QuarkusApplication {

    @Inject
    lateinit var firebaseConfig: FirebaseConfig  // 🔹 Use @Inject instead of CDI.current()

    override fun run(args: Array<String>): Int {
        // Manually load FirebaseConfig to ensure it's initialized
        // CDI.current().select(FirebaseConfig::class.java).get()
        println("🚀 Application started and FirebaseConfig initialized!")

        // Keep the application running
        Quarkus.waitForExit()
        return 0
    }
}

fun main(args: Array<String>) {
    Quarkus.run(Application::class.java, *args)
}
    /*
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Running main method")
            Quarkus.run(*args)
        }
    }*/