package ru.magomed.gamzatov.money.transfer

import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.slf4j.LoggerFactory
import ru.magomed.gamzatov.money.transfer.helper.SparkController
import spark.Spark.port
import spark.servlet.SparkApplication

class Server(private val args: Array<String>): SparkApplication {

    private val logger = LoggerFactory.getLogger(Server::class.java)

    private val defaultPort = 8080

    override fun init() = Unit

    init {
        initServer()
        initControllers()
    }

    /**
    * Initialize the configuration for embedded Jetty server.
    */
    private fun initServer() {
        val port = getPort()
        port(port)
    }

    /**
     * Get port from args or return default.
     */
    private fun getPort(): Int = args
            .asList()
            .indexOfLast { s ->
                s == "--port"
            }.takeIf {
                it < args.size - 1
            }?.let {
                val port = args[it + 1]
                port.toIntOrNull()
            } ?: defaultPort

    /**
     * Initialize controllers.
     *
     * @apiNote The Route setting is described in the class given the {@link SparkController) annotation.
     */
    private fun initControllers() {
        val reflections = Reflections(
                this.javaClass.`package`.name, MethodAnnotationsScanner(), TypeAnnotationsScanner(), SubTypesScanner()
        )
        val controllers = reflections.getTypesAnnotatedWith(SparkController::class.java)
        controllers.forEach {
            logger.info("Instantiating controller: {}", it.name)
            it.newInstance()
        }
    }

}