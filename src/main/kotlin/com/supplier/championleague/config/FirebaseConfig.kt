package main.kotlin.com.supplier.championleague.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import org.eclipse.microprofile.config.inject.ConfigProperties
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.FileInputStream
import java.io.IOException

@Singleton
class FirebaseConfig {

    // val firebaseAccountPhonic: String = "phonic-altar-450817-q4-firebase-adminsdk-fbsvc-749719c99c";
    // val firebaseAccountFlutter: String = "fir-flutter-codelab-be8c1-firebase-adminsdk-urvx0-8394bd3a24";

    // val firebaseProjectIdPhonic: String = "343004725643";
    // val firebaseProjectIdFlutter: String = "611293408654";

    @ConfigProperty(name = "firebase.project.id")
    lateinit var firebaseProjectId: String

    /* Removed properties related to database connection for clarity
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    lateinit var quarkusDataSourceJdbcUrl: String

    @ConfigProperty(name = "quarkus.datasource.username")
    lateinit var quarkusDataSourceUsername: String

    @ConfigProperty(name = "quarkus.datasource.password")
    lateinit var quarkusDataSourcePassword: String
    */
    
    @PostConstruct
    fun init () {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                val credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
                    ?: throw IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS is not set!")
                // Validate the credentials path to prevent path traversal or absolute path usage.
                if (credentialsPath.contains("..") || credentialsPath.contains("/") || credentialsPath.contains("\\")) {
                    throw IllegalArgumentException("Invalid credentials path: possible path traversal or directory separator detected.")
                }
                println("*  credentialsPath: $credentialsPath \n*  projectId: $firebaseProjectId")
                // val pgBouncerSupporterUri = System.getenv("PGBOUNCER_SUPPORTER_URI")
                //    ?: throw IllegalStateException("PGBOUNCER_SUPPORTER_URI is not set!")
                // println("*  pgBouncerSupporterUri: $pgBouncerSupporterUri")

                // println("*  quarkusDataSource: $quarkusDataSourceJdbcUrl, $quarkusDataSourceUsername, $quarkusDataSourcePassword")
                
                // val firebaseAccount: String = StringBuilder("src/main/resources/")
                //    .append(firebaseAccountPhonic)
                //     .append(".json").toString()
                // val serviceAccount = FileInputStream(firebaseAccount)
                val serviceAccount = FileInputStream(credentialsPath)
                // val firebaseProjectId: String = firebaseProjectIdPhonic
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://"+firebaseProjectId+".firebaseio.com")
                    .build()
                FirebaseApp.initializeApp(options)
                println("✅ Firebase initialized successfully!")
            } catch (e: IOException) {
                throw RuntimeException("❌ Error initializing Firebase: ${e.message}")
            }
        }
    }
}