package eu.iamgio.pikt

/**
 * Storage for global settings set via arguments.
 *
 * @author Giorgio Garofalo
 */
object GlobalSettings {

    /**
     * Enabled settings.
     */
    private val settings = mutableListOf<String>()

    /**
     * Enables a setting.
     *
     * Usage: `GlobalSettings += "setting"`
     * @param setting setting to set
     */
    operator fun plusAssign(setting: String) {
        settings += setting
    }

    /**
     * Used to check whether a setting is set.
     *
     * Usage: `val isSet = "setting" in GlobalSettings`
     * @param setting setting value to check existance for
     * @return whether [setting] is enabled
     */
    operator fun contains(setting: String) = setting in settings
}