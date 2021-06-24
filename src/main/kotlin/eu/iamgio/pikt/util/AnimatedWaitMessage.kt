package eu.iamgio.pikt.util

import java.util.*
import kotlin.concurrent.timer

/**
 * Prints out a message with an animated suffix to [System.out].
 *
 * @param text message text
 * @param period update time in milliseconds
 * @param animatedCharacter suffix character
 * @param animatedCharactersAmount max amount of suffix characters
 * @author Giorgio Garofalo
 */
data class AnimatedWaitMessage(
        private val text: String = "Please wait",
        private val period: Long = 500L,
        private val animatedCharacter: Char = '.',
        private val animatedCharactersAmount: Int = 3
) {

    private lateinit var timer: Timer

    /**
     * Prints the animated message to [System.out].
     */
    fun print() {
        var count = 0
        var isUpwards = true

        timer = timer(period = period, action = {
            if(isUpwards) {
                count++
            } else {
                count--
            }
            if(count % animatedCharactersAmount == 0) isUpwards = !isUpwards
            print("\r$text" + animatedCharacter.toString().repeat(count))
        })
    }

    /**
     * Stops the animation and replaces the last status with [message].
     * @param message completion message
     */
    fun stop(message: String = "") {
        timer.cancel()
        print("\r$message\n")
    }
}