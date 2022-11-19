package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.util.ERROR_BAD_ARGUMENT_VALUE
import eu.iamgio.pikt.util.KotlinCompilerDownloader
import eu.iamgio.pikt.util.KotlinCompilerType
import eu.iamgio.pikt.util.exit

/**
 * Triggered by -downloadcompiler=type[,version] argument
 *
 * @author Giorgio Garofalo
 */
class DownloadCompilerCommand : Command("-downloadcompiler", closeOnComplete = true) {
    override fun execute(args: String?) {
        val type: KotlinCompilerType?
        val version: String?

        with(args?.split(",") ?: emptyList()) {
            type = if(isNotEmpty()) {
                KotlinCompilerType.values().firstOrNull { it.name.equals(this[0], ignoreCase = true) }
            } else {
                null
            }
            version = getOrNull(1)
        }

        if(type == null) {
            val types = KotlinCompilerType.values().joinToString(", ") { it.name.lowercase() }
            exit(ERROR_BAD_ARGUMENT_VALUE, message = "Please specify a valid compiler type: $types.")
        }

        KotlinCompilerDownloader.download(version, type)
    }
}