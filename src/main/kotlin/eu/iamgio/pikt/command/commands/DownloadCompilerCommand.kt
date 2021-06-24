package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.util.KotlinCompilerDownloader
import eu.iamgio.pikt.util.KotlinCompilerType
import kotlin.system.exitProcess

/**
 * Triggered by -downloadcompiler=type[,version] argument
 *
 * @author Giorgio Garofalo
 */
class DownloadCompilerCommand : Command("-downloadcompiler", { args ->
    val type: KotlinCompilerType?
    val version: String?

    with(args?.split(",") ?: emptyList()) {
        type = if(isNotEmpty()) KotlinCompilerType.values().firstOrNull { it.name.equals(this[0], ignoreCase = true) } else null
        version = if(this.size > 1) this[1] else null
    }

    if(type == null) {
        System.err.println("Please specify a valid compiler type: " + KotlinCompilerType.values().joinToString(", ") { it.name.lowercase() } + ".")
        exitProcess(-1)
    }

    KotlinCompilerDownloader.download(version, type)
}, closeOnComplete = true)