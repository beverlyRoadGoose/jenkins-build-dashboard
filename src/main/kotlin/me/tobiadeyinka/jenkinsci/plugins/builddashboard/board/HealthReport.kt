package me.tobiadeyinka.jenkinsci.plugins.builddashboard.board

/**
 * Health status entity for monitored jobs
 *
 * @property[iconUrl] Url for the health status icon
 * @property[description] Description of the health status
 */
class HealthReport constructor(private val iconUrl: String, private val description: String)